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

package ryey.easer.plugins;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.plugins.event.battery.BatteryEventPlugin;
import ryey.easer.plugins.event.bluetooth_device.BTDeviceEventPlugin;
import ryey.easer.plugins.event.celllocation.CellLocationEventPlugin;
import ryey.easer.plugins.event.date.DateEventPlugin;
import ryey.easer.plugins.event.dayofweek.DayOfWeekEventPlugin;
import ryey.easer.plugins.event.time.TimeEventPlugin;
import ryey.easer.plugins.event.wifi.WifiEventPlugin;
import ryey.easer.plugins.operation.bluetooth.BluetoothOperationPlugin;
import ryey.easer.plugins.operation.brightness.BrightnessOperationPlugin;
import ryey.easer.plugins.operation.broadcast.BroadcastOperationPlugin;
import ryey.easer.plugins.operation.cellular.CellularOperationPlugin;
import ryey.easer.plugins.operation.command.CommandOperationPlugin;
import ryey.easer.plugins.operation.hotspot.HotspotOperationPlugin;
import ryey.easer.plugins.operation.ringer_mode.RingerModeOperationPlugin;
import ryey.easer.plugins.operation.rotation.RotationOperationPlugin;
import ryey.easer.plugins.operation.wifi.WifiOperationPlugin;

/**
 * Used to tell the app what plugins can be used.
 *
 * To register a new plugin, simply add a new line in the constructor of this class.
 */
final public class PluginRegistry {

    private Registry<EventPlugin, EventData> eventPluginRegistry = new Registry<>();
    private Registry<OperationPlugin, OperationData> operationPluginRegistry = new Registry<>();

    {
        event().registerPlugin(TimeEventPlugin.class);
        event().registerPlugin(DateEventPlugin.class);
        event().registerPlugin(WifiEventPlugin.class);
        event().registerPlugin(CellLocationEventPlugin.class);
        event().registerPlugin(BatteryEventPlugin.class);
        event().registerPlugin(DayOfWeekEventPlugin.class);
        event().registerPlugin(BTDeviceEventPlugin.class);

        operation().registerPlugin(WifiOperationPlugin.class);
        operation().registerPlugin(CellularOperationPlugin.class);
        operation().registerPlugin(BluetoothOperationPlugin.class);
        operation().registerPlugin(RotationOperationPlugin.class);
        operation().registerPlugin(BroadcastOperationPlugin.class);
        operation().registerPlugin(BrightnessOperationPlugin.class);
        operation().registerPlugin(RingerModeOperationPlugin.class);
        operation().registerPlugin(CommandOperationPlugin.class);
        operation().registerPlugin(HotspotOperationPlugin.class);
        //TODO: add more plugins
    }

    private static PluginRegistry instance = new PluginRegistry();

    public static PluginRegistry getInstance() {
        return instance;
    }

    private PluginRegistry() {}

    public Registry<EventPlugin, EventData> event() {
        return eventPluginRegistry;
    }

    public Registry<OperationPlugin, OperationData> operation() {
        return operationPluginRegistry;
    }

    public static class Registry<T extends PluginDef, T_data extends StorageData> {
        List<Class<? extends T>> pluginClassList = new ArrayList<>();
        List<T> pluginList = new ArrayList<>();

        synchronized void registerPlugin(Class<? extends T> pluginClass) {
            for (Class<? extends T> klass : pluginClassList) {
                if (klass == pluginClass)
                    return;
            }
            pluginClassList.add(pluginClass);
            try {
                T plugin = pluginClass.newInstance();
                pluginList.add(plugin);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public final List<Class<? extends T>> getPluginClasses() {
            return pluginClassList;
        }

        public final List<T> getPlugins() {
            return pluginList;
        }

        public T findPlugin(T_data data) {
            for (T plugin : getPlugins()) {
                if (data.getClass() == plugin.data().getClass()) {
                    return plugin;
                }
            }
            throw new IllegalAccessError();
        }

        public T findPlugin(String name) {
            for (T plugin : getPlugins()) {
                if (name.equals(plugin.name())) {
                    return plugin;
                }
            }
            throw new IllegalAccessError();
        }
    }
}
