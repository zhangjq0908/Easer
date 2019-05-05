/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.operation.brightness;

import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.operation.OperationLoader;

public class BrightnessLoader extends OperationLoader<BrightnessOperationData> {
    public BrightnessLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull BrightnessOperationData data) {
        return loadthis(data);
    }

    private boolean loadthis(BrightnessOperationData data) {
        Integer level = data.get();
        if (data.isAuto())
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        else {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    level);
        }

        DumbSettingBrightnessActivity.applyBrightness(context, ((float) level) / 255);

        return true;
    }
}
