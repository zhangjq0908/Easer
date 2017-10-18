package ryey.easer.commons.plugindef;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.IllegalArgumentTypeException;

/**
 * Base Fragment class for plugin's UI.
 * All subclasses should provide the correct {@link #expectedDataClass} during construction.
 * Normally, they should also provide the correct description string resource ID (see {@link #desc} {@link #setDesc(int)} {@link #desc(Resources)}).
 */
public abstract class PluginViewFragment extends Fragment {
    /**
     * Description resource (string) ID
     */
    protected @StringRes int desc = -1;

    /**
     * The expected data class.
     * Each subclass should pass the correct value during construction.
     *
     * Functions around this look like generic, but Java mechanism prevents me from using that way.
     * As a result, I have to use this dumb way.
     */
    protected Class<? extends StorageData> expectedDataClass = null;

    /**
     * Controls whether the content (view) is enabled/interactive or not in the beginning.
     * Works similar to {@link #passed_data}.
     */
    protected boolean initially_enabled = true;

    /**
     * Used in case {@link #onCreateView} is called after {@link #fill}`.
     * TODO: replace with an entrance-once method / class.
     */
    protected StorageData passed_data = null;

    /**
     * Normal {@link Fragment} method. Subclasses must override this method to provide the UI.
     * The only difference is the return value is now {@link NonNull}.
     */
    @NonNull
    @Override
    public abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * @see #passed_data
     * @see #initially_enabled
     * If overridden, call back-through is needed.
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (passed_data != null) {
            _fill(passed_data);
        }
        setEnabled(view, initially_enabled);
    }

    /**
     * Set the description text (shown in the UI).
     * Usually needs to be called in the constructor of the subclass.
     * @see #desc
     */
    protected void setDesc(int desc_resource) {
        this.desc = desc_resource;
    }

    /**
     * Get the description text from resources.
     * Could be overridden in subclasses if needed for customized text.
     */
    public String desc(Resources res) {
        return res.getString(desc);
    }

    /**
     * Subclasses don't need to override this method.
     * Get the expected data (represented in the form of {@link Class}).
     * Currently only used in {@link ryey.easer.core.ui.edit.EventPluginViewFragment} when initializing the selection for {@link ryey.easer.commons.plugindef.eventplugin.EventType}.
     * May be replaced later.
     */
    public Class<? extends StorageData> getExpectedDataClass() {
        return expectedDataClass;
    }

    /**
     * Check whether the to-be-filled data is of the correct type.
     */
    private void checkDataType(StorageData data) throws IllegalArgumentTypeException {
        if (expectedDataClass == null)
            Logger.e("Plugin not properly implemented (detected in %s)", getClass().getSimpleName());
        if (expectedDataClass.isAssignableFrom(data.getClass()))
            return;
        throw new IllegalArgumentTypeException(data.getClass(), expectedDataClass);
    }

    /**
     * The actual method to set the UI according to the data.
     * Plugin developers is expected to override this method rather than {@link #fill}.
     * This methods does NOT care about synchronization or other stuffs.
     */
    protected abstract void _fill(StorageData data);

    /**
     * Set the UI according to the data.
     * This methods takes care of synchronization (see {@link #passed_data}).
     * Plugin implementors normally only need to implement {@link #_fill} method.
     */
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

    /**
     * Construct the correct {@link StorageData} (subclass) containing the data in the UI.
     */
    public abstract StorageData getData();

    /**
     * Change the interactive state of the UI components.
     * Override this method only if the UI has other controls of the enabled state.
     */
    public void setEnabled(boolean enabled) {
        initially_enabled = enabled;
        View v = getView();
        if (v == null)
            Logger.v("view not yet created for <%s>???", getClass().getSimpleName());
        else {
            setEnabled(v, enabled);
        }
    }

    /**
     * Recursively change the interactive state of the UI components.
     */
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
