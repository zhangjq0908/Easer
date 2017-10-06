package ryey.easer.core.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class SelectableNamedPluginViewFragment extends PluginViewFragment {

    private CheckBox mCheckBox;

    static SelectableNamedPluginViewFragment createInstance(ContentLayout contentLayout) {
        SelectableNamedPluginViewFragment fragment = new SelectableNamedPluginViewFragment();
        fragment.contentLayout = contentLayout;
        return fragment;
    }

    public SelectableNamedPluginViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.setOrientation(HORIZONTAL);
        mCheckBox = new CheckBox(getContext());
        layout.addView(mCheckBox);

        String desc = contentLayout.desc();
        if (desc != null) {
            LinearLayout rhs = new LinearLayout(getContext());
            rhs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            rhs.setOrientation(VERTICAL);
            TextView tv_desc = new TextView(getContext());
            tv_desc.setText(contentLayout.desc());
            rhs.addView(tv_desc);
            rhs.addView(contentLayout);
            layout.addView(rhs);
        } else {
            layout.addView(contentLayout);
        }
        contentLayout.setEnabled(mCheckBox.isChecked());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentLayout.setEnabled(b);
            }
        });

        return layout;
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
