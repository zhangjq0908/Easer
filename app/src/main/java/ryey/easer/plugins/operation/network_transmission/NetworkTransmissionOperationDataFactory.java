package ryey.easer.plugins.operation.network_transmission;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class NetworkTransmissionOperationDataFactory implements OperationDataFactory<NetworkTransmissionOperationData> {
    @NonNull
    @Override
    public Class<NetworkTransmissionOperationData> dataClass() {
        return NetworkTransmissionOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public NetworkTransmissionOperationData dummyData() {
        NetworkTransmissionOperationData.Protocol protocol = NetworkTransmissionOperationData.Protocol.tcp;
        int remote_port = 146;
        String remote_address = "192.168.0.143";
        String data = "aaaData";
        return new NetworkTransmissionOperationData(protocol, remote_address, remote_port, data);
    }

    @ValidData
    @NonNull
    @Override
    public NetworkTransmissionOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new NetworkTransmissionOperationData(data, format, version);
    }
}
