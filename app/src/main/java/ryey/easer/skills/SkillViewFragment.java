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

package ryey.easer.skills;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.DelayedJob;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.StorageData;
import ryey.easer.commons.local_skill.ValidData;

/**
 * Base Fragment class for plugin's UI.
 */
public abstract class SkillViewFragment<T extends StorageData> extends SkillView<T> {

    /**
     * Used in case {@link #onCreateView} is called after {@link #fill}`.
     */
    private FillDataJob jobFillData = new FillDataJob("FillDataJob");

    /**
     * Controls whether the content (view) is enabled/interactive or not in the beginning.
     * Works similar to {@link #jobFillData}.
     */
    private SetEnabledJob jobSetEnabled = new SetEnabledJob("FillDataJob");

    /**
     * Normal {@link Fragment} method. Subclasses must override this method to provide the UI.
     * The only difference is the return value is now {@link NonNull}.
     */
    @NonNull
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * @see #jobFillData
     * @see #jobSetEnabled
     * If overridden, call back-through is needed.
     */
    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        jobFillData.tick(0);
        jobSetEnabled.tick(0);
    }

    /**
     * Get the description text from resources.
     * Could be overridden in subclasses if needed for customized text.
     */
    @NonNull
    public String desc(@NonNull Resources res) {
        return res.getString(LocalSkillRegistry.getInstance().all().findSkill(this).name());
    }

    /**
     * Check whether the to-be-filled data is of the correct type.
     */
    private void checkDataType(@ValidData @NonNull T data) throws IllegalArgumentTypeException {
        Class expectedDataClass = LocalSkillRegistry.getInstance()
                .all().findSkill(this)
                .dataFactory()
                .dataClass();
        if (data.getClass().equals(expectedDataClass))
            return;
        throw new IllegalArgumentTypeException(data.getClass(), expectedDataClass);
    }

    /**
     * The actual method to set the UI according to the data.
     * Skill developers is expected to override this method rather than {@link #fill}.
     * This methods does NOT care about synchronization or other stuffs.
     */
    protected abstract void _fill(@ValidData @NonNull T data);

    /**
     * Set the UI according to the data.
     * This methods takes care of synchronization (see {@link #jobFillData}).
     * Skill implementors normally only need to implement {@link #_fill} method.
     */
    public void fill(@ValidData @NonNull T data) {
        try {
            checkDataType(data);
            jobFillData.passed_data = data;
            jobFillData.tick(1);
        } catch (IllegalArgumentTypeException e) {
            Logger.e(e, "filling with illegal data type");
            throw e;
        }
    }

    /**
     * Construct the correct {@link StorageData} (subclass) containing the data in the UI.
     * @throws InvalidDataInputException If the data inputted by the user is invalid
     */
    @ValidData
    @NonNull
    public abstract T getData() throws InvalidDataInputException;

    /**
     * Change the interactive state of the UI components.
     * Override this method only if the UI has other controls of the enabled state.
     */
    public void setEnabled(boolean enabled) {
        jobSetEnabled.enabled = enabled;
        jobSetEnabled.tick(1);
    }

    /**
     * Recursively change the interactive state of the UI components.
     */
    protected static void setEnabled(@NonNull View v, boolean enabled) {
        v.setEnabled(enabled);
        if (v instanceof ViewGroup) {
            View child;
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                child = ((ViewGroup) v).getChildAt(i);
                setEnabled(child, enabled);
            }
        }
    }

    private class FillDataJob extends DelayedJob {
        private T passed_data = null;

        FillDataJob(String tag) {
            super(2, tag);
        }

        @Override
        public void exec() {
            if (passed_data != null) {
                _fill(passed_data);
            }
        }
    }

    private class SetEnabledJob extends DelayedJob {
        private boolean enabled = true;

        SetEnabledJob(String tag) {
            super(2, tag);
        }

        @Override
        public void exec() {
            //noinspection ConstantConditions
            setEnabled(getView(), enabled);
        }
    }
}
