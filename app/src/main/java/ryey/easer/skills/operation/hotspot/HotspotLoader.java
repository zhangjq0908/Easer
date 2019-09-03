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

package ryey.easer.skills.operation.hotspot;

import android.content.Context;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

class HotspotLoader extends OperationLoader<HotspotOperationData> {
    private final HotspotHelper helper;

    public HotspotLoader(Context context) {
        super(context);
        helper = HotspotHelper.getInstance(context);
    }

    @Override
    public void _load(@ValidData @NonNull HotspotOperationData data, @NonNull OnResultCallback callback) {
        Boolean state = data.get();
        try {
            if (helper.isApEnabled() == state)
                callback.onResult(true);
            if (state)
                callback.onResult(helper.enableAp());
            else
                callback.onResult(helper.disableAp());
        } catch (Exception e) {
            Logger.e(e, "error while changing hotspot state");
            e.printStackTrace();
            callback.onResult(false);
        }
    }
}
