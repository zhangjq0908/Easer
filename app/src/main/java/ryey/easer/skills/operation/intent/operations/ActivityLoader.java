package ryey.easer.skills.operation.intent.operations;

import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.skills.operation.intent.IntentLoader;
import ryey.easer.skills.operation.intent.IntentOperationData;

public class ActivityLoader extends IntentLoader<IntentOperationData> {

    public ActivityLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public void _load(@NonNull IntentOperationData data, @NonNull OnResultCallback callback) {

        context.startActivity(this.getIntent(data));
        callback.onResult(true);
    }
}
