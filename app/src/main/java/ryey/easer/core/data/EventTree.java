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

package ryey.easer.core.data;

import java.util.LinkedList;
import java.util.List;

import ryey.easer.commons.EventData;

public class EventTree {
    String name;
    EventData event;
    String profile;
    List<EventTree> subs;
//    protected static EventTree root = null;
//    synchronized public static EventTree getRoot() {
//        if (root == null) {
//            root = new EventTree("myroot", null, null);
//        }
//        return root;
//    }
    public EventTree(String name, EventData event, String profile) {
        this.name = name;
        this.event = event;
        this.profile = profile;
        this.subs = new LinkedList<>();
    }
    public String getName() {
        return name;
    }
    public EventData getEvent() {
        return event;
    }
    public void addSub(String name, EventData event, String profile) {
        EventTree sub = new EventTree(name, event, profile);
        addSub(sub);
    }
    public void addSub(EventTree sub) {
        subs.add(sub);
    }
    public List<EventTree> getSubs() {
        return subs;
    }
    public String getProfile() {
        return profile;
    }
}
