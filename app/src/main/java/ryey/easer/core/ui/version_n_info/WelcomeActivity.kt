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

package ryey.easer.core.ui.version_n_info

import android.os.Bundle
import android.preference.PreferenceManager
import com.github.paolorotolo.appintro.AppIntro2
import ryey.easer.R


class WelcomeActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(TextSlide.newInstance(R.string.welcome_message_1))
        addSlide(TextSlide.newInstance(R.string.welcome_message_2))
        addSlide(TextSlide.newInstance(R.string.welcome_message_3))
        addSlide(TextSlide.newInstance(R.string.title_welcome_message_privacy, R.string.welcome_message_privacy))

        showSkipButton(true)
    }

    override fun onDonePressed(currentFragment: androidx.fragment.app.Fragment?) {
        super.onDonePressed(currentFragment)
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(getString(R.string.key_pref_welcome), false)
                .apply()
        finish()
    }

    override fun onSkipPressed(currentFragment: androidx.fragment.app.Fragment?) {
        super.onSkipPressed(currentFragment)
        onDonePressed(currentFragment)
    }

}
