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

package ryey.easer.core.data.storage.backend.xml.profile;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

class ProfileParser implements Parser<ProfileStructure> {
    private static final String ns = null;

    private XmlPullParser parser = Xml.newPullParser();

    int version = ryey.easer.commons.C.VERSION_DEFAULT;
    ProfileStructure profile;

    public ProfileStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            profile = new ProfileStructure();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            if (readProfile())
                return profile;
            else
                throw new IllegalStorageDataException("illegal content");
        } catch (XmlPullParserException e) {
            throw new IllegalStorageDataException(e.getMessage());
        }
    }

    private boolean readProfile() throws XmlPullParserException, IOException, IllegalStorageDataException {
        parser.require(XmlPullParser.START_TAG, ns, C.PROFILE);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            switch (parser.getName()) {
                case C.NAME:
                    readName();
                    break;
                case C.ITEM:
                    readItem();
                    break;
                default:
                    XmlHelper.skip(parser);
                    break;
            }
        }
        return true;
    }

    private void readName() throws IOException, XmlPullParserException, IllegalStorageDataException {
        if (parser.next() == XmlPullParser.TEXT)
            profile.setName(parser.getText());
        else
            throw new IllegalStorageDataException("Illegal Profile: Name has No content");
    }

    private void readItem() throws IOException, XmlPullParserException, IllegalStorageDataException {
        String spec = parser.getAttributeValue(ns, C.SPEC);
        if (spec == null) {
            throw new IllegalStorageDataException("Illegal Item: No Spec");
        }
        switch (spec) {
            default:
                OperationPlugin plugin = PluginRegistry.getInstance().operation().findPlugin(spec);
                OperationData data = plugin.dataFactory().emptyData();
                data.parse(parser, version);
                profile.set(plugin.id(), data);
        }
    }

}
