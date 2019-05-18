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

package ryey.easer.skills.operation.command;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.operation.StringOperationData;

public class CommandOperationData extends StringOperationData {

    CommandOperationData(String command) {
        super(command);
    }

    CommandOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        super(data, format, version);
    }

    public static final Parcelable.Creator<CommandOperationData> CREATOR
            = new Parcelable.Creator<CommandOperationData>() {
        public CommandOperationData createFromParcel(Parcel in) {
            return new CommandOperationData(in);
        }

        public CommandOperationData[] newArray(int size) {
            return new CommandOperationData[size];
        }
    };

    private CommandOperationData(Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        return new CommandOperationData(Utils.applyDynamics(text, dynamicsAssignment));
    }
}
