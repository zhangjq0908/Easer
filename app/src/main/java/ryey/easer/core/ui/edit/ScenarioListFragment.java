package ryey.easer.core.ui.edit;

import android.content.Intent;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.data.storage.ScenarioDataStorage;

public class ScenarioListFragment extends AbstractDataListFragment<ScenarioDataStorage> {
    @Override
    protected String title() {
        return getString(R.string.title_scenario);
    }

    @Override
    protected ScenarioDataStorage retmStorage() {
        return ScenarioDataStorage.getInstance(getContext());
    }

    @Override
    protected void reloadList() {
        super.reloadList();
        EHService.reload(getContext());
    }

    @Override
    protected Intent intentForEditDataActivity() {
        return new Intent(getContext(), EditScenarioActivity.class);
    }
}
