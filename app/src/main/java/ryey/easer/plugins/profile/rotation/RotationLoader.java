/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.profile.rotation;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import ryey.easer.commons.ProfileData;
import ryey.easer.commons.ProfileLoader;

public class RotationLoader extends ProfileLoader {
    public RotationLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(ProfileData data) {
        ContentResolver resolver = context.getContentResolver();
        return Settings.System.putInt(resolver, Settings.System.ACCELEROMETER_ROTATION, 1);
    }
}
