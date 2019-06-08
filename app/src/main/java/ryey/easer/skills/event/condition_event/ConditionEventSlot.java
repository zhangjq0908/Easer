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

package ryey.easer.skills.event.condition_event;

import android.content.Context;

import ryey.easer.core.EHService;
import ryey.easer.skills.event.SelfNotifiableSlot;

public class ConditionEventSlot extends SelfNotifiableSlot<ConditionEventEventData> {

    ConditionEventSlot(Context context, ConditionEventEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    ConditionEventSlot(Context context, ConditionEventEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        EHService.registerConditionEventNotifier(context, eventData.conditionName, notifyLotusData);
    }

    @Override
    public void cancel() {
        EHService.unregisterConditionEventNotifier(context, eventData.conditionName, notifyLotusData);
    }

}
