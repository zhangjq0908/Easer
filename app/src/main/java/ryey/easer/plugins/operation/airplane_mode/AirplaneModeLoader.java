/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.operation.airplane_mode;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import java.io.IOException;

import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.plugins.reusable.PluginHelper;

public class AirplaneModeLoader extends OperationLoader<AirplaneModeOperationData> {
    public AirplaneModeLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull AirplaneModeOperationData data) {
        Boolean state = data.get();
        if (state == airplaneModeIsOn())
            return true;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            return switchBefore17(state);
        } else {
            if (switchAfter17(state))
                return true;
            else {
                switchBefore17(state);
                return airplaneModeIsOn();
            }
        }
    }

    private boolean airplaneModeIsOn() {
        boolean mode;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            mode = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        } else {
            mode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        }
        return mode;
    }

    private boolean switchBefore17(boolean newState) {
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, newState ? 1 : 0);

        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", !newState);
        context.sendBroadcast(intent);
        return true;
    }

    private boolean switchAfter17(boolean newState) {
        final String COMMAND_FLIGHT_MODE_1 = "settings put global airplane_mode_on";
        final String COMMAND_FLIGHT_MODE_2 = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state";
        if (PluginHelper.useRootFeature(context)) {
            try {
                int enabled = newState ? 1 : 0;
                String command = COMMAND_FLIGHT_MODE_1 + " " + enabled;
                PluginHelper.executeCommandAsRoot(context, command);
                command = COMMAND_FLIGHT_MODE_2 + " " + enabled;
                PluginHelper.executeCommandAsRoot(context, command);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
