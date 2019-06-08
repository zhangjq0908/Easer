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

package ryey.easer.core.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import java.util.Set;

import ryey.easer.R;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.core.RemoteOperationPluginInfo;
import ryey.easer.core.RemotePluginCommunicationHelper;
import ryey.easer.core.RemotePluginInfo;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.ConditionDataStorage;
import ryey.easer.core.data.storage.EventDataStorage;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.skills.LocalSkillRegistry;

public class SkillSettingsPreferenceFragment extends PreferenceFragment implements RemotePluginCommunicationHelper.OnOperationPluginListObtainedCallback {

    RemotePluginCommunicationHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.plugins_preference);

        helper = new RemotePluginCommunicationHelper(getActivity());
        helper.begin();

        Set<String> eventSkillsInUse = getEventSkillsInUse(getActivity());
        Set<String> conditionSkillsInUse = getConditionSkillsInUse(getActivity());
        Set<String> operationSkillsInUse = getOperationSkillsInUse(getActivity());

        PreferenceCategory preferenceCategory;
        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_operation_plugins));
        for (Skill plugin : LocalSkillRegistry.getInstance().operation().getAllSkills()) {
            SkillEnabledPreference preference = new SkillEnabledPreference(getActivity(), plugin, eventSkillsInUse.contains(plugin.id()));
            preferenceCategory.addPreference(preference);
        }

        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_event_plugins));
        for (Skill plugin : LocalSkillRegistry.getInstance().event().getAllSkills()) {
            SkillEnabledPreference preference = new SkillEnabledPreference(getActivity(), plugin, conditionSkillsInUse.contains(plugin.id()));
            preferenceCategory.addPreference(preference);
        }

        preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.key_pref_enabled_condition_plugins));
        for (Skill plugin : LocalSkillRegistry.getInstance().condition().getAllSkills()) {
            SkillEnabledPreference preference = new SkillEnabledPreference(getActivity(), plugin, operationSkillsInUse.contains(plugin.id()));
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

    private static Set<String> getEventSkillsInUse(Context context) {
        Set<String> skillSet = new ArraySet<>();
        EventDataStorage eventDataStorage = new EventDataStorage(context);
        for (String event : eventDataStorage.list()) {
            EventData eventData = eventDataStorage.get(event).getEventData();
            EventSkill eventSkill = LocalSkillRegistry.getInstance().event().findSkill(eventData);
            skillSet.add(eventSkill.id());
        }
        return skillSet;
    }

    private static Set<String> getConditionSkillsInUse(Context context) {
        Set<String> skillSet = new ArraySet<>();
        ConditionDataStorage conditionDataStorage = new ConditionDataStorage(context);
        for (String condition : conditionDataStorage.list()) {
            ConditionData conditionData = conditionDataStorage.get(condition).getData();
            ConditionSkill conditionSkill = LocalSkillRegistry.getInstance().condition().findSkill(conditionData);
            skillSet.add(conditionSkill.id());
        }
        return skillSet;
    }

    private static Set<String> getOperationSkillsInUse(Context context) {
        Set<String> skillSet = new ArraySet<>();
        ProfileDataStorage profileDataStorage = new ProfileDataStorage(context);
        for (String profile : profileDataStorage.list()) {
            ProfileStructure profileStructure = profileDataStorage.get(profile);
            skillSet.addAll(profileStructure.pluginIds());
        }
        return skillSet;
    }
}
