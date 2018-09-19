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

package ryey.easer.core.ui.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionPlugin;
import ryey.easer.plugins.PluginRegistry;

public class ConditionPluginViewPager extends ViewPager {

    MyPagerAdapter mPagerAdapter;

    final List<ConditionPlugin> conditionPluginList = new ArrayList<>();

    Integer initial_position = null;
    ConditionData initial_condition_data = null;

    public ConditionPluginViewPager(Context context) {
        super(context);
    }

    public ConditionPluginViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void init(AppCompatActivity activity) {
        conditionPluginList.clear();
        conditionPluginList.addAll(PluginRegistry.getInstance().condition().getEnabledPlugins(activity));
        mPagerAdapter = new MyPagerAdapter(activity.getSupportFragmentManager(), getContext());
        setAdapter(mPagerAdapter);
    }

    <T extends ConditionData> void setConditionData(T conditionData) {
        initial_condition_data = conditionData;
        int i = getPluginIndex(conditionData);
        initial_position = i;
        if (getCurrentItem() == i) {
            synchronized (this) {
                //noinspection unchecked
                ConditionPluginViewContainerFragment<T> fragment = mPagerAdapter.getRegisteredFragment(i);
                if (fragment != null)
                    //noinspection unchecked
                    fragment.fill((T) initial_condition_data);
            }
        } else {
            setCurrentItem(i);
        }
    }

    ConditionData getConditionData() throws InvalidDataInputException {
        return getConditionData(getCurrentItem());
    }

    ConditionData getConditionData(int position) throws InvalidDataInputException {
        return mPagerAdapter.getRegisteredFragment(position).getData();
    }

    private int getPluginIndex(ConditionData conditionData) {
        for (int i = 0; i < conditionPluginList.size(); i++) {
            if (conditionData.getClass() == conditionPluginList.get(i).dataFactory().dataClass())
                return i;
        }
        throw new IllegalAccessError("Plugin not found???");
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<ConditionPluginViewContainerFragment> registeredFragments = new SparseArray<>();

        private final Context context;
        final String[] titles;

        public MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            titles = new String[conditionPluginList.size()];
            for (int i = 0; i < conditionPluginList.size(); i++) {
                titles[i] = conditionPluginList.get(i).view().desc(getResources());
            }
        }

        @Override
        public Fragment getItem(int position) {
            PluginViewContainerFragment fragment = ConditionPluginViewContainerFragment.createInstance(
                    conditionPluginList.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ConditionPluginViewContainerFragment fragment = (ConditionPluginViewContainerFragment) super.instantiateItem(container, position);
            synchronized (ConditionPluginViewPager.this) {
                if ((initial_position != null) && (position == initial_position)) {
                    //noinspection unchecked
                    fragment.fill(initial_condition_data);
                }
            }
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public ConditionPluginViewContainerFragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
