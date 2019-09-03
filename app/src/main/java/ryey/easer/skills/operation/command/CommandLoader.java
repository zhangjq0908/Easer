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
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.operation.OperationLoader;

public class CommandLoader extends OperationLoader<CommandOperationData> {
    public CommandLoader(Context context) {
        super(context);
    }

    @Override
    public void _load(@ValidData @NonNull CommandOperationData data, @NonNull OnResultCallback callback) {
        boolean success = true;
        String text = data.get();
        String []commands = text.split("\n");
        Process process;
        try {
            if (SkillUtils.useRootFeature(context)) {
                process = SkillUtils.executeCommandsAsRoot(commands);
            } else {
                process = SkillUtils.executeCommandsContinuously(commands);
            }
        } catch (IOException e) {
            Logger.e(e, "error while launching process");
            e.printStackTrace();
            success = false;
        }
        callback.onResult(success);
    }
}
