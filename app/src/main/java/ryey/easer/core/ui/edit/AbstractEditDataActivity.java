package ryey.easer.core.ui.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.core.data.Named;
import ryey.easer.core.data.Verifiable;
import ryey.easer.core.data.storage.AbstractDataStorage;

abstract class AbstractEditDataActivity<T extends Named & Verifiable, T_storage extends AbstractDataStorage<T, ?>> extends AppCompatActivity {

    protected static String TAG_DATA_TYPE = "<unspecified data type>";

    T_storage storage = null;

    EditDataProto.Purpose purpose;
    String oldName = null;

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
                T data = storage.get(oldName);
                loadFromData(data);
            }
        }
    }

    abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storage = null;
    }

    protected abstract void loadFromData(T data);

    protected abstract T saveToData() throws InvalidDataInputException;

    protected boolean persistChange() {
        try {
            boolean success;
            if (purpose == EditDataProto.Purpose.delete)
                success = storage.delete(oldName);
            else {
                try {
                    T newData = saveToData();
                    if (newData == null || !newData.isValid()) {
                        Toast.makeText(this, getString(R.string.prompt_data_illegal), Toast.LENGTH_LONG).show();
                        return false;
                    }
                    switch (purpose) {
                        case add:
                            success = storage.add(newData);
                            break;
                        case edit:
                            success = storage.edit(oldName, newData);
                            break;
                        default:
                            Logger.wtf("Unexpected purpose: %s", purpose);
                            throw new UnsupportedOperationException("Unknown Purpose");
                    }
                } catch (InvalidDataInputException e) {
                    Toast.makeText(this, getString(R.string.prompt_data_illegal), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            if (success) {
                setResult(RESULT_OK);
                Logger.d("Successfully altered " + TAG_DATA_TYPE);
                finish();
            } else {
                Logger.e("Failed to alter " + TAG_DATA_TYPE);
                Toast.makeText(this, getString(R.string.prompt_save_failed), Toast.LENGTH_SHORT).show();
            }
            return success;
        } catch (IOException e) {
            Logger.e(e, "IOException encountered when %s", purpose);
            return false;
        }
    }

}
