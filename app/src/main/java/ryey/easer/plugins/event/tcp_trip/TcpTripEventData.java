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

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.EnumSet;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

public class TcpTripEventData extends TypedEventData {

    private static final String K_RADDR = "remote address";
    private static final String K_RPORT = "remote port";
    private static final String K_SEND_DATA = "send data";
    private static final String K_CHECK_REPLY = "check reply?";
    private static final String K_REPLY_DATA = "reply data";

    String rAddr;
    int rPort;
    String send_data;
    boolean check_reply;
    String reply_data;

    {
        default_type = EventType.is;
        availableTypes = EnumSet.of(EventType.is);
    }

    public TcpTripEventData() {}

    TcpTripEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    TcpTripEventData(String rAddr, int rPort, String send_data, boolean check_reply, String reply_data) {
        this.rAddr = rAddr;
        this.rPort = rPort;
        this.send_data = send_data;
        this.check_reply = check_reply;
        this.reply_data = reply_data;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (rAddr == null)
            return false;
        if (rPort <= 0)
            return false;
        if (check_reply && !Utils.isBlank(reply_data))
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TcpTripEventData))
            return false;
        if (!Utils.eEquals(this, (EventData) obj))
            return false;
        if (!Utils.nullableEqual(rAddr, ((TcpTripEventData) obj).rAddr))
            return false;
        if (rPort != ((TcpTripEventData) obj).rPort)
            return false;
        if (!Utils.nullableEqual(send_data, ((TcpTripEventData) obj).send_data))
            return false;
        if (check_reply != ((TcpTripEventData) obj).check_reply)
            return false;
        if (!Utils.nullableEqual(reply_data, ((TcpTripEventData) obj).reply_data))
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        throw new IllegalAccessError();
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        throw new IllegalAccessError();
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    rAddr = jsonObject.getString(K_RADDR);
                    rPort = jsonObject.getInt(K_RPORT);
                    send_data = jsonObject.optString(K_SEND_DATA, null);
                    check_reply = jsonObject.getBoolean(K_CHECK_REPLY);
                    if (check_reply) {
                        reply_data = jsonObject.getString(K_REPLY_DATA);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_RADDR, rAddr);
                    jsonObject.put(K_RPORT, rPort);
                    if (!Utils.isBlank(send_data)) {
                        jsonObject.put(K_SEND_DATA, send_data);
                    }
                    jsonObject.put(K_CHECK_REPLY, check_reply);
                    if (check_reply) {
                        jsonObject.put(K_REPLY_DATA, reply_data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException(e);
                }
                res = jsonObject.toString();

        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rAddr);
        dest.writeInt(rPort);
        dest.writeString(send_data);
        dest.writeByte((byte) (check_reply ? 1 : 0));
        if (check_reply) {
            dest.writeString(reply_data);
        }
    }

    public static final Creator<TcpTripEventData> CREATOR
            = new Creator<TcpTripEventData>() {
        public TcpTripEventData createFromParcel(Parcel in) {
            return new TcpTripEventData(in);
        }

        public TcpTripEventData[] newArray(int size) {
            return new TcpTripEventData[size];
        }
    };

    private TcpTripEventData(Parcel in) {
        rAddr = in.readString();
        rPort = in.readInt();
        send_data = in.readString();
        check_reply = in.readByte() != 0;
        if (check_reply) {
            reply_data = in.readString();
        }
    }
}
