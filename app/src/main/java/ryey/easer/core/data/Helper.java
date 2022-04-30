/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.core.data;

import android.content.Context;
import android.net.Uri;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Helper {
    public static void export_data(Context context, Uri uri) throws IOException {
        OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
        export_data(context, outputStream);
    }

    public static void export_data(Context context, File dest) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(dest);
        export_data(context, fileOutputStream);
    }

    public static void export_data(Context context, OutputStream outputStream) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        File parent_dir = context.getFilesDir();

        for (File file : parent_dir.listFiles()) {
            if (file.isDirectory())
                addDirToZip(zip, parent_dir, file.getName());
        }

        zip.close();
    }

    private static boolean is_valid_easer_export_data(Context context, Uri uri) throws IOException {
        final String re_top_level = "^[^/]+$";
        final String re_any_of_valid_dirs = "^(?:(?:event)|(?:script)|(?:profile)|(?:scenario)|(?:condition))(?:/.*)?$";

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ZipInputStream zip = new ZipInputStream(inputStream);
        try {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                String name = entry.getName();
                if (!name.matches(re_any_of_valid_dirs)) {
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

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        import_data(context, inputStream);
    }

    public static void import_data(Context context, InputStream inputStream) throws IOException {
        File output_dir = context.getFilesDir();
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
