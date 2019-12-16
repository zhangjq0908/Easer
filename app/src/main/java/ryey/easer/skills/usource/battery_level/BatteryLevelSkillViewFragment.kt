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
package ryey.easer.skills.usource.battery_level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.skill_usource__battery_level.*
import ryey.easer.R
import ryey.easer.commons.local_skill.InvalidDataInputException
import ryey.easer.commons.local_skill.ValidData
import ryey.easer.skills.SkillViewFragment

class BatteryLevelSkillViewFragment : SkillViewFragment<BatteryLevelUSourceData?>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.skill_usource__battery_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView === rb_level_system) {
                rb_level_custom.isChecked = !isChecked
                group_system!!.visibility = if (isChecked) View.VISIBLE else View.GONE
            } else { // buttonView == rb_level_custom
                rb_level_system.isChecked = !isChecked
                group_custom!!.visibility = if (isChecked) View.VISIBLE else View.GONE
            }
        }
        rb_level_system.setOnCheckedChangeListener(listener)
        rb_level_custom.setOnCheckedChangeListener(listener)
        rb_level_custom.isChecked = true
        rb_level_system.isChecked = true
    }

    override fun _fill(@ValidData data: BatteryLevelUSourceData) {
        when (data.type) {
            BatteryLevelUSourceData.Type.system -> {
                rb_level_system.isChecked = true
                data.level as BatteryLevelUSourceData.SystemLevel
                when (data.level.levelChoice) {
                    BatteryLevelUSourceData.SystemLevel.LevelChoice.low -> rb_low.isChecked = true
                    BatteryLevelUSourceData.SystemLevel.LevelChoice.ok_after_low -> rb_ok_after_low.isChecked = true
                }
            }
            BatteryLevelUSourceData.Type.custom -> {
                rb_level_custom.isChecked = true
                data.level as BatteryLevelUSourceData.CustomLevel
                userText_level.setText(data.level.battery_level.toString())
                cb_inclusive.isChecked = data.level.inclusive
            }
        }
    }

    @ValidData
    @Throws(InvalidDataInputException::class)
    override fun getData(): BatteryLevelUSourceData {
        return when {
            rb_level_system.isChecked -> {
                BatteryLevelUSourceData.SystemLevel(when {
                    rb_low.isChecked -> {
                        BatteryLevelUSourceData.SystemLevel.LevelChoice.low
                    }
                    rb_ok_after_low.isChecked -> {
                        BatteryLevelUSourceData.SystemLevel.LevelChoice.ok_after_low
                    }
                    else -> {
                        throw InvalidDataInputException("either low or ok_after_low should be checked")
                    }
                }).let { level ->
                    BatteryLevelUSourceData(BatteryLevelUSourceData.Type.system, level)
                }
            }
            rb_level_custom.isChecked -> {
                val ln = userText_level.text.toString().toInt()
                if (ln < 0 || ln > 100) {
                    throw InvalidDataInputException("battery level should be between 0-100")
                }
                val inclusive = cb_inclusive.isChecked
                BatteryLevelUSourceData.CustomLevel(ln, inclusive).let { level ->
                    BatteryLevelUSourceData(BatteryLevelUSourceData.Type.custom, level)
                }
            }
            else -> throw InvalidDataInputException("either system or custom should be checked")
        }
    }
}