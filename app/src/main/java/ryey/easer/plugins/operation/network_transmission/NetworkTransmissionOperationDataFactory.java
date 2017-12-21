package ryey.easer.plugins.operation.network_transmission;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class NetworkTransmissionOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return NetworkTransmissionOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new NetworkTransmissionOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        NetworkTransmissionOperationData dummyData = new NetworkTransmissionOperationData();
        TransmissionData transmissionData = new TransmissionData();
        transmissionData.protocol = TransmissionData.Protocol.tcp;
        transmissionData.remote_port = 146;
        transmissionData.remote_address = "192.168.0.143";
        transmissionData.data = "aaaData";
        dummyData.set(transmissionData);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new NetworkTransmissionOperationData(data, format, version);
    }
}
