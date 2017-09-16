package ryey.easer.plugins.reusable;

import ryey.easer.Utils;
import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.StorageData;

public abstract class StringData implements StorageData {
    protected String text = null;

    public StringData() {}

    public StringData(String text) {
        this.text = text;
    }

    @Override
    public Object get() {
        return text;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            text = (String) obj;
        } else {
            throw new IllegalArgumentTypeException(obj.getClass(), String.class);
        }
    }

    @Override
    public boolean isValid() {
        if (Utils.isBlank(text))
            return false;
        return true;
    }
}
