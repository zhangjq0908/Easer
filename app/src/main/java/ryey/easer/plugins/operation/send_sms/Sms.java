package ryey.easer.plugins.operation.send_sms;

import android.os.Parcel;
import android.os.Parcelable;

import ryey.easer.Utils;

class Sms implements Parcelable {
    String destination;
    String content;

    Sms() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Sms))
            return false;
        if (!destination.equals(((Sms) obj).destination))
            return false;
        if (!Utils.nullableEqual(content, ((Sms) obj).content))
            return false;
        return true;
    }

    private Sms(Parcel in) {
        destination = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(destination);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sms> CREATOR = new Creator<Sms>() {
        @Override
        public Sms createFromParcel(Parcel in) {
            return new Sms(in);
        }

        @Override
        public Sms[] newArray(int size) {
            return new Sms[size];
        }
    };
}
