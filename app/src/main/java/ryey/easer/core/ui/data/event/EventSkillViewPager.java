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

package ryey.easer.core.ui.data.event;

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
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.core.ui.data.SkillViewContainerFragment;
import ryey.easer.skills.LocalSkillRegistry;

public class EventSkillViewPager extends ViewPager {

    MyPagerAdapter mPagerAdapter;

    final List<EventSkill> eventSkillList = new ArrayList<>();

    Integer initial_position = null;
    EventData initial_event_data = null;

    public EventSkillViewPager(Context context) {
        super(context);
    }

    public EventSkillViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(AppCompatActivity activity) {
        eventSkillList.clear();
        eventSkillList.addAll(LocalSkillRegistry.getInstance().event().getEnabledSkills(activity));
        mPagerAdapter = new MyPagerAdapter(activity.getSupportFragmentManager(), getContext());
        setAdapter(mPagerAdapter);
    }

    public <T extends EventData> void setEventData(T eventData) {
        initial_event_data = eventData;
        int i = getPluginIndex(eventData);
        initial_position = i;
        if (getCurrentItem() == i) {
            synchronized (this) {
                //noinspection unchecked
                EventSkillViewContainerFragment<T> fragment = mPagerAdapter.getRegisteredFragment(i);
                if (fragment != null)
                    //noinspection unchecked
                    fragment.fill((T) initial_event_data);
            }
        } else {
            setCurrentItem(i);
        }
    }

    public EventData getEventData() throws InvalidDataInputException {
        return getEventData(getCurrentItem());
    }

    public EventData getEventData(int position) throws InvalidDataInputException {
        return mPagerAdapter.getRegisteredFragment(position).getData();
    }

    private int getPluginIndex(EventData eventData) {
        for (int i = 0; i < eventSkillList.size(); i++) {
            if (eventData.getClass() == eventSkillList.get(i).dataFactory().dataClass())
                return i;
        }
        throw new IllegalAccessError("Plugin not found???");
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<EventSkillViewContainerFragment> registeredFragments = new SparseArray<>();

        private final Context context;
        final String[] titles;

        public MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            titles = new String[eventSkillList.size()];
            for (int i = 0; i < eventSkillList.size(); i++) {
                titles[i] = eventSkillList.get(i).view().desc(getResources());
            }
        }

        @Override
        public Fragment getItem(int position) {
            SkillViewContainerFragment fragment = EventSkillViewContainerFragment.createInstance(
                    eventSkillList.get(position));
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
            EventSkillViewContainerFragment fragment = (EventSkillViewContainerFragment) super.instantiateItem(container, position);
            synchronized (EventSkillViewPager.this) {
                if ((initial_position != null) && (position == initial_position)) {
                    //noinspection unchecked
                    fragment.fill(initial_event_data);
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

        public EventSkillViewContainerFragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
