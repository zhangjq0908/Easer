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

package ryey.easer.skills.operation.launch_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class LaunchAppLoader extends OperationLoader<LaunchAppOperationData> {
    LaunchAppLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull LaunchAppOperationData data) {
        Intent intent;
        if (Utils.isBlank(data.app_class)) {
            intent = context.getPackageManager().getLaunchIntentForPackage(data.app_package);
        } else {
            intent = new Intent();
            intent.setComponent(new ComponentName(data.app_package, data.app_class));
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (data.extras != null)
                intent.putExtras(data.extras.asBundle());
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
