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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;

/**
 * Common interface used by both EventData and OperationData
 * Defines a series of methods that are needed.
 */
public interface StorageData {
    /**
     * @return The data that the instance holds.
     */
    Object get();

    /**
     * Set the data of the instance.
     * @param obj Data to-be-set.
     */
    void set(Object obj);

    /**
     * Compare two data instance. Two objects hold the same data are considered as equal.
     * @param o The other object to be compared.
     * @return Whether the two data instances are the same or not.
     */
    boolean equals(Object o);

    /**
     * This is an OLD interface and is kept only for compatibility.
     * Implementors should not really inherit this method (just leave it empty).
     * Please use the {@link #parse(String, C.Format, int)} method instead.
     *
     * Read data (to the instance) from a source (currently only XML)
     * @param parser
     * @param version The version of the to-be-parsed data. (See also {@link ryey.easer.commons.C})
     * @throws IOException
     * @throws XmlPullParserException
     * @throws IllegalStorageDataException
     */
    @Deprecated
    void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException;

    /**
     * This is an OLD interface and is kept only for compatibility.
     * Implementors should not really inherit this method (just leave it empty).
     * Please use the {@link #serialize(C.Format)} method instead.
     *
     * Dump the data (of the instance) to a destination (currently only XML)
     * @param serializer
     * @throws IOException
     */
    @Deprecated
    void serialize(XmlSerializer serializer) throws IOException;

    /**
     * Parse the given {@param data} to the current object.
     * @param format The format which the data source is in.
     *               This parameter can be ignored in the same way as in {@link #serialize(C.Format)}.
     */
    void parse(String data, C.Format format, int version) throws IllegalStorageDataException;

    /**
     * Serialize the current data to the given {@param format}.
     * @param format The format which the data source is in.
     *               You can ignore this parameter if you don't want to design a "localized" format.
     * @return The serialized text (which will be used directly in the data field).
     */
    String serialize(C.Format format);

    /**
     * Check the data's validity. If not valid, this data won't be loaded and/or saved.
     * @return Whether the data is valid or not.
     */
    boolean isValid();
}
