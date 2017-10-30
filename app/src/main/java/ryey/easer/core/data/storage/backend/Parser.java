package ryey.easer.core.data.storage.backend;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.commons.IllegalStorageDataException;

public interface Parser<T> {
    T parse(InputStream in) throws IOException, IllegalStorageDataException;
}
