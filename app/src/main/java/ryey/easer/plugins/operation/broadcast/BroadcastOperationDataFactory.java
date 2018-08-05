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

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class BroadcastOperationDataFactory implements OperationDataFactory<BroadcastOperationData> {
    @NonNull
    @Override
    public Class<BroadcastOperationData> dataClass() {
        return BroadcastOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BroadcastOperationData dummyData() {
        IntentData intentData = new IntentData();
        intentData.action = "testAction";
        intentData.category = new ArrayList<>();
        intentData.category.add("testCategory");
        intentData.type = "myType";
        intentData.data = Uri.parse("myprot://seg1/seg2");
        intentData.extras = new ArrayList<>();
        IntentData.ExtraItem extraItem = new IntentData.ExtraItem();
        extraItem.key = "extra_key1";
        extraItem.value = "extra_value1";
        extraItem.type = "string";
        intentData.extras.add(extraItem);
        return new BroadcastOperationData(intentData);
    }

    @ValidData
    @NonNull
    @Override
    public BroadcastOperationData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new BroadcastOperationData(data, format, version);
    }
}
