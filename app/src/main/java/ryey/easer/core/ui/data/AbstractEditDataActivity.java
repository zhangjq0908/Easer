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

package ryey.easer.core.ui.data;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.ui.CommonBaseActivity;
import ryey.easer.core.DataSavingFailedException;
import ryey.easer.core.ItemBeingUsedException;
import ryey.easer.core.data.Named;
import ryey.easer.core.data.Verifiable;
import ryey.easer.core.data.WithCreatedVersion;
import ryey.easer.core.data.storage.AbstractDataStorage;
import ryey.easer.core.data.storage.RequiredDataNotFoundException;

public abstract class AbstractEditDataActivity<T extends Named & Verifiable & WithCreatedVersion, T_storage extends AbstractDataStorage<T, ?>> extends CommonBaseActivity {

    protected static String TAG_DATA_TYPE = "<unspecified data type>";

    @Nullable
    T_storage storage = null;

    EditDataProto.Purpose purpose;
    protected String oldName = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                persistChange();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    protected abstract T_storage retDataStorage();

    protected abstract String title();

    @LayoutRes
    protected abstract int contentViewRes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = retDataStorage();
        purpose = (EditDataProto.Purpose) getIntent().getSerializableExtra(EditDataProto.PURPOSE);
        if (purpose != EditDataProto.Purpose.add)
            oldName = getIntent().getStringExtra(EditDataProto.CONTENT_NAME);
        if (purpose == EditDataProto.Purpose.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    setResult(RESULT_CANCELED);
                    dialog.cancel();
                }
            }).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    persistChange();
                }
            });
            builder.setMessage(getString(R.string.prompt_delete, oldName));
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            setTheme(R.style.AppTheme_ActivityDialog);
            builder.show();
        } else {
            setContentView(contentViewRes());
            ActionBar actionbar = getSupportActionBar();
            if (actionbar != null) {
                actionbar.setHomeAsUpIndicator(R.drawable.ic_close_24dp);
                actionbar.setDisplayHomeAsUpEnabled(true);
            }
            setTitle(title());
            init();
            if (purpose == EditDataProto.Purpose.edit) {
                T data = null;
                try {
                    //noinspection ConstantConditions
                    data = storage.get(oldName);
                } catch (RequiredDataNotFoundException e) {
                    throw new AssertionError(e);
                }
                loadFromData(data);
            }
        }
    }

    protected abstract void init();

    protected abstract void loadFromData(T data);

    protected abstract T saveToData() throws InvalidDataInputException;

    @Nullable
    protected Exception tryPersistChange() {
        try {
            if (purpose == EditDataProto.Purpose.delete) {
                //noinspection ConstantConditions
                boolean success = storage.delete(oldName);
                if (success) {
                    return null;
                } else {
                    return new ItemBeingUsedException();
                }
            } else {
                T newData;
                try {
                    newData = saveToData();
                    if (newData == null || !newData.isValid()) {
                        throw new InvalidDataInputException();
                    }
                } catch (InvalidDataInputException e) {
                    return e;
                } catch (Exception e) {
                    return new InvalidDataInputException(e);
                }
                try {
                    boolean success;
                    switch (purpose) {
                        case add:
                            //noinspection ConstantConditions
                            success = storage.add(newData);
                            break;
                        case edit:
                            //noinspection ConstantConditions
                            success = storage.edit(oldName, newData);
                            break;
                        default:
                            throw new IllegalAccessError("Unknown Purpose");
                    }
                    if (success) {
                        return null;
                    } else {
                        return new DataSavingFailedException();
                    }
                } catch (IOException e) {
                    return e;
                }
            }
        } catch (Exception e) {
            return e;
        }
    }

    protected void persistChange() {
        Exception e = tryPersistChange();
        if (e == null) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (e instanceof ItemBeingUsedException) {
            Logger.d(e);
            Toast.makeText(this, getString(R.string.prompt_delete_failed), Toast.LENGTH_SHORT).show();
        } else if (e instanceof InvalidDataInputException) {
            Logger.d(e);
            Toast.makeText(this, getString(R.string.prompt_data_illegal), Toast.LENGTH_LONG).show();
        } else if (e instanceof DataSavingFailedException) {
            Logger.d(e);
            Toast.makeText(this, getString(R.string.prompt_data_clash), Toast.LENGTH_LONG).show();
        } else if (e instanceof IOException) {
            Logger.d(e);
            Toast.makeText(this, getString(R.string.prompt_data_save_io_exception), Toast.LENGTH_LONG).show();
        } else {
            Logger.e(e, "Unknown exception happened in persistChange()");
            throw new RuntimeException(e);
        }
    }

}
