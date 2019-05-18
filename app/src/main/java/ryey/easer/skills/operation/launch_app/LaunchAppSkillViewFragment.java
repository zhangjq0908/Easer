package ryey.easer.skills.operation.launch_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class LaunchAppSkillViewFragment extends SkillViewFragment<LaunchAppOperationData> {
    EditText et_app_package;
    EditText et_class;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__launch_app, container, false);
        et_app_package = view.findViewById(R.id.editText_app_package);
        et_class = view.findViewById(R.id.editText_app_activity);
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull LaunchAppOperationData data) {
        et_app_package.setText(data.app_package);
        et_class.setText(data.app_class);
    }

    @ValidData
    @NonNull
    @Override
    public LaunchAppOperationData getData() throws InvalidDataInputException {
        return new LaunchAppOperationData(et_app_package.getText().toString(), et_class.getText().toString());
    }
}
