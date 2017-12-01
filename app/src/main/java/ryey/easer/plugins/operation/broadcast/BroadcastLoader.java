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

package ryey.easer.plugins.operation.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.Utils;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;

public class BroadcastLoader extends OperationLoader {
    public BroadcastLoader(Context context) {
        super(context);
    }

    @Override
    public boolean _load(@NonNull OperationData data) {
        IntentData iData = (IntentData) data.get();
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
        } else {
            intent.setData(iData.data);
        }
        if (iData.extras != null) {
            Bundle extras = new Bundle();
            for (IntentData.ExtraItem item : iData.extras) {
                switch (item.type) {
                    case "string":
                        extras.putString(item.key, item.value);
                        break;
                    case "int":
                        extras.putInt(item.key, Integer.parseInt(item.value));
                        break;
                }
            }
            intent.putExtras(extras);
        }
        Logger.d(intent);
        context.sendBroadcast(intent);
        Logger.d("broadcast has been sent [%s]", iData);
        return true;
    }
}
