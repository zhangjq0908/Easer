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

package ryey.easer.core.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_data_collection.*
import ryey.easer.R
import ryey.easer.core.ui.data.DataListContainerFragment
import ryey.easer.core.ui.data.DataListContainerInterface

class DataCollectionFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_data_collection)

        val view = inflater.inflate(R.layout.fragment_data_collection, container, false)

        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = PagerAdapter(resources, childFragmentManager)

        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    fun currentItem(): Int {
        return view_pager.currentItem
    }

    class PagerAdapter(val resources: Resources, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            if (position >= count) {
                throw IllegalStateException("DataCollectionFragment:: ViewPager exceeded 4")
            }
            return DataListContainerFragment.create(fragmentOrder[position])
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            if (position >= count) {
                throw IllegalStateException("DataCollectionFragment:: ViewPager exceeded 4")
            }
            return resources.getString(titleOrder[position])
        }

        companion object {
            val fragmentOrder = arrayListOf(
                    DataListContainerInterface.ListType.script,
                    DataListContainerInterface.ListType.condition,
                    DataListContainerInterface.ListType.event,
                    DataListContainerInterface.ListType.profile
            )

            val titleOrder = arrayListOf(
                    R.string.title_script,
                    R.string.title_condition,
                    R.string.title_event,
                    R.string.title_profile
            )
        }
    }

}