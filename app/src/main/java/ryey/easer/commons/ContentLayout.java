package ryey.easer.commons;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class ContentLayout extends LinearLayout {
    protected String desc = null;

    public ContentLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
    }

    public ContentLayout(Context context, String desc) {
        this(context);
        this.desc = desc;
    }

    protected void setDesc(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public abstract void fill(StorageData data);

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
