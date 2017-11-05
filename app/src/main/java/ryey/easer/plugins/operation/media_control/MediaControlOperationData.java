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

package ryey.easer.plugins.operation.media_control;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

public class MediaControlOperationData implements OperationData {

    enum ControlChoice {
        play_pause,
        play,
        pause,
        previous,
        next,
    }

    ControlChoice choice = null;

    public MediaControlOperationData() {
    }

    public MediaControlOperationData(ControlChoice choice) {
        set(choice);
    }

    @Override
    public Object get() {
        return choice;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof ControlChoice) {
            choice = (ControlChoice) obj;
        } else {
            throw new IllegalArgumentException();
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
        switch (format) {
            default:
                try {
                    this.choice = ControlChoice.valueOf(data);
                } catch (Exception e) {
                    throw new IllegalStorageDataException(e.getMessage());
                }
        }
    }

    @Override
    public String serialize(C.Format format) {
        String res = "";
        switch (format) {
            default:
                res = choice.toString();
        }
        return res;
    }

    @Override
    public boolean isValid() {
        if (choice == null)
            return false;
        return true;
    }

}
