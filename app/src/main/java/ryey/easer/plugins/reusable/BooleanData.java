package ryey.easer.plugins.reusable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.StorageData;

public abstract class BooleanData implements StorageData {
    protected Boolean state = null;

    protected BooleanData() {}

    protected BooleanData(@NonNull Boolean state) {
        this.state = state;
    }

    @NonNull
    @Override
    public Object get() {
        return state;
    }

    @Override
    public void set(@NonNull Object obj) {
        if (obj instanceof Boolean) {
            state = (Boolean) obj;
        } else {
            throw new IllegalArgumentTypeException(obj.getClass(), Boolean.class);
        }
    }

    @Override
    public boolean isValid() {
        if (state == null)
            return false;
        return true;
    }

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
