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
package ryey.easer.skills.usource.battery_level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.skill_usource__battery_level_condition.*
import ryey.easer.R
import ryey.easer.commons.local_skill.InvalidDataInputException
import ryey.easer.commons.local_skill.ValidData
import ryey.easer.skills.SkillViewFragment

class BatteryLevelConditionSkillViewFragment : SkillViewFragment<BatteryLevelUSourceData?>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.skill_usource__battery_level_condition, container, false)
    }

    override fun _fill(@ValidData data: BatteryLevelUSourceData) {
        when (data.type) {
            BatteryLevelUSourceData.Type.custom -> {
                data.level as BatteryLevelUSourceData.CustomLevel
                userText_level.setText(data.level.battery_level.toString())
                cb_inclusive.isChecked = data.level.inclusive
            }
            else -> {
                throw IllegalStateException("BatteryLevelCondition only supports 'custom' level")
            }
        }
    }

    @ValidData
    @Throws(InvalidDataInputException::class)
    override fun getData(): BatteryLevelUSourceData {
        return let {
                val ln = userText_level.text.toString().toInt()
                if (ln < 0 || ln > 100) {
                    throw InvalidDataInputException("battery level should be between 0-100")
                }
                val inclusive = cb_inclusive.isChecked
                BatteryLevelUSourceData.CustomLevel(ln, inclusive).let { level ->
                    BatteryLevelUSourceData(BatteryLevelUSourceData.Type.custom, level)
                }
            }
    }
}