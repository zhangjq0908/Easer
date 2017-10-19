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
import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.plugindef.eventplugin.EventType;

/**
 * Helper functions (to save and load data) when dealing with XML storage
 */
public class XmlHelper {
    protected static final String ns = null;

    public static String getText(XmlPullParser parser, String which) throws IOException, XmlPullParserException, IllegalXmlException {
        if (parser.next() == XmlPullParser.TEXT) {
            String text = parser.getText();
            parser.next(); // Move to END_TAG since there shouldn't be anything else
            return text;
        } else {
            throw new IllegalXmlException(String.format("Illegal XML field: %s has no TEXT", which));
        }
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

    public static class EventHelper {

        protected static final String AT = "at";

        public static void writeSingleSituation(XmlSerializer serializer, String spec, String situation) throws IOException {
            serializer.startTag(ns, C.SIT);
            serializer.attribute(ns, C.SPEC, spec);
            serializer.startTag(ns, AT);
            serializer.text(situation);
            serializer.endTag(ns, AT);
            serializer.endTag(ns, C.SIT);
        }

        public static String readSingleSituation(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
            String str_data = null;
            if (parser.next() == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case AT:
                        str_data = getText(parser, "At");
                        break;
                }
            }
            return str_data;
        }

        public static void writeMultipleSituation(XmlSerializer serializer, String spec, String[] situations) throws IOException {
            serializer.startTag(ns, C.SIT);
            serializer.attribute(ns, C.SPEC, spec);
            for (String situation : situations) {
                serializer.startTag(ns, AT);
                serializer.text(situation);
                serializer.endTag(ns, AT);
            }
            serializer.endTag(ns, C.SIT);
        }

        public static String[] readMultipleSituation(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
            List<String> list = new ArrayList<>();
            while (parser.next() != XmlPullParser.END_TAG) {
                switch (parser.getName()) {
                    case AT:
                        list.add(getText(parser, "At"));
                        break;
                    default:
                        break;
                }
            }
            return list.toArray(new String[0]);
        }

        public static void writeLogic(XmlSerializer serializer, EventType type) throws IOException {
            serializer.startTag(ns, C.LOGIC);
            serializer.text(type.toString());
            serializer.endTag(ns, C.LOGIC);
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
    }

    public static class OperationHelper {

        protected static final String OFF = "off";
        protected static final String ON = "on";
        protected static final String STATE = "state";

        public static void writeString(XmlSerializer serializer, String spec, String text) throws IOException {
            if (text != null) {
                serializer.startTag(ns, C.ITEM);

                serializer.attribute(ns, C.SPEC, spec);

                serializer.startTag(ns, STATE);
                serializer.text(text);
                serializer.endTag(ns, STATE);

                serializer.endTag(ns, C.ITEM);
            }
        }

        public static String readString(XmlPullParser parser, String spec) throws IOException, XmlPullParserException, IllegalXmlException {
            int depth = parser.getDepth();
            int event_type = parser.next();
            String text = null;
            while (parser.getDepth() > depth) {
                if (event_type == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                        case STATE:
                            text = getText(parser, spec);
                            break;
                        default:
                            skip(parser);
                    }
                }
                event_type = parser.next();
            }
            if (text == null)
                throw new IllegalXmlException(String.format("Illegal Xml field: (%s) has no STATE", spec));

            return text;
        }

        public static void writeBoolean(XmlSerializer serializer, String spec, Boolean state) throws IOException {
            if (state != null) {
                String ss;
                if (state)
                    ss = ON;
                else
                    ss = OFF;
                writeString(serializer, spec, ss);
            }
        }

        public static Boolean readBoolean(XmlPullParser parser, String spec) throws IOException, XmlPullParserException, IllegalXmlException {
            String text = readString(parser, spec);
            switch (text) {
                case ON:
                    return true;
                case OFF:
                    return false;
                default:
                    throw new IllegalXmlException(String.format("Illegal Xml field: (%s) Unknown or Illegal State", spec));
            }
        }

        public static void writeInteger(XmlSerializer serializer, String spec, Integer level) throws IOException {
            if (level != null) {
                String ss = level.toString();
                writeString(serializer, spec, ss);
            }
        }

        public static Integer readInteger(XmlPullParser parser, String spec) throws IOException, XmlPullParserException, IllegalXmlException {
            String text = readString(parser, spec);
            Integer level = null;
            try {
                level = Integer.valueOf(text);
            } catch (NumberFormatException e) {
                throw new IllegalXmlException(String.format("Illegal Xml field: (%s) Unknown or Illegal Number", spec));
            }
            return level;
        }
    }

}
