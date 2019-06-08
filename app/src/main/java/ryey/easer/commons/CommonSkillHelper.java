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

package ryey.easer.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;

public class CommonSkillHelper {

    public static final int TYPE_OPERATION = 0;
    public static final int TYPE_EVENT = 1;
    public static final int TYPE_CONDITION = 2;
    private static final String[] TYPE_NAMES = {"operation", "event", "condition"};

    public static String pluginEnabledKey(Skill plugin) {
        String type_name;
        if (plugin instanceof OperationSkill)
            type_name = TYPE_NAMES[0];
        else if (plugin instanceof EventSkill)
            type_name = TYPE_NAMES[1];
        else if (plugin instanceof ConditionSkill)
            type_name = TYPE_NAMES[2];
        else
            throw new IllegalAccessError("Unknown plugin type???");
        return String.format(Locale.US, "%s_plugin_enabled_%s", type_name, plugin.id());
    }

    public static String pluginEnabledKey(int type, String id) {
        return String.format(Locale.US, "%s_plugin_enabled_%s", TYPE_NAMES[type], id);
    }

    /**
     * This method assumes the given id is corresponded to a plugin
     * @param context
     * @param type
     * @param id
     * @return
     */
    public static boolean isEnabled(Context context, int type, String id) {
        SharedPreferences settingsPreference =
                PreferenceManager.getDefaultSharedPreferences(context);
        return settingsPreference.getBoolean(CommonSkillHelper.pluginEnabledKey(type, id), true);
    }
}
