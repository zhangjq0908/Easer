package ryey.easer.plugins.reusable;

import android.support.annotation.NonNull;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.StorageData;

public abstract class IntegerData implements StorageData {
    protected Integer level = null;
    protected Integer lbound = null;
    protected Integer rbound = null;

    public IntegerData() {}

    public IntegerData(Integer level) {
        this.level = level;
    }

    @NonNull
    @Override
    public Object get() {
        return level;
    }

    @Override
    public void set(@NonNull Object obj) {
        if (obj instanceof Integer) {
            level = (Integer) obj;
        } else {
            throw new IllegalArgumentTypeException(obj.getClass(), Integer.class);
        }
    }

    @Override
    public boolean isValid() {
        if (level == null)
            return false;
        if (lbound != null)
            if (level < lbound)
                return false;
        if (rbound != null)
            if (level > rbound)
                return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!getClass().equals(obj.getClass()))
            return false;
        if (!isValid() || !((StorageData) obj).isValid())
            return false;
        if (((IntegerData) obj).lbound != lbound.intValue())
            return false;
        if (((IntegerData) obj).rbound != rbound.intValue())
            return false;
        if (((IntegerData) obj).level != level.intValue())
            return false;
        return true;
    }
}
