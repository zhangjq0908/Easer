package ryey.easer.plugins.reusable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ryey.easer.Utils;
import ryey.easer.commons.plugindef.StorageData;

public abstract class StringData implements StorageData {
    protected String text = null;

    protected StringData() {}

    protected StringData(@NonNull String text) {
        this.text = text;
    }

    @NonNull
    public String get() {
        return text;
    }

    public void set(@NonNull String text) {
        this.text = text;
    }

    @Override
    public boolean isValid() {
        if (Utils.isBlank(text))
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
        return text.equals(((StringData) obj).text);
    }
}
