package ryey.easer.plugins;

import android.os.Parcel;
import android.os.Parcelable;

public class TestHelper {
    public static Parcel writeToParcel(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return parcel;
    }
}
