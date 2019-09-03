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

package ryey.easer.skills.operation.cellular;

import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.operation.OperationLoader;

public class CellularLoader extends OperationLoader<CellularOperationData> {
    public CellularLoader(Context context) {
        super(context);
    }

    @Override
    public void _load(@ValidData @NonNull CellularOperationData data, @NonNull OnResultCallback callback) {
        Boolean state = data.get();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (state == (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)) {
            callback.onResult(true);
            return;
        } else {
            if (SkillUtils.useRootFeature(context)) {
                try {
                    String command = "svc data " + (state ? "enable" : "disable");
                    SkillUtils.executeCommandAsRoot(context, command);
                    callback.onResult(true);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onResult(false);
                    return;
                }
            } else {
                try {
                    Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                    Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                    getITelephonyMethod.setAccessible(true);
                    Object ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
                    Class ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
                    Method dataConnSwitchMethod;
                    if (state) {
                        dataConnSwitchMethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
                    } else {
                        dataConnSwitchMethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
                    }
                    dataConnSwitchMethod.setAccessible(true);
                    dataConnSwitchMethod.invoke(ITelephonyStub);
                    callback.onResult(true);
                    return;
                } catch (ClassNotFoundException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                }
            }
        }
        callback.onResult(false);
    }
}
