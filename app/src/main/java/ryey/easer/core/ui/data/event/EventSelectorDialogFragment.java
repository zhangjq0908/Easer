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

import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.core.ui.data.SourceSelectorDialogFragment;
import ryey.easer.skills.LocalSkillRegistry;

public class EventSelectorDialogFragment extends SourceSelectorDialogFragment<EventSkill> {
    @Override
    protected int titleRes() {
        return R.string.title_select_event;
    }

    @Override
    protected List<EventSkill> skillList() {
        return LocalSkillRegistry.getInstance().event().getEnabledSkills(getContext());
    }
}
