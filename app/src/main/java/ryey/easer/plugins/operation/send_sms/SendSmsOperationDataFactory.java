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

package ryey.easer.plugins.operation.send_sms;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class SendSmsOperationDataFactory implements OperationDataFactory<SmsOperationData> {
    @NonNull
    @Override
    public Class<SmsOperationData> dataClass() {
        return SmsOperationData.class;
    }

    @NonNull
    @Override
    public SmsOperationData emptyData() {
        return new SmsOperationData();
    }

    @ValidData
    @NonNull
    @Override
    public SmsOperationData dummyData() {
        String destination = "15077707777";
        String content = "mysmscontent";
        return new SmsOperationData(destination, content);
    }

    @ValidData
    @NonNull
    @Override
    public SmsOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SmsOperationData(data, format, version);
    }
}
