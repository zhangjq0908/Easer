package ryey.easer.commons.plugindef;

public interface PluginDef {
    /**
     * Returns the plugin identifier.
     * Only used internally. Never displayed to user.
     */
    String name();

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
