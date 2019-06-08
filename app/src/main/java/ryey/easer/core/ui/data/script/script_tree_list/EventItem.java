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

package ryey.easer.core.ui.data.script.script_tree_list;

import ryey.easer.R;
import tellh.com.recyclertreeview_lib.LayoutItemType;

public final class EventItem implements LayoutItemType {

    final String eventName;
    final boolean active;
    final boolean valid;

    public EventItem(String eventName, boolean active, boolean valid) {
        this.eventName = eventName;
        this.active = active;
        this.valid = valid;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_script_data_node;
    }
}
