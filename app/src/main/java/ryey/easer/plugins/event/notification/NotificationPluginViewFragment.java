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

package ryey.easer.plugins.event.notification;

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

public class NotificationPluginViewFragment extends PluginViewFragment<NotificationEventData> {
    private EditText editText_app;
    private EditText editText_title;
    private EditText editText_content;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__notification, container, false);

        editText_app = view.findViewById(R.id.editText_app);
        editText_title = view.findViewById(R.id.editText_title);
        editText_content = view.findViewById(R.id.editText_content);

        return view;
    }

    @Override
    protected void _fill(@NonNull NotificationEventData data) {
        if (data instanceof NotificationEventData) {
            NotificationSelection notificationSelection = ((NotificationEventData) data).selection;
            editText_app.setText(notificationSelection.app);
            editText_title.setText(notificationSelection.title);
            editText_content.setText(notificationSelection.content);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @NonNull
    @Override
    public NotificationEventData getData() throws InvalidDataInputException {
        NotificationSelection notificationSelection = new NotificationSelection();
        notificationSelection.app = textOf(editText_app);
        notificationSelection.title = textOf(editText_title);
        notificationSelection.content = textOf(editText_content);
        return new NotificationEventData(notificationSelection);
    }

    @Nullable
    private static String textOf(EditText editText) {
        String text = editText.getText().toString();
        if (text.isEmpty())
            return null;
        else
            return text;
    }
}
