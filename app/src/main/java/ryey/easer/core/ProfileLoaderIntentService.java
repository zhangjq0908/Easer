/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.util.Collection;

import ryey.easer.commons.dynamics.DynamicsLink;
import ryey.easer.commons.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.core.dynamics.CoreDynamics;
import ryey.easer.core.dynamics.CoreDynamicsInterface;
import ryey.easer.core.log.ActivityLogService;
import ryey.easer.plugins.PluginRegistry;

public class ProfileLoaderIntentService extends IntentService {
    public static final String ACTION_LOAD_PROFILE = "ryey.easer.action.LOAD_PROFILE";

    public static final String EXTRA_PROFILE_NAME = "ryey.easer.extra.PROFILE_NAME";
    public static final String EXTRA_SCRIPT_NAME = "ryey.easer.extra.EVENT_NAME";

    public static void triggerProfile(Context context, String profileName) {
        Intent intent = new Intent(context, ProfileLoaderIntentService.class);
        intent.setAction(ProfileLoaderIntentService.ACTION_LOAD_PROFILE);
        intent.putExtra(ProfileLoaderIntentService.EXTRA_PROFILE_NAME, profileName);
        context.startService(intent);
    }

    public ProfileLoaderIntentService() {
        super("ProfileLoaderIntentService");
    }

    @Override
    public void onCreate() {
        Logger.v("ProfileLoaderIntentService onCreate()");
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            Logger.wtf("ProfileLoaderIntentService got null Intent to handle");
            return;
        }
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
            Logger.wtf("ProfileLoaderIntentService got unknown Intent action <%s>", action);
        }
    }

    private void handleActionLoadProfile(@NonNull String name, @Nullable String event, @NonNull Bundle extras) {
        Logger.d("Loading profile <%s> by <%s>", name, event);
        ProfileStructure profile;
        ProfileDataStorage storage = ProfileDataStorage.getInstance(this);
        profile = storage.get(name);

        DynamicsLink dynamicsLink = extras.getParcelable(Lotus.EXTRA_DYNAMICS_LINK);
        Bundle macroData = extras.getBundle(Lotus.EXTRA_DYNAMICS_PROPERTIES);
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

        if (profile != null) {
            boolean loaded = false;
            for (OperationPlugin plugin : PluginRegistry.getInstance().operation().getEnabledPlugins(this)) {
                OperationLoader loader = plugin.loader(getApplicationContext());
                Collection<OperationData> possibleData = profile.get(plugin.id());
                if (possibleData != null) {
                    for (OperationData data : possibleData) {
                        try {
                            //noinspection unchecked
                            if (loader.load(data.applyDynamics(solidMacroAssignment)))
                                loaded = true;
                        } catch (RuntimeException e) {
                            Logger.e(e, "error while loading operation <%s> for profile <%s>", data.getClass().getSimpleName(), profile.getName());
                        }
                    }
                }
            }
            String extraInfo;
            if (loaded) {
                extraInfo = null;
                Logger.i("Profile <%s> loaded", name);
            } else {
                extraInfo = "Profile failed to load in full";
                Logger.w("Profile <%s> not loaded (none of the operations successfully loaded", name);
            }
            if (event == null) {
                ActivityLogService.Companion.notifyProfileLoaded(this, name, extraInfo);
            } else {
                ActivityLogService.Companion.notifyScriptSatisfied(this, event, name, extraInfo);
            }
        }
    }
}
