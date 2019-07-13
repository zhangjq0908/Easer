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

package ryey.easer.skills.operation.broadcast;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class BroadcastLoader extends OperationLoader<BroadcastOperationData> {
    public BroadcastLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull BroadcastOperationData data) {
        IntentData iData = data.data;
        Intent intent = new Intent();
        intent.setAction(iData.action);
        if (iData.category != null)
            for (String category : iData.category) {
                intent.addCategory(category);
            }
        boolean hasType = false, hasData = false;
        if (!Utils.isBlank(iData.type))
            hasType = true;
        if (iData.data != null && !Utils.isBlank(iData.data.toString()))
            hasData = true;
        if (hasType && hasData) {
            intent.setDataAndType(iData.data, iData.type);
        } else if (hasType) {
            intent.setType(iData.type);
        } else if (hasData) {
            intent.setData(iData.data);
        }
        if (iData.extras != null) {
            intent.putExtras(iData.extras.asBundle());
        }
        context.sendBroadcast(intent);
        return true;
    }
}
