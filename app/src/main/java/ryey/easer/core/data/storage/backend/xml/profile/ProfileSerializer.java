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

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.Serializer;
import ryey.easer.core.data.storage.backend.UnableToSerializeException;
import ryey.easer.plugins.PluginRegistry;

class ProfileSerializer implements Serializer<ProfileStructure> {
    private static final String ns = null;

    XmlSerializer serializer = Xml.newSerializer();
    ProfileStructure mProfile;

    public String serialize(ProfileStructure profile) throws UnableToSerializeException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mProfile = profile;
        try {
            Logger.d("serializing %s", profile.getName());
            serializer.setOutput(out, "utf-8");
            serializer.startDocument("utf-8", false);
            writeProfile();
            serializer.flush();
            Logger.i("serialized %s", profile.getName());
            return out.toString();
        } catch (IOException e) {
            throw new UnableToSerializeException(e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Logger.e(e, "Unable to close ByteArrayOutputStream");
                e.printStackTrace();
            }
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
        for (OperationPlugin plugin : PluginRegistry.getInstance().operation().getAllPlugins()) {
            Collection<OperationData> possibleData = mProfile.get(plugin.name());
            if (possibleData != null)
                for (OperationData data : possibleData)
                    data.serialize(serializer);
        }
    }

}
