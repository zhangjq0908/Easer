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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.commons.local_skill.dynamics.DynamicsLink;

public final class AsyncHelper {

    public static class DelayedLoadProfileJobsWrapper {
        private final DelayedLoadProfileJobs jobLoadProfile = new DelayedLoadProfileJobs();
        private final ServiceConnection connection;

        public DelayedLoadProfileJobsWrapper() {
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    jobLoadProfile.onBind((ProfileLoaderService.PLSBinder) service);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    jobLoadProfile.onUnbind();
                }
            };
        }

        public DelayedLoadProfileJobsWrapper(ServiceConnection connection) {
            this.connection = connection;
        }

        public void bindService(Context context) {
            context.bindService(new Intent(context, ProfileLoaderService.class), connection, Context.BIND_AUTO_CREATE);
        }

        public void unbindService(Context context) {
            context.unbindService(connection);
        }

        public void triggerProfile(String profileName) {
            jobLoadProfile.triggerProfile(profileName);
        }

        public void triggerProfile(String profileName, String scriptName, Bundle dynamicsProperties, DynamicsLink dynamicsLink) {
            jobLoadProfile.triggerProfile(profileName, scriptName, dynamicsProperties, dynamicsLink);
        }
    }

    public static class DelayedLoadProfileJobs {

        private Lock lck_tasks = new ReentrantLock();

        private ProfileLoaderService.PLSBinder binder;

        private List<Task> tasksAfterConnect = new ArrayList<>();

        public void onBind(ProfileLoaderService.PLSBinder binder) {
            lck_tasks.lock();
            try {
                this.binder = binder;
                for (int i = tasksAfterConnect.size() - 1; i >= 0; i--) {
                    Task task = tasksAfterConnect.get(i);
                    load(binder, task);
                    tasksAfterConnect.remove(i);
                }
            } finally {
                lck_tasks.unlock();
            }
        }

        public void onUnbind() {
            lck_tasks.lock();
            try {
                this.binder = null;
            } finally {
                lck_tasks.unlock();
            }
        }

        private void doAfterSatisfied(Task task) {
            lck_tasks.lock();
            try {
                if (binder != null) {
                    load(binder, task);
                } else {
                    tasksAfterConnect.add(task);
                }
            } finally {
                lck_tasks.unlock();
            }
        }

        private static void load(ProfileLoaderService.PLSBinder binder, Task task) {
            if (task.scriptName == null) {
                binder.triggerProfile(task.profileName);
            } else {
                binder.triggerProfile(task.profileName, task.scriptName,
                        task.dynamicsProperties, task.dynamicsLink);
            }
        }

        public void triggerProfile(String profileName) {
            doAfterSatisfied(new Task(profileName, null, null, null));
        }
        public void triggerProfile(String profileName, String scriptName,
                                   Bundle dynamicsProperties, DynamicsLink dynamicsLink) {
            doAfterSatisfied(new Task(profileName, scriptName, dynamicsProperties, dynamicsLink));
        }

        private static class Task {
            @NonNull public final String profileName;
            @Nullable public final String scriptName;
            @Nullable public final Bundle dynamicsProperties;
            @Nullable public final DynamicsLink dynamicsLink;

            public Task(@NonNull String profileName, @Nullable String scriptName, @Nullable Bundle dynamicsProperties, @Nullable DynamicsLink dynamicsLink) {
                this.profileName = profileName;
                this.scriptName = scriptName;
                this.dynamicsProperties = dynamicsProperties;
                this.dynamicsLink = dynamicsLink;
            }
        }
    }

}
