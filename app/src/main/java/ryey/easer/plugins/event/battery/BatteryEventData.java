package ryey.easer.plugins.event.battery;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.EnumSet;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

public class BatteryEventData extends TypedEventData {

    Integer battery_status = null;

    {
        default_type = EventType.is;
        availableTypes = EnumSet.of(EventType.is, EventType.is_not);
    }

    public BatteryEventData() {
    }

    public BatteryEventData(Integer battery_status) {
        this.battery_status = battery_status;
    }

    BatteryEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                battery_status = Integer.parseInt(data);
                break;
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                res = String.valueOf(battery_status);
                break;
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if ((battery_status == BatteryStatus.charging) || (battery_status == BatteryStatus.discharging))
            return true;
        return false;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BatteryEventData))
            return false;
        if (!Utils.eEquals(this, (EventData) obj))
            return false;
        if (!battery_status.equals(((BatteryEventData) obj).battery_status))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(battery_status);
    }

    public static final Parcelable.Creator<BatteryEventData> CREATOR
            = new Parcelable.Creator<BatteryEventData>() {
        public BatteryEventData createFromParcel(Parcel in) {
            return new BatteryEventData(in);
        }

        public BatteryEventData[] newArray(int size) {
            return new BatteryEventData[size];
        }
    };

    private BatteryEventData(Parcel in) {
        battery_status = in.readInt();
    }
}
