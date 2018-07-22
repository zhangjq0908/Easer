package ryey.easer.plugins.operation.launch_app;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.operationplugin.Category;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.commons.plugindef.operationplugin.PrivilegeUsage;

public class LaunchAppOperationPlugin implements OperationPlugin<LaunchAppOperationData> {

    @NonNull
    @Override
    public String id() {
        return "launch_app";
    }

    @Override
    public int name() {
        return R.string.op_launch_app;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @NonNull
    @Override
    public PrivilegeUsage privilege() {
        return PrivilegeUsage.no_root;
    }

    @Override
    public int maxExistence() {
        return 0;
    }

    @NonNull
    @Override
    public Category category() {
        return Category.android;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return true;
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
    }

    @NonNull
    @Override
    public OperationDataFactory<LaunchAppOperationData> dataFactory() {
        return new LaunchAppOperationDataFactory();

    }

    @NonNull
    @Override
    public PluginViewFragment<LaunchAppOperationData> view() {
        return new LaunchAppPluginViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<LaunchAppOperationData> loader(@NonNull Context context) {
        return new LaunchAppLoader(context);
    }

}
