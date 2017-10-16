package ryey.easer.core.ui.edit;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ryey.easer.commons.plugindef.ContentFragment;
import ryey.easer.commons.plugindef.StorageData;

public abstract class PluginViewFragment extends Fragment {

    protected ContentFragment contentFragment = null;

    protected StorageData passed_data = null;

    public PluginViewFragment() {
    }

    @NonNull
    @Override
    public abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (passed_data != null) {
            _fill(passed_data);
        }
    }

    protected void _fill(StorageData data) {
        contentFragment.fill(data);
    }

    void fill(StorageData data) {
        if (data != null) {
            passed_data = data;
            if (getView() != null) {
                _fill(data);
            }
        }
    }

    StorageData getData() {
        return contentFragment.getData();
    }
}
