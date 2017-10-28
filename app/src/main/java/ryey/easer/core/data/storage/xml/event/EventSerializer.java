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

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.EventStructure;

class EventSerializer {

    XmlSerializer serializer = Xml.newSerializer();
    private final String ns = null;

    public EventSerializer() {
    }

    public void serialize(OutputStream out, EventStructure eventStructure) throws IOException {
        try {
            Logger.d("serializing %s", eventStructure.getName());
            serializer.setOutput(out, "utf-8");
            serializer.startDocument("utf-8", false);
            writeEvent(eventStructure);
            serializer.flush();
            Logger.i("serialized %s", eventStructure.getName());
        } finally {
            out.close();
        }
    }

    private void writeEvent(EventStructure eventStructure) throws IOException {
        serializer.startTag(ns, C.EVENT);
        serializer.attribute(ns, ryey.easer.commons.C.VERSION_NAME, String.valueOf(ryey.easer.commons.C.VERSION_CURRENT));
        writeActiveState(eventStructure.isActive());
        writeName(eventStructure.getName());
        writeProfile(eventStructure.getProfileName());
        writeTrigger(eventStructure.getEventData(), eventStructure.getParentName());
        serializer.endTag(ns, C.EVENT);
    }

    private void writeActiveState(boolean isActive) throws IOException {
        serializer.startTag(ns, C.ACTIVE);
        serializer.text(String.valueOf(isActive));
        serializer.endTag(ns, C.ACTIVE);
    }

    private void writeName(String name) throws IOException {
        serializer.startTag(ns, C.NAME);
        serializer.text(name);
        serializer.endTag(ns, C.NAME);
    }

    private void writeProfile(String profileName) throws IOException {
        if ((profileName != null) && (!profileName.isEmpty())) {
            serializer.startTag(ns, C.PROFILE);
            serializer.text(profileName);
            serializer.endTag(ns, C.PROFILE);
        } else {
            serializer.startTag(ns, C.PROFILE);
            serializer.text(C.NON);
            serializer.endTag(ns, C.PROFILE);
        }
    }

    private void writeTrigger(EventData eventData, String parentName) throws IOException {
        serializer.startTag(ns, C.TRIG);

        if ((parentName != null) && (!parentName.isEmpty())) {
            serializer.startTag(ns, C.AFTER);
            serializer.text(parentName);
            serializer.endTag(ns, C.AFTER);
        } else {
            serializer.startTag(ns, C.AFTER);
            serializer.text(C.NON);
            serializer.endTag(ns, C.AFTER);
        }

        eventData.serialize(serializer);

        serializer.endTag(ns, C.TRIG);
    }

}
