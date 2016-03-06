/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.wifi;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.AbstractSlot;
import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;
import ryey.easer.commons.SwitchItemLayout;

public class WifiEventPlugin implements EventPlugin {
    @Override
    public String name() {
        return "wifi connection";
    }

    @Override
    public EventData data() {
        return new WifiEventData();
    }

    @Override
    public EventData parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        WifiEventData eventData = new WifiEventData();

        String str_data = XmlHelper.readSingleSituation(parser);
        eventData.set(str_data);

        return eventData;
    }

    @Override
    public void serialize(XmlSerializer serializer, EventData data) throws IOException {
        String wifi = (String) data.get();
        if (wifi != null) {
            XmlHelper.writeSingleSituation(serializer, name(), wifi);
            XmlHelper.writeLogic(serializer);
        }
    }

    @Override
    public SwitchItemLayout view(Context context) {
        return new SwitchItemLayout(context, new WifiContentLayout(context));
    }

    @Override
    public AbstractSlot slot(Context context) {
        return new WifiConnSlot(context);
    }
}
