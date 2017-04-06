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

package ryey.easer.core.data.storage.xml.profile;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.plugins.PluginRegistry;

public class ProfileSerializer {
    private static final String ns = null;

    XmlSerializer serializer = Xml.newSerializer();;
    ProfileStructure mProfile;

    public void serialize(OutputStream out, ProfileStructure profile) throws IOException {
        mProfile = profile;
        try {
            Log.d("ProfileSerializer", "seralizing");
            serializer.setOutput(out, "utf-8");
            serializer.startDocument("utf-8", false);
            writeProfile();
            serializer.flush();
            Log.d("ProfileSerializer", "seralized");
        } finally {
            out.close();
        }
    }

    private void writeProfile() throws IOException {
        serializer.startTag(ns, C.PROFILE);
        writeInfo();
        serializer.endTag(ns, C.PROFILE);
    }

    private void writeInfo() throws IOException {
        serializer.startTag(ns, C.NAME);
        serializer.text(mProfile.getName());
        serializer.endTag(ns, C.NAME);
        writeItems();
    }

    private void writeItems() throws IOException {
        for (OperationPlugin plugin : PluginRegistry.getInstance().getOperationPlugins()) {
            OperationData data = mProfile.get(plugin.name());
            if (data != null)
                data.serialize(serializer);
        }
    }

}
