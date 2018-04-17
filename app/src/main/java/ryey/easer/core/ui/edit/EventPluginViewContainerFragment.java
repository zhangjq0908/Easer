package ryey.easer.core.ui.edit;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.PluginRegistry;

public class EventPluginViewContainerFragment<T extends EventData> extends PluginViewContainerFragment<T> {

    static <T extends EventData> EventPluginViewContainerFragment<T> createInstance(PluginViewFragment<T> pluginViewFragment) {
        EventPluginViewContainerFragment<T> fragment = new EventPluginViewContainerFragment<>();
        fragment.pluginViewFragment = pluginViewFragment;
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pluginview_event, container, false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_pluginview, pluginViewFragment)
                .commit();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventPlugin plugin = PluginRegistry.getInstance().event().findPlugin(pluginViewFragment);
        //noinspection ConstantConditions
        if (!plugin.checkPermissions(getContext())) {
            setEnabled(false);
            //noinspection ConstantConditions
            plugin.requestPermissions(getActivity(), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            setEnabled(true);
        }
    }

    /***
     * {@inheritDoc}
     * Explicitly override and call back through to snooze compiler data type checking
     */
    @NonNull
    @Override
    T getData() throws InvalidDataInputException {
        return super.getData();
    }

    private void setEnabled(boolean enabled) {
        pluginViewFragment.setEnabled(enabled);
    }
}
