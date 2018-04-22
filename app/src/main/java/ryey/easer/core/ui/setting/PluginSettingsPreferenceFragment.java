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
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.plugins.PluginRegistry;

public class PluginSettingsPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.plugins_preference);
        PreferenceCategory preferenceCategory;
        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_operation_plugins));
        for (PluginDef plugin : PluginRegistry.getInstance().operation().getAllPlugins()) {
            PluginEnabledPreference preference = new PluginEnabledPreference(getActivity(), plugin);
            preferenceCategory.addPreference(preference);
        }

        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_event_plugins));
        for (PluginDef plugin : PluginRegistry.getInstance().event().getAllPlugins()) {
            PluginEnabledPreference preference = new PluginEnabledPreference(getActivity(), plugin);
            preferenceCategory.addPreference(preference);
        }
    }
}
