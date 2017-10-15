package ryey.easer.core.ui.edit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentFragment;
import ryey.easer.commons.plugindef.StorageData;

public class PluginViewFragment extends Fragment {

    protected ContentFragment contentFragment;

    private StorageData passed_data = null;

    static PluginViewFragment createInstance(ContentFragment contentFragment) {
        PluginViewFragment fragment = new PluginViewFragment();
        fragment.contentFragment = contentFragment;
        return fragment;
    }

    public PluginViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pluginview, container, false);
        getChildFragmentManager().beginTransaction().replace(R.id.content_pluginview, contentFragment).commit();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (passed_data != null) {
            _fill(passed_data);
        }
    }

    protected void _fill(StorageData data) {
        contentFragment.fill(data);
    }

    public void fill(StorageData data) {
        if (data != null) {
            passed_data = data;
            if (getView() != null) {
                _fill(data);
            }
        }
    }

    public StorageData getData() {
        return contentFragment.getData();
    }
}
