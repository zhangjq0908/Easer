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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!getClass().equals(obj.getClass()))
            return false;
        if (!isValid() || !((StorageData) obj).isValid())
            return false;
        return text.equals(((StringData) obj).text);
    }
}
