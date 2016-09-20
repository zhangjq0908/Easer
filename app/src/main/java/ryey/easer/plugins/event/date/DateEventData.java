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

package ryey.easer.plugins.event.date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;

import static ryey.easer.plugins.event.date.DateEventPlugin.pname;

public class DateEventData implements EventData {
    private static SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

    private static String DateToText(Calendar calendar) {
        return sdf_date.format(calendar.getTime());
    }

    private static Calendar TextToDate(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_date.parse(text));
        return calendar;
    }

    Calendar date = null;

    public DateEventData() {}

    public DateEventData(Calendar date) {
        this.date = date;
    }

    @Override
    public Object get() {
        return date;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Calendar) {
            date = (Calendar) obj;
        } else {
            throw new RuntimeException("illegal data type");
        }
    }

    @Override
    public boolean isValid() {
        if (date == null)
            return false;
        return true;
    }

    @Override
    public Class<? extends EventPlugin> pluginClass() {
        return DateEventPlugin.class;
    }

    @Override
    public void parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        String str_data = XmlHelper.readSingleSituation(parser);
        try {
            set(TextToDate(str_data));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalXmlException(String.format("Illegal Event: illegal time format %s", str_data));
        }
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        Calendar date = (Calendar) get();
        if (date != null) {
            XmlHelper.writeSingleSituation(serializer, pname(), DateToText(date));
            XmlHelper.writeLogic(serializer);
        }
    }
}
