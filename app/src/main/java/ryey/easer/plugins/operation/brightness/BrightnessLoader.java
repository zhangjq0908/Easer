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

package ryey.easer.plugins.operation.brightness;

import android.content.Context;
import android.provider.Settings;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;

public class BrightnessLoader extends OperationLoader {
    public BrightnessLoader(Context context) {
        super(context);
    }

    @Override
    public boolean _load(OperationData data) {
        if (data instanceof BrightnessOperationData)
            return loadthis((BrightnessOperationData) data);
        else {
            throw new IllegalArgumentTypeException(data.getClass(), BrightnessOperationData.class);
        }
    }

    public boolean loadthis(BrightnessOperationData data) {
        Integer level = (Integer) data.get();
        if (level == null)
            return true;
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
        return true;
    }
}
