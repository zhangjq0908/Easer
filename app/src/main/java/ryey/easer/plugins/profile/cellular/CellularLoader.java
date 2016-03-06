/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.profile.cellular;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ryey.easer.commons.ProfileData;
import ryey.easer.commons.ProfileLoader;

public class CellularLoader extends ProfileLoader {
    public CellularLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(ProfileData data) {
        Boolean state = (Boolean) data.get();
        if (state == null)
            return true;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (state == (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)) {
            return true;
        } else {
            try {
                Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                getITelephonyMethod.setAccessible(true);
                Object ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
                Class ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
                Method dataConnSwitchmethod;
                if (state) {
                    dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
                } else {
                    dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
                }
                dataConnSwitchmethod.setAccessible(true);
                dataConnSwitchmethod.invoke(ITelephonyStub);
                return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
