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

import android.app.Activity;
import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.view.View;
import android.widget.ImageView;

import ryey.easer.BuildConfig;
import ryey.easer.R;
import ryey.easer.commons.CommonSkillHelper;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;

class SkillEnabledPreference extends CheckBoxPreference implements Preference.OnPreferenceChangeListener {

    private static final int REQCODE = 2333;

    private final Skill skill;

    SkillEnabledPreference(Context context, Skill skill) {
        super(context);
        this.skill = skill;
        setOnPreferenceChangeListener(this);
        setKey(CommonSkillHelper.pluginEnabledKey(skill));
        setLayoutResource(R.layout.pref_plugin_enable);
        setTitle(skill.name());
        boolean isCompatible = skill.isCompatible(context);
        if (isCompatible) {
            setDefaultValue(true);
        } else {
            setDefaultValue(false);
            setEnabled(false);
            setSummary(R.string.message_skill_not_compatible);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (BuildConfig.DEBUG && !(newValue instanceof Boolean)) throw new AssertionError();
        if ((Boolean) newValue && !skill.checkPermissions(getContext())) {
            skill.requestPermissions((Activity) getContext(), REQCODE);
            return false;
        }
        return true;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        ImageView img_root = view.findViewById(R.id.img_root);
        if (skill instanceof OperationSkill) {
            PrivilegeUsage privilege = ((OperationSkill) skill).privilege();
            if (privilege == PrivilegeUsage.root_only || privilege == PrivilegeUsage.prefer_root) {
                img_root.setVisibility(View.VISIBLE);
            } else {
                img_root.setVisibility(View.GONE);
            }
        }
    }
}
