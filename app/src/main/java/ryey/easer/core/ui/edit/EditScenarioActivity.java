package ryey.easer.core.ui.edit;

import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.storage.ScenarioDataStorage;

public class EditScenarioActivity extends AbstractEditDataActivity<ScenarioStructure, ScenarioDataStorage> {

    EditText mEditText_name = null;
    EventPluginViewPager mViewPager;

    @Override
    protected ScenarioDataStorage retDataStorage() {
        return ScenarioDataStorage.getInstance(this);
    }

    @Override
    protected String title() {
        return getString(R.string.title_scenario);
    }

    @Override
    protected int contentViewRes() {
        return R.layout.activity_edit_scenario;
    }

    @Override
    protected void init() {
        mEditText_name = findViewById(R.id.editText_name);
        mViewPager = findViewById(R.id.pager);
        mViewPager.init(this);
    }

    @Override
    protected void loadFromData(ScenarioStructure scenario) {
        oldName = scenario.getName();
        mEditText_name.setText(scenario.getName());
        mViewPager.setEventData(scenario.getEventData());
    }

    @Override
    protected ScenarioStructure saveToData() throws InvalidDataInputException {
        EventData eventData = mViewPager.getEventData();
        return new ScenarioStructure(C.VERSION_CREATED_IN_RUNTIME, mEditText_name.getText().toString(), eventData);
    }
}
