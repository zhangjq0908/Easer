package ryey.easer.core.data.storage.backend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    public static String inputStreamToString(InputStream in) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    public static File mustGetSubDir(File dir, String sub) {
        File subdir;
        subdir = new File(dir, sub);
        if (subdir.exists())
            if (!subdir.isDirectory())
                throw new IllegalStateException("Given path exists and is not a dir:" + sub);
            else
                return subdir;
        else
            if (!subdir.mkdir())
                throw new IllegalStateException("Unable to create subdir in the given path:" + sub);
            else
                return subdir;
    }

    public static boolean fileExists(File dir, String name) {
        File file = new File(dir, name);
        if (file.exists()) {
            if (file.isFile())
                return true;
            else
                throw new IllegalStateException("File exists but is not a file");
        }
        return false;
    }
}
