package ryey.easer.core.ui.edit;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.StorageData;

public abstract class PluginViewContainerFragment<T extends StorageData> extends Fragment {

    protected ryey.easer.commons.plugindef.PluginViewFragment<T> pluginViewFragment = null;
    private Drawable initial_background;

    protected T passed_data = null;

    public PluginViewContainerFragment() {
    }

    @NonNull
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initial_background = view.getBackground();
        if (passed_data != null) {
            _fill(passed_data);
        }
    }

    protected void _fill(@NonNull T data) {
        pluginViewFragment.fill(data);
    }

    void fill(T data) {
        if (data != null) {
            passed_data = data;
            if (getView() != null) {
                _fill(data);
            }
        }
    }

    @NonNull
    T getData() throws InvalidDataInputException {
        return pluginViewFragment.getData();
    }

    void setHighlight(boolean state) {
        if (state) {
            getView().setBackgroundResource(R.drawable.boarder_alert);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getView().setBackground(initial_background);
            } else {
                getView().setBackgroundDrawable(initial_background);
            }
        }
    }
}
