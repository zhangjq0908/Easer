package ryey.easer.core.data.storage.backend;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ryey.easer.commons.IllegalStorageDataException;

public class FileDataStorageBackendHelper {

    public static <T> T get(Parser<T> parser, File file) throws FileNotFoundException, IllegalStorageDataException {
        try {
            FileInputStream fin = new FileInputStream(file);
            T eventStructure = parser.parse(fin);
            fin.close();
            return eventStructure;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalAccessError();
    }

    public static <T> void write(Serializer<T> serializer, File file, T data) throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(file);
            String serialized_str = serializer.serialize(data);
            fout.write(serialized_str.getBytes());
            fout.close();
        } catch (UnableToSerializeException e) {
            Logger.e(e, "Unable to serialize");
            e.printStackTrace();
            throw new IOException(e.getMessage());
            //TODO: Maybe throw this exception out?
        }
    }
}
