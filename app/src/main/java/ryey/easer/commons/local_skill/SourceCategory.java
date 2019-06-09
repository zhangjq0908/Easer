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

package ryey.easer.commons.local_skill;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import ryey.easer.R;

public enum SourceCategory {
    android,
    data_communication,
    date_time,
    easer,
    personal,
    device,
    misc,
    unknown;

    @NonNull
    public String toString(Resources resources) {
        switch (this) {
            case android:
                return resources.getString(R.string.category_source_android);
            case date_time:
                return resources.getString(R.string.category_source_date_time);
            case easer:
                return resources.getString(R.string.category_source_easer);
            case data_communication:
                return resources.getString(R.string.category_source_data_communication);
            case personal:
                return resources.getString(R.string.category_source_personal);
            case device:
                return resources.getString(R.string.category_source_device);
            case misc:
                return resources.getString(R.string.category_source_misc);
            case unknown:
                return resources.getString(R.string.category_source_unknown);
        }
        throw new IllegalStateException("Category isn't caught in switch statement");
    }

    public interface Categorized {
        @NonNull SourceCategory category();
    }
}
