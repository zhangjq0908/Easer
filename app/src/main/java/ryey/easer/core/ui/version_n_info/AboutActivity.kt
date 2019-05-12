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

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import ryey.easer.R


class AboutActivity : MaterialAboutActivity() {

    protected override fun getMaterialAboutList(context: Context): MaterialAboutList {
        val app_general = MaterialAboutCard.Builder()
                .addItem(MaterialAboutTitleItem.Builder()
                        .icon(R.mipmap.ic_launcher)
                        .text(R.string.easer)
                        .desc("2016 - 2019 renyuneyun")
                        .setOnClickAction {
                            AlertDialog.Builder(this)
                                    .setTitle(R.string.easer)
                                    .setMessage(R.string.appInformation)
                                    .create().show()
                        }
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.drawable.ic_info_outline_black)
                        .text(packageManager.getPackageInfo(packageName, 0).versionName)
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.drawable.ic_copyleft_black)
                        .text("GPLv3+")
                        .setOnClickAction { openWebpage("https://www.gnu.org/licenses/gpl.txt") }
                        .setOnLongClickAction {
                            AlertDialog.Builder(this)
                                    .setMessage(String.format("%s\n%s\n%s", getString(R.string.liscence_stringa), getString(R.string.liscence_stringb), getString(R.string.liscence_stringc)))
                                    .create()
                                    .show()
                        }
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.drawable.ic_public_black)
                        .text(R.string.title_website)
                        .setOnClickAction { openWebpage("https://renyuneyun.github.io/Easer/") }
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.drawable.githubicon)
                        .text(R.string.title_source)
                        .setOnClickAction { openWebpage("https://github.com/renyuneyun/Easer/") }
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.drawable.ic_love_border_black)
                        .text(R.string.title_donate)
                        .setOnClickAction { openWebpage(R.string.url_donate) }
                        .build())
                .build()

        val author = MaterialAboutCard.Builder()
                .title(R.string.title_author)
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.drawable.ic_person_outline_black)
                        .text("renyuneyun")
                        .subText("Rui Zhao")
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.drawable.ic_public_black)
                        .text(R.string.title_homepage)
                        .setOnClickAction { openWebpage("https://renyuneyun.github.io/") }
                        .build())
                .build()

        return MaterialAboutList.Builder()
                .addCard(app_general)
                .addCard(author)
                .build()
    }

    override fun getActivityTitle(): CharSequence? {
        return getString(R.string.title_about)
    }

    private fun openWebpage(@StringRes urlRes: Int) {
        openWebpage(getString(urlRes))
    }

    private fun openWebpage(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}
