package ryey.easer.plugins.reusable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ryey.easer.commons.plugindef.StorageData;

public abstract class BooleanData implements StorageData {
    protected Boolean state = null;

    protected BooleanData() {}

    protected BooleanData(@NonNull Boolean state) {
        this.state = state;
    }

    @NonNull
    public Boolean get() {
        return state;
    }

    public void set(boolean obj) {
        state = obj;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (state == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (!getClass().equals(obj.getClass()))
            return false;
        if (!isValid() || !((StorageData) obj).isValid())
            return false;
        return ((BooleanData) obj).state == state.booleanValue();
    }
}
