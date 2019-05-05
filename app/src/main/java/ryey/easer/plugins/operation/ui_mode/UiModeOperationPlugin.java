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

package ryey.easer.plugins.operation.ui_mode;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.PluginViewFragmentInterface;
import ryey.easer.commons.local_plugin.operationplugin.OperationDataFactory;
import ryey.easer.commons.local_plugin.operationplugin.OperationPlugin;
import ryey.easer.commons.local_plugin.operationplugin.PrivilegeUsage;
import ryey.easer.plugin.operation.Category;
import ryey.easer.plugins.operation.OperationLoader;

public class UiModeOperationPlugin implements OperationPlugin<UiModeOperationData> {

    @NonNull
    @Override
    public String id() {
        return "ui_mode";
    }

    @Override
    public int name() {
        return R.string.op_ui_mode;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @NonNull
    @Override
    public PrivilegeUsage privilege() {
        return PrivilegeUsage.no_root;
    }

    @Override
    public int maxExistence() {
        return 0;
    }

    @NonNull
    @Override
    public Category category() {
        return Category.system_config;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return true;
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
    }

    @NonNull
    @Override
    public OperationDataFactory<UiModeOperationData> dataFactory() {
        return new UiModeOperationDataFactory();

    }

    @NonNull
    @Override
    public PluginViewFragmentInterface<UiModeOperationData> view() {
        return new UiModePluginViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<UiModeOperationData> loader(@NonNull Context context) {
        return new UiModeLoader(context);
    }

}
