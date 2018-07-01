package ryey.easer.plugins.operation.launch_app;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

public class LaunchAppOperationData implements OperationData {
    private static final String K_APP_PACKAGE = "package";

    final String app_package;

    LaunchAppOperationData(String app_package) {
        this.app_package = app_package;
    }

    LaunchAppOperationData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    app_package = jsonObject.getString(K_APP_PACKAGE);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String ret;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_APP_PACKAGE, app_package);
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
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(app_package);
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
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        return null;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        return null;
    }
}
