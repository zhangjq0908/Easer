package ryey.easer.commons;

import android.support.annotation.IntRange;

import com.orhanobut.logger.Logger;

public abstract class DelayedJob {
    private final String TAG;

    private final boolean[] locations;
    private int current_count = 0;

    public DelayedJob(@IntRange(from = 1) int count) {
        this(count, null);
    }

    public DelayedJob(@IntRange(from = 1) int count, String tag) {
        locations = new boolean[count];
        TAG = tag;
    }

    public void tick(int location) {
        if (TAG != null) {
            Logger.d("[%s] tick on %d", TAG, location);
        }
        if (!locations[location]) // We are changing it from false to true
            current_count++;
        locations[location] = true;
        if (current_count >= locations.length) // If all locations are true
            exec();
    }

    public abstract void exec();

}
