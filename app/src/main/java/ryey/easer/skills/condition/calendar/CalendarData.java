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

package ryey.easer.skills.condition.calendar;

class CalendarData {
    final long calendar_id;
    final CalendarConditionMatchType matchType;
    final String matchPattern;
    final boolean isAllDayEvent;

    CalendarData(long calendar_id, CalendarConditionMatchType matchType, String matchPattern, boolean isAllDayEvent) {
        this.calendar_id = calendar_id;
        this.matchType = matchType;
        this.matchPattern = matchPattern;
        this.isAllDayEvent = isAllDayEvent;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof CalendarData))
            return false;
        if (calendar_id != ((CalendarData) obj).calendar_id
                || isAllDayEvent != ((CalendarData) obj).isAllDayEvent
                || matchType != ((CalendarData) obj).matchType) {
            return false;
        }
        if (matchType == CalendarConditionMatchType.EVENT_TITLE && !(matchPattern.equals(((CalendarData) obj).matchPattern))) {
            return false;
        }
        return true;
    }
}
