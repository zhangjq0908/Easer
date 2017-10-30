package ryey.easer.core.data.storage.backend;

import java.io.IOException;
import java.util.List;

import ryey.easer.commons.IllegalStorageDataException;

public interface DataStorageCommonInterface<T> {
    boolean has(String name);

    List<String> list();

    T get(String name) throws IllegalStorageDataException;

    void add(T profile) throws IOException;

    void delete(String name);

    List<T> all();
}
