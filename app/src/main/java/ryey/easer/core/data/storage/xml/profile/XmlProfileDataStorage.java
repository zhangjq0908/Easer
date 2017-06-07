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

import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.FileUtils;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.core.data.storage.xml.event.XmlEventDataStorage;

public class XmlProfileDataStorage implements ProfileDataStorage {
    protected static XmlProfileDataStorage instance = null;

    protected static Context s_context = null;

    protected static File dir;

    public static XmlProfileDataStorage getInstance(Context context) throws IOException {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = FileUtils.getSubDir(s_context.getFilesDir(), "profile");
            instance = new XmlProfileDataStorage();
        }
        return instance;
    }

    XmlProfileDataStorage() {
    }

    @Override
    public List<String> list() {
        ArrayList<String> list = new ArrayList<>();
        for (ProfileStructure profile : allProfiles()) {
            list.add(profile.getName());
        }
        return list;
    }

    @Override
    public ProfileStructure get(String name) {
        for (ProfileStructure profile : allProfiles()) {
            if (name.equals(profile.getName()))
                return profile;
        }
        return null;
    }

    @Override
    public boolean add(ProfileStructure profile) {
        try {
            ProfileSerializer serializer = new ProfileSerializer();
            File file = new File(dir, fileName(profile));
            if (file.exists()) { // see if the existing one is invalid. If so, remove it in favor of the new one
                ProfileStructure existing = get(profile.getName());
                if ((existing == null) || (!existing.isValid())) {
                    file.delete(); //TODO?: handle return value
                }
            }
            if (file.createNewFile()) {
                FileOutputStream fout = new FileOutputStream(file);
                serializer.serialize(fout, profile);
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
        for (ProfileStructure profile : allProfiles()) {
            if (name.equals(profile.getName())) {
                File file = new File(dir, fileName(profile));
                return file.delete();
            }
        }
        return false;
    }

    @Override
    public boolean edit(String oldName, ProfileStructure profile) {
        ProfileStructure oldProfile = get(oldName);
        if (delete(oldName)) {
            if (add(profile)) {
                if (!oldName.equals(profile.getName())) {
                    try {
                        XmlEventDataStorage eventDataStorage = XmlEventDataStorage.getInstance(s_context);
                        eventDataStorage.handleProfileRename(oldName, profile.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            } else
                add(oldProfile);
        }
        return false;
    }

    List<ProfileStructure> allProfiles() {
        List<ProfileStructure> list = new ArrayList<>();
        try {
            String[] files = dir.list();
            ProfileParser parser = new ProfileParser();
            for (String file : files) {
                try {
                    FileInputStream fin = new FileInputStream(new File(dir, file));
                    ProfileStructure profile = parser.parse(fin);
                    list.add(profile);
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

    String fileName(ProfileStructure profile) {
        String fn = "";
        fn += profile.getName();
        fn += ".xml";
        return fn;
    }
}
