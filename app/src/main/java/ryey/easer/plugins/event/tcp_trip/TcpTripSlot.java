/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.plugins.event.tcp_trip;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ryey.easer.Utils;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;

public class TcpTripSlot extends AbstractSlot<TcpTripEventData> {

    private Thread waiter;

    TcpTripSlot(Context context, TcpTripEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    TcpTripSlot(Context context, TcpTripEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        try {
            Logger.v("sending TCP packet");
            InetAddress remote_address = InetAddress.getByName(eventData.rAddr);
            Socket socket = new Socket(remote_address, eventData.rPort);
            if (!Utils.isBlank(eventData.send_data)) {
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeBytes(eventData.send_data);
            }
            socket.shutdownOutput();
            Logger.v("TCP packet sent and has told output to close");
            waiter = new PostSend(socket);
            waiter.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        if (waiter != null)
            waiter.interrupt();
    }

    @Override
    public void check() {
    }

    private class PostSend extends Thread {
        final Socket socket;

        PostSend(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            if (!eventData.check_reply) {
                changeSatisfiedState(true);
                return;
            }
            Logger.v("waiting for TCP response");
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Logger.v("Reader for TCP response got");
            } catch (IOException e) {
                Logger.v("no valid InputStream");
                if (eventData.check_reply)
                    changeSatisfiedState(false);
                return;
            }
            int num_of_line = 0;
            int tail = 0;
            do { // waiting for input
                Logger.v("closed? %s :: inputShutdown? %s", socket.isClosed(), socket.isInputShutdown());
                try {
                    if (reader.ready()) {
                        String line = reader.readLine();
                        Logger.v("got message <%s>", line);
                        if (!line.equals(eventData.reply_data.substring(tail, tail+line.length()))) {
                            Logger.d("message is NOT correct on line %d", num_of_line);
                            changeSatisfiedState(false);
                            break;
                        }
                        Logger.d("message is correct on line %d", num_of_line++);
                        tail += line.length();
                        if (tail == eventData.reply_data.length()) {
                            Logger.i("got whole match for response");
                            changeSatisfiedState(true);
                            break;
                        }
                    } else {
                        sleep(2 * 1000);
                    }
                } catch (IOException e) { // socket is closed
                    Logger.i("Socket unexpectedly closed while waiting for (more) response");
                    changeSatisfiedState(false);
                } catch (InterruptedException e) {
                    break;
                }
            } while (!socket.isClosed());
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            Logger.v("Done listening for reply");
        }
    }
}