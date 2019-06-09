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

import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.storage.ConditionDataStorage;
import ryey.easer.core.ui.data.AbstractEditDataActivity;

public class EditConditionActivity extends AbstractEditDataActivity<ConditionStructure, ConditionDataStorage> {

    EditText mEditText_name = null;
    ConditionSkillViewPager mViewPager;
    
    @Override
    protected ConditionDataStorage retDataStorage() {
        return new ConditionDataStorage(this);
    }

    @Override
    protected String title() {
        return getString(R.string.title_edit_condition);
    }

    @Override
    protected int contentViewRes() {
        return R.layout.activity_edit_condition;
    }

    @Override
    protected void init() {
        mEditText_name = findViewById(R.id.editText_name);
        mViewPager = findViewById(R.id.pager);
        mViewPager.init(this);
    }

    @Override
    protected void loadFromData(ConditionStructure condition) {
        oldName = condition.getName();
        mEditText_name.setText(condition.getName());
        mViewPager.setData(condition.getData());
    }

    @Override
    protected ConditionStructure saveToData() throws InvalidDataInputException {
        ConditionData conditionData = mViewPager.getData();
        return new ConditionStructure(C.VERSION_CREATED_IN_RUNTIME, mEditText_name.getText().toString(), conditionData);
    }
}
