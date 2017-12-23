package ryey.easer.plugins.reusable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ryey.easer.commons.plugindef.StorageData;

public abstract class IntegerData implements StorageData {
    protected Integer level = null;
    protected Integer lbound = null;
    protected Integer rbound = null;

    protected IntegerData() {}

    protected IntegerData(@NonNull Integer level) {
        this.level = level;
    }

    @NonNull
    public Integer get() {
        return level;
    }

    public void set(int level) {
        this.level = level;
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
    public boolean equals(@Nullable Object obj) {
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
