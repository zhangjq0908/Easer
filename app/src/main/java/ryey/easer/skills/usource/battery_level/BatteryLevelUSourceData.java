/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.usource.battery_level;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class BatteryLevelUSourceData implements USourceData {

    enum Type {
        system,
        custom,
    }

    interface Level extends USourceData {
    }

    static class SystemLevel implements Level {

        enum LevelChoice {
            low,
            ok_after_low,
        }

        @NonNull final LevelChoice levelChoice;

        SystemLevel(@NonNull LevelChoice levelChoice) {
            this.levelChoice = levelChoice;
        }

        SystemLevel(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
            levelChoice = LevelChoice.valueOf(data);
        }

        @Override
        @NonNull
        public String serialize(@NonNull PluginDataFormat format) {
            return levelChoice.name();
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof SystemLevel))
                return false;
            if (levelChoice != ((SystemLevel) obj).levelChoice)
                return false;
            return true;
        }

        @Nullable
        @Override
        public Dynamics[] dynamics() {
            return null;
        }

        SystemLevel(Parcel in) {
            levelChoice = (LevelChoice) Objects.requireNonNull(in.readSerializable());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(levelChoice);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SystemLevel> CREATOR = new Creator<SystemLevel>() {
            @Override
            public SystemLevel createFromParcel(Parcel in) {
                return new SystemLevel(in);
            }

            @Override
            public SystemLevel[] newArray(int size) {
                return new SystemLevel[size];
            }
        };
    }

    static class CustomLevel implements Level {

        private static final String K_LEVEL = "level";
        private static final String K_INCLUSIVE = "inclusive";

        final int battery_level;
        final boolean inclusive;

        CustomLevel(int battery_level, boolean inclusive) {
            this.battery_level = battery_level;
            this.inclusive = inclusive;
        }

        CustomLevel(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
            //noinspection SwitchStatementWithTooFewBranches
            switch (format) {
                default:
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        battery_level = jsonObject.getInt(K_LEVEL);
                        inclusive = jsonObject.getBoolean(K_INCLUSIVE);
                    } catch (JSONException e) {
                        throw new IllegalStorageDataException(e);
                    }
            }
        }

        @NonNull
        public String serialize(@NonNull PluginDataFormat format) {
            String res;
            //noinspection SwitchStatementWithTooFewBranches
            switch (format) {
                default:
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(K_LEVEL, battery_level);
                        jsonObject.put(K_INCLUSIVE, inclusive);
                        res = jsonObject.toString();
                    } catch (JSONException e) {
                        throw new IllegalStateException(e);
                    }
            }
            return res;
        }

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean isValid() {
            if (battery_level < 0)
                return false;
            if (battery_level > 100)
                return false;
            return true;
        }

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof CustomLevel))
                return false;
            if (battery_level != ((CustomLevel) obj).battery_level)
                return false;
            if (inclusive != ((CustomLevel) obj).inclusive)
                return false;
            return true;
        }

        @Nullable
        @Override
        public Dynamics[] dynamics() {
            return null;
        }

        CustomLevel(Parcel in) {
            battery_level = in.readInt();
            inclusive = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(battery_level);
            dest.writeByte((byte) (inclusive ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CustomLevel> CREATOR = new Creator<CustomLevel>() {
            @Override
            public CustomLevel createFromParcel(Parcel in) {
                return new CustomLevel(in);
            }

            @Override
            public CustomLevel[] newArray(int size) {
                return new CustomLevel[size];
            }
        };
    }

    private static final String K_TYPE = "type";
    private static final String K_LEVEL = "level";

    @NonNull final Type type;
    @NonNull final Level level;

    BatteryLevelUSourceData(@NonNull Type type, @NonNull Level level) {
        this.type = type;
        this.level = level;
    }

    BatteryLevelUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    type = Type.valueOf(jsonObject.getString(K_TYPE));
                    switch (type) {
                        case system:
                            level = new SystemLevel(jsonObject.getString(K_LEVEL), format, version);
                            break;
                        case custom:
                            level = new CustomLevel(jsonObject.getString(K_LEVEL), format, version);
                            break;
                        default:
                            throw new IllegalStorageDataException("unknown BatteryLevelUSourceData type");
                    }
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_TYPE, type.toString());
                    jsonObject.put(K_LEVEL, level.serialize(format));
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return level.isValid();
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return level.dynamics();
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof BatteryLevelUSourceData))
            return false;
        if (type != ((BatteryLevelUSourceData) obj).type)
            return false;
        if (!level.equals(((BatteryLevelUSourceData) obj).level))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(type);
        dest.writeParcelable(level, 0);
    }

    public static final Creator<BatteryLevelUSourceData> CREATOR
            = new Creator<BatteryLevelUSourceData>() {
        public BatteryLevelUSourceData createFromParcel(Parcel in) {
            return new BatteryLevelUSourceData(in);
        }

        public BatteryLevelUSourceData[] newArray(int size) {
            return new BatteryLevelUSourceData[size];
        }
    };

    private BatteryLevelUSourceData(Parcel in) {
        type = (Type) Objects.requireNonNull(in.readSerializable());
        level = Objects.requireNonNull(in.readParcelable(Level.class.getClassLoader()));
    }
}
