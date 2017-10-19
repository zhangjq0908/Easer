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

import ryey.easer.commons.IllegalXmlException;

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
     * Read data (to the instance) from a source (currently only XML)
     * @param parser
     * @param version The version of the to-be-parsed data. (See also {@link ryey.easer.commons.C})
     * @throws IOException
     * @throws XmlPullParserException
     * @throws IllegalXmlException
     */
    void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException;

    /**
     * Dump the data (of the instance) to a destination (currently only XML)
     * @param serializer
     * @throws IOException
     */
    void serialize(XmlSerializer serializer) throws IOException;

    /**
     * Check the data's validity. If not valid, this data won't be loaded and/or saved.
     * @return Whether the data is valid or not.
     */
    boolean isValid();
}
