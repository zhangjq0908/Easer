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

package ryey.easer.core.data.storage.xml.event;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.core.data.EventStructure;
import ryey.easer.plugins.PluginRegistry;

class EventParser {
    private static final String ns = null;

    private XmlPullParser parser = Xml.newPullParser();

    private EventStructure event;

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
        parser.require(XmlPullParser.START_TAG, ns, C.EVENT);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            switch (parser.getName()) {
                case C.ACTIVE: //same as in EventSerializer
                    readActiveState();
                    break;
                case C.NAME:
                    readName();
                    break;
                case C.PROFILE:
                    readProfile();
                    break;
                case C.TRIG:
                    readTrigger();
                    break;
                default:
                    XmlHelper.skip(parser);
                    break;
            }
        }
        return true;
    }

    private void readActiveState() throws IOException, XmlPullParserException, IllegalXmlException {
        event.setActive(Boolean.parseBoolean(XmlHelper.getText(parser, "Enabled")));
    }

    private void readName() throws IOException, XmlPullParserException, IllegalXmlException {
        event.setName(XmlHelper.getText(parser, "Name"));
    }

    private void readProfile() throws IOException, XmlPullParserException, IllegalXmlException {
        String text = XmlHelper.getText(parser, "Profile");
        if (!text.equals(C.NON))
            event.setProfileName(text);
    }

    private void readTrigger() throws IOException, XmlPullParserException, IllegalXmlException {
        parser.next(); // C.AFTER
        String text = XmlHelper.getText(parser, "After");
        if (!text.equals(C.NON))
            event.setParentName(text);
        while (parser.next() != XmlPullParser.START_TAG);
        assert parser.getName().equals(C.SIT);
        String spec = parser.getAttributeValue(ns, C.SPEC);
        EventPlugin plugin = PluginRegistry.getInstance().findEventPlugin(spec);
        EventData data = plugin.data();
        data.parse(parser);
        if (data.isValid())
            event.setEventData(data);
        else
            throw new IllegalXmlException("data read but is not valid");
    }

}
