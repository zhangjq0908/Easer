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

package ryey.easer.core;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.ScriptTree;

class ConditionLotus extends Lotus {
    private final ConditionStructure conditionStructure;

    ConditionLotus(@NonNull Context context, @NonNull ScriptTree scriptTree, @NonNull ExecutorService executorService, @NonNull ConditionHolderService.CHBinder chBinder) {
        super(context, scriptTree, executorService, chBinder);
        conditionStructure = scriptTree.getCondition();
    }

    @Override
    protected void onListen() {
        chBinder.registerAssociation(conditionStructure.getName(), uri);
        Boolean state = chBinder.conditionState(conditionStructure.getName());
        if (state == null) {
        } else {
            onStateSignal(state);
        }
    }

    @Override
    protected void onCancel() {
        chBinder.unregisterAssociation(conditionStructure.getName(), uri);
    }
}
