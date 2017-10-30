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

package ryey.easer.core.data.storage.backend.xml.event;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.IOUtils;

public class XmlEventDataStorageBackend implements EventDataStorageBackendInterface {
    private static XmlEventDataStorageBackend instance = null;

    private static Context s_context = null;

    private static File dir;

    public static XmlEventDataStorageBackend getInstance(Context context) {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = IOUtils.mustGetSubDir(s_context.getFilesDir(), "event");
            instance = new XmlEventDataStorageBackend();
        }
        return instance;
    }

    private XmlEventDataStorageBackend() {
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
            File file = new File(dir, event.getName() + ".xml");
            if (file.exists()) { // see if the existing one is invalid. If so, remove it in favor of the new one
                EventStructure existing = get(event.getName());
                if ((existing == null) || (!existing.isValid())) {
                    Logger.i("replace an invalid existing event with the same filename <%s>", file.getName());
                    boolean success = file.delete();
                    if (!success) {
                        Logger.e("failed to remove existing file <%s>", file.getName());
                    }
                }
            }
            if (file.createNewFile()) {
                FileOutputStream fout = new FileOutputStream(file);
                serializer.serialize(fout, event);
                fout.close();
                return true;
            }
        } catch (IOException e) {
            Logger.e(e, "IOException during `XmlEventDataStorageBackend.add()` (not sure the exact location)");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String name) {
        File file = new File(dir, name + ".xml");
        return file.delete();
    }

    @Override
    public boolean update(EventStructure event) {
        return delete(event.getName()) && add(event);
    }

    /*
     * Rescan and reload from the directory everytime
     */
    @Override
    public List<EventStructure> allEvents() {
        List<EventStructure> list = new ArrayList<>();
        try {
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isFile()) {
                        if (pathname.getName().endsWith(".xml")) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            EventParser parser = new EventParser();
            for (File file : files) {
                try {
                    FileInputStream fin = new FileInputStream(file);
                    EventStructure event = parser.parse(fin);
                    list.add(event);
                } catch (FileNotFoundException e) {
                    Logger.e(e, "event file <%s> exists when listing but disappears when reading", file);
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IllegalXmlException e) {
                    Logger.e(e, "event file <%s> has illegal data", file);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            Logger.e(e, "failed to list files in dir <%s> (in app internal storage)", dir);
            e.printStackTrace();
        }

        return list;
    }

}
