package ryey.easer.commons;

import android.support.annotation.IntRange;

public abstract class DelayedJob {
    private final int expected_count;
    private int current_count = 0;

    public DelayedJob(@IntRange(from = 1) int count) {
        expected_count = count;
    }

    public void tick() {
        if (++current_count >= expected_count) {
            exec();
        }
    }

    public abstract void exec();

}
