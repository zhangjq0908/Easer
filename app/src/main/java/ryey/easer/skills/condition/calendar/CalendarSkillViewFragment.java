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

package ryey.easer.skills.condition.calendar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.commons.local_skill.ValidData;

import static ryey.easer.skills.condition.calendar.CalendarConditionMatchType.EVENT_TITLE;

public class CalendarSkillViewFragment extends SkillViewFragment<CalendarConditionData> {

    private final static String ACTION_RETURN = "ryey.easer.skills.condition.calendar.RETURN_FROM_DIALOG";
    private final static String EXTRA_CALENDAR_ID = "ryey.easer.skills.condition.calendar.extra.calendar_id";
    private final static String EXTRA_CALENDAR_NAME = "ryey.easer.skills.condition.calendar.extra.calendar_name";

    private long calendar_id = -1;
    private TextView tv_calendar_name;
    private RadioGroup rg_event_title;
    private RadioButton rb_event_title_any;
    private RadioButton rb_event_title_pattern;
    private EditText ti_event_title_pattern;
    private CheckBox cb_all_day_event;

    private final IntentFilter mFilter = new IntentFilter(ACTION_RETURN);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_RETURN)) {
                calendar_id = intent.getLongExtra(EXTRA_CALENDAR_ID, -1);
                tv_calendar_name.setText(intent.getStringExtra(EXTRA_CALENDAR_NAME));
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context.registerReceiver(mReceiver, mFilter);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_condition__calendar, container, false);
        tv_calendar_name = view.findViewById(R.id.text_calendar_name);
        rg_event_title = view.findViewById(R.id.radioGroup_event_title);
        rb_event_title_any = view.findViewById(R.id.radioButton_any_event);
        rb_event_title_pattern = view.findViewById(R.id.radioButton_event_match_title);
        ti_event_title_pattern = view.findViewById(R.id.textInput_event_pattern);
        cb_all_day_event = view.findViewById(R.id.checkBox_all_day_event);

        view.findViewById(R.id.calendar_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.hasPermission(getContext(), Manifest.permission.READ_CALENDAR))
                    return;
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle(R.string.ecalendar_select_dialog_title);

                final CursorLoader cursorLoader = new CursorLoader(getContext(),
                        CalendarContract.Calendars.CONTENT_URI,
                        new String[]{
                                CalendarContract.Calendars._ID,
                                CalendarContract.Calendars.ACCOUNT_NAME,
                                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        },
                        null, null, null);
                final SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                        getContext(), android.R.layout.simple_list_item_2,
                        cursorLoader.loadInBackground(),
                        new String[]{
                                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                                CalendarContract.Calendars.ACCOUNT_NAME,
                        },
                        new int[]{android.R.id.text1, android.R.id.text2}, 0) {
                    @Override
                    public void setViewText(TextView v, String text) {
                        if (v.getId() == android.R.id.text2)
                            text = "(" + text + ")";
                        super.setViewText(v, text);
                    }
                };

                cursorLoader.registerListener(0, (loader, data) -> { simpleCursorAdapter.swapCursor(data); });

                builderSingle.setAdapter(simpleCursorAdapter, (dialog, which) -> {
                        Cursor cursor = (Cursor) simpleCursorAdapter.getItem(which);
                        long id = cursor.getLong(0);
                        String name = cursor.getString(2);
                        Intent intent = new Intent(ACTION_RETURN);
                        intent.putExtra(EXTRA_CALENDAR_ID, id);
                        intent.putExtra(EXTRA_CALENDAR_NAME, name);
                        getContext().sendBroadcast(intent);
                    });

                builderSingle.show();
            }
        });

        rg_event_title.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == rb_event_title_pattern.getId()) {
                    ti_event_title_pattern.setEnabled(true);
                } else {
                    ti_event_title_pattern.setEnabled(false);
                }
            });

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull CalendarConditionData data) {
        CalendarData calendarData = data.data;
        calendar_id = calendarData.calendar_id;
        tv_calendar_name.setText(CalendarHelper.getCalendarName(
                getContext().getContentResolver(), calendar_id));
        if (calendarData.matchType == EVENT_TITLE) {
            rg_event_title.check(rb_event_title_pattern.getId());
            ti_event_title_pattern.setEnabled(true);
        } else {
            rg_event_title.check(rb_event_title_any.getId());
            ti_event_title_pattern.setEnabled(false);
        }
        ti_event_title_pattern.setText(calendarData.matchPattern);
        cb_all_day_event.setChecked(calendarData.isAllDayEvent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().unregisterReceiver(mReceiver);
    }

    @ValidData
    @NonNull
    @Override
    public CalendarConditionData getData() throws InvalidDataInputException {
        CalendarConditionMatchType matchType = CalendarConditionMatchType.ANY;
        int selected_rb = rg_event_title.getCheckedRadioButtonId();
        if (selected_rb == rb_event_title_any.getId()) {
            matchType = CalendarConditionMatchType.ANY;
        } else if (selected_rb == rb_event_title_pattern.getId()) {
            matchType = EVENT_TITLE;
        }
        CalendarData calendarData = new CalendarData(calendar_id, matchType, ti_event_title_pattern.getText().toString().trim(), cb_all_day_event.isChecked());
        return new CalendarConditionData(calendarData);
    }
}
