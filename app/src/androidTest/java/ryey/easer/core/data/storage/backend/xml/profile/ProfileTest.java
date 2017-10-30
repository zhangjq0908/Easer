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

package ryey.easer.core.data.storage.backend.xml.profile;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.plugins.operation.bluetooth.BluetoothOperationData;
import ryey.easer.plugins.operation.bluetooth.BluetoothOperationPlugin;
import ryey.easer.plugins.operation.cellular.CellularOperationData;
import ryey.easer.plugins.operation.cellular.CellularOperationPlugin;

import static org.junit.Assert.assertEquals;

public class ProfileTest {

    public static String t_xml;
    public static ProfileStructure t_profile;

    @BeforeClass
    public static void setUpAll() {
        t_xml = "<?xml version='1.0' encoding='utf-8' standalone='no' ?><profile><name>myTest</name><item spec=\"cellular\"><state>off</state></item><item spec=\"bluetooth\"><state>on</state></item></profile>";

        t_profile = new ProfileStructure();
        t_profile.setName("myTest");
        t_profile.set(new CellularOperationPlugin().name(), new CellularOperationData(false));
        t_profile.set(new BluetoothOperationPlugin().name(), new BluetoothOperationData(true));
    }

    @Test
    public void testParse() throws IOException, XmlPullParserException, IllegalXmlException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(t_xml.getBytes());;
        ProfileParser profileParser = new ProfileParser();
        ProfileStructure profile = profileParser.parse(byteArrayInputStream);
        assertEquals("myTest", profile.getName());
        assertEquals(profile.get(new CellularOperationPlugin().name()).get(), false);
        assertEquals(profile.get(new BluetoothOperationPlugin().name()).get(), true);
        byteArrayInputStream.close();
    }

    @Test
    public void testSerialize() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProfileSerializer profileSerializer = new ProfileSerializer();
        profileSerializer.serialize(byteArrayOutputStream, t_profile);
        String xml = byteArrayOutputStream.toString();
        assertEquals(t_xml, xml);
        byteArrayOutputStream.close();
    }
}