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

package ryey.easer.commons;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.plugindef.eventplugin.EventType;

/*
 * Helper functions (to save and load data) when dealing with XML storage
 */
public class XmlHelper {
    protected static final String STATE = "state";
    protected static final String OFF = "off";
    protected static final String ON = "on";
    protected static final String AT = "at";
    protected static final String ns = null;

    public static void writeSingleSituation(XmlSerializer serializer, String spec, String at) throws IOException {
        serializer.startTag(ns, C.SIT);
        serializer.attribute(ns, C.SPEC, spec);
        serializer.startTag(ns, AT);
        serializer.text(at);
        serializer.endTag(ns, AT);
        serializer.endTag(ns, C.SIT);
    }

    public static void writeLogic(XmlSerializer serializer, EventType type) throws IOException {
        serializer.startTag(ns, C.LOGIC);
        serializer.text(type.toString());
        serializer.endTag(ns, C.LOGIC);
    }

    public static String getText(XmlPullParser parser, String which) throws IOException, XmlPullParserException, IllegalXmlException {
        if (parser.next() == XmlPullParser.TEXT)
            return parser.getText();
        else {
            throw new IllegalXmlException(String.format("Illegal Event: %s has No content", which));
        }
    }

    public static String readSingleSituation(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        String str_data = null;
        if (parser.next() == XmlPullParser.START_TAG) {
            switch (parser.getName()) {
                case AT:
                    str_data = XmlHelper.getText(parser, "At");
                    break;
            }
        }
        return str_data;
    }

    public static EventType readLogic(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {

        while (parser.next() != XmlPullParser.START_TAG);
        String logic = getText(parser, "Logic");
        EventType type = null;
        try {
            type = EventType.valueOf(logic);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return type;
    }

    public static void dealBoolean(XmlSerializer serializer, String spec, Boolean state) throws IOException {
        if (state != null) {
            String ss;
            if (state)
                ss = ON;
            else
                ss = OFF;

            serializer.startTag(ns, C.ITEM);

            serializer.attribute(ns, C.SPEC, spec);

            serializer.startTag(ns, STATE);
            serializer.text(ss);
            serializer.endTag(ns, STATE);

            serializer.endTag(ns, C.ITEM);
        }
    }

    public static Boolean handleBoolean(XmlPullParser parser, String spec) throws IOException, XmlPullParserException, IllegalXmlException {
        int depth = parser.getDepth();
        int event_type = parser.next();
        String text = null;
        while (parser.getDepth() > depth) {
            if (event_type == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case STATE:
                        if (parser.next() == XmlPullParser.TEXT)
                            text = parser.getText();
                        else
                            throw new IllegalXmlException(String.format("Illegal Item: (%s) State has No Content", spec));
                        break;
                    default:
                        skip(parser);
                }
            }
            event_type = parser.next();
        }
        if (text == null)
            throw new IllegalXmlException(String.format("Illegal Item: (%s) No State", spec));

        switch (text) {
            case ON:
                return true;
            case OFF:
                return false;
            default:
                throw new IllegalXmlException(String.format("Illegal Item: (%s) Unknown or Illegal State", spec));
        }
    }

    public static void dealInteger(XmlSerializer serializer, String spec, Integer level) throws IOException {
        if (level != null) {
            String ss = level.toString();

            serializer.startTag(ns, C.ITEM);

            serializer.attribute(ns, C.SPEC, spec);

            serializer.startTag(ns, STATE);
            serializer.text(ss);
            serializer.endTag(ns, STATE);

            serializer.endTag(ns, C.ITEM);
        }
    }

    public static Integer handleInteger(XmlPullParser parser, String spec) throws IOException, XmlPullParserException, IllegalXmlException {
        int depth = parser.getDepth();
        int event_type = parser.next();
        String text = null;
        while (parser.getDepth() > depth) {
            if (event_type == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case STATE:
                        if (parser.next() == XmlPullParser.TEXT)
                            text = parser.getText();
                        else
                            throw new IllegalXmlException(String.format("Illegal Item: (%s) State has No Content", spec));
                        break;
                    default:
                        skip(parser);
                }
            }
            event_type = parser.next();
        }
        if (text == null)
            throw new IllegalXmlException(String.format("Illegal Item: (%s) No State", spec));

        Integer level = null;

        try {
            level = Integer.valueOf(text);
        } catch (NumberFormatException e) {
            throw new IllegalXmlException(String.format("Illegal Item: (%s) Unknown or Illegal Number", spec));
        }

        return level;
    }

    public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
