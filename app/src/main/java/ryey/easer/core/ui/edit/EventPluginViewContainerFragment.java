package ryey.easer.core.ui.edit;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;

public class EventPluginViewContainerFragment<T extends EventData> extends PluginViewContainerFragment<T> {

    static <T extends EventData> EventPluginViewContainerFragment<T> createInstance(PluginViewFragment<T> pluginViewFragment) {
        EventPluginViewContainerFragment<T> fragment = new EventPluginViewContainerFragment<>();
        fragment.pluginViewFragment = pluginViewFragment;
        return fragment;
    }

    protected RadioGroup type_radioGroup;
    protected List<Integer> radioButtonIds = new ArrayList<>();
    protected List<EventType> availableTypes = new ArrayList<>();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pluginview_event, container, false);
        type_radioGroup = v.findViewById(R.id.radiogroup_eventtype);
        pluginViewFragment.setEnabled(true);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_pluginview, pluginViewFragment)
                .commit();
        EventData dummyData = PluginRegistry.getInstance().event().findPlugin(pluginViewFragment)
                .dataFactory()
                .emptyData();
        fillType(dummyData);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventPlugin plugin = PluginRegistry.getInstance().event().findPlugin(pluginViewFragment);
        if (!plugin.checkPermissions(getContext())) {
            pluginViewFragment.setEnabled(false);
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
            pluginViewFragment.setEnabled(true);
        }
    }

    @Override
    protected void _fill(@NonNull T data) {
        pluginViewFragment.fill(data);
        if (getView() != null) {
            fillType(data);
        }
    }

    @NonNull
    @Override
    T getData() {
        T data = null;
        try {
            data = pluginViewFragment.getData();
        } catch (ryey.easer.commons.plugindef.InvalidDataInputException e) {
            e.printStackTrace();
        }
        data.setType(selectedType());
        return data;
    }

    protected void fillType(EventData data) {
        if (data instanceof EventData) {
            setAvailableTypes(((EventData) data).availableTypes());
            setType(((EventData) data).type());
        }
    }

    protected void setAvailableTypes(Set<EventType> availableTypes) {
        if (radioButtonIds.size() > 0) //already initialised
            return;
        for (EventType type: availableTypes) {
            this.availableTypes.add(type);
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(type.desc(getContext()));
            type_radioGroup.addView(radioButton);
            radioButtonIds.add(radioButton.getId());
        }
    }

    protected void setType(EventType type) {
        type_radioGroup.check(radioButtonIds.get(availableTypes.indexOf(type)));
    }

    protected EventType selectedType() {
        int ordinal = radioButtonIds.indexOf(type_radioGroup.getCheckedRadioButtonId());
        return availableTypes.get(ordinal);
    }
}
