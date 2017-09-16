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

import android.net.Uri;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

import static ryey.easer.plugins.operation.broadcast.BroadcastOperationPlugin.pname;

public class BroadcastOperationData implements OperationData {
    protected static final String ns = null;

    static final String ACTION = "action";
    static final String CATEGORY = "category";
    static final String TYPE = "type";
    static final String DATA = "data";

    IntentData data = new IntentData();

    public BroadcastOperationData() {
    }

    public BroadcastOperationData(IntentData data) {
        this.data = data;
    }

    @Override
    public Object get() {
        return data;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            data.action = (String) obj;
        } else if (obj instanceof IntentData) {
            data = (IntentData) obj;
        } else {
            throw new IllegalArgumentTypeException(data.getClass(), new Class[]{String.class, IntentData.class});
        }
    }

    @Override
    public void parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        int depth = parser.getDepth();
        int event_type = parser.next();
        IntentData intentData = new IntentData();
        while (parser.getDepth() > depth) {
            if (event_type == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case ACTION:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.action = parser.getText();
                        else
                            throw new IllegalXmlException(String.format("Illegal Item: (%s) Action has No Content", pname()));
                        break;
                    case CATEGORY:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.category = IntentData.stringToCategory(parser.getText());
                        else
                            throw new IllegalXmlException(String.format("Illegal Item: (%s) Category is not valid", pname()));
                        break;
                    case TYPE:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.type = parser.getText();
                        else
                            throw new IllegalXmlException(String.format("Illegal Item: (%s) Type is not valid", pname()));
                        break;
                    case DATA:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.data = Uri.parse(parser.getText());
                        else
                            throw new IllegalXmlException(String.format("Illegal Item: (%s) Data is not valid", pname()));
                        break;
                    default:
                        XmlHelper.skip(parser);
                }
            }
            event_type = parser.next();
        }
        if (intentData.action == null)
            throw new IllegalXmlException(String.format("Illegal Item: (%s) No Action", pname()));

        set(intentData);
    }

    /*
     * `isValid()` needs to be called before calling this function.
     */
    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        serializer.startTag(ns, C.ITEM);

        serializer.attribute(ns, C.SPEC, pname());

        if (!Utils.isBlank(data.action)) {
            serializer.startTag(ns, ACTION);
            serializer.text(data.action.trim());
            serializer.endTag(ns, ACTION);
        }

        if (!data.category.isEmpty()) {
            serializer.startTag(ns, CATEGORY);
            serializer.text(IntentData.categoryToString(data.category));
            serializer.endTag(ns, CATEGORY);
        }

        if (!Utils.isBlank(data.action)) {
            serializer.startTag(ns, TYPE);
            serializer.text(data.type.trim());
            serializer.endTag(ns, TYPE);
        }

        if (data.data != null && !Utils.isBlank(data.data.toString())) {
            serializer.startTag(ns, DATA);
            serializer.text(data.data.toString());
            serializer.endTag(ns, DATA);
        }

        serializer.endTag(ns, C.ITEM);
    }

    @Override
    public boolean isValid() {
        if (!Utils.isBlank(data.action))
            return true;
        if (data.category != null && !data.category.isEmpty())
            return true;
        if (!Utils.isBlank(data.type))
            return true;
        if (data.data != null && !Utils.isBlank(data.data.toString()))
            return true;
        return false;
    }
}
