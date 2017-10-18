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

import android.os.ConditionVariable;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.plugindef.PluginDef;
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

/*
 * Used to tell the app what plugins can be used.
 *
 * To register a new plugin, simply add a new line in the `init()` method of this class.
 */
final public class PluginRegistry {
    private static PluginRegistry instance = new PluginRegistry();
    private ConditionVariable initialised = new ConditionVariable();

    synchronized public static PluginRegistry getInstance() {
        return instance;
    }

    public static void init() {
        PluginRegistry pluginRegistry = PluginRegistry.getInstance();

        pluginRegistry.registerEventPlugin(TimeEventPlugin.class);
        pluginRegistry.registerEventPlugin(DateEventPlugin.class);
        pluginRegistry.registerEventPlugin(WifiEventPlugin.class);
        pluginRegistry.registerEventPlugin(CellLocationEventPlugin.class);
        pluginRegistry.registerEventPlugin(BatteryEventPlugin.class);
        pluginRegistry.registerEventPlugin(DayOfWeekEventPlugin.class);
        pluginRegistry.registerEventPlugin(BTDeviceEventPlugin.class);

        pluginRegistry.registerOperationPlugin(WifiOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(CellularOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(BluetoothOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(RotationOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(BroadcastOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(BrightnessOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(RingerModeOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(CommandOperationPlugin.class);
        pluginRegistry.registerOperationPlugin(HotspotOperationPlugin.class);
        //TODO: register plugins

        pluginRegistry.initialised.open();
    }

    class Registry<T extends PluginDef, T_data> {
        List<Class<? extends T>> pluginClassList = new ArrayList<>();
        List<T> pluginList = new ArrayList<>();

        synchronized public void registerPlugin(Class<? extends T> pluginClass) {
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
            initialised.block();
            return pluginClassList;
        }

        public final List<T> getPlugins() {
            initialised.block();
            return pluginList;
        }

        public T findPlugin(T_data data) {
            initialised.block();
            for (T plugin : getPlugins()) {
                if (data.getClass() == plugin.data().getClass()) {
                    return plugin;
                }
            }
            throw new IllegalAccessError();
        }

        public T findPlugin(String name) {
            initialised.block();
            for (T plugin : getPlugins()) {
                if (name.equals(plugin.name())) {
                    return plugin;
                }
            }
            throw new IllegalAccessError();
        }
    }

    Registry<EventPlugin, EventData> eventPluginRegistry = new Registry<>();
    Registry<OperationPlugin, OperationData> operationPluginRegistry = new Registry<>();

    private PluginRegistry() {}

    synchronized public void registerEventPlugin(Class<? extends EventPlugin> eventPluginClass) {
        eventPluginRegistry.registerPlugin(eventPluginClass);
    }

    synchronized public void registerOperationPlugin(Class<? extends OperationPlugin> operationPluginClass) {
        operationPluginRegistry.registerPlugin(operationPluginClass);
    }

    public final List<Class<? extends EventPlugin>> getEventPluginClasses() {
        return eventPluginRegistry.getPluginClasses();
    }

    public final List<EventPlugin> getEventPlugins() {
        return eventPluginRegistry.getPlugins();
    }

    public final List<Class<? extends OperationPlugin>> getOperationPluginClasses() {
        return operationPluginRegistry.getPluginClasses();
    }

    public final List<OperationPlugin> getOperationPlugins() {
        return operationPluginRegistry.getPlugins();
    }

    public EventPlugin findEventPlugin(EventData data) {
        return eventPluginRegistry.findPlugin(data);
    }

    public EventPlugin findEventPlugin(String name) {
        return eventPluginRegistry.findPlugin(name);
    }
}
