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

package ryey.easer.core.data.storage.backend.xml.event;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.backend.UnableToSerializeException;
import ryey.easer.plugins.event.time.TimeEventData;

import static org.junit.Assert.assertEquals;

public class EventTest {

    public static String t_xml_root, t_xml_child;
    public static EventStructure t_event_child;

    @BeforeClass
    public static void setUpAll() throws ParseException {
        t_xml_root = "<?xml version='1.0' encoding='utf-8' standalone='no' ?><event><name>myparent</name><profile>profile1</profile><trigger><after>NO DATA</after><situation spec=\"time\"><at>13:23</at></situation><logic>after</logic></trigger></event>";
        t_xml_child = "<?xml version='1.0' encoding='utf-8' standalone='no' ?><event version=\"2\"><active>true</active><name>123</name><profile>profile1</profile><trigger><after>myparent</after><situation spec=\"time\"><at>13:23</at></situation><logic>after</logic></trigger></event>";

        t_event_child = new EventStructure();
        t_event_child.setName("123");
        t_event_child.setParentName("myparent");
//        t_event_child.setActive(false);
        t_event_child.setProfileName("profile1");
        EventData eventData = new TimeEventData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 23);
        eventData.set(calendar);
        eventData.setType(EventType.after);
        t_event_child.setEventData(eventData);
    }

    @Test
    public void testParse() throws IOException, XmlPullParserException, IllegalStorageDataException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(t_xml_child.getBytes());
        EventParser eventParser = new EventParser();
        EventStructure event = eventParser.parse(byteArrayInputStream);
        assertEquals("123", event.getName());
//        assertEquals(false, event.isActive());
        assertEquals("profile1", event.getProfileName());
        assertEquals("myparent", event.getParentName());
        byteArrayInputStream.close();
    }

    @Test
    public void testSerialize() throws IOException, UnableToSerializeException {
        EventSerializer eventSerializer = new EventSerializer();
        String xml = eventSerializer.serialize(t_event_child);
        assertEquals(t_xml_child, xml);
    }
}