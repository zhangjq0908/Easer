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

package ryey.easer.skills.operation.intent;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.operation.ExtraItem;
import ryey.easer.skills.operation.Extras;

class IntentOperationDataFactory implements OperationDataFactory<IntentOperationData> {
    @NonNull
    @Override
    public Class<IntentOperationData> dataClass() {
        return IntentOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public IntentOperationData dummyData() {
        IntentData intentData = new IntentData();
        intentData.action = "testAction";
        intentData.category = new ArrayList<>();
        intentData.category.add("testCategory");
        intentData.type = "myType";
        intentData.data = Uri.parse("myprot://seg1/seg2");
        ArrayList<ExtraItem> extras = new ArrayList<>();
        ExtraItem extraItem = new ExtraItem("extra_key1", "extra_value1", "string");
        extras.add(extraItem);
        intentData.extras = Extras.mayConstruct(extras);
        return new IntentOperationData(intentData);
    }

    @ValidData
    @NonNull
    @Override
    public IntentOperationData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new IntentOperationData(data, format, version);
    }
}
