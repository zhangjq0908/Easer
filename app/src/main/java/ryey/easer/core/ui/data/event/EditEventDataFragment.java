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

import ryey.easer.R;
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.core.ui.data.EditSourceDataFragment;
import ryey.easer.core.ui.data.SourceSelectorDialogFragment;
import ryey.easer.core.ui.data.SourceSkillViewContainerFragment;
import ryey.easer.skills.LocalSkillRegistry;

public class EditEventDataFragment extends EditSourceDataFragment<EventData, EventSkill> {

    @Override
    protected int buttonText() {
        return R.string.title_select_event;
    }

    @Override
    protected SourceSelectorDialogFragment<EventSkill> selectorDialogFragment() {
        return new EventSelectorDialogFragment();
    }

    @Override
    protected EventSkill findSkill(EventData data) {
        return LocalSkillRegistry.getInstance().event().findSkill(data);
    }

    @Override
    protected SourceSkillViewContainerFragment<EventData, EventSkill> skillViewContainerFragment(EventSkill skill) {
        return EventSkillViewContainerFragment.createInstance(skill);
    }
}

//public class EditEventDataFragment extends Fragment implements EventSelectorDialogFragment.SelectedListener<EventSkill> {
//
//    private static final String TAG_SELECT_DIALOG = "select_dialog";
//
//    protected Button mButtonSelect = null;
//    protected EventSkillViewContainerFragment skillViewContainerFragment = null;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);
//
//        mButtonSelect = view.findViewById(R.id.btn_select);
//        mButtonSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventSelectorDialogFragment eventSelectorFragment = new EventSelectorDialogFragment();
//                eventSelectorFragment.setSelectedListener(EditEventDataFragment.this);
//                eventSelectorFragment.show(getChildFragmentManager(), TAG_SELECT_DIALOG);
//            }
//        });
//
//        return view;
//    }
//
//    public void loadFromData(EventData data) {
//        EventSkill<?> skill = LocalSkillRegistry.getInstance().event().findSkill(data);
//        assert skill != null;
//        setSkill(EventSkillViewContainerFragment.createInstance(skill),
//                getString(skill.name()),
//                data);
//    }
//
//    public EventData saveToData() throws InvalidDataInputException {
//        if (skillViewContainerFragment == null)
//            throw new InvalidDataInputException("No Event selected");
//        return skillViewContainerFragment.getData();
//    }
//
//    @Override
//    public void onSelected(SourceSelectorDialogFragment.SkillItemWrapper<EventSkill> skillItemWrapper) {
//        setSkill(EventSkillViewContainerFragment.createInstance(skillItemWrapper.skill), skillItemWrapper.name);
//    }
//
//    private void setSkill(EventSkillViewContainerFragment skillViewContainerFragment, String name) {
//        setSkill(skillViewContainerFragment, name, null);
//    }
//
//    private void setSkill(EventSkillViewContainerFragment skillViewContainerFragment, String name, EventData data) {
//        mButtonSelect.setText(name);
//        this.skillViewContainerFragment = skillViewContainerFragment;
//        getChildFragmentManager().beginTransaction()
//                .replace(R.id.skill_view, skillViewContainerFragment)
//                .commit();
//        if (data != null)
//            skillViewContainerFragment.fill(data);
//    }
//}
