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
        addDirToZip(zip, parent_dir, "scenario");
        addDirToZip(zip, parent_dir, "profile");

        zip.close();
    }

    private static boolean is_valid_easer_export_data(Context context, Uri uri) throws IOException {
        final String re_top_level = "^[^/]+$";
        final String re_any_of_three_any = "^(?:(?:event)|(?:profile)|(?:scenario))(?:/.*)?$";

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ZipInputStream zip = new ZipInputStream(inputStream);
        try {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                String name = entry.getName();
                if (!name.matches(re_any_of_three_any)) {
                    return false;
                }
                if (name.matches(re_top_level)) {
                    if (!entry.isDirectory())
                        return false;
                }
            }
        } finally {
            zip.closeEntry();
            zip.close();
        }
        return true;
    }

    public static void import_data(Context context, Uri uri) throws IOException, InvalidExportedDataException {
        if (!is_valid_easer_export_data(context, uri)) {
            throw new InvalidExportedDataException("exported data is not valid");
        }

        File output_dir = context.getFilesDir();

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ZipInputStream zip = new ZipInputStream(inputStream);
        byte[] buffer = new byte[1024];

        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (entry.isDirectory()) {
                File dir = new File(output_dir, entry.getName());
                if (!dir.exists()) {
                    if (!dir.mkdir()) {
                        String error_msg = String.format("failed to create dir <%s>", dir.toString());
                        Logger.e(error_msg);
                        throw new IOException(error_msg);
                    } else {
                        Logger.d("successfully created dir <%s>", dir.toString());
                    }
                } else {
                    if (!dir.isDirectory()) {
                        String error_msg = String.format("<%s> exists but is not a directory", dir.toString());
                        Logger.e(error_msg);
                        throw new IOException(error_msg);
                    } else {
                        Logger.v("dir <%s> exists", dir.toString());
                    }
                }
            } else {
                String filename = entry.getName();
                File newFile = new File(output_dir, filename);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zip.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
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
