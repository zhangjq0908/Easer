/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.operation.network_transmission;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;

public class NetworkTransmissionLoader extends OperationLoader<NetworkTransmissionOperationData> {
    public NetworkTransmissionLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull NetworkTransmissionOperationData data) {
        try {
            InetAddress remote_address = InetAddress.getByName(data.remote_address);
            switch (data.protocol) {
                case tcp:
                    try {
                        Socket socket = new Socket(remote_address, data.remote_port);
                        OutputStream outputStream = socket.getOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                        dataOutputStream.writeBytes(data.data);
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    break;
                case udp:
                    DatagramPacket datagramPacket = new DatagramPacket(data.data.getBytes(), data.data.length(), remote_address, data.remote_port);
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        socket.send(datagramPacket);
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    break;
                default:
                    throw new IllegalAccessError("data should be valid when calling this method");
            }
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }
}
