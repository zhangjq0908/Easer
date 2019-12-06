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

package ryey.easer.core.ui.data;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@SuppressWarnings("WeakerAccess")
public final class EditDataProto {
    public static final String CONTENT_NAME = "ryey.easer.RUNTIME.EditDataProto.CONTENT_NAME";
    public static final String PREDECESSOR = "ryey.easer.RUNTIME.EditDataProto.PREDECESSOR";
    public static final String PURPOSE = "ryey.easer.RUNTIME.EditDataProto.PURPOSE";

    public enum Purpose {
        add, edit, delete
    }

    public static void add(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.add);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void addScript(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @Nullable String predecessor) {
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.add);
        if (predecessor != null)
            intent.putExtra(EditDataProto.PREDECESSOR, predecessor);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void edit(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @NonNull String name) {
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.edit);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        fragment.startActivityForResult(intent, requestCode);
    }
    
    public static void delete(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @NonNull String name) {
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.delete);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        fragment.startActivityForResult(intent, requestCode);
    }
}
