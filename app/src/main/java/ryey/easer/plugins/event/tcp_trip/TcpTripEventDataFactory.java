package ryey.easer.plugins.event.tcp_trip;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class TcpTripEventDataFactory implements EventDataFactory<TcpTripEventData> {
    @NonNull
    @Override
    public Class<TcpTripEventData> dataClass() {
        return TcpTripEventData.class;
    }

    @NonNull
    @Override
    public TcpTripEventData emptyData() {
        return new TcpTripEventData();
    }

    @ValidData
    @NonNull
    @Override
    public TcpTripEventData dummyData() {
        String raddr = "192.168.12.4";
        int rport = 3163;
        String send_data = "data to send";
        boolean check_data = true;
        String reply_data = "www my reply data";
        return new TcpTripEventData(raddr, rport, send_data, check_data, reply_data);
    }

    @ValidData
    @NonNull
    @Override
    public TcpTripEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new TcpTripEventData(data, format, version);
    }
}
