package ryey.easer.plugins.reusable;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.StorageData;

public abstract class BooleanData implements StorageData {
    protected Boolean state = null;

    public BooleanData() {}

    public BooleanData(Boolean state) {
        this.state = state;
    }

    @Override
    public Object get() {
        return state;
    }

    @Override
    public void set(Object obj) {
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
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!getClass().equals(obj.getClass()))
            return false;
        if (!isValid() || !((StorageData) obj).isValid())
            return false;
        return ((BooleanData) obj).state == state.booleanValue();
    }
}
