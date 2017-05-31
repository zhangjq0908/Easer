package ryey.easer.plugins.reusable;

import ryey.easer.commons.plugindef.StorageData;

public abstract class IntegerData implements StorageData {
    protected Integer level = null;
    protected Integer lbound = null;
    protected Integer rbound = null;

    public IntegerData() {}

    public IntegerData(Integer level) {
        this.level = level;
    }

    @Override
    public Object get() {
        return level;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Integer) {
            level = (Integer) obj;
        } else {
            throw new RuntimeException("illegal data");
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
}
