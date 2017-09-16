package ryey.easer;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

import ryey.easer.plugins.PluginRegistry;

public class EaserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PluginRegistry.init();

        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.addLogAdapter(new DiskLogAdapter());

        Logger.log(Logger.ASSERT, null, "======Easer started======", null);
    }
}
