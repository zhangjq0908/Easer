/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.operation.volume;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.orhanobut.logger.Logger;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;

public class VolumeOpeartionPluginViewFragment extends PluginViewFragment<VolumeOperationData> {

    private static final int STREAM_BLUETOOTH = 6;
    
    CheckBox checkBox_ring, checkBox_media, checkBox_alarm, checkBox_notification, checkBox_bt;
    SeekBar seekBar_ring, seekBar_media, seekBar_alarm, seekBar_notification, seekBar_bt;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__volume, container, false);

        //noinspection ConstantConditions
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            Logger.e("Couldn't get AudioManager");
            throw new IllegalAccessError();
        }

        seekBar_ring = view.findViewById(R.id.seekBar_ring);
        seekBar_ring.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        checkBox_ring = view.findViewById(R.id.checkBox_ring);
        checkBox_ring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                seekBar_ring.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_media = view.findViewById(R.id.seekBar_media);
        seekBar_media.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        checkBox_media = view.findViewById(R.id.checkBox_media);
        checkBox_media.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                seekBar_media.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_alarm = view.findViewById(R.id.seekBar_alarm);
        seekBar_alarm.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        checkBox_alarm = view.findViewById(R.id.checkBox_alarm);
        checkBox_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                seekBar_alarm.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_notification = view.findViewById(R.id.seekBar_notification);
        seekBar_notification.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        checkBox_notification = view.findViewById(R.id.checkBox_notification);
        checkBox_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                seekBar_notification.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_bt = view.findViewById(R.id.seekBar_bt);
        seekBar_bt.setMax(audioManager.getStreamMaxVolume(STREAM_BLUETOOTH));
        checkBox_bt = view.findViewById(R.id.checkBox_bt);
        checkBox_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                seekBar_bt.setVisibility(b? View.VISIBLE : View.GONE);
            }
        });

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull VolumeOperationData data) {
        setVolumeVisual(checkBox_ring, seekBar_ring, data.vol_ring);
        setVolumeVisual(checkBox_media, seekBar_media, data.vol_media);
        setVolumeVisual(checkBox_alarm, seekBar_alarm, data.vol_alarm);
        setVolumeVisual(checkBox_notification, seekBar_notification, data.vol_notification);
        setVolumeVisual(checkBox_bt, seekBar_bt, data.vol_bt);
    }

    @ValidData
    @NonNull
    @Override
    public VolumeOperationData getData() throws InvalidDataInputException {
        Integer vol_ring = getVolume(seekBar_ring);
        Integer vol_media = getVolume(seekBar_media);
        Integer vol_alarm = getVolume(seekBar_alarm);
        Integer vol_notification = getVolume(seekBar_notification);
        Integer vol_bt = getVolume(seekBar_bt);
        return new VolumeOperationData(vol_ring, vol_media, vol_alarm, vol_notification, vol_bt);
    }

    private static void setVolumeVisual(CheckBox checkBox, SeekBar seekBar, Integer value) {
        if (value == null) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
            seekBar.setProgress(value);
        }
    }

    private static Integer getVolume(SeekBar seekBar) {
        if (seekBar.getVisibility() == View.VISIBLE)
            return seekBar.getProgress();
        else
            return null;
    }
}
