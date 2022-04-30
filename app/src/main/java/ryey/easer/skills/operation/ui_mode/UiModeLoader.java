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

package ryey.easer.skills.operation.ui_mode;

import android.app.UiModeManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class UiModeLoader extends OperationLoader<UiModeOperationData> {
    UiModeLoader(Context context) {
        super(context);
    }

    @Override
    public void _load(@ValidData @NonNull UiModeOperationData data, @NonNull OnResultCallback callback) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager == null) {
            Logger.wtf("Can't get UiModeManager???");
            callback.onResult(false);
            return;
        }
        if (data.ui_mode == UiModeOperationData.UiMode.car) {
            uiModeManager.enableCarMode(0);
        } else { // if (data.ui_mode == UiModeOperationData.UiMode.normal) {
            // noinspection WrongConstant    Lint is wrong in this case
            uiModeManager.disableCarMode(0);
        }
        callback.onResult(true);
    }
}
