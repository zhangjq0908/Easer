package ryey.easer.core.ui.edit;

import android.content.Intent;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.data.storage.EventDataStorage;

public class EventListFragment extends AbstractDataListFragment<EventDataStorage> {
    @Override
    protected String title() {
        return getString(R.string.title_event);
    }

    @Override
    protected EventDataStorage retmStorage() {
        return EventDataStorage.getInstance(getContext());
    }

    @Override
    protected void reloadList() {
        super.reloadList();
        Intent intent = new Intent();
        intent.setAction(EHService.ACTION_RELOAD);
        getContext().sendBroadcast(intent);
    }

    @Override
    protected Intent intentForEditDataActivity() {
        return new Intent(getActivity(), EditEventActivity.class);
    }
}
