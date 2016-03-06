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

package ryey.easer.plugins;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.EventPlugin;
import ryey.easer.commons.ProfilePlugin;
import ryey.easer.plugins.event.celllocation.CellLocationEventPlugin;
import ryey.easer.plugins.event.date.DateEventPlugin;
import ryey.easer.plugins.event.time.TimeEventPlugin;
import ryey.easer.plugins.event.wifi.WifiEventPlugin;
import ryey.easer.plugins.profile.bluetooth.BluetoothProfilePlugin;
import ryey.easer.plugins.profile.cellular.CellularProfilePlugin;
import ryey.easer.plugins.profile.rotation.RotationProfilePlugin;
import ryey.easer.plugins.profile.wifi.WifiProfilePlugin;

public class PluginRegistry {
    private static PluginRegistry instance = new PluginRegistry();

    synchronized public static PluginRegistry getInstance() {
        return instance;
    }

    public static void init() {
        PluginRegistry pluginRegistry = PluginRegistry.getInstance();

        pluginRegistry.registerEventPlugin(TimeEventPlugin.class);
        pluginRegistry.registerEventPlugin(DateEventPlugin.class);
        pluginRegistry.registerEventPlugin(WifiEventPlugin.class);
        pluginRegistry.registerEventPlugin(CellLocationEventPlugin.class);

        pluginRegistry.registerProfilePlugin(WifiProfilePlugin.class);
        pluginRegistry.registerProfilePlugin(CellularProfilePlugin.class);
        pluginRegistry.registerProfilePlugin(BluetoothProfilePlugin.class);
        pluginRegistry.registerProfilePlugin(RotationProfilePlugin.class);
        //TODO: register plugins
    }

    List<Class<? extends EventPlugin>> eventPluginClassList = new ArrayList<>();
    List<EventPlugin> eventPluginList = new ArrayList<>();

    List<Class<? extends ProfilePlugin>> profilePluginClassList = new ArrayList<>();
    List<ProfilePlugin> profilePluginList = new ArrayList<>();

    private PluginRegistry() {}

    synchronized public void registerEventPlugin(Class<? extends EventPlugin> eventPluginClass) {
        for (Class<? extends EventPlugin> klass : eventPluginClassList) {
            if (klass == eventPluginClass)
                return;
        }
        eventPluginClassList.add(eventPluginClass);
        try {
            EventPlugin plugin = eventPluginClass.newInstance();
            eventPluginList.add(plugin);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    synchronized public void registerProfilePlugin(Class<? extends ProfilePlugin> profilePluginClass) {
        for (Class<? extends ProfilePlugin> klass : profilePluginClassList) {
            if (klass == profilePluginClass)
                return;
        }
        profilePluginClassList.add(profilePluginClass);
        try {
            ProfilePlugin plugin = profilePluginClass.newInstance();
            profilePluginList.add(plugin);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    synchronized public final List<Class<? extends EventPlugin>> getEventPluginClasses() {
        return eventPluginClassList;
    }

    synchronized public final List<EventPlugin> getEventPlugins() {
        return eventPluginList;
    }

    synchronized public final List<Class<? extends ProfilePlugin>> getProfilePluginClasses() {
        return profilePluginClassList;
    }

    synchronized public final List<ProfilePlugin> getProfilePlugins() {
        return profilePluginList;
    }
}
