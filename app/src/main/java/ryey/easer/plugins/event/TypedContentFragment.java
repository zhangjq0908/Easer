package ryey.easer.plugins.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.ContentFragment;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

import static android.widget.LinearLayout.HORIZONTAL;

public abstract class TypedContentFragment extends ContentFragment {
    protected RadioGroup type_radioGroup;
    protected List<Integer> radioButtonIds = new ArrayList<>();
    protected List<EventType> availableTypes = new ArrayList<>();

    {
        expectedDataClass = EventData.class;
        initial_enabled = true;
    }

    @NonNull
    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        type_radioGroup = new RadioGroup(getContext());
        type_radioGroup.setOrientation(HORIZONTAL);
        linearLayout.addView(type_radioGroup);
        return linearLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (passed_data != null) {
            fillType(passed_data);
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

    @Override
    public void fill(StorageData data) {
        try {
            super.fill(data);
            if (getView() != null) {
                fillType(data);
            }
        } catch (IllegalArgumentTypeException e) {
            throw e;
        }
    }

    protected void fillType(StorageData data) {
        if (data instanceof EventData) {
            setAvailableTypes(((EventData) data).availableTypes());
            setType(((EventData) data).type());
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
