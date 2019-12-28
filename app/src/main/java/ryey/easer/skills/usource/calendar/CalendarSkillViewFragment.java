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

package ryey.easer.skills.usource.calendar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
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
import androidx.collection.ArraySet;

import java.util.Objects;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.SkillViewFragment;

public class CalendarSkillViewFragment extends SkillViewFragment<CalendarUSourceData> {

    private final static String ACTION_RETURN = "ryey.easer.skills.condition.calendar.RETURN_FROM_DIALOG";
    private final static String EXTRA_CALENDAR_ID = "ryey.easer.skills.condition.calendar.extra.calendar_id";
    private final static String EXTRA_CALENDAR_NAME = "ryey.easer.skills.condition.calendar.extra.calendar_name";

    enum Type {
        event,
        condition,
    }
    private Type type;
    private long calendar_id = -1;

    // For event, coupled with CalEventInnerData
    private final CheckBox[] cb_conditions = new CheckBox[CalEventInnerData.condition_name.length]; // The same order as `CalEventInnerData.condition_name`

    // For condition, coupled with CalConditionInnerData
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        context.registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().unregisterReceiver(mReceiver);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        switch (Objects.requireNonNull(getArguments().getString(USourceSkill.EXTRA_INFO_TYPE))) {
            case USourceSkill.INFO_TYPE_CONDITION:
                type = Type.condition;
                break;
            case USourceSkill.INFO_TYPE_EVENT:
                type = Type.event;
                break;
            default:
                throw new IllegalStateException("CalendarSkillViewFragment should either be a condition or event one");
        }
        if (type == Type.condition) {
            View view = inflater.inflate(R.layout.skill_usource__calendar_condition, container, false);
            rg_event_title = view.findViewById(R.id.radioGroup_event_title);
            rb_event_title_any = view.findViewById(R.id.radioButton_any_event);
            rb_event_title_pattern = view.findViewById(R.id.radioButton_event_match_title);
            ti_event_title_pattern = view.findViewById(R.id.textInput_event_pattern);
            cb_all_day_event = view.findViewById(R.id.checkBox_all_day_event);

            rg_event_title.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == rb_event_title_pattern.getId()) {
                    ti_event_title_pattern.setEnabled(true);
                } else {
                    ti_event_title_pattern.setEnabled(false);
                }
            });

            return view;
        } else {
            View view = inflater.inflate(R.layout.skill_usource__calendar_event, container, false);
            cb_conditions[0] = view.findViewById(R.id.checkBox_start);
            cb_conditions[1] = view.findViewById(R.id.checkBox_end);

            return view;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tv_calendar_name = view.findViewById(R.id.text_calendar_name);
        view.findViewById(R.id.calendar_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SkillUtils.checkPermission(getContext(), Manifest.permission.READ_CALENDAR))
                    return;
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle(R.string.calendar_select_dialog_title);

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
                cursorLoader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {
                    @Override
                    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                        simpleCursorAdapter.swapCursor(data);
                    }
                });
                builderSingle.setAdapter(simpleCursorAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor cursor = (Cursor) simpleCursorAdapter.getItem(which);
                        long id = cursor.getLong(0);
                        String name = cursor.getString(2);
                        Intent intent = new Intent(ACTION_RETURN);
                        intent.putExtra(EXTRA_CALENDAR_ID, id);
                        intent.putExtra(EXTRA_CALENDAR_NAME, name);
                        getContext().sendBroadcast(intent);
                    }
                });

                builderSingle.show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void _fill(@NonNull CalendarUSourceData data0) {
        calendar_id = data0.calendar_id;
        if (type == Type.condition) {
            CalConditionInnerData data = (CalConditionInnerData) data0.data;
            tv_calendar_name.setText(CalendarHelper.getCalendarName(
                    getContext().getContentResolver(), calendar_id));
            if (data.matchType == CalendarMatchType.EVENT_TITLE) {
                rg_event_title.check(rb_event_title_pattern.getId());
                ti_event_title_pattern.setEnabled(true);
            } else {
                rg_event_title.check(rb_event_title_any.getId());
                ti_event_title_pattern.setEnabled(false);
            }
            ti_event_title_pattern.setText(data.matchPattern);
            cb_all_day_event.setChecked(data.isAllDayEvent);
        } else {
            CalEventInnerData data = (CalEventInnerData) data0.data;
            tv_calendar_name.setText(CalendarHelper.getCalendarName(
                    getContext().getContentResolver(), calendar_id));
            for (int i = 0; i < CalEventInnerData.condition_name.length; i++) {
                cb_conditions[i].setChecked(data.conditions.contains(CalEventInnerData.condition_name[i]));
            }
        }
    }

    @NonNull
    @Override
    public CalendarUSourceData getData() throws InvalidDataInputException {
        if (type == Type.condition) {
            CalendarMatchType matchType = CalendarMatchType.ANY;
            int selected_rb = rg_event_title.getCheckedRadioButtonId();
            if (selected_rb == rb_event_title_any.getId()) {
                matchType = CalendarMatchType.ANY;
            } else if (selected_rb == rb_event_title_pattern.getId()) {
                matchType = CalendarMatchType.EVENT_TITLE;
            }
            return new CalendarUSourceData(calendar_id, new CalConditionInnerData(matchType, ti_event_title_pattern.getText().toString().trim(), cb_all_day_event.isChecked()));
        } else {
            ArraySet<String> conditions = new ArraySet<>();
            for (int i = 0; i < cb_conditions.length; i++) {
                if (cb_conditions[i].isChecked())
                    conditions.add(CalEventInnerData.condition_name[i]);
            }
            return new CalendarUSourceData(calendar_id, new CalEventInnerData(conditions));
        }
    }
}
