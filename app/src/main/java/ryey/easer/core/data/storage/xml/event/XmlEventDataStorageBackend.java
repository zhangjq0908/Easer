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
import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.storage.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.FileUtils;
import ryey.easer.core.data.storage.StorageHelper;

public class XmlEventDataStorageBackend implements EventDataStorageBackendInterface {
    private static XmlEventDataStorageBackend instance = null;

    private static Context s_context = null;

    private static File dir;

    public static XmlEventDataStorageBackend getInstance(Context context) throws IOException {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = FileUtils.getSubDir(s_context.getFilesDir(), "event");
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
            File file = new File(dir, fileName(event));
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
        return _delete(name, true);
    }

    private boolean _delete(String name, boolean check_is_leaf) {
        if (check_is_leaf) {
            if (StorageHelper.eventParentMap(allEvents()).containsKey(name)) { //if is not leaf node
                //TODO: add alerts or remove the whole subtree
                Logger.w("blocked the attempt to remove an event <%s> with subtrees", name);
                return false;
            }
        }
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
        //TODO: use return value
        String newName = event.getName();
        if (!oldName.equals(newName)) {
            // alter subnodes to point to the new name
            List<EventStructure> subs = StorageHelper.eventParentMap(allEvents()).get(oldName);
            if (subs != null) {
                for (EventStructure sub : subs) {
                    sub.setParentName(newName);
                    edit(sub.getName(), sub);
                }

            }
        }
        EventStructure oldEvent = get(oldName);
        if (_delete(oldName, false)) {
            if (add(event))
                return true;
            else
                return add(oldEvent);
        }
        return false;
    }

    /*
     * Rescan and reload from the directory everytime
     */
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

    /*
     * TODO: Combine different trees which have the same parent (probably by comparing their data)
     */
    @Override
    public List<EventTree> getEventTrees() {
        return StorageHelper.eventListToTrees(allEvents());
    }

    @Override
    public boolean handleProfileRename(String oldName, String newName) {
        for (EventStructure event : allEvents()) {
            if (oldName.equals(event.getProfileName())) {
                event.setProfileName(newName);
                edit(event.getName(), event);
            }
        }
        return true;
    }

    private static String fileName(EventStructure event) {
        String fn = "";
        fn += event.getName();
        fn += ".xml";
        return fn;
    }
}
