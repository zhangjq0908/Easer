/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.core.data.storage.backend.xml.event;

import android.util.Xml;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

class EventParser implements Parser<EventStructure> {
    private static final String ns = null;

    private XmlPullParser parser = Xml.newPullParser();

    private int version = ryey.easer.commons.C.VERSION_FALLBACK;
    private EventStructure event;

    public EventStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            boolean no_version = false;
            event = new EventStructure();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, ns, C.EVENT);
            try {
                version = Integer.valueOf(parser.getAttributeValue(ns, ryey.easer.commons.C.VERSION));
            } catch (NumberFormatException e) {
                no_version = true;
            } catch (Exception e) {
                Logger.e(e, "Unexpected error");
                throw e;
            }
            if (readEvent()) {
                if (no_version)
                    Logger.d("Event <%s> has no \"version\" attribute. Used fallback version instead.", event.getName());
                return event;
            } else
                throw new IllegalStorageDataException("illegal content");
        } catch (XmlPullParserException e) {
            throw new IllegalStorageDataException(e);
        }
    }

    private boolean readEvent() throws IOException, XmlPullParserException, IllegalStorageDataException {
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

    private void readActiveState() throws IOException, XmlPullParserException, IllegalStorageDataException {
        event.setActive(Boolean.parseBoolean(XmlHelper.getText(parser, "Enabled")));
    }

    private void readName() throws IOException, XmlPullParserException, IllegalStorageDataException {
        event.setName(XmlHelper.getText(parser, "Name"));
    }

    private void readProfile() throws IOException, XmlPullParserException, IllegalStorageDataException {
        String text = XmlHelper.getText(parser, "Profile");
        if (!text.equals(C.NON))
            event.setProfileName(text);
    }

    private void readTrigger() throws IOException, XmlPullParserException, IllegalStorageDataException {
        parser.next(); // NC.AFTER
        String text = XmlHelper.getText(parser, "After");
        if (!text.equals(C.NON))
            event.setParentName(text);
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            while (parser.nextTag() != XmlPullParser.START_TAG);
        }
        parser.require(XmlPullParser.START_TAG, ns, C.SIT);
        String spec = parser.getAttributeValue(ns, C.SPEC);
        EventPlugin plugin = PluginRegistry.getInstance().event().findPlugin(spec);
        EventData data = plugin.dataFactory().emptyData();
        data.parse(parser, version);
        if (data.isValid())
            event.setEventData(data);
        else
            throw new IllegalStorageDataException("data read but is not valid");
    }

}
