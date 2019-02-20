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

import android.app.Activity;
import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import ryey.easer.BuildConfig;
import ryey.easer.R;
import ryey.easer.commons.CommonSkillUtils;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;

class SkillEnabledPreference extends CheckBoxPreference implements Preference.OnPreferenceChangeListener {

    private static final int REQCODE = 2333;

    private final Skill skill;
    private final boolean in_use;

    private ImageButton btnPermission;

    SkillEnabledPreference(Context context, Skill skill, boolean in_use) {
        super(context);
        this.skill = skill;
        this.in_use = in_use;
        setOnPreferenceChangeListener(this);
        setKey(CommonSkillUtils.pluginEnabledKey(skill));
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
        if (in_use) {
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
        recSetEnabled(view.findViewById(android.R.id.widget_frame), !in_use);
        btnPermission = view.findViewById(R.id.btn_permission);
        redrawPermissionButton();
        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skill.requestPermissions((Activity) getContext(), REQCODE);
            }
        });
    }

    private static void recSetEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                recSetEnabled(((ViewGroup) view).getChildAt(i), enabled);
            }
        }
    }

    void redrawPermissionButton() {
        if (skill.checkPermissions(getContext())) {
            btnPermission.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_status_positive_inner));
        } else {
            btnPermission.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_status_negative_inner));
        }
    }
}
