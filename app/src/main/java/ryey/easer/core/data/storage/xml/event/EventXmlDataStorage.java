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

import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.DataStorage;
import ryey.easer.core.data.storage.FileUtils;
import ryey.easer.commons.IllegalXmlException;

public class EventXmlDataStorage implements DataStorage<EventStructure> {
    protected static EventXmlDataStorage instance = null;

    protected static Context s_context = null;

    protected static File dir;

    public static EventXmlDataStorage getInstance(Context context) throws IOException {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = FileUtils.getSubDir(s_context.getFilesDir(), "event");
            instance = new EventXmlDataStorage();
        }
        return instance;
    }

    EventXmlDataStorage() {
    }

    @Override
    public List<String> list() {
        List<String> list = new ArrayList<>();
        for (EventStructure event : allEvents()) {
            list.add(event.getName());
        }
        return list;
    }

    @Override
    public EventStructure get(String name) {
        for (EventStructure event : allEvents()) {
            if (name.equals(event.getName()))
                return event;
        }
        return null;
    }

    @Override
    public boolean add(EventStructure event) {
        try {
            EventSerializer serializer = new EventSerializer();
            File file = new File(dir, fileName(event));
            if (file.createNewFile()) {
                FileOutputStream fout = new FileOutputStream(file);
                serializer.serialize(fout, event);
                fout.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String name) {
        for (EventStructure event : allEvents()) {
            if (name.equals(event.getName())) {
                File file = new File(dir, fileName(event));
                return file.delete();
            }
        }
        return false;
    }

    @Override
    public boolean edit(String oldName, EventStructure event) {
        EventStructure oldEvent = get(oldName);
        if (delete(oldName)) {
            if (add(event))
                return true;
            else
                add(oldEvent);
        }
        return false;
    }

    List<EventStructure> allEvents() {
        List<EventStructure> list = new ArrayList<>();
        try {
            String[] files = dir.list();
            EventParser parser = new EventParser();
            for (String file : files) {
                try {
                    FileInputStream fin = new FileInputStream(new File(dir, file));
                    EventStructure event = parser.parse(fin);
                    list.add(event);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IllegalXmlException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    String fileName(EventStructure event) {
        String fn = "";
        fn += event.getName();
        fn += ".xml";
        return fn;
    }
}
