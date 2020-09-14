package ryey.easer.skills.operation.intent.operations;

import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.skills.operation.intent.IntentLoader;
import ryey.easer.skills.operation.intent.IntentOperationData;

public class ServiceLoader extends IntentLoader<IntentOperationData> {
    public ServiceLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public void _load(@NonNull IntentOperationData data, @NonNull OnResultCallback callback) {
        context.startService(this.getIntent(data));
        callback.onResult(true);
    }
}
