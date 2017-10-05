package ryey.easer.plugins.event;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public abstract class TypedContentLayout extends ContentLayout {
    protected RadioGroup type_radioGroup;
    protected List<Integer> radioButtonIds = new ArrayList<>();
    protected List<EventType> availableTypes = new ArrayList<>();

    {
        expectedDataClass = EventData.class;
    }

    public TypedContentLayout(Context context) {
        super(context);
        type_radioGroup = new RadioGroup(context);
        type_radioGroup.setOrientation(HORIZONTAL);
        addView(type_radioGroup);
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
            fillType(data);
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
