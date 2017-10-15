package ryey.easer.commons.plugindef;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.IllegalArgumentTypeException;

/*
 * Abstract Control UI of both OperationPlugin and EventPlugin
 * All plugins should implement their special subclasses.
 *
 * `expectedDataClass` must be assigned to set possible StorageData for the plugin
 */
public abstract class ContentLayout extends LinearLayout {
    protected String desc = null;

    protected Class<? extends StorageData> expectedDataClass = null;

    public ContentLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
    }

    protected void setDesc(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    private void checkDataType(StorageData data) throws IllegalArgumentTypeException {
        if (expectedDataClass == null)
            Logger.e("Plugin not properly implemented (detected in %s)", getClass().getSimpleName());
        if (expectedDataClass.isAssignableFrom(data.getClass()))
            return;
        throw new IllegalArgumentTypeException(data.getClass(), expectedDataClass);
    }

    protected abstract void _fill(StorageData data);

    public void fill(StorageData data) {
        try {
            checkDataType(data);
            _fill(data);
        } catch (IllegalArgumentTypeException e) {
            Logger.e(e, "filling with illegal data type");
            throw e;
        }
    }

    public abstract StorageData getData();

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            setEnabled(child, enabled);
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
