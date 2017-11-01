package ryey.easer.plugins.operation.network_transmission;

class TransmissionData {
    Protocol protocol = null;
    String remote_address = null;
    Integer remote_port = null;
    String data = null; //TODO: change to byte array to support arbitrary data

    enum Protocol {
        tcp,
        udp,
    }
}
