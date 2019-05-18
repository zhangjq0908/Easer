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

package ryey.easer.core.ui.data.condition;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.core.ui.data.SkillViewContainerFragment;
import ryey.easer.skills.LocalSkillRegistry;

public class ConditionSkillViewContainerFragment<T extends ConditionData> extends SkillViewContainerFragment<T> {

    private static final String EXTRA_PLUGIN = "plugin";

    static <T extends ConditionData> ConditionSkillViewContainerFragment<T> createInstance(ConditionSkill<T> plugin) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PLUGIN, plugin.id());
        ConditionSkillViewContainerFragment<T> fragment = new ConditionSkillViewContainerFragment<>();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String plugin_id = getArguments().getString(EXTRA_PLUGIN);
        @SuppressWarnings("unchecked") ConditionSkill<T> plugin = LocalSkillRegistry.getInstance().condition().findSkill(plugin_id);
        pluginViewFragment = plugin.view();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pluginview_condition, container, false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_pluginview, pluginViewFragment)
                .commit();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        ConditionSkill plugin = LocalSkillRegistry.getInstance().condition().findSkill(pluginViewFragment);
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
    public T getData() throws InvalidDataInputException {
        return super.getData();
    }

    private void setEnabled(boolean enabled) {
        pluginViewFragment.setEnabled(enabled);
    }
}
