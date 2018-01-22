package ryey.easer.core.ui.edit;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.plugins.PluginRegistry;

public class EditProfileActivity extends AbstractEditDataActivity<ProfileStructure, ProfileDataStorage> implements OperationSelectorFragment.SelectedListener {

    static {
        TAG_DATA_TYPE = "profile";
    }

    EditText editText_profile_name = null;

    OperationSelectorFragment operationSelectorFragment;
    List<ProfilePluginViewContainerFragment> operationViewList = new ArrayList<>();

    @Override
    protected ProfileDataStorage retDataStorage() {
        return ProfileDataStorage.getInstance(this);
    }

    @Override
    protected String title() {
        return getString(R.string.title_profile);
    }

    @Override
    protected int contentViewRes() {
        return R.layout.activity_edit_profile;
    }

    @Override
    void init() {
        editText_profile_name = findViewById(R.id.editText_profile_title);
        operationSelectorFragment = new OperationSelectorFragment();
        operationSelectorFragment.setSelectedListener(this);
        findViewById(R.id.button_add_operation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationSelectorFragment.show(getSupportFragmentManager(), "add_op");
            }
        });
    }

    @Override
    protected void loadFromData(ProfileStructure profile) {
        editText_profile_name.setText(oldName);

        clearPluginView();
        List<OperationPlugin> plugins = PluginRegistry.getInstance().operation().getEnabledPlugins(this);
        for (int i = 0; i < plugins.size(); i++) {
            Collection<OperationData> possibleOperationData = profile.get(plugins.get(i).id());
            if (possibleOperationData != null) {
                for (OperationData operationData : possibleOperationData) {
                    addAndFillPluginView(plugins.get(i), operationData);
                }
            }
        }
    }

    @Override
    protected ProfileStructure saveToData() throws InvalidDataInputException {
        ProfileStructure profile = new ProfileStructure(editText_profile_name.getText().toString());

        for (ProfilePluginViewContainerFragment fragment : operationViewList) {
            if (!fragment.isEnabled())
                continue;
            try {
                OperationData data = fragment.getData();
                if (!data.isValid())
                    throw new InvalidDataInputException();
                fragment.setHighlight(false);
                profile.set(PluginRegistry.getInstance().operation().findPlugin(data).id(), data);
            } catch (InvalidDataInputException e) {
                fragment.setHighlight(true);
                return null;
            }
        }

        return profile;
    }

    synchronized void clearPluginView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (PluginViewContainerFragment fragment : operationViewList) {
            transaction.remove(fragment);
        }
        operationViewList.clear();
        transaction.commit();
    }

    synchronized <T extends OperationData> void addAndFillPluginView(OperationPlugin<T> plugin, T data) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfilePluginViewContainerFragment<T> fragment = ProfilePluginViewContainerFragment.createInstance(plugin.view());
        transaction.add(R.id.layout_profiles, fragment, plugin.id());
        operationViewList.add(fragment);
        operationSelectorFragment.addSelectedPlugin(plugin);
        transaction.commit();
        fragment.fill(data);
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
