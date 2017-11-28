package ryey.easer.plugins.operation.brightness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

/**
 * Dumb activity to correctly set screen brightness
 * Adapted from https://stackoverflow.com/questions/6708692/changing-the-screen-brightness-system-setting-android
 */
public class DumbSettingBrightnessActivity extends Activity {

    private static final String EXTRA_BRIGHTNESS = "brightness";

    static void applyBrightness(Context context, float brightness) {
        Intent intent = new Intent(context, DumbSettingBrightnessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_BRIGHTNESS, brightness);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new StopHandler(this);
        Intent intent = this.getIntent();
        float brightness = intent.getFloatExtra(EXTRA_BRIGHTNESS, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = brightness;
        getWindow().setAttributes(params);

        Message message = handler.obtainMessage(StopHandler.DELAYED_MESSAGE);
        handler.sendMessageDelayed(message,1000);
    }

    private static class StopHandler extends Handler {
        static final int DELAYED_MESSAGE = 1;

        Activity activity;
        StopHandler(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DELAYED_MESSAGE) {
                activity.finish();
            }
            super.handleMessage(msg);
        }
    }

}
