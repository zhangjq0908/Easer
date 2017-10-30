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

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.xml.NC;

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
    public boolean has(String name) {
        return IOUtils.fileExists(dir, name + NC.SUFFIX);
    }

    @Override
    public List<String> list() {
        List<String> list = new ArrayList<>();
        for (EventStructure event : all()) {
            list.add(event.getName());
        }
        return list;
    }

    @Override
    public EventStructure get(String name) throws IllegalStorageDataException {
        File file = new File(dir, name + NC.SUFFIX);
        return get(file);
    }

    private EventStructure get(File file) throws IllegalStorageDataException {
        EventParser parser = new EventParser();
        try {
            FileInputStream fin = new FileInputStream(file);
            EventStructure eventStructure = parser.parse(fin);
            fin.close();
            return eventStructure;
        } catch (FileNotFoundException e) {
            Logger.e(e, "event file <%s> exists when listing but disappears when reading", file);
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalAccessError();
    }

    @Override
    public void add(EventStructure event) throws IOException {
        EventSerializer serializer = new EventSerializer();
        File file = new File(dir, event.getName() + NC.SUFFIX);
        FileOutputStream fout = new FileOutputStream(file);
        serializer.serialize(fout, event);
        fout.close();
    }

    @Override
    public void delete(String name) {
        File file = new File(dir, name + NC.SUFFIX);
        if (!file.delete())
            throw new IllegalStateException("Unable to delete file " + file);
    }

    @Override
    public void update(EventStructure event) throws IOException {
        delete(event.getName());
        add(event);
    }

    /*
     * Rescan and reload from the directory everytime
     */
    @Override
    public List<EventStructure> all() {
        List<EventStructure> list = new ArrayList<>();
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile()) {
                    if (pathname.getName().endsWith(NC.SUFFIX)) {
                        return true;
                    }
                }
                return false;
            }
        });
        for (File file : files) {
            try {
                list.add(get(file));
            } catch (IllegalStorageDataException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

}
