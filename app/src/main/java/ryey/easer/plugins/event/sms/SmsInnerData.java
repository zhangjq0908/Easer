package ryey.easer.plugins.event.sms;

import android.os.Parcel;
import android.os.Parcelable;

import ryey.easer.Utils;

class SmsInnerData implements Parcelable {
    String sender;
    String content;

    SmsInnerData() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof SmsInnerData))
            return false;
        if (!Utils.nullableEqual(sender, ((SmsInnerData) obj).sender))
            return false;
        if (!Utils.nullableEqual(content, ((SmsInnerData) obj).content))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<SmsInnerData> CREATOR
            = new Parcelable.Creator<SmsInnerData>() {
        public SmsInnerData createFromParcel(Parcel in) {
            return new SmsInnerData(in);
        }

        public SmsInnerData[] newArray(int size) {
            return new SmsInnerData[size];
        }
    };

    private SmsInnerData(Parcel in) {
        sender = in.readString();
        content = in.readString();
    }
}
