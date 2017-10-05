package ryey.easer.core.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class PluginViewFragment extends Fragment {

    private ContentLayout contentLayout;
    private CheckBox mCheckBox;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (passed_data != null) {
            _fill(passed_data);
        }
    }

    protected void _fill(StorageData data) {
        if (data == null) {
            mCheckBox.setChecked(false);
        } else {
            mCheckBox.setChecked(true);
            contentLayout.fill(data);
        }
    }

    public void fill(StorageData data) {
        passed_data = data;
        if (getView() != null) {
            _fill(data);
        }
    }

    public StorageData getData() {
        if (mCheckBox.isChecked()) {
            return contentLayout.getData();
        } else {
            return null;
        }
    }
}
