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

package ryey.easer.core.ui.edit;

import android.support.constraint.ConstraintLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.core.data.storage.ScenarioDataStorage;
import ryey.easer.core.data.storage.ScriptDataStorage;

/*
 * TODO: change the layout
 */
public class EditScriptActivity extends AbstractEditDataActivity<ScriptStructure, ScriptDataStorage> {

    static {
        TAG_DATA_TYPE = "script";
    }

    EventPluginViewPager mViewPager;

    EditText mEditText_name = null;
    private static final String NON = ""; //TODO: more robust
    Spinner mSpinner_parent = null;
    List<String> mScriptList = null;
    Spinner mSpinner_profile = null;
    List<String> mProfileList = null;
    boolean isActive = true;
    CompoundButton mSwitch_use_scenario;

    Spinner mSpinner_scenario;
    List<String> mScenarioList;
    CompoundButton mSwitch_reverse;
    CompoundButton mSwitch_repeatable;
    CompoundButton mSwitch_persistent;

    ConstraintLayout layout_use_scenario;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_event, menu);
        menu.findItem(R.id.action_toggle_active).setChecked(isActive);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_active:
                isActive = !item.isChecked();
                item.setChecked(isActive);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ScriptDataStorage retDataStorage() {
        return ScriptDataStorage.getInstance(this);
    }

    @Override
    protected String title() {
        return getString(R.string.title_script);
    }

    @Override
    protected int contentViewRes() {
        return R.layout.activity_edit_script;
    }

    void init() {
        mEditText_name = findViewById(R.id.editText_script_title);

        mSpinner_parent = findViewById(R.id.spinner_parent);
        mScriptList = (ScriptDataStorage.getInstance(this)).list();
        mScriptList.add(0, NON);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_simple, mScriptList); //TODO: change layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner_parent.setAdapter(adapter);
        mSpinner_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinner_profile = findViewById(R.id.spinner_profile);
        mProfileList = (ProfileDataStorage.getInstance(this)).list();
        mProfileList.add(0, NON);
        ArrayAdapter<String> adapter_profile = new ArrayAdapter<>(this, R.layout.spinner_simple, mProfileList);
        adapter_profile.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner_profile.setAdapter(adapter_profile);
        mSpinner_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSwitch_use_scenario = findViewById(R.id.switch_use_scenario);
        mSwitch_use_scenario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                layout_use_scenario.setVisibility(checked ? View.VISIBLE : View.GONE);
                mViewPager.setVisibility(!checked ? View.VISIBLE : View.GONE);
            }
        });
        mSpinner_scenario = findViewById(R.id.spinner_scenario);
        mScenarioList = ScenarioDataStorage.getInstance(this).list();
        ArrayAdapter<String> adapter_scenario = new ArrayAdapter<>(this, R.layout.spinner_simple, mScenarioList);
        adapter_scenario.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner_scenario.setAdapter(adapter_scenario);
        mSpinner_scenario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mViewPager = findViewById(R.id.pager);
        mViewPager.init(this);

        layout_use_scenario = findViewById(R.id.layout_use_scenario);
        mSwitch_reverse = findViewById(R.id.switch_reverse);
        mSwitch_repeatable = findViewById(R.id.switch_repeatable);
        mSwitch_persistent = findViewById(R.id.switch_persistent);

        mSwitch_use_scenario.setChecked(true);
        mSwitch_use_scenario.setChecked(false);
    }

    @Override
    protected void loadFromData(ScriptStructure script) {
        oldName = script.getName();
        mEditText_name.setText(oldName);
        String profile = script.getProfileName();
        if (profile == null)
            profile = NON;
        mSpinner_profile.setSelection(mProfileList.indexOf(profile));
        String parent = script.getParentName();
        mSpinner_parent.setSelection(mScriptList.indexOf(parent));

        ScenarioStructure scenario = script.getScenario();
        if (scenario.isTmpScenario()) {
            mSwitch_use_scenario.setChecked(false);
            EventData eventData = scenario.getEventData();
            mViewPager.setEventData(eventData);
        } else {
            mSwitch_use_scenario.setChecked(true);
            mSpinner_scenario.setSelection(mScenarioList.indexOf(scenario.getName()));
            mSwitch_reverse.setChecked(script.isReverse());
            mSwitch_repeatable.setChecked(script.isRepeatable());
            mSwitch_persistent.setChecked(script.isPersistent());
        }

        isActive = script.isActive();
    }

    @Override
    protected ScriptStructure saveToData() throws InvalidDataInputException {
        ScriptStructure script = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
        script.setName(mEditText_name.getText().toString());
        String profile = (String) mSpinner_profile.getSelectedItem();
        script.setProfileName(profile);
        script.setActive(isActive);
        String parent = (String) mSpinner_parent.getSelectedItem();
        if (!parent.equals(NON))
            script.setParentName(parent);

        ScenarioDataStorage scenarioDataStorage = ScenarioDataStorage.getInstance(this);
        if (mSwitch_use_scenario.isChecked()) {
            String scenario_name = (String) mSpinner_scenario.getSelectedItem();
            script.setScenario(scenarioDataStorage.get(scenario_name));
            script.setReverse(mSwitch_reverse.isChecked());
            script.setRepeatable(mSwitch_repeatable.isChecked());
            script.setPersistent(mSwitch_persistent.isChecked());
        } else {
            script.setEventData(mViewPager.getEventData());
        }
        return script;
    }

}
