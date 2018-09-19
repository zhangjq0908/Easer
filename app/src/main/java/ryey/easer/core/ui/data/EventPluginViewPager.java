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
import ryey.easer.commons.local_plugin.eventplugin.EventData;
import ryey.easer.commons.local_plugin.eventplugin.EventPlugin;
import ryey.easer.plugins.PluginRegistry;

public class EventPluginViewPager extends ViewPager {

    MyPagerAdapter mPagerAdapter;

    final List<EventPlugin> eventPluginList = new ArrayList<>();

    Integer initial_position = null;
    EventData initial_event_data = null;

    public EventPluginViewPager(Context context) {
        super(context);
    }

    public EventPluginViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void init(AppCompatActivity activity) {
        eventPluginList.clear();
        eventPluginList.addAll(PluginRegistry.getInstance().event().getEnabledPlugins(activity));
        mPagerAdapter = new MyPagerAdapter(activity.getSupportFragmentManager(), getContext());
        setAdapter(mPagerAdapter);
    }

    <T extends EventData> void setEventData(T eventData) {
        initial_event_data = eventData;
        int i = getPluginIndex(eventData);
        initial_position = i;
        if (getCurrentItem() == i) {
            synchronized (this) {
                //noinspection unchecked
                EventPluginViewContainerFragment<T> fragment = mPagerAdapter.getRegisteredFragment(i);
                if (fragment != null)
                    //noinspection unchecked
                    fragment.fill((T) initial_event_data);
            }
        } else {
            setCurrentItem(i);
        }
    }

    EventData getEventData() throws InvalidDataInputException {
        return getEventData(getCurrentItem());
    }

    EventData getEventData(int position) throws InvalidDataInputException {
        return mPagerAdapter.getRegisteredFragment(position).getData();
    }

    private int getPluginIndex(EventData eventData) {
        for (int i = 0; i < eventPluginList.size(); i++) {
            if (eventData.getClass() == eventPluginList.get(i).dataFactory().dataClass())
                return i;
        }
        throw new IllegalAccessError("Plugin not found???");
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<EventPluginViewContainerFragment> registeredFragments = new SparseArray<>();

        private final Context context;
        final String[] titles;

        public MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            titles = new String[eventPluginList.size()];
            for (int i = 0; i < eventPluginList.size(); i++) {
                titles[i] = eventPluginList.get(i).view().desc(getResources());
            }
        }

        @Override
        public Fragment getItem(int position) {
            PluginViewContainerFragment fragment = EventPluginViewContainerFragment.createInstance(
                    eventPluginList.get(position));
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
            EventPluginViewContainerFragment fragment = (EventPluginViewContainerFragment) super.instantiateItem(container, position);
            synchronized (EventPluginViewPager.this) {
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

        public EventPluginViewContainerFragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
