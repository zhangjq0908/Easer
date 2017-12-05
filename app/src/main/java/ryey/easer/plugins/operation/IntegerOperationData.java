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
import android.support.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.reusable.IntegerData;

public abstract class IntegerOperationData extends IntegerData implements OperationData {

    protected IntegerOperationData() {super();}

    protected IntegerOperationData(@NonNull Integer level) {
        super(level);
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        set(XmlHelper.OperationHelper.readInteger(parser, PluginRegistry.getInstance().operation().findPlugin(this).id()));
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        XmlHelper.OperationHelper.writeInteger(serializer, PluginRegistry.getInstance().operation().findPlugin(this).id(), (Integer) get());
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                Integer level = Integer.valueOf(data);
                set(level);
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                res = get().toString();
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(level);
    }

    protected IntegerOperationData(@NonNull Parcel in) {
        level = in.readInt();
    }
}
