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

package ryey.easer.plugins.operation.brightness;

import ryey.easer.plugins.operation.IntegerOperationData;

public class BrightnessOperationData extends IntegerOperationData {

    {
        lbound = -1;
        rbound = 255;
    }

    public BrightnessOperationData() {
    }

    public BrightnessOperationData(boolean auto) {
        super(-1);
    }

    public BrightnessOperationData(Integer level) {
        super(level);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    boolean isAuto() {
        return get() == Integer.valueOf(-1);
    }
}
