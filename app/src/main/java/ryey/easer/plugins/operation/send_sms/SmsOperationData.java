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

package ryey.easer.plugins.operation.send_sms;

import android.telephony.PhoneNumberUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

public class SmsOperationData implements OperationData {
    private static final String K_DEST = "destination";
    private static final String K_CONTENT = "content";

    Sms sms;

    public SmsOperationData() {
    }

    public SmsOperationData(Sms sms) {
        this.sms = sms;
    }

    @Override
    public Object get() {
        return sms;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Sms) {
            this.sms = (Sms) obj;
        } else {
            throw new IllegalArgumentException("wrong argument type " + obj.getClass().getSimpleName());
        }
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
    public void parse(String data, C.Format format, int version) throws IllegalStorageDataException {
        sms = new Sms();
        try {
            JSONObject jsonObject = new JSONObject(data);
            sms.destination = jsonObject.getString(K_DEST);
            sms.content = jsonObject.getString(K_CONTENT);
        } catch (JSONException e) {
            Logger.e(e, "error");
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String serialize(C.Format format) {
        String res = "";
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_DEST, sms.destination);
                    jsonObject.put(K_CONTENT, sms.content);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    Logger.e(e, "error");
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @Override
    public boolean isValid() {
        if (Utils.isBlank(sms.destination))
            return false;
        if (!PhoneNumberUtils.isWellFormedSmsAddress(sms.destination))
            return false;
        if (Utils.isBlank(sms.content))
            return false;
        return true;
    }
}
