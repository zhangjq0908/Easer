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

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

import ryey.easer.commons.C;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.core.data.EventStructure;
import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;

public class EventSerializer {

    XmlSerializer serializer = Xml.newSerializer();
    EventStructure mEvent;
    private final String ns = null;

    public EventSerializer() {
    }

    public void serialize(OutputStream out, EventStructure event) throws IOException {
        mEvent = event;
        try {
            Log.d("EventSerializer", "serializing");
            serializer.setOutput(out, "utf-8");
            serializer.startDocument("utf-8", false);
            writeEvent();
            serializer.flush();
            Log.d("EventSerializer", "serialized");
        } finally {
            out.close();
        }
    }

    private void writeEvent() throws IOException {
        serializer.startTag(ns, ryey.easer.core.data.storage.xml.event.C.EVENT);
        writeEnabled();
        writeName();
        writeProfile();
        writeTrigger();
        serializer.endTag(ns, ryey.easer.core.data.storage.xml.event.C.EVENT);
    }

    private void writeEnabled() throws IOException {
        serializer.startTag(ns, ryey.easer.core.data.storage.xml.event.C.ENABLED);
        serializer.text(String.valueOf(mEvent.isEnabled()));
        serializer.endTag(ns, ryey.easer.core.data.storage.xml.event.C.ENABLED);
    }

    private void writeName() throws IOException {
        serializer.startTag(ns, C.NAME);
        serializer.text(mEvent.getName());
        serializer.endTag(ns, C.NAME);
    }

    private void writeProfile() throws IOException {
        serializer.startTag(ns, C.PROFILE);
        serializer.text(mEvent.getProfile());
        serializer.endTag(ns, C.PROFILE);
    }

    private void writeTrigger() throws IOException {
        serializer.startTag(ns, ryey.easer.core.data.storage.xml.event.C.TRIG);

        for (EventPlugin plugin : PluginRegistry.getInstance().getEventPlugins()) {
            EventData data = mEvent.get(plugin.name());
            if (data != null && data.isValid()) {
                plugin.serialize(serializer, data);
            }
        }

        serializer.endTag(ns, ryey.easer.core.data.storage.xml.event.C.TRIG);
    }

}
