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

package ryey.easer.skills.event.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

import com.orhanobut.logger.Logger;

import ryey.easer.Utils;
import ryey.easer.skills.event.AbstractSlot;

public class SmsConnSlot extends AbstractSlot<SmsEventData> {
    private SmsInnerData smsInnerData = null;

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                try {
                    Bundle bundle = intent.getExtras();
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        if (!Utils.isBlank(smsInnerData.sender)) {
                            if (!PhoneNumberUtils.compare(context, msg_from, smsInnerData.sender)) {
                                continue;
                            }
                        }
                        if (!Utils.isBlank(smsInnerData.content)) {
                            if (!msgBody.contains(smsInnerData.content)) {
                                continue;
                            }
                        }
                        Bundle dynamics = new Bundle();
                        dynamics.putString(SmsEventData.SenderDynamics.id, msg_from);
                        dynamics.putString(SmsEventData.ContentDynamics.id, msgBody);
                        changeSatisfiedState(true, dynamics);
                        return;
                    }
                    changeSatisfiedState(false);
                } catch (Exception e) {
                    Logger.d("Exception caught",e.getMessage());
                }
            }
        }
    };

    private IntentFilter filter;

    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        } else {
            filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        }
    }

    public SmsConnSlot(Context context, SmsEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    SmsConnSlot(Context context, SmsEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        smsInnerData = data.innerData;
    }

    @Override
    public void listen() {
        context.registerReceiver(connReceiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(connReceiver);
    }

}
