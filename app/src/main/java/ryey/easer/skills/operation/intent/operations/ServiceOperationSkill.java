package ryey.easer.skills.operation.intent.operations;

import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.skills.operation.OperationLoader;
import ryey.easer.skills.operation.intent.IntentOperationData;
import ryey.easer.skills.operation.intent.IntentOperationSkill;
import ryey.easer.skills.operation.intent.IntentSkillViewFragment;

public class ServiceOperationSkill extends IntentOperationSkill {
    @NonNull
    @Override
    public String id() {
        return "start_service";
    }

    @Override
    public int name() {
        return R.string.operation_start_service;
    }


    @NonNull
    @Override
    public OperationLoader<IntentOperationData> loader(@NonNull Context context) {
        return new ServiceLoader(context);
    }

    @NonNull
    @Override
    public SkillView<IntentOperationData> view() {
        IntentSkillViewFragment ret = new DummyIntentSkillViewFragment();
        ret.setSkillID(id());
        return ret;
    }

    public static class DummyIntentSkillViewFragment extends IntentSkillViewFragment
    {

    }
}
