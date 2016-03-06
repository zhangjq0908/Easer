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

package ryey.easer.plugins.profile.cellular;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.ProfileData;
import ryey.easer.commons.ProfileLoader;
import ryey.easer.commons.ProfilePlugin;
import ryey.easer.commons.SwitchItemLayout;

public class CellularProfilePlugin implements ProfilePlugin {
    @Override
    public String name() {
        return "cellular";
    }

    @Override
    public ProfileData data() {
        return new CellularProfileData();
    }

    @Override
    public ProfileData parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        CellularProfileData profileData = new CellularProfileData();
        profileData.set(XmlHelper.handleBoolean(parser, name()));
        return profileData;
    }

    @Override
    public void serialize(XmlSerializer serializer, ProfileData data) throws IOException {
        XmlHelper.dealBoolean(serializer, name(), (Boolean) data.get());
    }

    @Override
    public SwitchItemLayout view(Context context) {
        return new SwitchItemLayout(context, new CellularContentLayout(context));
    }

    @Override
    public ProfileLoader loader(Context context) {
        return new CellularLoader(context);
    }
}
