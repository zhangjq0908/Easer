/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ryey.easer.plugins.PluginRegistry;
import ryey.easer.R;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.DataStorage;
import ryey.easer.core.data.storage.xml.profile.ProfileXmlDataStorage;
import ryey.easer.commons.ProfileData;
import ryey.easer.commons.ProfilePlugin;
import ryey.easer.commons.StorageData;
import ryey.easer.commons.SwitchItemLayout;

public class EditProfileDialogFragment extends DialogFragment {
    public enum Purpose {
        add, edit, delete;
    }
    public static final String CONTENT_NAME = "ryey.easer.PROFILE.NAME";

    DataStorage<ProfileStructure> storage = null;

    Purpose purpose;
    String oldName = null;
    View mView = null;

    EditText mEditText = null;

    Map<String, SwitchItemLayout> items = new HashMap<>();

    public static EditProfileDialogFragment newInstance(Purpose purpose, String name) {
        EditProfileDialogFragment dialogFragment = new EditProfileDialogFragment();
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
            storage = ProfileXmlDataStorage.getInstance(activity);
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
                EditProfileDialogFragment.this.getDialog().cancel();
            }
        })
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean success = alterProfile();
                        if (success)
                            EditProfileDialogFragment.this.getDialog().dismiss();
                        else {
                            Toast.makeText(getActivity(), getString(R.string.prompt_save_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.alert_dialog_edit_profile, null);

        mEditText = (EditText) mView.findViewById(R.id.editText_profile_title);

        for (ProfilePlugin profilePlugin : PluginRegistry.getInstance().getProfilePlugins()) {
            SwitchItemLayout view = profilePlugin.view(getActivity());
            LinearLayout layout = (LinearLayout) mView.findViewById(R.id.layout_profiles);
            layout.addView(view);
            items.put(profilePlugin.name(), view);
        }

        Bundle bundle = getArguments();
        if (purpose != Purpose.add) {
            oldName = bundle.getString(CONTENT_NAME);
            if (purpose == Purpose.edit) {
                ProfileStructure profile = storage.get(oldName);
                loadFromProfile(profile);
            }
        }
        if (purpose == Purpose.delete) {
            builder.setMessage(getString(R.string.prompt_delete, oldName));
        } else {
            builder.setView(mView);
        }

        return builder.create();
    }

    protected void loadFromProfile(ProfileStructure profile) {
        mEditText.setText(oldName);

        for (ProfilePlugin plugin : PluginRegistry.getInstance().getProfilePlugins()) {
            SwitchItemLayout item = items.get(plugin.name());
            item.fill(profile.get(plugin.name()));
        }
    }

    protected ProfileStructure saveToProfile() {
        ProfileStructure profile = new ProfileStructure(mEditText.getText().toString());

        for (ProfilePlugin plugin : PluginRegistry.getInstance().getProfilePlugins()) {
            SwitchItemLayout item = items.get(plugin.name());
            StorageData data = item.getData();
            if (data == null)
                continue;
            if (data instanceof ProfileData) {
                profile.set(plugin.name(), (ProfileData) data);
            } else {
                Log.wtf(getClass().getSimpleName(), "data of plugin's Layout is not instance of ProfileData");
                throw new RuntimeException("data of plugin's Layout is not instance of ProfileData");
            }
        }

        return profile;
    }

    protected boolean alterProfile() {
        ProfileStructure newProfile = saveToProfile();
        boolean success;
        switch (purpose) {
            case add:
                success = storage.add(newProfile);
                break;
            case edit:
                success = storage.edit(oldName, newProfile);
                break;
            case delete:
                success = storage.delete(oldName);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Purpose");
        }
        if (success) {
            getActivity().sendBroadcast(new Intent(ProfileListFragment.ACTION_DATA_CHANGED));
        }
        return success;
    }
}
