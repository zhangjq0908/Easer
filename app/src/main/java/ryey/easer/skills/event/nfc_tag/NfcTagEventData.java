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

package ryey.easer.skills.event.nfc_tag;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import ryey.easer.R;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.event.AbstractEventData;


public class NfcTagEventData extends AbstractEventData {

    private static final String K_ID = "id";

    byte[] id;

    static String byteArray2hexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = Character.forDigit(v >>> 4, 16);
            hexChars[j * 2 + 1] = Character.forDigit(v & 0x0F, 16);
        }
        return new String(hexChars);
    }

    static byte[] hexString2byteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    NfcTagEventData() {}

    NfcTagEventData(String id_str) {
        id = hexString2byteArray(id_str);
    }

    NfcTagEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @NonNull
    @Override
    public String toString() {
        return byteArray2hexString(id);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (id == null)
            return false;
        return true;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return new Dynamics[]{new NfcTagIdDynamics()};
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NfcTagEventData))
            return false;
        return Arrays.equals(id, ((NfcTagEventData) obj).id);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String tag_id_str = jsonObject.getString(K_ID);
                    id = hexString2byteArray(tag_id_str);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_ID, byteArray2hexString(id));
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException();
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
        dest.writeInt(id.length);
        dest.writeByteArray(id);
    }

    public static final Creator<NfcTagEventData> CREATOR
            = new Creator<NfcTagEventData>() {
        public NfcTagEventData createFromParcel(Parcel in) {
            return new NfcTagEventData(in);
        }

        public NfcTagEventData[] newArray(int size) {
            return new NfcTagEventData[size];
        }
    };

    private NfcTagEventData(Parcel in) {
        id = new byte[in.readInt()];
        in.readByteArray(id);
    }

    static class NfcTagIdDynamics implements Dynamics {

        public static final String id = "ryey.easer.skills.event.nfc_tag.tag_id";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.event_nfc_tag__dynamics_tag_id;
        }
    }
}
