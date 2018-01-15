package ryey.easer.core.data;

import android.content.Context;
import android.net.Uri;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Helper {
    public static void export_data(Context context, File dest) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(dest);
        ZipOutputStream zip = new ZipOutputStream(fileOutputStream);

        File parent_dir = context.getFilesDir();

        addDirToZip(zip, parent_dir, "event");
        addDirToZip(zip, parent_dir, "profile");

        zip.close();
    }

    public static void import_data(Context context, Uri uri) throws IOException {
        File output_dir = context.getFilesDir();

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ZipInputStream zip = new ZipInputStream(inputStream);
        byte[] buffer = new byte[1024];

        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            String filename = entry.getName();
            File newFile = new File(output_dir, filename);
            File parentDir = newFile.getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs() && !parentDir.exists()) {
                    String error_msg = String.format("failed to create full parent dirs for <%s>", newFile.toString());
                    Logger.e(error_msg);
                    throw new IOException(error_msg);
                } else {
                    Logger.d("successfully created parent dir for <%s>", newFile.toString());
                }
            } else {
                if (!(parentDir.canRead() && parentDir.canWrite() && parentDir.canExecute())) {
                    String error_msg = String.format("parent dir for <%s> exists but not with proper permissions", newFile.toString());
                    Logger.e(error_msg);
                    throw new IOException(error_msg);
                } else {
                    Logger.v("parent dir for <%s> exists with proper permissions", newFile.toString());
                }
            }
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zip.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
        }
        zip.closeEntry();
        zip.close();
    }

    private static void addDirToZip(ZipOutputStream zip, File dir, String subdir_name) throws IOException {
        File subdir = new File(dir, subdir_name);
        for (File file : subdir.listFiles()) {
            byte[] buffer = new byte[1024];
            FileInputStream in = new FileInputStream(file);
            zip.putNextEntry(new ZipEntry(subdir_name + File.separator + file.getName()));
            int len;
            while ((len = in.read(buffer)) > 0) {
                zip.write(buffer, 0, len);
            }
        }
        zip.flush();
    }
}
