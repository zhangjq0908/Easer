package ryey.easer.skills.operation.launch_app;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class LaunchAppOperationData implements OperationData {
    private static final String K_APP_PACKAGE = "package";
    private static final String K_CLASS = "class";

    final String app_package;
    final @Nullable String app_class;

    LaunchAppOperationData(String app_package, @Nullable String app_class) {
        this.app_package = app_package;
        this.app_class = app_class;
    }

    LaunchAppOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    app_package = jsonObject.getString(K_APP_PACKAGE);
                    app_class = jsonObject.optString(K_CLASS);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String ret;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_APP_PACKAGE, app_package);
                    jsonObject.put(K_CLASS, app_class);
                    ret = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
        }
        return ret;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return app_package != null && !Utils.isBlank(app_package);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof LaunchAppOperationData))
            return false;
        if (!Utils.nullableEqual(app_package, ((LaunchAppOperationData) obj).app_package))
            return false;
        if (!Utils.nullableEqual(app_class, ((LaunchAppOperationData) obj).app_class))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(app_package);
        dest.writeString(app_class);
    }

    public static final Creator<LaunchAppOperationData> CREATOR
            = new Creator<LaunchAppOperationData>() {
        public LaunchAppOperationData createFromParcel(Parcel in) {
            return new LaunchAppOperationData(in);
        }

        public LaunchAppOperationData[] newArray(int size) {
            return new LaunchAppOperationData[size];
        }
    };

    private LaunchAppOperationData(Parcel in) {
        app_package = in.readString();
        app_class = in.readString();
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        return null;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        return this;
    }
}
