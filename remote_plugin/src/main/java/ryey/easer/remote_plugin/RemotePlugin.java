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

package ryey.easer.remote_plugin;

public class RemotePlugin {

    private RemotePlugin() {
    }

    public static final String TYPE_OPERATION_PLUGIN = "ryey.easer.remote_plugin.TYPE.OPERATION";

    // these actions are for passing between main player and plugins
    public static final String ACTION_REQUEST_PLUGIN_INFO = "ryey.easer.remote_plugin.action.REQUEST.PLUGIN_INFO"; // broadcast
    public static final String ACTION_RESPONSE_PLUGIN_INFO = "ryey.easer.remote_plugin.action.RESPONSE_PLUGIN_INFO"; // answer
    public static final String ACTION_REQUEST_EDIT_DATA = "ryey.easer.remote_plugin.action.REQUEST.EDIT_DATA";
    public static final String ACTION_RESPONSE_EDIT_DATA = "ryey.easer.remote_plugin.action.RESPONSE.EDIT_DATA";
    public static final String ACTION_REQUEST_PARSE_DATA = "ryey.easer.remote_plugin.action.REQUEST.PARSE_DATA";
    public static final String ACTION_RESPONSE_PARSE_DATA = "ryey.easer.remote_plugin.action.RESPONSE.PARSE_DATA";

    public static final String EXTRA_MESSAGE_ID = "ryey.easer.remote_plugin.extra.MESSAGE_ID";
    public static final String EXTRA_PACKAGE_NAME = "ryey.easer.remote_plugin.extra.PACKAGE_NAME";
    public static final String EXTRA_PLUGIN_ID = "ryey.easer.remote_plugin.extra.PLUGIN_ID";
    public static final String EXTRA_PLUGIN_NAME = "ryey.easer.remote_plugin.extra.PLUGIN_NAME";
    public static final String EXTRA_PLUGIN_TYPE = "ryey.easer.remote_plugin.extra.PLUGIN_TYPE";
    public static final String EXTRA_DATA = "ryey.easer.remote_plugin.extra.DATA";

    public static class OperationPlugin {

        public static final String ACTION_TRIGGER = "ryey.easer.remote_plugin.action.TRIGGER_ACTION";

        public static final String EXTRA_PLUGIN_CATEGORY = "ryey.easer.remote_plugin.extra.PLUGIN_CATEGORY";

    }
}
