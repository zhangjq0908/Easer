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

package ryey.easer.core.data.storage;

public class C extends ryey.easer.commons.C {
    public static final String TYPE = "type";
    public static final String EVENT = "event";
    public static final String SCENARIO = "scenario";
    public static final String CONDITION = "condition";
    public static final String ACTIVE = "active";
    public static final String TRIG = "trigger";
    public static final String AFTER = "after";
    public static final String REVERSE = "reverse";
    public static final String REPEATABLE = "repeatable";
    public static final String PERSISTENT = "persistent";

    public static final String DYNAMICS = "dynamics";

    public class TriggerType {
        public static final String T_RAW = "raw_event";
        public static final String T_PRE = "pre_defined";
        public static final String T_CONDITION = "condition";
    }
}
