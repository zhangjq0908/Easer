package ryey.easer.core.ui.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.core.data.storage.xml.profile.XmlProfileDataStorage;
import ryey.easer.plugins.PluginRegistry;

public class EditProfileActivity extends AppCompatActivity {

    ProfileDataStorage storage = null;

    EditDataProto.Purpose purpose;
    String oldName = null;

    EditText mEditText = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                alterProfile();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            storage = XmlProfileDataStorage.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    alterProfile();
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
            setContentView(R.layout.activity_edit_profile);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            actionbar.setDisplayHomeAsUpEnabled(true);
            setTitle(R.string.title_edit_profile);
            init();
            if (purpose == EditDataProto.Purpose.edit) {
                ProfileStructure profile = storage.get(oldName);
                loadFromProfile(profile);
            }
        }
    }

    void init() {
        mEditText = (EditText) findViewById(R.id.editText_profile_title);

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (OperationPlugin operationPlugin : PluginRegistry.getInstance().getOperationPlugins()) {
            PluginViewFragment fragment = ProfilePluginViewFragment.createInstance(operationPlugin.view());
            fragmentManager.beginTransaction().add(R.id.layout_profiles, fragment, operationPlugin.name()).commit();
        }
        fragmentManager.executePendingTransactions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storage = null;
    }

    protected void loadFromProfile(ProfileStructure profile) {
        mEditText.setText(oldName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (OperationPlugin plugin : PluginRegistry.getInstance().getOperationPlugins()) {
            PluginViewFragment fragment = (PluginViewFragment) fragmentManager.findFragmentByTag(plugin.name());
            fragment.fill(profile.get(plugin.name()));
        }
    }

    protected ProfileStructure saveToProfile() {
        ProfileStructure profile = new ProfileStructure(mEditText.getText().toString());

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (OperationPlugin plugin : PluginRegistry.getInstance().getOperationPlugins()) {
            PluginViewFragment fragment = (PluginViewFragment) fragmentManager.findFragmentByTag(plugin.name());
            StorageData data = fragment.getData();
            if (data == null)
                continue;
            if (data instanceof OperationData) {
                if (data.isValid())
                    profile.set(plugin.name(), (OperationData) data);
            } else {
                Logger.wtf("data of plugin's Layout is not instance of OperationData");
                throw new RuntimeException("data of plugin's Layout is not instance of OperationData");
            }
        }

        return profile;
    }

    protected boolean alterProfile() {
        boolean success;
        if (purpose == EditDataProto.Purpose.delete)
            success = storage.delete(oldName);
        else {
            ProfileStructure newProfile = saveToProfile();
            if (!newProfile.isValid()) {
                return false;
            }
            switch (purpose) {
                case add:
                    success = storage.add(newProfile);
                    break;
                case edit:
                    success = storage.edit(oldName, newProfile);
                    break;
                default:
                    Logger.wtf("Unexpected purpose: %s", purpose);
                    throw new UnsupportedOperationException("Unknown Purpose");
            }
        }
        if (success) {
            setResult(RESULT_OK);
            Logger.v("Successfully altered event");
            finish();
        } else {
            Logger.d("Failed to alter event");
            Toast.makeText(this, getString(R.string.prompt_save_failed), Toast.LENGTH_SHORT).show();
        }
        return success;
    }
}
