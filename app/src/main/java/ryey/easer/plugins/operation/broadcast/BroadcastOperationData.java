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

package ryey.easer.plugins.operation.broadcast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

import static ryey.easer.plugins.operation.broadcast.BroadcastOperationPlugin.pname;

public class BroadcastOperationData implements OperationData {
    protected static final String ns = null;

    static final String ACTION = "action";

    protected String action = null;

    public BroadcastOperationData() {
    }

    public BroadcastOperationData(String action) {
        this.action = action;
    }

    @Override
    public Object get() {
        return action;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            action = (String) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public void parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        int depth = parser.getDepth();
        int event_type = parser.next();
        String action = null;
        while (parser.getDepth() > depth) {
            if (event_type == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case ACTION:
                        if (parser.next() == XmlPullParser.TEXT)
                            action = parser.getText();
                        else
                            throw new IllegalXmlException(String.format("Illegal Item: (%s) Action has No Content", pname()));
                        break;
                    default:
                        XmlHelper.skip(parser);
                }
            }
            event_type = parser.next();
        }
        if (action == null)
            throw new IllegalXmlException(String.format("Illegal Item: (%s) No Action", pname()));

        set(action);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        serializer.startTag(ns, C.ITEM);

        serializer.attribute(ns, C.SPEC, pname());

        serializer.startTag(ns, ACTION);
        serializer.text((String) get());
        serializer.endTag(ns, ACTION);

        serializer.endTag(ns, C.ITEM);
    }

    @Override
    public boolean isValid() {
        if (action == null)
            return false;
        if (action.isEmpty())
            return false;
        return true;
    }
}
