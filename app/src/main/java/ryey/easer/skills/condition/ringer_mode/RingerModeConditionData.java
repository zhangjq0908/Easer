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

package ryey.easer.skills.condition.ringer_mode;

import android.media.AudioManager;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.plugin.PluginDataFormat;

public class RingerModeConditionData implements ConditionData {
    public static final int COMPARE_MODE_LOWER_OR_EQUAL = 0;
    public static final int COMPARE_MODE_HIGHER_OR_EQUAL = 1;
    public static final int COMPARE_MODE_EQUALS = 2;

    int ringerMode;
    int ringerLevel;
    int compareMode;

    private static final String T_ringerMode = "ringerMode";
    private static final String T_ringerLevel = "ringerLevel";
    private static final String T_compareMode = "compareMode";

    RingerModeConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    this.ringerMode = jsonObject.optInt(T_ringerMode);
                    this.ringerLevel = jsonObject.optInt(T_ringerLevel);
                    this.compareMode = jsonObject.optInt(T_compareMode);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    RingerModeConditionData(int ringerMode, int ringerLevel, int compareMode) {
        this.ringerMode = ringerMode;
        this.ringerLevel = ringerLevel;
        this.compareMode = compareMode;
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(T_ringerMode, ringerMode);
                    jsonObject.put(T_ringerLevel, ringerLevel);
                    jsonObject.put(T_compareMode, compareMode);
                } catch (JSONException e) {
                    Logger.e(e, "Error putting %s data", getClass().getSimpleName());
                    e.printStackTrace();
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            return true;
        }
        if (ringerMode == AudioManager.RINGER_MODE_NORMAL &&
                (compareMode == COMPARE_MODE_LOWER_OR_EQUAL ||
                 compareMode == COMPARE_MODE_HIGHER_OR_EQUAL ||
                 compareMode == COMPARE_MODE_EQUALS)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof RingerModeConditionData))
            return false;
        if (((RingerModeConditionData) obj).ringerMode == ringerMode) {
            if (ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                return true;
            } else if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
                if (((RingerModeConditionData) obj).ringerLevel == ringerLevel && ((RingerModeConditionData) obj).compareMode == compareMode) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean match(int ringerMode, int ringerLevel) {
        if (ringerMode == this.ringerMode) {
            if (ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                return true;
            } else {
                switch (compareMode) {
                    case COMPARE_MODE_LOWER_OR_EQUAL:
                        return ringerLevel <= this.ringerLevel;
                    case COMPARE_MODE_HIGHER_OR_EQUAL:
                        return ringerLevel >= this.ringerLevel;
                    case COMPARE_MODE_EQUALS:
                        return ringerLevel == this.ringerLevel;
                }
            }
        }
        return false;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ringerMode);
        dest.writeInt(ringerLevel);
        dest.writeInt(compareMode);
    }

    public static final Creator<RingerModeConditionData> CREATOR
            = new Creator<RingerModeConditionData>() {
        public RingerModeConditionData createFromParcel(Parcel in) {
            return new RingerModeConditionData(in);
        }

        public RingerModeConditionData[] newArray(int size) {
            return new RingerModeConditionData[size];
        }
    };

    private RingerModeConditionData(Parcel in) {
        this.ringerMode = in.readInt();
        this.ringerLevel = in.readInt();
        this.compareMode = in.readInt();
    }
}
