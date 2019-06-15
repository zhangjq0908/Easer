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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.StorageData;

public abstract class EditSourceDataFragment<D extends StorageData, S extends Skill & SourceCategory.Categorized> extends Fragment implements SourceSelectorDialogFragment.SelectedListener<S> {

    private static final String TAG_SELECT_DIALOG = "select_dialog";

    protected Button mButtonSelect = null;
    protected SourceSkillViewContainerFragment<D, S> skillViewContainerFragment = null;

    @StringRes protected abstract int buttonText();
    protected abstract SourceSelectorDialogFragment<S> selectorDialogFragment();
    protected abstract S findSkill(D data);
    protected abstract SourceSkillViewContainerFragment<D, S> skillViewContainerFragment(S skill);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_source_data, container, false);

        mButtonSelect = view.findViewById(R.id.btn_select);
        mButtonSelect.setText(buttonText());
        mButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SourceSelectorDialogFragment<S> eventSelectorFragment = selectorDialogFragment();
                eventSelectorFragment.setSelectedListener(EditSourceDataFragment.this);
                eventSelectorFragment.show(getChildFragmentManager(), TAG_SELECT_DIALOG);
            }
        });

        return view;
    }

    public void loadFromData(D data) {
        S skill = findSkill(data);
        assert skill != null;
        setSkill(skillViewContainerFragment(skill),
                getString(skill.name()),
                data);
    }

    public D saveToData() throws InvalidDataInputException {
        if (skillViewContainerFragment == null)
            throw new InvalidDataInputException("No Event selected");
        return skillViewContainerFragment.getData();
    }

    @Override
    public void onSelected(SourceSelectorDialogFragment.SkillItemWrapper<S> skillItemWrapper) {
        setSkill(skillViewContainerFragment(skillItemWrapper.skill), skillItemWrapper.name);
    }

    private void setSkill(SourceSkillViewContainerFragment<D, S> skillViewContainerFragment, String name) {
        setSkill(skillViewContainerFragment, name, null);
    }

    private void setSkill(SourceSkillViewContainerFragment<D, S> skillViewContainerFragment, String name, D data) {
        mButtonSelect.setText(name);
        this.skillViewContainerFragment = skillViewContainerFragment;
        getChildFragmentManager().beginTransaction()
                .replace(R.id.skill_view, skillViewContainerFragment)
                .commit();
        if (data != null)
            skillViewContainerFragment.fill(data);
    }
}
