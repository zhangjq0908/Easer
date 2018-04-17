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

package ryey.easer.commons;

/*
 * Const values used when handling storage
 */
public class C {
    public static final String NAME = "name";
    public static final String PROFILE = "profile";
    public static final String SIT = "situation";
    public static final String SPEC = "spec";
    public static final String OPERATION = "operation";
    public static final String DATA = "data";
    public static final String OFF = "off";
    public static final String ON = "on";

    public static final String VERSION = "version";
    public static final int VERSION_REMOVED_EVENT_TYPE = 7;
    public static final int VERSION_GRANULAR_RINGER_MODE = 6;
    public static final int VERSION_CLEANER_SCENARIO = 5;
    public static final int VERSION_USE_SCENARIO = 4;
    public static final int VERSION_WIFI_ADD_BSSID = 3;
    public static final int VERSION_ADD_JSON = 2;
    public static final int VERSION_FULL_MULTI = 1;
    public static final int VERSION_FALLBACK = 0;
    public static final int VERSION_CREATED_IN_RUNTIME = -1;
    public static final int VERSION_CURRENT = VERSION_REMOVED_EVENT_TYPE;

    public enum Format {
        JSON,
    }
}
