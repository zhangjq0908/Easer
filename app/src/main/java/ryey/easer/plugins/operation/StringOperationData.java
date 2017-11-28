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

package ryey.easer.plugins.operation;

import android.os.Parcel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.reusable.StringData;

public abstract class StringOperationData extends StringData implements OperationData {

    public StringOperationData() {super();}

    public StringOperationData(String text) {
        super(text);
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        set(XmlHelper.OperationHelper.readString(parser, PluginRegistry.getInstance().operation().findPlugin(this).name()));
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        XmlHelper.OperationHelper.writeString(serializer, PluginRegistry.getInstance().operation().findPlugin(this).name(), (String) get());
    }

    @Override
    public void parse(String data, C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                set(data);
        }
    }

    @Override
    public String serialize(C.Format format) {
        String res;
        switch (format) {
            default:
                res = (String) get();
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }

    protected StringOperationData(Parcel in) {
        text = in.readString();
    }
}
