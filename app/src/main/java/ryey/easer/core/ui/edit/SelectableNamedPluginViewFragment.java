package ryey.easer.core.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentFragment;
import ryey.easer.commons.plugindef.StorageData;

public class SelectableNamedPluginViewFragment extends PluginViewFragment {

    private CheckBox mCheckBox;

    static SelectableNamedPluginViewFragment createInstance(ContentFragment contentFragment) {
        SelectableNamedPluginViewFragment fragment = new SelectableNamedPluginViewFragment();
        fragment.contentFragment = contentFragment;
        return fragment;
    }

    public SelectableNamedPluginViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pluginview_named_selectable, container, false);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_pluginview_enabled);
        contentFragment.setEnabled(false);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentFragment.setEnabled(b);
            }
        });
        String desc = contentFragment.desc();
        if (desc == null)
            Logger.wtf("desc == null!!!???");
        ((TextView) view.findViewById(R.id.text_pluginview_desc)).setText(desc);
        getChildFragmentManager().beginTransaction()
                .add(R.id.content_pluginview, contentFragment)
                .commit();

        return view;
    }

    @Override
    protected void _fill(StorageData data) {
        if (data == null) {
            mCheckBox.setChecked(false);
        } else {
            mCheckBox.setChecked(true);
            super._fill(data);
        }
    }

    @Override
    public StorageData getData() {
        if (mCheckBox.isChecked()) {
            return super.getData();
        } else {
            return null;
        }
    }
}
