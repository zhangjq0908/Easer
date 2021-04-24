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

package ryey.easer.skills.operation.wireguard;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import ryey.easer.skills.operation.OperationLoader;

public class WireguardLoader extends OperationLoader<WireguardOperationData> {
    static final String INTENT_STATE_UP = "com.wireguard.android.action.SET_TUNNEL_UP";
    static final String INTENT_STATE_DOWN = "com.wireguard.android.action.SET_TUNNEL_DOWN";

    static final String EXTRA_TUNNEL_NAME = "tunnel";

    WireguardLoader(Context context) {
        super(context);
    }

    @Override
    public void _load(@NonNull WireguardOperationData data, @NonNull OnResultCallback callback) {
        Intent intent = new Intent(data.tunnel_state ? INTENT_STATE_UP : INTENT_STATE_DOWN);
        intent.setPackage(WireguardOperationSkill.WIREGUARD_APP);
        intent.putExtra(EXTRA_TUNNEL_NAME, data.tunnel_name);
        context.sendBroadcast(intent);
        callback.onResult(true);
    }
}
