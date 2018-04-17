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

package ryey.easer.commons.plugindef;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;

/**
 * Common interface used by both EventData and OperationData
 * Defines a series of methods that are needed.
 */
public interface StorageData extends Parcelable {

    /**
     * Parse the given {@param data} to the current object.
     * @param format The format which the data source is in.
     *               This parameter can be ignored in the same way as in {@link #serialize(C.Format)}.
     */
    void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException;

    /**
     * Serialize the current data to the given {@param format}.
     * @param format The format which the data source is in.
     *               You can ignore this parameter if you don't want to design a "localized" format.
     * @return The serialized text (which will be used directly in the data field).
     */
    @NonNull
    String serialize(@NonNull C.Format format);

    /**
     * Check the data's validity. If not valid, this data won't be loaded and/or saved.
     * @return Whether the data is valid or not.
     */
    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    boolean isValid();

    /**
     * Compare two data instance. Two objects hold the same data are considered as equal.
     * @param o The other object to be compared.
     * @return Whether the two data instances are the same or not.
     */
    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    boolean equals(Object o);
}
