package ryey.easer;

import android.app.Application;

import ryey.easer.core.EHService;
import ryey.easer.plugins.PluginRegistry;

public class EaserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PluginRegistry.init();
    }
}
