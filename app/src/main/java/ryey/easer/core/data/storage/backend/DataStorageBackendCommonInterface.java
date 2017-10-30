package ryey.easer.core.data.storage.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ryey.easer.commons.IllegalStorageDataException;

/**
 * All methods won't check clashes (e.g. {@link #write(Object)} or existence (e.g. {@link #delete(String)}).
 * They are designed to be checked in {@link ryey.easer.core.data.storage.AbstractDataStorage} and its subclasses.
 * @param <T>
 */
public interface DataStorageBackendCommonInterface<T> {

    boolean has(String name);

    List<String> list();

    T get(String name) throws FileNotFoundException, IllegalStorageDataException;

    void write(T profile) throws IOException;

    void delete(String name);

    List<T> all();
}
