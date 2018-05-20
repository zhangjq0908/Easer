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

package ryey.easer.core.ui.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.data.storage.ScenarioDataStorage;
import ryey.easer.plugins.PluginRegistry;

public class ScenarioListFragment extends AbstractDataListFragment<ScenarioDataStorage> {

    static {
        TAG = "[ScenarioListFragment] ";
    }

    @Override
    protected String title() {
        return getString(R.string.title_scenario);
    }

    @Override
    protected ScenarioDataStorage retmStorage() {
        return ScenarioDataStorage.getInstance(getContext());
    }

    @Override
    protected void onDataChangedFromEditDataActivity() {
        super.onDataChangedFromEditDataActivity();
        EHService.reload(getContext());
    }

    @Override
    protected Intent intentForEditDataActivity() {
        return new Intent(getContext(), EditScenarioActivity.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //noinspection ConstantConditions
        if (PluginRegistry.getInstance().event().getEnabledPlugins(getContext()).size() == 0) {
            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.hide();
        }
    }
}
