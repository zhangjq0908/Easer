package ryey.easer.plugins.event.battery;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.event.TypedContentLayout;

public class BatteryContentLayout extends TypedContentLayout {
    String []mode_names = getResources().getStringArray(R.array.battery_status);
    int []values = {
            BatteryStatus.charging,
            BatteryStatus.discharging
    };
    RadioButton []radioButtons = new RadioButton[values.length];

    Integer checked_item = null;

    {
        expectedDataClass = BatteryEventData.class;
    }

    public BatteryContentLayout(Context context) {
        super(context);
        setAvailableTypes(new BatteryEventData().availableTypes());
        setType(new BatteryEventData().type());
        setDesc(context.getString(R.string.event_battery));
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(HORIZONTAL);
        addView(radioGroup);
        OnClickListener radioButtonOnClickListener = new OnClickListener() {
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
            radioButtons[i] = new RadioButton(context);
            radioButtons[i].setText(mode_names[i]);
            radioButtons[i].setOnClickListener(radioButtonOnClickListener);
            radioGroup.addView(radioButtons[i]);
        }
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
