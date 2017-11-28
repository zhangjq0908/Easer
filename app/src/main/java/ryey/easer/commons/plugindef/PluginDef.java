package ryey.easer.commons.plugindef;

import android.app.Activity;
import android.content.Context;

public interface PluginDef {
    /**
     * Returns the plugin identifier.
     * Only used internally. Never displayed to user.
     */
    String name();

    /**
     * Checks all permission(s) used by this plugin
     * @param context Context object used to check permission
     * @return whether all permissions are satisfied (or not)
     */
    boolean checkPermissions(Context context);

    /**
     * Request for all permissions used by this plugin
     * @param activity Activity used to request permissions
     * @param requestCode Request code used to request permissions
     */
    void requestPermissions(Activity activity, int requestCode);

    /**
     * Returns a dummy (empty) instance of the relevant data structure.
     */
    StorageData data();

    /**
     * Returns the control UI of this plugin.
     * Used in relevant UI.
     */
    PluginViewFragment view();
}
