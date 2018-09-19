package ryey.easer.plugins.operation.launch_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.operation.OperationLoader;

public class LaunchAppLoader extends OperationLoader<LaunchAppOperationData> {
    LaunchAppLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull LaunchAppOperationData data) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(data.app_package);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
