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

package ryey.easer.plugins.operation.broadcast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.operation.OperationLoader;

public class BroadcastLoader extends OperationLoader<BroadcastOperationData> {
    public BroadcastLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull BroadcastOperationData data) {
        IntentData iData = preprocess(data.data);
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
        context.sendBroadcast(intent);
        return true;
    }

    private static IntentData preprocess(IntentData data) {
        IntentData res = new IntentData();
        res.action = data.action;
        if (data.category != null) {
            res.category = new ArrayList<>(data.category.size());
            res.category.addAll(data.category);
        }
        res.type = data.type;
        res.data = Uri.parse(data.data.getPath());
        if (data.extras != null) {
            res.extras = new ArrayList<>(data.extras.size());
            for (IntentData.ExtraItem extra : data.extras) {
                String key = extra.key;
                String value = extra.value;
                String type = extra.type;
                res.extras.add(new IntentData.ExtraItem(key, value, type));
            }
        }
        return res;
    }
}
