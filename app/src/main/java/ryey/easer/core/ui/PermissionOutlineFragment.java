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

package ryey.easer.core.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orhanobut.logger.Logger;

import java.util.List;

import ryey.easer.R;
import ryey.easer.SettingsHelper;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.skills.LocalSkillRegistry;

public class PermissionOutlineFragment extends Fragment {

    Button mButton;

    public PermissionOutlineFragment() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permission_outline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mButton = view.findViewById(R.id.button_more);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAllPermissions();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasAllRequiredPermissions()) {
            getView().setVisibility(View.GONE);
        } else {
            getView().setVisibility(View.VISIBLE);
        }
    }

    boolean hasAllRequiredPermissions() {
        final boolean logging = SettingsHelper.logging(getContext());
        boolean satisfied = true;
        for (Object obj_plugin : LocalSkillRegistry.getInstance().all().getEnabledSkills(getContext())) {
            Skill plugin = (Skill) obj_plugin;
            if (!plugin.checkPermissions(getContext())) {
                Logger.d("Permission for plugin <%s> not satisfied", plugin.id());
                if (!logging)
                    return false;
                satisfied = false;
            }
        }
        return satisfied;
    }

    void requestAllPermissions() {
        List plugins = LocalSkillRegistry.getInstance().all().getEnabledSkills(getContext());
        for (int i = 0; i < plugins.size(); i++) {
            Skill plugin = (Skill) plugins.get(i);
            if (!plugin.checkPermissions(getContext()))
                plugin.requestPermissions(getActivity(), i);
        }
    }
}
