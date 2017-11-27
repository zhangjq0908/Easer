package ryey.easer.plugins.operation.network_transmission;

import android.os.Parcel;
import android.os.Parcelable;

import ryey.easer.Utils;

class TransmissionData implements Parcelable {

    enum Protocol {
        tcp,
        udp,
    }

    Protocol protocol;
    String remote_address;
    int remote_port;
    String data; //TODO: change to byte array to support arbitrary data

    TransmissionData() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof TransmissionData))
            return false;
        if (protocol != ((TransmissionData) obj).protocol)
            return false;
        if (!remote_address.equals(((TransmissionData) obj).remote_address))
            return false;
        if (remote_port != ((TransmissionData) obj).remote_port)
            return false;
        if (!Utils.nullableEqual(data, ((TransmissionData) obj).data))
            return false;
        return true;
    }

    private TransmissionData(Parcel in) {
        protocol = (Protocol) in.readSerializable();
        remote_address = in.readString();
        remote_port = in.readInt();
        data = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(protocol);
        dest.writeString(remote_address);
        dest.writeInt(remote_port);
        dest.writeString(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransmissionData> CREATOR = new Creator<TransmissionData>() {
        @Override
        public TransmissionData createFromParcel(Parcel in) {
            return new TransmissionData(in);
        }

        @Override
        public TransmissionData[] newArray(int size) {
            return new TransmissionData[size];
        }
    };
}
