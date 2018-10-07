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

package ryey.easer.core.ui.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import java.util.Set;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.PluginDef;
import ryey.easer.core.RemoteOperationPluginInfo;
import ryey.easer.core.RemotePluginCommunicationHelper;
import ryey.easer.core.RemotePluginInfo;
import ryey.easer.plugins.LocalPluginRegistry;

public class PluginSettingsPreferenceFragment extends PreferenceFragment implements RemotePluginCommunicationHelper.OnOperationPluginListObtainedCallback {

    RemotePluginCommunicationHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.plugins_preference);

        helper = new RemotePluginCommunicationHelper(getActivity());
        helper.begin();

        PreferenceCategory preferenceCategory;
        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_operation_plugins));
        for (PluginDef plugin : LocalPluginRegistry.getInstance().operation().getAllPlugins()) {
            PluginEnabledPreference preference = new PluginEnabledPreference(getActivity(), plugin);
            preferenceCategory.addPreference(preference);
        }

        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_event_plugins));
        for (PluginDef plugin : LocalPluginRegistry.getInstance().event().getAllPlugins()) {
            PluginEnabledPreference preference = new PluginEnabledPreference(getActivity(), plugin);
            preferenceCategory.addPreference(preference);
        }

        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_condition_plugins));
        for (PluginDef plugin : LocalPluginRegistry.getInstance().condition().getAllPlugins()) {
            PluginEnabledPreference preference = new PluginEnabledPreference(getActivity(), plugin);
            preferenceCategory.addPreference(preference);
        }

        helper.asyncCurrentOperationPluginList(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.end();
    }

    @Override
    public void onListObtained(Set<RemoteOperationPluginInfo> operationPluginInfos) {
        PreferenceCategory preferenceCategory;
        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_remote_operation_plugins));
        for (RemotePluginInfo pluginInfo : operationPluginInfos) {
            Preference preference = new RemotePluginInfoPreference(getActivity(), pluginInfo);
            preferenceCategory.addPreference(preference);
        }
    }
}
