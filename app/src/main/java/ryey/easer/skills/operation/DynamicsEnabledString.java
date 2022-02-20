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

package ryey.easer.skills.operation;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;

public class DynamicsEnabledString implements Parcelable {

    public static DynamicsEnabledString fromView(EditText editText) {
        return new DynamicsEnabledString(editText.getText().toString());
    }

    @NonNull public final String raw;

    public DynamicsEnabledString(@NonNull String raw) {
        this.raw = raw;
    }

    @NonNull
    public Set<String> placeholders() {
        return Utils.extractPlaceholder(raw);
    }

    @NonNull
    public DynamicsEnabledString applyDynamics(@NonNull SolidDynamicsAssignment dynamicsAssignment) {
        String new_str = Utils.applyDynamics(raw, dynamicsAssignment);
        return new DynamicsEnabledString(new_str);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof  DynamicsEnabledString))
            return false;
        return raw.equals(((DynamicsEnabledString) obj).raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    protected DynamicsEnabledString(Parcel in) {
        raw = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(raw);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DynamicsEnabledString> CREATOR = new Creator<DynamicsEnabledString>() {
        @Override
        public DynamicsEnabledString createFromParcel(Parcel in) {
            return new DynamicsEnabledString(in);
        }

        @Override
        public DynamicsEnabledString[] newArray(int size) {
            return new DynamicsEnabledString[size];
        }
    };
}
