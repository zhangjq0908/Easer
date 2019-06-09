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

import androidx.fragment.app.FragmentManager;

import java.util.List;

import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.core.ui.data.SourceSkillViewContainerFragment;
import ryey.easer.core.ui.data.SourceSkillViewPager;
import ryey.easer.skills.LocalSkillRegistry;

public class EventSkillViewPager extends SourceSkillViewPager<EventData, EventSkill> {

    public EventSkillViewPager(Context context) {
        super(context);
    }

    public EventSkillViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected List<EventSkill> enabledSkills() {
        return LocalSkillRegistry.getInstance().event().getEnabledSkills(getContext());
    }

    @Override
    protected MyPagerAdapter newPagerAdapter(FragmentManager fm, Context context) {
        return new MyCPagerAdapter(fm, context);
    }

    class MyCPagerAdapter extends MyPagerAdapter {

        MyCPagerAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override
        protected SourceSkillViewContainerFragment<EventData, EventSkill> newFragment(EventSkill skill) {
            return EventSkillViewContainerFragment.createInstance(skill);
        }
    }
}
