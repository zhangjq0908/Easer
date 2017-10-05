package ryey.easer.core.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.EventDataStorage;
import ryey.easer.core.data.storage.xml.event.XmlEventDataStorage;
import ryey.easer.core.data.storage.xml.profile.XmlProfileDataStorage;
import ryey.easer.plugins.PluginRegistry;

/*
 * TODO: change the layout
 *  TODO: change from checkbox to radiobutton
 */
public class EditEventActivity extends AppCompatActivity {

    EventDataStorage storage = null;

    EditDataProto.Purpose purpose;
    String oldName = null;

    EditText mEditText_name = null;
    private static final String NON = ""; //TODO: more robust
    Spinner mSpinner_parent = null;
    List<String> mEventList = null;
    Spinner mSpinner_profile = null;
    List<String> mProfileList = null;
    boolean isActive = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_event, menu);
        menu.findItem(R.id.action_toggle_active).setChecked(isActive);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                alterEvent();
                break;
            case R.id.action_toggle_active:
                isActive = !item.isChecked();
                item.setChecked(isActive);
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
            storage = XmlEventDataStorage.getInstance(this);
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
                    alterEvent();
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
            setContentView(R.layout.activity_edit_event);
            ActionBar actionbar = getSupportActionBar();
            if (actionbar != null) {
                actionbar.setHomeAsUpIndicator(R.drawable.ic_close_24dp);
                actionbar.setDisplayHomeAsUpEnabled(true);
            }
            setTitle(R.string.title_edit_event);
            init();
            if (purpose == EditDataProto.Purpose.edit) {
                EventStructure event = storage.get(oldName);
                loadFromEvent(event);
            }
        }
    }

    void init() {
        mEditText_name = (EditText) findViewById(R.id.editText_event_title);

        mSpinner_parent = (Spinner) findViewById(R.id.spinner_parent);
        try {
            mEventList = (XmlEventDataStorage.getInstance(this)).list();
            mEventList.add(0, NON);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_profile, mEventList); //TODO: change layout
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

        mSpinner_profile = (Spinner) findViewById(R.id.spinner_profile);
        try {
            mProfileList = (XmlProfileDataStorage.getInstance(this)).list();
            mProfileList.add(0, NON);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_profile, mProfileList);
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (EventPlugin eventPlugin : PluginRegistry.getInstance().getEventPlugins()) {
            PluginViewFragment fragment = PluginViewFragment.createInstance(eventPlugin.view(this));
            fragmentManager.beginTransaction().add(R.id.layout_events, fragment, eventPlugin.name()).commit();
        }
        fragmentManager.executePendingTransactions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storage = null;
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

        isActive = event.isActive();

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (EventPlugin plugin : PluginRegistry.getInstance().getEventPlugins()) {
            PluginViewFragment fragment = (PluginViewFragment) fragmentManager.findFragmentByTag(plugin.name());
            if (event.getEventData().pluginClass() == plugin.getClass()) {
                fragment.fill(event.getEventData());
            } else {
                fragment.fill(null);
            }
        }
    }

    protected EventStructure saveToEvent() {
        EventStructure event = new EventStructure(mEditText_name.getText().toString());
        String profile = (String) mSpinner_profile.getSelectedItem();
        event.setProfileName(profile);
        event.setActive(isActive);
        String parent = (String) mSpinner_parent.getSelectedItem();
        if (!parent.equals(NON))
            event.setParentName(parent);

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (EventPlugin plugin : PluginRegistry.getInstance().getEventPlugins()) {
            PluginViewFragment fragment = (PluginViewFragment) fragmentManager.findFragmentByTag(plugin.name());
            StorageData data = fragment.getData();
            if (data == null)
                continue;
            if (data instanceof EventData) {
                event.setEventData((EventData) data);
            } else {
                Logger.wtf("data of plugin's Layout is not instance of EventData");
                throw new RuntimeException("data of plugin's Layout is not instance of EventData");
            }
            break;
        }

        return event;
    }

    boolean alterEvent() {
        boolean success;
        if (purpose == EditDataProto.Purpose.delete)
            success = storage.delete(oldName);
        else {
            EventStructure newEvent = saveToEvent();
            if (!newEvent.isValid()) {
                Toast.makeText(this, getString(R.string.prompt_data_illegal), Toast.LENGTH_LONG).show();
                return false;
            }
            switch (purpose) {
                case add:
                    success = storage.add(newEvent);
                    break;
                case edit:
                    success = storage.edit(oldName, newEvent);
                    break;
                default:
                    Logger.wtf("Unexpected purpose: %s", purpose);
                    throw new UnsupportedOperationException("Unknown Purpose");
            }
        }
        if (success) {
            setResult(RESULT_OK);
            Logger.d("Successfully altered event");
            finish();
        } else {
            Logger.e("Failed to alter event");
            Toast.makeText(this, getString(R.string.prompt_save_failed), Toast.LENGTH_SHORT).show();
        }
        return success;
    }
}
