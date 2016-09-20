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

package ryey.easer.core.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ryey.easer.R;
import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;
import ryey.easer.commons.StorageData;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.EventDataStorage;
import ryey.easer.core.data.storage.xml.event.XmlEventDataStorage;
import ryey.easer.core.data.storage.xml.profile.XmlProfileDataStorage;
import ryey.easer.plugins.PluginRegistry;

/*
 * TODO: change the layout
 *  TODO: change from checkbox to radiobutton
 */
public class EditEventDialogFragment extends DialogFragment {
    public enum Purpose {
        add, edit, delete;
    }
    public static final String CONTENT_NAME = "ryey.easer.EVENT.NAME";

    EventDataStorage storage = null;

    Purpose purpose;
    String oldName = null;
    View mView = null;

    Map<String, SwitchItemLayout> items = new HashMap<>();

    EditText mEditText_name = null;
    private static final String NON = ""; //TODO: more robust
    Spinner mSpinner_parent = null;
    List<String> mEventList = null;
    Spinner mSpinner_profile = null;
    List<String> mProfileList = null;

    public static EditEventDialogFragment newInstance(Purpose purpose, String name) {
        EditEventDialogFragment dialogFragment = new EditEventDialogFragment();
        dialogFragment.purpose = purpose;
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_NAME, name);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            storage = XmlEventDataStorage.getInstance(activity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        storage = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreateDialog. Activity:" + getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditEventDialogFragment.this.getDialog().cancel();
            }
        })
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean success = saveEvent();
                        if (success)
                            EditEventDialogFragment.this.getDialog().dismiss();
                        else {
                            Toast.makeText(getActivity(), getString(R.string.prompt_save_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.alert_dialog_edit_event, null);

        mEditText_name = (EditText) mView.findViewById(R.id.editText_event_title);

        mSpinner_parent = (Spinner) mView.findViewById(R.id.spinner_parent);
        try {
            mEventList = (XmlEventDataStorage.getInstance(getActivity())).list();
            mEventList.add(0, NON);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_profile, mEventList); //TODO: change layout
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            mSpinner_parent.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSpinner_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinner_profile = (Spinner) mView.findViewById(R.id.spinner_profile);
        try {
            mProfileList = (XmlProfileDataStorage.getInstance(getActivity())).list();
            mProfileList.add(0, NON);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_profile, mProfileList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            mSpinner_profile.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSpinner_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        for (EventPlugin eventPlugin : PluginRegistry.getInstance().getEventPlugins()) {
            SwitchItemLayout view = new SwitchItemLayout(getActivity(), eventPlugin.view(getActivity()));
            LinearLayout layout = (LinearLayout) mView.findViewById(R.id.layout_events);
            layout.addView(view);
            items.put(eventPlugin.name(), view);
        }

        Bundle bundle = getArguments();
        if (purpose != Purpose.add) {
            oldName = bundle.getString(CONTENT_NAME);
            if (purpose == Purpose.edit) {
                EventStructure event = storage.get(oldName);
                loadFromEvent(event);
            }
        }
        if (purpose == Purpose.delete) {
            builder.setMessage(getString(R.string.prompt_delete, oldName));
        } else {
            builder.setView(mView);
        }

        return builder.create();
    }

    protected void loadFromEvent(EventStructure event) {
        oldName = event.getName();
        mEditText_name.setText(oldName);
        String profile = event.getProfileName();
        if (profile == null)
            profile = NON;
        mSpinner_profile.setSelection(mProfileList.indexOf(profile));
        String parent = event.getParentName();
        mSpinner_parent.setSelection(mEventList.indexOf(parent));

        for (EventPlugin plugin : PluginRegistry.getInstance().getEventPlugins()) {
            SwitchItemLayout item = items.get(plugin.name());
            if (event.getEventData().pluginClass() == plugin.getClass()) {
                item.fill(event.getEventData());
            } else {
                item.fill(null);
            }
        }
    }

    protected EventStructure saveToEvent() {
        EventStructure event = new EventStructure(mEditText_name.getText().toString());
        String profile = (String) mSpinner_profile.getSelectedItem();
        event.setProfileName(profile);
        String parent = (String) mSpinner_parent.getSelectedItem();
        if (!parent.equals(NON))
            event.setParentName(parent);

        for (EventPlugin plugin : PluginRegistry.getInstance().getEventPlugins()) {
            SwitchItemLayout item = items.get(plugin.name());
            StorageData data = item.getData();
            if (data == null)
                continue;
            if (data instanceof EventData) {
                event.setEventData((EventData) data);
            } else {
                Log.wtf(getClass().getSimpleName(), "data of plugin's Layout is not instance of EventData");
                throw new RuntimeException("data of plugin's Layout is not instance of EventData");
            }
            break;
        }

        return event;
    }

    protected boolean saveEvent() {
        EventStructure newEvent = saveToEvent();
        boolean success;
        switch (purpose) {
            case add:
                success = storage.add(newEvent);
                break;
            case edit:
                success = storage.edit(oldName, newEvent);
                break;
            case delete:
                success = storage.delete(oldName);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Purpose");
        }
        if (success) {
            getActivity().sendBroadcast(new Intent(EventListFragment.ACTION_DATA_CHANGED));
        }
        return success;
    }
}
