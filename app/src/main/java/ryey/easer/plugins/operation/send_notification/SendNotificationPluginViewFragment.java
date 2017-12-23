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

package ryey.easer.plugins.operation.send_notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;

public class SendNotificationPluginViewFragment extends PluginViewFragment<SendNotificationOperationData> {
    private EditText editText_title;
    private EditText editText_content;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__send_notification, container, false);
        editText_title = view.findViewById(R.id.editText_title);
        editText_content = view.findViewById(R.id.editText_content);
        return view;
    }

    @Override
    protected void _fill(@NonNull SendNotificationOperationData data) {
        if (data instanceof SendNotificationOperationData) {
            NotificationContent notificationContent = ((SendNotificationOperationData) data).notificationContent;
            editText_title.setText(notificationContent.title);
            editText_content.setText(notificationContent.content);
        } else {
            throw new IllegalStateException();
        }
    }

    @NonNull
    @Override
    public SendNotificationOperationData getData() throws InvalidDataInputException {
        NotificationContent notificationContent = new NotificationContent();
        notificationContent.title = editText_title.getText().toString();
        notificationContent.content = editText_content.getText().toString();
        return new SendNotificationOperationData(notificationContent);
    }
}
