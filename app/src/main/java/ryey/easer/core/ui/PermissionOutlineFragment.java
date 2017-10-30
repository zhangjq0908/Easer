package ryey.easer.core.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.orhanobut.logger.Logger;

import ryey.easer.R;

public class PermissionOutlineFragment extends Fragment {
    static String[] normal_permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_SYNC_SETTINGS,
    };

    Button mButton;

    public PermissionOutlineFragment() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permission_outline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mButton = (Button) view.findViewById(R.id.button_more);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAllPermissions();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasAllRequiredPermissions()) {
            getView().setVisibility(View.GONE);
        } else {
            getView().setVisibility(View.VISIBLE);
        }
    }

    boolean hasAllRequiredPermissions() {
        for (String permission : normal_permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                Logger.d("Permission <%s> not satisfied", permission);
                return false;
            }
        }
        if (!hasSpecialPermissions())
            return false;
        return true;
    }

    void requestAllPermissions() {
        for (int i = 0; i < normal_permissions.length; i++) {
            final String permission = normal_permissions[i];
            if (ActivityCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{normal_permissions[i]}, i);
        }
        requestSpecialPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    boolean canWriteSettings() {
        return Settings.System.canWrite(getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestWriteSettings() {
        startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
    }

    boolean hasSpecialPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!canWriteSettings()) {
                Logger.d("Special Permission <%s> not satisfied",
                        Manifest.permission.WRITE_SETTINGS);
                return false;
            }
        }
        return true;
    }

    void requestSpecialPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!canWriteSettings()) {
                requestWriteSettings();
            }
        }
    }
}
