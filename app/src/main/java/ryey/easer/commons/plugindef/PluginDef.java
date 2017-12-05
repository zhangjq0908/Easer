package ryey.easer.commons.plugindef;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface PluginDef {
    /**
     * Returns the plugin identifier.
     * Only used internally. Never displayed to user.
     */
    @NonNull
    String id();

    /**
     * @return The string resource id of the name (of this plugin)
     */
    @StringRes
    int name();

    /**
     * Check whether this plugin is compatible to the current device.
     * @return Whether the plugin is compatible or not
     */
    boolean isCompatible();

    /**
     * Checks all permission(s) used by this plugin
     * @param context Context object used to check permission
     * @return whether all permissions are satisfied (or not)
     */
    boolean checkPermissions(@NonNull Context context);

    /**
     * Request for all permissions used by this plugin
     * @param activity Activity used to request permissions
     * @param requestCode Request code used to request permissions
     */
    void requestPermissions(@NonNull Activity activity, int requestCode);

    /**
     * Returns a dummy (empty) instance of the relevant data structure.
     */
    @NonNull
    StorageData data();

    /**
     * Returns the control UI of this plugin.
     * Used in relevant UI.
     */
    @NonNull
    PluginViewFragment view();
}
