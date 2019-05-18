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

package ryey.easer.core.ui.data.condition;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.core.ui.data.SkillViewContainerFragment;
import ryey.easer.skills.LocalSkillRegistry;

public class ConditionSkillViewPager extends ViewPager {

    MyPagerAdapter mPagerAdapter;

    final List<ConditionSkill> conditionSkillList = new ArrayList<>();

    Integer initial_position = null;
    ConditionData initial_condition_data = null;

    public ConditionSkillViewPager(Context context) {
        super(context);
    }

    public ConditionSkillViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void init(AppCompatActivity activity) {
        conditionSkillList.clear();
        conditionSkillList.addAll(LocalSkillRegistry.getInstance().condition().getEnabledSkills(activity));
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
                ConditionSkillViewContainerFragment<T> fragment = mPagerAdapter.getRegisteredFragment(i);
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
        for (int i = 0; i < conditionSkillList.size(); i++) {
            if (conditionData.getClass() == conditionSkillList.get(i).dataFactory().dataClass())
                return i;
        }
        throw new IllegalAccessError("Plugin not found???");
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<ConditionSkillViewContainerFragment> registeredFragments = new SparseArray<>();

        private final Context context;
        final String[] titles;

        public MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            titles = new String[conditionSkillList.size()];
            for (int i = 0; i < conditionSkillList.size(); i++) {
                titles[i] = conditionSkillList.get(i).view().desc(getResources());
            }
        }

        @Override
        public Fragment getItem(int position) {
            SkillViewContainerFragment fragment = ConditionSkillViewContainerFragment.createInstance(
                    conditionSkillList.get(position));
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
            ConditionSkillViewContainerFragment fragment = (ConditionSkillViewContainerFragment) super.instantiateItem(container, position);
            synchronized (ConditionSkillViewPager.this) {
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

        public ConditionSkillViewContainerFragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
