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
