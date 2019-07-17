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

package ryey.easer.core.ui.data;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.StorageData;

public abstract class SourceSkillViewContainerFragment<D extends StorageData, S extends Skill>
        extends SkillViewContainerFragment<D> {

    private static final String EXTRA_SKILL = "skill";

    protected static <D extends StorageData, S extends Skill<D>, F extends SourceSkillViewContainerFragment<D, S>> F createInstance(
            @NonNull S skill,
            @NonNull F fragment) {
        return createInstance(skill, fragment, null);
    }

    protected static <D extends StorageData, S extends Skill<D>, F extends SourceSkillViewContainerFragment<D, S>> F createInstance(
            @NonNull S skill,
            @NonNull F fragment,
            @Nullable Bundle bundle) {
        return createInstance(skill.id(), fragment, bundle);
    }

    protected static <D extends StorageData, S extends Skill<D>, F extends SourceSkillViewContainerFragment<D, S>> F createInstance(
            @NonNull String skillId,
            @NonNull F fragment,
            @Nullable Bundle bundle) {
        if (bundle == null)
            bundle = new Bundle();
        bundle.putString(EXTRA_SKILL, skillId);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected String skillID = null;

    protected abstract S findSkill(String skillID);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skillID = getArguments().getString(EXTRA_SKILL);
        S skill = findSkill(skillID);
        pluginViewFragment = skill.view();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_skillview_container_source, container, false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_pluginview, pluginViewFragment)
                .commit();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        S plugin = findSkill(skillID);
        //noinspection ConstantConditions
        if (plugin.checkPermissions(getContext()) == Boolean.FALSE) {
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
    public D getData() throws InvalidDataInputException {
        return super.getData();
    }

    private void setEnabled(boolean enabled) {
        pluginViewFragment.setEnabled(enabled);
    }
}
