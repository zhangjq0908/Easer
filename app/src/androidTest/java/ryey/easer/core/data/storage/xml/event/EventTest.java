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

import org.junit.BeforeClass;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.core.data.EventStructure;

import static org.junit.Assert.assertEquals;

public class EventTest {

    public static String t_xml;
    public static EventStructure t_event;

    @BeforeClass
    public static void setUpAll() throws ParseException {
        t_xml = "<?xml version='1.0' encoding='utf-8' standalone='no' ?><event><name>123</name><profile>profile</profile><after>myparent</after><trigger><situation spec=\"time\"><at>13:23</at></situation></trigger></event>";

        t_event = new EventStructure();
        t_event.setName("123");
        t_event.setParentName("myparent");
//        t_event.setActive(false);
        t_event.setProfileName("profile");
//        t_event.setTime(Utils.TextToTime("13:23"));
    }

    @Test
    public void testParse() throws IOException, XmlPullParserException, IllegalXmlException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(t_xml.getBytes());
        EventParser eventParser = new EventParser();
        EventStructure event = eventParser.parse(byteArrayInputStream);
        assertEquals("123", event.getName());
//        assertEquals(false, event.isActive());
        assertEquals("profile", event.getProfileName());
        assertEquals("myparent", event.getParentName());
//        assertEquals("13:23", Utils.TimeToText(event.getTime()));
        byteArrayInputStream.close();
    }

    @Test
    public void testSerialize() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        EventSerializer eventSerializer = new EventSerializer();
        eventSerializer.serialize(byteArrayOutputStream, t_event);
        String xml = byteArrayOutputStream.toString();
        assertEquals(t_xml, xml);
        byteArrayOutputStream.close();
    }
}