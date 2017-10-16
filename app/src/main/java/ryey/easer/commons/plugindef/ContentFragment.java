package ryey.easer.commons.plugindef;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.IllegalArgumentTypeException;

public abstract class ContentFragment extends Fragment {
    protected int desc = -1;

    protected Class<? extends StorageData> expectedDataClass = null;

    protected StorageData passed_data = null;
    protected boolean initial_enabled = false;

    @NonNull
    @Override
    public abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (passed_data != null) {
            _fill(passed_data);
        }
        setEnabled(view, initial_enabled);
    }

    public Class<? extends StorageData> getExpectedDataClass() {
        return expectedDataClass;
    }

    private void checkDataType(StorageData data) throws IllegalArgumentTypeException {
        if (expectedDataClass == null)
            Logger.e("Plugin not properly implemented (detected in %s)", getClass().getSimpleName());
        if (expectedDataClass.isAssignableFrom(data.getClass()))
            return;
        throw new IllegalArgumentTypeException(data.getClass(), expectedDataClass);
    }

    protected void setDesc(int desc_resource) {
        this.desc = desc_resource;
    }

    public String desc(Resources res) {
        return res.getString(desc);
    }

    protected abstract void _fill(StorageData data);

    public void fill(StorageData data) {
        try {
            checkDataType(data);
            passed_data = data;
            if (getView() != null) {
                _fill(data);
            }
        } catch (IllegalArgumentTypeException e) {
            Logger.e(e, "filling with illegal data type");
            throw e;
        }
    }

    public abstract StorageData getData();

    public void setEnabled(boolean enabled) {
        initial_enabled = enabled;
        View v = getView();
        if (v == null)
            Logger.v("view not yet created for <%s>???", getClass().getSimpleName());
        else {
            setEnabled(v, enabled);
        }
    }

    protected static void setEnabled(View v, boolean enabled) {
        v.setEnabled(enabled);
        if (v instanceof ViewGroup) {
            View child;
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                child = ((ViewGroup) v).getChildAt(i);
                setEnabled(child, enabled);
            }
        }
    }
}
