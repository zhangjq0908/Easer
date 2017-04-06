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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.XmlHelper;

public abstract class BooleanOperationData implements OperationData {
    protected Boolean state = null;

    public BooleanOperationData() {}

    public BooleanOperationData(Boolean state) {
        this.state = state;
    }

    @Override
    public Object get() {
        return state;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Boolean) {
            state = (Boolean) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    protected void mParse(XmlPullParser parser, String name) throws IOException, XmlPullParserException, IllegalXmlException {
        set(XmlHelper.handleBoolean(parser, name));
    }

    protected void mSerialize(XmlSerializer serializer, String name) throws IOException {
        XmlHelper.dealBoolean(serializer, name, (Boolean) get());
    }
}
