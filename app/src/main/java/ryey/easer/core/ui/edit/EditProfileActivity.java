package ryey.easer.core.ui.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.plugins.PluginRegistry;

public class EditProfileActivity extends AppCompatActivity implements OperationSelectorFragment.SelectedListener {

    ProfileDataStorage storage = null;

    EditDataProto.Purpose purpose;
    String oldName = null;

    EditText editText_profile_name = null;

    OperationSelectorFragment operationSelectorFragment;
    List<ProfilePluginViewContainerFragment> operationViewList = new ArrayList<>();

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
        storage = ProfileDataStorage.getInstance(this);
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
            editText_profile_name = findViewById(R.id.editText_profile_title);
            operationSelectorFragment = new OperationSelectorFragment();
            operationSelectorFragment.setSelectedListener(this);
            findViewById(R.id.button_add_operation).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operationSelectorFragment.show(getSupportFragmentManager(), "add_op");
                }
            });
            if (purpose == EditDataProto.Purpose.edit) {
                ProfileStructure profile = storage.get(oldName);
                loadFromProfile(profile);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storage = null;
    }

    protected void loadFromProfile(ProfileStructure profile) {
        editText_profile_name.setText(oldName);

        clearPluginView();
        List<OperationPlugin> plugins = PluginRegistry.getInstance().operation().getEnabledPlugins(this);
        for (int i = 0; i < plugins.size(); i++) {
            Collection<OperationData> possibleOperationData = profile.get(plugins.get(i).id());
            if (possibleOperationData != null) {
                for (OperationData operationData : possibleOperationData) {
                    PluginViewContainerFragment fragment = addPluginView(new OperationPlugin[]{plugins.get(i)})[0];
                    fragment.fill(operationData);
                }
            }
        }
    }

    protected ProfileStructure saveToProfile() {
        ProfileStructure profile = new ProfileStructure(editText_profile_name.getText().toString());

        for (ProfilePluginViewContainerFragment fragment : operationViewList) {
            if (!fragment.isEnabled())
                continue;
            try {
                StorageData data = fragment.getData();
                if (data instanceof OperationData) {
                    if (data.isValid()) {
                        fragment.setHighlight(false);
                        profile.set(PluginRegistry.getInstance().operation().findPlugin((OperationData) data).id(), (OperationData) data);
                    } else {
                        fragment.setHighlight(true);
                        return null;
                    }
                } else {
                    Logger.wtf("data of plugin's Layout is not instance of OperationData");
                    throw new IllegalStateException("data of plugin's Layout is not instance of OperationData");
                }
            } catch (InvalidDataInputException e) {

            }
        }

        return profile;
    }

    protected boolean alterProfile() {
        try {
            boolean success;
            if (purpose == EditDataProto.Purpose.delete)
                success = storage.delete(oldName);
            else {
                ProfileStructure newProfile = saveToProfile();
                if (newProfile == null || !newProfile.isValid()) {
                    Toast.makeText(this, getString(R.string.prompt_data_illegal), Toast.LENGTH_LONG).show();
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
        } catch (IOException e) {
            Logger.e(e, "IOException when altering");
            return false;
        }
    }

    synchronized void clearPluginView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (PluginViewContainerFragment fragment : operationViewList) {
            transaction.remove(fragment);
        }
        operationViewList.clear();
        transaction.commit();
    }

    synchronized PluginViewContainerFragment[] addPluginView(OperationPlugin[] plugins) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PluginViewContainerFragment[] fragments = new PluginViewContainerFragment[plugins.length];
        for (int i = 0; i < plugins.length; i++) {
            OperationPlugin plugin = plugins[i];
            ProfilePluginViewContainerFragment fragment = ProfilePluginViewContainerFragment.createInstance(plugin.view());
            transaction.add(R.id.layout_profiles, fragment, plugin.id());
            fragments[i] = fragment;
            operationViewList.add(fragment);
            operationSelectorFragment.addSelectedPlugin(plugin);
        }
        transaction.commit();
        return fragments;
    }

    @Override
    public void onSelected(OperationPlugin plugin) {
        addPluginView(new OperationPlugin[]{plugin});
    }
}
