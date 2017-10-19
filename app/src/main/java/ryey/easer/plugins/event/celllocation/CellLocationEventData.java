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

package ryey.easer.plugins.event.celllocation;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class CellLocationEventData extends TypedEventData {
    protected List<CellLocationSingleData> data;

    {
        data = new ArrayList<>();
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public CellLocationEventData() {
    }

    public static CellLocationEventData fromString(String repr) {
        CellLocationEventData cellLocationEventData = new CellLocationEventData();
        cellLocationEventData.set(repr);
        if (cellLocationEventData.isValid())
            return cellLocationEventData;
        else
            return null;
    }

    @Override
    public Object get() {
        return data;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            String[] parts = ((String) obj).split("\n");
            for (String single : parts) {
                CellLocationSingleData singleData = new CellLocationSingleData();
                singleData.set(single);
                if (singleData.isValid())
                    data.add(singleData);
            }
        } else if (obj instanceof String[]) {
            String[] parts = (String[]) obj;
            Logger.d(parts);
            for (String single : parts) {
                CellLocationSingleData singleData = new CellLocationSingleData();
                singleData.set(single);
                if (singleData.isValid())
                    data.add(singleData);
            }
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (data.size() == 0)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException {
        if (version == C.VERSION_DEFAULT) {
            String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
            String[] parts = str_data.split(",");
            set(parts);
        } else {
            set(XmlHelper.EventHelper.readMultipleSituation(parser));
        }
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (data.size() == 0) {
            Logger.wtf("trying to serialize empty data");
            return;
        }
        List<String> list = new ArrayList<>();
        for (CellLocationSingleData singleData : data) {
            list.add(singleData.toString());
        }
        XmlHelper.EventHelper.writeMultipleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).name(), list.toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    public boolean add(CellLocationSingleData singleData) {
        if (contains(singleData))
            return false;
        data.add(singleData);
        return true;
    }

    public String toString() {
        String str = "";
        if (data.size() > 0) {
            str += data.get(0).toString();
            for (int i = 1; i < data.size(); i++) {
                CellLocationSingleData singleData = data.get(i);
                str += "\n" + singleData.toString();
            }
        }
        return str;
    }

    boolean contains(CellLocationSingleData singleData) {
        for (CellLocationSingleData singleData1 : data) {
            if (singleData.equals(singleData1))
                return true;
        }
        return false;
    }
}
