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

package ryey.easer.plugins.operation.send_sms;

import android.content.Context;
import android.telephony.SmsManager;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import ryey.easer.Utils;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.plugins.reusable.PluginHelper;

public class SmsLoader extends OperationLoader {
    public SmsLoader(Context context) {
        super(context);
    }

    @Override
    public boolean _load(OperationData data) {
        Sms sms = (Sms) data.get();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sms.destination, null, sms.content, null, null);
        return true;
    }
}
