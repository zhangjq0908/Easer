package ryey.easer.skills.operation.intent.operations;

import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.skills.operation.OperationLoader;
import ryey.easer.skills.operation.intent.IntentOperationData;
import ryey.easer.skills.operation.intent.IntentOperationSkill;

public class ActivityOperationSkill extends IntentOperationSkill {
    @NonNull
    @Override
    public String id() {
        return "start_activity";
    }

    @Override
    public int name() {
        return R.string.operation_start_activity;
    }


    @NonNull
    @Override
    public OperationLoader<IntentOperationData> loader(@NonNull Context context) {
        return new ActivityLoader(context);
    }
}
