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

package ryey.easer.core.ui.data.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.C;
import ryey.easer.commons.CommonSkillHelper;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.core.RemotePluginCommunicationHelper;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.RemoteLocalOperationDataWrapper;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.core.ui.data.AbstractEditDataActivity;
import ryey.easer.skills.LocalSkillRegistry;
import ryey.easer.remote_plugin.RemoteOperationData;

public class EditProfileActivity extends AbstractEditDataActivity<ProfileStructure, ProfileDataStorage> implements OperationSelectorFragment.SelectedListener {

    static {
        TAG_DATA_TYPE = "profile";
    }

    RemotePluginCommunicationHelper helper;

    EditText editText_profile_name = null;

    OperationSelectorFragment operationSelectorFragment;
    List<OperationSkillViewContainerFragment<?>> operationViewList = new ArrayList<>();
    List<RemoteOperationSkillViewContainerFragment> remoteOperationViewList = new ArrayList<>();

    @Override
    protected ProfileDataStorage retDataStorage() {
        return new ProfileDataStorage(this);
    }

    @Override
    protected String title() {
        return getString(R.string.title_profile);
    }

    @Override
    protected int contentViewRes() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new RemotePluginCommunicationHelper(this);
        helper.begin();
    }

    @Override
    protected void init() {
        editText_profile_name = findViewById(R.id.editText_profile_title);
        operationSelectorFragment = new OperationSelectorFragment();
        operationSelectorFragment.setSelectedListener(this);
        findViewById(R.id.button_add_operation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationSelectorFragment.show(getSupportFragmentManager(), "add_op");
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            transaction.remove(fragment);
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.end();
    }

    @Override
    protected void loadFromData(ProfileStructure profile) {
        editText_profile_name.setText(oldName);

        clearPluginView();

        LocalSkillRegistry.Registry<OperationSkill, OperationData> operationRegistry = LocalSkillRegistry.getInstance().operation();
        for (String pluginId : profile.pluginIds()) {
            Collection<RemoteLocalOperationDataWrapper> operationDataCollection = profile.get(pluginId);
            if (operationRegistry.hasSkill(pluginId)) {
                if (CommonSkillHelper.isEnabled(this, CommonSkillHelper.TYPE_OPERATION, pluginId)) {
                    OperationSkill plugin = operationRegistry.findSkill(pluginId);
                    for (RemoteLocalOperationDataWrapper dataWrapper : operationDataCollection) {
                        addAndFillLocalPluginView(plugin, dataWrapper.localData);
                    }
                }
            } else {
                for (RemoteLocalOperationDataWrapper dataWrapper : operationDataCollection) {
                    addAndFillRemotePluginView(pluginId, dataWrapper.remoteData);
                }
            }
        }
    }

    @Override
    protected ProfileStructure saveToData() throws InvalidDataInputException {
        ProfileStructure profile = new ProfileStructure(C.VERSION_CREATED_IN_RUNTIME);
        profile.setName(editText_profile_name.getText().toString());

        for (OperationSkillViewContainerFragment<?> fragment : operationViewList) {
            if (!fragment.isEnabled())
                continue;
            try {
                OperationData data = fragment.getData();
                if (!data.isValid())
                    throw new InvalidDataInputException();
                fragment.setHighlight(false);
                String id = LocalSkillRegistry.getInstance().operation().findSkill(data).id();
                profile.put(id, data);
            } catch (InvalidDataInputException e) {
                fragment.setHighlight(true);
                return null;
            }
        }

        for (RemoteOperationSkillViewContainerFragment fragment : remoteOperationViewList) {
            if (!fragment.isEnabled())
                continue;
            try {
                RemoteOperationData data = fragment.getData();
                fragment.setHighlight(false);
                String id = fragment.id();
                profile.put(id, data);
            } catch (InvalidDataInputException e) {
                fragment.setHighlight(true);
                return null;
            }
        }

        return profile;
    }

    synchronized void clearPluginView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : operationViewList) {
            transaction.remove(fragment);
        }
        operationViewList.clear();
        for (Fragment fragment : remoteOperationViewList) {
            transaction.remove(fragment);
        }
        remoteOperationViewList.clear();
        transaction.commit();
    }

    synchronized <T extends OperationData> void addAndFillLocalPluginView(OperationSkill<T> plugin, T data) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        OperationSkillViewContainerFragment<T> fragment = OperationSkillViewContainerFragment.createInstance(plugin);
        transaction.add(R.id.layout_profiles, fragment, plugin.id());
        operationViewList.add(fragment);
        operationSelectorFragment.addSelectedPlugin(plugin);
        transaction.commit();
        fragment.fill(data);
    }

    synchronized void addAndFillRemotePluginView(@NonNull String id, @Nullable RemoteOperationData data) {
        RemoteOperationSkillViewContainerFragment fragment = RemoteOperationSkillViewContainerFragment.createInstance(id, data);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_profiles, fragment);
        remoteOperationViewList.add(fragment);
        transaction.commit();
    }

//    synchronized SkillViewContainerFragment[] addPluginView(OperationSkill[] plugins) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        SkillViewContainerFragment[] fragments = new SkillViewContainerFragment[plugins.length];
//        for (int i = 0; i < plugins.length; i++) {
//            OperationSkill plugin = plugins[i];
//            OperationSkillViewContainerFragment fragment = OperationSkillViewContainerFragment.createInstance(plugin);
//            transaction.add(R.id.layout_profiles, fragment, plugin.id());
//            fragments[i] = fragment;
//            operationViewList.add(fragment);
//            operationSelectorFragment.addSelectedPlugin(plugin);
//        }
//        transaction.commit();
//        return fragments;
//    }

    @Override
    public void onSelected(OperationSelectorFragment.OperationPluginItemWrapper operationPluginItemWrapper) {
        if (!operationPluginItemWrapper.isRemote()) {
            addAndFillLocalPluginView(operationPluginItemWrapper.plugin, null);
        } else {
            addAndFillRemotePluginView(operationPluginItemWrapper.id, null);
        }
    }
}
