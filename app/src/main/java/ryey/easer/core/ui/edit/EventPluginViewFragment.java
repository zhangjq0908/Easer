package ryey.easer.core.ui.edit;

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
import ryey.easer.commons.plugindef.ContentFragment;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class EventPluginViewFragment extends PluginViewFragment {
    static EventPluginViewFragment createInstance(ContentFragment contentFragment) {
        EventPluginViewFragment fragment = new EventPluginViewFragment();
        fragment.contentFragment = contentFragment;
        return fragment;
    }

    protected RadioGroup type_radioGroup;
    protected List<Integer> radioButtonIds = new ArrayList<>();
    protected List<EventType> availableTypes = new ArrayList<>();

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pluginview_event, container, false);
        type_radioGroup = (RadioGroup) v.findViewById(R.id.radiogroup_eventtype);
        contentFragment.setEnabled(true);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_pluginview, contentFragment)
                .commit();
        try {
            EventData dummyData = (EventData) contentFragment.getExpectedDataClass().newInstance();
            fillType(dummyData);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    protected void _fill(StorageData data) {
        contentFragment.fill(data);
        if (getView() != null) {
            fillType(data);
        }
    }

    @Override
    StorageData getData() {
        EventData data = (EventData) contentFragment.getData();
        data.setType(selectedType());
        return data;
    }

    protected void fillType(StorageData data) {
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
