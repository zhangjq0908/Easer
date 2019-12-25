/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.event.tcp_trip;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ryey.easer.Utils;
import ryey.easer.skills.event.AbstractSlot;

import static java.lang.Thread.sleep;

public class TcpTripSlot extends AbstractSlot<TcpTripEventData> {

    private SendTask task = new SendTask(this);

    TcpTripSlot(Context context, TcpTripEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    TcpTripSlot(Context context, TcpTripEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        task.execute(eventData);
    }

    @Override
    public void cancel() {
        task.cancel(true);
    }

    static class SendTask extends AsyncTask<TcpTripEventData, Void, SendTask.Result> {

        private WeakReference<TcpTripSlot> slot;

        SendTask(TcpTripSlot tcpTripSlot) {
            slot = new WeakReference<>(tcpTripSlot);
        }

        @Override
        protected Result doInBackground(TcpTripEventData... tcpTripEventData) {
            assert tcpTripEventData != null;
            TcpTripEventData eventData = tcpTripEventData[0];
            assert eventData != null;
            Result.Builder builder = Result.Builder.fromData(eventData);
            try {
                Logger.v("sending TCP packet");
                InetAddress remote_address = InetAddress.getByName(eventData.rAddr);
                Socket socket = new Socket(remote_address, eventData.rPort);
                if (!Utils.isBlank(eventData.send_data)) {
                    try (OutputStream outputStream = socket.getOutputStream()) {
                        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                            dataOutputStream.writeBytes(eventData.send_data);
                        }
                    }
                }
                Logger.v("TCP data sent");
                builder.putIP(remote_address.getHostAddress());
                if (!eventData.check_reply) {
                    return builder.success();
                } else {
                    Logger.v("waiting for TCP response");
                    BufferedReader reader;
                    try {
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        Logger.v("Reader for TCP response got");
                    } catch (IOException e) {
                        Logger.v("no valid InputStream");
                        builder.putRecvData("");
                        return builder.build(Utils.isBlank(eventData.reply_data));
                    }
                    int num_of_line = 0;
                    int tail = 0;
                    do { // waiting for input
                        Logger.v("closed? %s :: inputShutdown? %s", socket.isClosed(), socket.isInputShutdown());
                        try {
                            if (reader.ready()) {
                                String line = reader.readLine();
                                Logger.v("got message <%s>", line);
                                int end = Math.min(tail + line.length(), eventData.reply_data.length());
                                if (!line.equals(eventData.reply_data.substring(tail, end))) {
                                    Logger.d("message is NOT correct on line %d", num_of_line);
                                    return builder.fail();
                                }
                                Logger.d("message is correct on line %d", num_of_line++);
                                tail += line.length();
                                if (tail >= eventData.reply_data.length()) {
                                    Logger.i("got whole match for response");
                                    builder.putRecvData(eventData.reply_data); // TODO: read until EOF of reply
                                    return builder.success();
                                }
                            } else {
                                sleep(2 * 1000);
                            }
                        } catch (IOException e) { // socket is closed
                            Logger.i("Socket unexpectedly closed while waiting for (more) response");
                            return builder.fail();
                        } catch (InterruptedException e) {
                            break;
                        }
                    } while (!socket.isClosed());
                    if (tail == 0) {
                        builder.putRecvData("");
                        return builder.build(Utils.isBlank(eventData.reply_data));
                    }
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                Logger.v("Done listening for reply");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.fail();
        }

        @Override
        protected void onPostExecute(Result result) {
            assert result != null;
            slot.get().changeSatisfiedState(result.success, result.dynamics);
        }

        static final class Result {
            final boolean success;
            @NonNull final Bundle dynamics;

            Result(boolean success) {
                this.success = success;
                this.dynamics = new Bundle();
            }

            Result(boolean success, @NonNull Bundle dynamics) {
                this.success = success;
                this.dynamics = dynamics;
            }

            static final class Builder {

                @NonNull final Bundle dynamics = new Bundle();

                static Builder fromData(TcpTripEventData data) {
                    Builder builder = new Builder();
                    builder.dynamics.putString(TcpTripEventData.RemoteHostDynamics.id, data.rAddr);
                    builder.dynamics.putString(TcpTripEventData.RemotePortDynamics.id, Integer.toString(data.rPort));
                    builder.dynamics.putString(TcpTripEventData.SentDataDynamics.id, data.send_data);
                    return builder;
                }

                Builder putIP(String ip) {
                    dynamics.putString(TcpTripEventData.RemoteIPDynamics.id, ip);
                    return this;
                }

                Builder putRecvData(@Nullable String data) {
                    if (data == null)
                        data = "";
                    dynamics.putString(TcpTripEventData.ReceivedDataDynamics.id, data);
                    return this;
                }

                Result build(boolean success) {
                    return new Result(success, dynamics);
                }

                Result success() {
                    return build(true);
                }

                Result fail() {
                    return build(false);
                }
            }
        }
    }

}