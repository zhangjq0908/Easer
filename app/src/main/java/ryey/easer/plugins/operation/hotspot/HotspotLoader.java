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

package ryey.easer.plugins.operation.hotspot;

import android.content.Context;

import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;

public class HotspotLoader extends OperationLoader {
    HotspotHelper helper;

    public HotspotLoader(Context context) {
        super(context);
        helper = HotspotHelper.getInstance(context);
    }

    @Override
    public boolean _load(OperationData data) {
        Boolean state = (Boolean) data.get();
        if (state == null)
            return true;
        try {
            if (helper.isApEnabled() == state)
                return true;
            if (state)
                return helper.enableAp();
            else
                return helper.disableAp();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
