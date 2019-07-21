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

package ryey.easer.core.ui.data;

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
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.StorageData;

@Deprecated
public abstract class SourceSkillViewPager<GD extends StorageData, GS extends Skill> extends ViewPager {

    MyPagerAdapter mPagerAdapter;

    final List<GS> skillList = new ArrayList<>();

    Integer initial_position = null;
    GD initial_data = null;

    public SourceSkillViewPager(Context context) {
        super(context);
    }

    public SourceSkillViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract List<GS> enabledSkills();
    protected abstract MyPagerAdapter newPagerAdapter(FragmentManager fm, Context context);

    public void init(AppCompatActivity activity) {
        skillList.clear();
        skillList.addAll(enabledSkills());
        mPagerAdapter = newPagerAdapter(activity.getSupportFragmentManager(), getContext());
        setAdapter(mPagerAdapter);
    }

    public <T extends GD> void setData(T data) {
        initial_data = data;
        int i = getPluginIndex(data);
        initial_position = i;
        if (getCurrentItem() == i) {
            synchronized (this) {
                SourceSkillViewContainerFragment<GD, GS> fragment = mPagerAdapter.getRegisteredFragment(i);
                if (fragment != null)
                    fragment.fill(initial_data);
            }
        } else {
            setCurrentItem(i);
        }
    }

    public GD getData() throws InvalidDataInputException {
        return getData(getCurrentItem());
    }

    GD getData(int position) throws InvalidDataInputException {
        return mPagerAdapter.getRegisteredFragment(position).getData();
    }

    private int getPluginIndex(GD data) {
        for (int i = 0; i < skillList.size(); i++) {
            if (data.getClass() == skillList.get(i).dataFactory().dataClass())
                return i;
        }
        throw new IllegalAccessError("Plugin not found???");
    }

    protected abstract class MyPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<SourceSkillViewContainerFragment<GD, GS>> registeredFragments = new SparseArray<>();

        private final Context context;
        final String[] titles;

        protected MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            titles = new String[skillList.size()];
            for (int i = 0; i < skillList.size(); i++) {
                titles[i] = skillList.get(i).view().desc(getResources());
            }
        }

        protected abstract SourceSkillViewContainerFragment<GD, GS> newFragment(GS skill);

        @Override
        public Fragment getItem(int position) {
            return newFragment(skillList.get(position));
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
            SourceSkillViewContainerFragment<GD, GS> fragment = (SourceSkillViewContainerFragment<GD, GS>) super.instantiateItem(container, position);
            synchronized (SourceSkillViewPager.this) {
                if ((initial_position != null) && (position == initial_position)) {
                    fragment.fill(initial_data);
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

        public SourceSkillViewContainerFragment<GD, GS> getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
