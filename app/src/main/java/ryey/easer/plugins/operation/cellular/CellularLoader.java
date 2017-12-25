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

package ryey.easer.plugins.operation.cellular;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.plugins.reusable.PluginHelper;

public class CellularLoader extends OperationLoader<CellularOperationData> {
    public CellularLoader(Context context) {
        super(context);
    }

    @Override
    public boolean _load(@NonNull CellularOperationData data) {
        Boolean state = data.get();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (state == (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)) {
            return true;
        } else {
            if (PluginHelper.useRootFeature(context)) {
                try {
                    String command = "svc data " + (state ? "enable" : "disable");
                    PluginHelper.executeCommandAsRoot(context, command);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
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
                    return true;
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
        return false;
    }
}
