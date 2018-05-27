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

package ryey.easer.core.ui.data;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.PluginRegistry;

public class EventPluginViewContainerFragment<T extends EventData> extends PluginViewContainerFragment<T> {

    private static final String EXTRA_PLUGIN = "plugin";

    static <T extends EventData> EventPluginViewContainerFragment<T> createInstance(EventPlugin<T> plugin) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PLUGIN, plugin.id());
        EventPluginViewContainerFragment<T> fragment = new EventPluginViewContainerFragment<>();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String plugin_id = getArguments().getString(EXTRA_PLUGIN);
        @SuppressWarnings("unchecked") EventPlugin<T> plugin = PluginRegistry.getInstance().event().findPlugin(plugin_id);
        pluginViewFragment = plugin.view();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pluginview_event, container, false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_pluginview, pluginViewFragment)
                .commit();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventPlugin plugin = PluginRegistry.getInstance().event().findPlugin(pluginViewFragment);
        //noinspection ConstantConditions
        if (!plugin.checkPermissions(getContext())) {
            setEnabled(false);
            //noinspection ConstantConditions
            plugin.requestPermissions(getActivity(), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            setEnabled(true);
        }
    }

    /***
     * {@inheritDoc}
     * Explicitly override and call back through to snooze compiler data type checking
     */
    @NonNull
    @Override
    T getData() throws InvalidDataInputException {
        return super.getData();
    }

    private void setEnabled(boolean enabled) {
        pluginViewFragment.setEnabled(enabled);
    }
}
