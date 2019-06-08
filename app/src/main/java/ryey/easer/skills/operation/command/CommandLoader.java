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

package ryey.easer.skills.operation.command;

import android.content.Context;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillHelper;
import ryey.easer.skills.operation.OperationLoader;

public class CommandLoader extends OperationLoader<CommandOperationData> {
    public CommandLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull CommandOperationData data) {
        boolean success = true;
        String text = data.get();
        String []commands = text.split("\n");
        Process process;
        try {
            if (SkillHelper.useRootFeature(context)) {
                process = SkillHelper.executeCommandsAsRoot(commands);
            } else {
                process = SkillHelper.executeCommandsContinuously(commands);
            }
        } catch (IOException e) {
            Logger.e(e, "error while launching process");
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
