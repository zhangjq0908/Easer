package ryey.easer.core.ui.edit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

public class PluginViewFragment extends Fragment {

    protected ContentLayout contentLayout;

    private StorageData passed_data = null;

    static PluginViewFragment createInstance(ContentLayout contentLayout) {
        PluginViewFragment fragment = new PluginViewFragment();
        fragment.contentLayout = contentLayout;
        return fragment;
    }

    public PluginViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.addView(contentLayout);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (passed_data != null) {
            _fill(passed_data);
        }
    }

    protected void _fill(StorageData data) {
        contentLayout.fill(data);
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
        return contentLayout.getData();
    }
}
