/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.skills.condition.ringer_mode;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.commons.local_skill.ValidData;

public class RingerModeSkillViewFragment extends SkillViewFragment<RingerModeConditionData> {

    RadioGroup rg_ringer_mode, rg_volume_match_type;
    RadioButton rb_ringer_mode_silent, rb_ringer_mode_vibrate, rb_ringer_mode_normal,
                rb_volume_match_above, rb_volume_match_below, rb_volume_match_equals;
    SeekBar sb_volume_match_value;
    TextView tv_volume_match_value;

    private void setRingerOptionsState() {
        if (rg_ringer_mode.getCheckedRadioButtonId() == rb_ringer_mode_normal.getId()) {
            rb_volume_match_above.setEnabled(true);
            rb_volume_match_below.setEnabled(true);
            rb_volume_match_equals.setEnabled(true);
            sb_volume_match_value.setEnabled(true);
        } else {
            rb_volume_match_above.setEnabled(false);
            rb_volume_match_below.setEnabled(false);
            rb_volume_match_equals.setEnabled(false);
            sb_volume_match_value.setEnabled(false);
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_condition__ringer_mode, container, false);
        rg_ringer_mode = view.findViewById(R.id.rg_ringer_mode);
        rb_ringer_mode_silent = view.findViewById(R.id.rb_ringer_mode_silent);
        rb_ringer_mode_vibrate = view.findViewById(R.id.rb_ringer_mode_vibrate);
        rb_ringer_mode_normal = view.findViewById(R.id.rb_ringer_mode_normal);
        rg_volume_match_type = view.findViewById(R.id.rg_volume_match_type);
        rb_volume_match_above = view.findViewById(R.id.rb_volume_match_above);
        rb_volume_match_below = view.findViewById(R.id.rb_volume_match_below);
        rb_volume_match_equals = view.findViewById(R.id.rb_volume_match_equals);
        sb_volume_match_value = view.findViewById(R.id.sb_volume_match_value);
        tv_volume_match_value = view.findViewById(R.id.tv_volume_match_value);

        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);


        setRingerOptionsState();
        rg_ringer_mode.setOnCheckedChangeListener((group, checkedId) -> {
            setRingerOptionsState();
        });

        int maxVol = (am != null) ? am.getStreamMaxVolume(AudioManager.STREAM_RING) : 0;
        sb_volume_match_value.setMax(maxVol);
        sb_volume_match_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_volume_match_value.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        tv_volume_match_value.setText(String.valueOf(sb_volume_match_value.getProgress()));
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull RingerModeConditionData data) {
        switch (data.ringerMode) {
            case AudioManager.RINGER_MODE_VIBRATE:
                rg_ringer_mode.check(R.id.rb_ringer_mode_vibrate);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                rg_ringer_mode.check(R.id.rb_ringer_mode_normal);
                break;
            default:
                rg_ringer_mode.check(R.id.rb_ringer_mode_silent);
        }
        if (data.ringerMode == AudioManager.RINGER_MODE_NORMAL) {
            switch (data.compareMode) {
                case RingerModeConditionData.COMPARE_MODE_HIGHER_OR_EQUAL:
                    rg_volume_match_type.check(R.id.rb_volume_match_above);
                    break;
                case RingerModeConditionData.COMPARE_MODE_EQUALS:
                    rg_volume_match_type.check(R.id.rb_volume_match_equals);
                    break;
                default:
                    rg_volume_match_type.check(R.id.rb_volume_match_below);
            }
            sb_volume_match_value.setProgress(data.ringerLevel);
        }
    }

    @ValidData
    @NonNull
    @Override
    public RingerModeConditionData getData() throws InvalidDataInputException {
        int ringerMode, compareType, ringerLevel;
        switch (rg_ringer_mode.getCheckedRadioButtonId()) {
            case R.id.rb_ringer_mode_vibrate:
                ringerMode = AudioManager.RINGER_MODE_VIBRATE;
                break;
            case R.id.rb_ringer_mode_normal:
                ringerMode = AudioManager.RINGER_MODE_NORMAL;
                break;
            default:
                ringerMode = AudioManager.RINGER_MODE_SILENT;
        }
        switch (rg_volume_match_type.getCheckedRadioButtonId()) {
            case R.id.rb_volume_match_below:
                compareType = RingerModeConditionData.COMPARE_MODE_LOWER_OR_EQUAL;
                break;
            case R.id.rb_volume_match_equals:
                compareType = RingerModeConditionData.COMPARE_MODE_EQUALS;
                break;
            default:
                compareType = RingerModeConditionData.COMPARE_MODE_HIGHER_OR_EQUAL;
        }
        ringerLevel = sb_volume_match_value.getProgress();
        return new RingerModeConditionData(ringerMode, ringerLevel, compareType);
    }
}
