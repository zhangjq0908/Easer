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

import java.util.HashMap;
import java.util.Map;

import ryey.easer.commons.plugindef.operationplugin.OperationData;

/*
 * A Profile is a bundle of several Operation(s).
 * Only the `EditProfileDialogFragment` and `ProfileLoaderIntentService` need to know the detailed structure of a Profile,
 * other classes only need to know the *name* of the Profile.
 */
final public class ProfileStructure {
    String name;

    Map<String, OperationData> data = new HashMap<>();

    public ProfileStructure() {
    }

    public ProfileStructure(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperationData get(String key) {
        return data.get(key);
    }
    public void set(String key, OperationData value) {
        data.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProfileStructure))
            return false;
        ProfileStructure ot = (ProfileStructure) o;
        if (!getName().equals(ot.getName()))
            return false;
        if (!data.equals(((ProfileStructure) o).data))
            return false;
        return true;
    }

    public boolean isValid() {
        if ((name == null) || (name.isEmpty()))
            return false;
        return true;
    }
}
