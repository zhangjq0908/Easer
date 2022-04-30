/*
 * Copyright (c) 2016 - 2022 Rui Zhao <renyuneyun@gmail.com>
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
package ryey.easer.core

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ServiceTestRule
import org.junit.Rule
import org.junit.Test

class EHServiceTest {
    @get:Rule
    val serviceRule = ServiceTestRule()

    @Test
    fun testWithBoundService() {
        val serviceIntent = Intent(
            ApplicationProvider.getApplicationContext<Context>(),
            EHService::class.java
        ).apply {
            putExtra(EHService.ARG_WORKING_CONTEXT, EHService.TEST)
        }

        val binder: EHService.EHTestBinder = serviceRule.bindService(serviceIntent) as EHService.EHTestBinder

    }
}