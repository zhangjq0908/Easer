/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.core.data.storage.xml.event;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ryey.easer.commons.C;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.core.data.EventStructure;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;

public class EventParser {
    private static final String ns = null;

    private XmlPullParser parser = Xml.newPullParser();

    EventStructure event;

    public EventStructure parse(InputStream in) throws XmlPullParserException, IOException, IllegalXmlException {
        event = new EventStructure();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        if (readEvent())
            return event;
        else
            throw new IllegalXmlException("illegal content");
    }

    private boolean readEvent() throws IOException, XmlPullParserException, IllegalXmlException {
        parser.require(XmlPullParser.START_TAG, ns, ryey.easer.core.data.storage.xml.event.C.EVENT);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            switch (parser.getName()) {
                case ryey.easer.core.data.storage.xml.event.C.ENABLED:
                    readEnabled();
                    break;
                case C.NAME:
                    readName();
                    break;
                case C.PROFILE:
                    readProfile();
                    break;
                case ryey.easer.core.data.storage.xml.event.C.TRIG:
                    readTrigger();
                    break;
                default:
                    XmlHelper.skip(parser);
                    break;
            }
        }
        return true;
    }

    private void readEnabled() throws IOException, XmlPullParserException, IllegalXmlException {
        event.setEnabled(Boolean.parseBoolean(XmlHelper.getText(parser, "Enabled")));
    }

    private void readName() throws IOException, XmlPullParserException, IllegalXmlException {
        event.setName(XmlHelper.getText(parser, "Name"));
    }

    private void readProfile() throws IOException, XmlPullParserException, IllegalXmlException {
        event.setProfile(XmlHelper.getText(parser, "Profile"));
    }

    private void readTrigger() throws IOException, XmlPullParserException, IllegalXmlException {
        int depth = parser.getDepth();
        int event_type = parser.next();
        while (parser.getDepth() > depth) {
            if (event_type == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case C.SIT:
                        readSituation();
                        break;
                    case C.LOGIC:
                        XmlHelper.readLogic(parser);
                        break;
                }
            }
            event_type = parser.next();
        }
    }

    private void readSituation() throws IOException, XmlPullParserException, IllegalXmlException {
        String spec = parser.getAttributeValue(ns, C.SPEC);
        List<EventPlugin> plugins = PluginRegistry.getInstance().getEventPlugins();
        for (EventPlugin plugin : plugins) {
            if (spec.equals(plugin.name())) {
                EventData data = plugin.parse(parser);
                event.set(plugin.name(), data);
                break;
            }
        }
    }

}
