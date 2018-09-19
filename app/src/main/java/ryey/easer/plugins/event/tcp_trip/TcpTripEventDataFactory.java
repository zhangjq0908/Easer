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

import android.support.annotation.NonNull;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.local_plugin.eventplugin.EventDataFactory;

class TcpTripEventDataFactory implements EventDataFactory<TcpTripEventData> {
    @NonNull
    @Override
    public Class<TcpTripEventData> dataClass() {
        return TcpTripEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public TcpTripEventData dummyData() {
        String raddr = "192.168.12.4";
        int rport = 3163;
        String send_data = "data to send";
        boolean check_data = true;
        String reply_data = "www my reply data";
        return new TcpTripEventData(raddr, rport, send_data, check_data, reply_data);
    }

    @ValidData
    @NonNull
    @Override
    public TcpTripEventData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new TcpTripEventData(data, format, version);
    }
}
