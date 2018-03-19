package ryey.easer.core.data;

public class InvalidExportedDataException extends Exception {
    public InvalidExportedDataException(String msg) {
        super(msg);
    }
    public InvalidExportedDataException(Exception e) {
        super(e);
    }
}
