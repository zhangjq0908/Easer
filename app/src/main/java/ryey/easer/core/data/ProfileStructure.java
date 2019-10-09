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

package ryey.easer.core.data;

import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.remote_plugin.RemoteOperationData;

/*
 * A Profile is a bundle of several Operation(s).
 * Only the `EditProfileActivity` and `ProfileLoaderService` need to know the detailed structure of a Profile,
 * other classes only need to know the *name* of the Profile.
 */
final public class ProfileStructure implements Named, Verifiable, WithCreatedVersion {
    private final int createdVersion;
    private final String name;

    private final Multimap<String, RemoteLocalOperationDataWrapper> data;

    public ProfileStructure(int createdVersion, String name, Multimap<String, RemoteLocalOperationDataWrapper> data) {
        this.createdVersion = createdVersion;
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    //TODO: concurrent
    public Set<String> pluginIds() {
        return data.keySet();
    }
    public Collection<RemoteLocalOperationDataWrapper> get(String key) {
        return data.get(key);
    }

    @Nullable
    public Set<String> placeholders() {
        Set<String> placeholders = new ArraySet<>();
        for (String key : data.keys()) {
            for (RemoteLocalOperationDataWrapper dataWrapper : data.get(key)) {
                if (dataWrapper.isRemote())
                    break;
                OperationData operationData = dataWrapper.localData;
                assert operationData != null;
                Set<String> ph = operationData.placeholders();
                if (ph != null)
                    placeholders.addAll(ph);
            }
        }
        return placeholders;
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

    @Override
    public int createdVersion() {
        return createdVersion;
    }

    public Builder inBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private final int createdVersion;
        String name;
        Multimap<String, RemoteLocalOperationDataWrapper> data = LinkedListMultimap.create();

        public Builder(int createdVersion) {
            this.createdVersion = createdVersion;
        }

        public Builder(ProfileStructure copyFrom) {
            this.createdVersion = copyFrom.createdVersion;
            this.name = copyFrom.name;
            this.data = copyFrom.data;
        }

        public ProfileStructure build() throws BuilderInfoClashedException {
            if (createdVersion < -1)
                throw new BuilderInfoClashedException("Profile createdVersion should not be less than -1");
            if (name == null)
                throw new BuilderInfoClashedException("Profile name shall not be null");
            if (data == null)
                throw new BuilderInfoClashedException("Profile data shall not be null");
            return new ProfileStructure(createdVersion, name, data);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setData(Multimap<String, RemoteLocalOperationDataWrapper> data) {
            this.data = data;
            return this;
        }

        public Builder put(String key, RemoteOperationData value) {
            data.put(key, new RemoteLocalOperationDataWrapper(value));
            return this;
        }

        public Builder put(String key, OperationData value) {
            data.put(key, new RemoteLocalOperationDataWrapper(value));
            return this;
        }

        public Builder set(String key, Collection<OperationData> dataCollection) {
            Collection<RemoteLocalOperationDataWrapper> wrapperCollection = new ArrayList<>(dataCollection.size());
            for (OperationData operationData : dataCollection) {
                wrapperCollection.add(new RemoteLocalOperationDataWrapper(operationData));
            }
            data.replaceValues(key, wrapperCollection);
            return this;
        }
    }
}
