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

package ryey.easer.commons.plugindef;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base Fragment class for plugin's UI.
 */
public abstract class PluginViewFragmentInterface<T extends StorageData> extends Fragment {


    /**
     * Normal {@link Fragment} method. Subclasses must override this method to provide the UI.
     * The only difference is the return value is now {@link NonNull}.
     */
    @NonNull
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    /**
     * Get the description text from resources.
     * Could be overridden in subclasses if needed for customized text.
     */
    @NonNull
    public abstract String desc(@NonNull Resources res);

    /**
     * Set the UI according to the data.
     */
    public abstract void fill(@ValidData @NonNull T data);

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
    public abstract void setEnabled(boolean enabled);
}
