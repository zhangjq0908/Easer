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

package ryey.easer.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.Lotus;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.DataStorage;
import ryey.easer.core.data.storage.xml.event.EventXmlDataStorage;
import ryey.easer.commons.AbstractSlot;
import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;

public class EHService extends Service {
    public static final String ACTION_RELOAD = "ryey.easer.action.RELOAD";

    public static final String ACTION_STATE_CHANGED = "ryey.easer.action.STATE_CHANGED";

    List<EventStructure> mEvents;
    List<Lotus> mLotus;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(EHService.class.getSimpleName(), "Received broadcast: " + action);
            switch (action) {
                case ACTION_RELOAD:
                    reloadTriggers();
            }
        }
    };

    private static boolean running = false;

    {
        mLotus = new ArrayList<>();
    }

    public static boolean isRunning() {
        return running;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EHService.class);
        context.startService(intent);
    }

    public static void reload(Context context) {
        Intent intent = new Intent();
        intent.setAction(EHService.ACTION_RELOAD);
        context.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        Log.d(getClass().getSimpleName(), "onCreate");
        running = true;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        super.onCreate();
        reloadTriggers();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RELOAD);
        registerReceiver(mReceiver, filter);
        PluginRegistry.init();
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
        unregisterReceiver(mReceiver);
        running = false;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
    }

    private void reloadTriggers() {
        Log.d(getClass().getSimpleName(), "reloadTriggers");
        mGetEvents();
        mCancelTriggers();
        mSetTriggers();
    }

    private void mCancelTriggers() {
        for (Lotus lotus : mLotus) {
            lotus.cancel();
        }
        mLotus.clear();
    }

    private void mGetEvents() {
        List<EventStructure> events = new ArrayList<>();
        DataStorage<EventStructure> storage = null;
        try {
            storage = EventXmlDataStorage.getInstance(this);
            for (String name : storage.list()) {
                events.add(storage.get(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEvents = events;
    }

    private void mSetTriggers() {
        Log.d(getClass().getSimpleName(), "setting triggers");
        for (EventStructure event : mEvents) {
            Log.d(getClass().getSimpleName(), "  setting: " + event.getName());
            Lotus lotus = new Lotus(this, event.getName(), event.getProfile());

            for (EventPlugin plugin : PluginRegistry.getInstance().getEventPlugins()) {
                EventData data = event.get(plugin.name());
                if (data != null && data.isValid()) {
                    AbstractSlot slot = plugin.slot(this);
                    slot.set(data);
                    lotus.register(slot);
                }
            }

            lotus.apply();
            Log.d(getClass().getSimpleName(), "  " + event.getName() + " is set");
            mLotus.add(lotus);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
