/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.commons.local_skill.dynamics.DynamicsLink;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.RemoteLocalOperationDataWrapper;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.core.dynamics.CoreDynamics;
import ryey.easer.core.dynamics.CoreDynamicsInterface;
import ryey.easer.core.log.ActivityLogService;

import static ryey.easer.core.Lotus.EXTRA_DYNAMICS_LINK;
import static ryey.easer.core.Lotus.EXTRA_DYNAMICS_PROPERTIES;

public class ProfileLoaderService extends Service {
    public static final String ACTION_LOAD_PROFILE = "ryey.easer.action.LOAD_PROFILE";

    public static final String EXTRA_PROFILE_NAME = "ryey.easer.extra.PROFILE_NAME";
    public static final String EXTRA_SCRIPT_NAME = "ryey.easer.extra.EVENT_NAME";

    private SkillHelper.OperationHelper helper;

    private IntentFilter intentFilter = new IntentFilter(ACTION_LOAD_PROFILE);
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_LOAD_PROFILE.equals(action)) {
                final String name = intent.getStringExtra(EXTRA_PROFILE_NAME);
                final String event = intent.getStringExtra(EXTRA_SCRIPT_NAME);
                if (intent.getExtras() == null) {
                    Logger.wtf("ProfileLoaderIntent has null extras???");
                    throw new IllegalStateException("ProfileLoaderIntent has null extras???");
                }
                handleActionLoadProfile(name, event, intent.getExtras());
            } else {
                Logger.wtf("ProfileLoaderService got unknown Intent action <%s>", action);
            }
        }
    };

    @Override
    public void onCreate() {
        Logger.v("ProfileLoaderService onCreate()");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
        helper = new SkillHelper.OperationHelper(this);
        helper.begin();
        ServiceUtils.Companion.startNotification(this);
    }

    @Override
    public void onDestroy() {
        ServiceUtils.Companion.stopNotification(this);
        helper.end();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PLSBinder();
    }

    public class PLSBinder extends Binder {
        public void triggerProfile(String profileName) {
            Bundle extras = new Bundle();
            extras.putString(ProfileLoaderService.EXTRA_PROFILE_NAME, profileName);
            handleActionLoadProfile(profileName, null, extras);
        }
        public void triggerProfile(String profileName, String scriptName,
                                          Bundle dynamicsProperties, DynamicsLink dynamicsLink) {
            Bundle extras = new Bundle();
            extras.putString(ProfileLoaderService.EXTRA_PROFILE_NAME, profileName);
            extras.putString(ProfileLoaderService.EXTRA_SCRIPT_NAME, scriptName);
            if (dynamicsProperties.containsKey(EXTRA_DYNAMICS_PROPERTIES)) //TODO: Make the semantics better so this if condition is not needed
                extras.putAll(dynamicsProperties);
            else
                extras.putParcelable(EXTRA_DYNAMICS_PROPERTIES, dynamicsProperties);
            extras.putParcelable(EXTRA_DYNAMICS_LINK, dynamicsLink);
            handleActionLoadProfile(profileName, scriptName, extras);
        }
    }

    private void handleActionLoadProfile(@NonNull String name, @Nullable String event, @NonNull Bundle extras) {
        Logger.d("Loading profile <%s> by <%s>", name, event);
        ProfileStructure profile;
        ProfileDataStorage storage = new ProfileDataStorage(this);
        profile = storage.get(name);
        assert profile != null;

        DynamicsLink dynamicsLink = extras.getParcelable(EXTRA_DYNAMICS_LINK);
        Bundle macroData = extras.getBundle(EXTRA_DYNAMICS_PROPERTIES);
        if (dynamicsLink == null)
            dynamicsLink = new DynamicsLink();
        if (macroData == null)
            macroData = new Bundle();
        for (CoreDynamicsInterface dynamics : CoreDynamics.coreDynamics()) {
            if (dynamicsLink.identityMap().containsValue(dynamics.id())) {
                macroData.putString(dynamics.id(), dynamics.invoke(this, extras));
            }
        }
        final SolidDynamicsAssignment solidMacroAssignment = dynamicsLink.assign(macroData);

        LoadWatcher profileLoadWatcher = new LoadWatcher(this, name, event);

        new Thread(profileLoadWatcher).start();

        for (String pluginId : profile.pluginIds()) {
            Collection<RemoteLocalOperationDataWrapper> dataWrappers = profile.get(pluginId);
            for (RemoteLocalOperationDataWrapper dataWrapper : dataWrappers) {
                UUID jobId = UUID.randomUUID();
                profileLoadWatcher.addJob(jobId, pluginId);
                helper.triggerOperation(jobId, pluginId, dataWrapper, solidMacroAssignment, profileLoadWatcher);
            }
        }
        profileLoadWatcher.finishInit();
    }

    /**
     * Self-made concurrent task counter
     * There ought to be existing classes for this purpose, but I didn't find any.
     * TODO: replace with existing class
     */
    class LoadWatcher implements Runnable, SkillHelper.OperationHelper.OnOperationLoadResultCallback {

        private final Context context;
        private final String name;
        private final String event;

        private final Map<UUID, String> jobMap = new ArrayMap<>();
        private final Set<String> failedSkills = new ArraySet<>();
        private final Set<String> unknownSkills = new ArraySet<>();

        private final ReentrantLock lock = new ReentrantLock();
        private final AtomicInteger taskCount = new AtomicInteger();
        private final AtomicInteger finishedCount = new AtomicInteger();

        LoadWatcher(Context context, String name, String event) {
            this.context = context;
            this.name = name;
            this.event = event;
            lock.lock();
        }

        @Override
        public void run() {
            lock.lock();
            try {
            } finally {
                lock.unlock();
            } // Check if initialization has completed
            while (finishedCount.get() < taskCount.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            allResultsCollected();
        }

        @Override
        public void onResult(@NonNull UUID id, @Nullable Boolean success) {
            String skillId = jobMap.get(id);
            if (skillId != null) {
                lock.lock();
                try {
                    jobMap.remove(id);
                    if (success == null) {
                        unknownSkills.add(skillId);
                    } else if (!success) {
                        failedSkills.add(skillId);
                    }
                    finishedCount.incrementAndGet();
                } finally {
                    lock.unlock();
                }
            }
        }

        void addJob(UUID jobId, String pluginId) {
            jobMap.put(jobId, pluginId);
            taskCount.incrementAndGet();
        }

        void finishInit() {
            lock.unlock();
        }

        private void allResultsCollected() {
            lock.lock(); // Ensure no onResult() is running
            try {
                StringBuilder extraInfoBuilder = new StringBuilder();
                if (unknownSkills.size() == 0 && failedSkills.size() == 0) {
                    Logger.i("Profile <%s> loaded", name);
                } else {
                    if (unknownSkills.size() > 0) {
                        extraInfoBuilder.append("Unknown results from skills:");
                        for (String id : unknownSkills) {
                            extraInfoBuilder.append(" ").append(id);
                        }
                        extraInfoBuilder.append("\n");
                        Logger.i("Profile <%s> has unidentified Operation load results", name);
                    }
                    if (failedSkills.size() > 0) {
                        extraInfoBuilder.append("Failed:");
                        for (String id : failedSkills) {
                            extraInfoBuilder.append(" ").append(id);
                        }
                        Logger.w("Profile <%s> has Operations failed to load", name);
                    }
                }
                String extraInfo = extraInfoBuilder.toString();
                if (event == null) {
                    ActivityLogService.Companion.notifyProfileLoaded(context, name, extraInfo);
                } else {
                    ActivityLogService.Companion.notifyScriptSatisfied(context, event, name, extraInfo);
                }
            } finally {
                lock.unlock();
            }
        }
    }

}
