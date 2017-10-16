package ryey.easer.plugins.event.battery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.event.TypedContentFragment;

import static android.widget.LinearLayout.HORIZONTAL;

public class BatteryContentFragment extends TypedContentFragment {
    String []mode_names;
    int []values = {
            BatteryStatus.charging,
            BatteryStatus.discharging
    };
    RadioButton []radioButtons = new RadioButton[values.length];

    Integer checked_item = null;

    {
        expectedDataClass = BatteryEventData.class;
        setDesc(R.string.event_battery);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode_names = getResources().getStringArray(R.array.battery_status);
    }

    @NonNull
    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = super.onCreateView(inflater, container, savedInstanceState);

        setAvailableTypes(new BatteryEventData().availableTypes());
        setType(new BatteryEventData().type());
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(HORIZONTAL);
        view.addView(radioGroup);
        View.OnClickListener radioButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < radioButtons.length; i++) {
                    if (v == radioButtons[i]) {
                        checked_item = i;
                        break;
                    }
                }
            }
        };
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new RadioButton(getContext());
            radioButtons[i].setText(mode_names[i]);
            radioButtons[i].setOnClickListener(radioButtonOnClickListener);
            radioGroup.addView(radioButtons[i]);
        }
        return view;
    }

    @Override
    protected void _fill(StorageData data) {
        if (data instanceof BatteryEventData) {
            int status = (int) data.get();
            for (int i = 0; i < values.length; i++) {
                if (values[i] == status) {
                    radioButtons[i].setChecked(true);
                    checked_item = i;
                }
            }
        }
    }

    @Override
    public StorageData getData() {
        if (checked_item == null)
            return null;
        return new BatteryEventData(values[checked_item], selectedType());
    }
}
