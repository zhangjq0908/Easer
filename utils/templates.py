#!/usr/bin/env python3
# -*- coding:utf-8 -*-
#
#   Author  :   renyuneyun
#   E-mail  :   renyuneyun@gmail.com
#   Date    :   18/04/22 01:00:10
#   License :   Apache 2.0 (See LICENSE)
#

'''

'''

tmpl_copyright = '''/*
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
'''

tmpl_plugin_view_fragment = '''package {package};

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import local_skill.InvalidDataInputException;
import skills.PluginViewFragment;
import local_skill.ValidData;

public class {view_fragment} extends PluginViewFragment<{data}> {{

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {{
        //TODO
    }}

    @Override
    protected void _fill(@ValidData @NonNull {data} data) {{
        //TODO
    }}

    @ValidData
    @NonNull
    @Override
    public {data} getData() throws InvalidDataInputException {{
        //TODO
    }}
}}
'''

tmpl_androidTest_data = '''package {package};

import android.os.Parcel;

import org.junit.Test;

import skills.TestHelper;

import static org.junit.Assert.assertEquals;

public class {androidTest$data} {{

    @Test
    public void testParcel() {{
        {data} dummyData = new {data_factory}().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        {data} parceledData = {data}.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }}

}}
'''

tmpl_operation_plugin = '''package {package};

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import skills.PluginViewFragment;
import local_skill.operationplugin.OperationDataFactory;
import skills.operation.OperationLoader;
import local_skill.operationplugin.OperationPlugin;
import local_skill.operationplugin.PrivilegeUsage;

public class {plugin} implements OperationPlugin<{data}> {{

    @NonNull
    @Override
    public String id() {{
        return "{id}";
    }}

    @Override
    public int name() {{
        //TODO
    }}

    @Override
    public boolean isCompatible(@NonNull final Context context) {{
        return true;
    }}

    @NonNull
    @Override
    public PrivilegeUsage privilege() {{
        return PrivilegeUsage.no_root;
    }}

    @Override
    public int maxExistence() {{
        return 0;
    }}

    @Override
    public boolean checkPermissions(@NonNull Context context) {{
        return true;
    }}

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {{
    }}

    @NonNull
    @Override
    public OperationDataFactory<{data}> dataFactory() {{
        return new {data_factory}();

    }}

    @NonNull
    @Override
    public PluginViewFragment<{data}> view() {{
        return new {view_fragment}();
    }}

    @NonNull
    @Override
    public OperationLoader<{data}> loader(@NonNull Context context) {{
        return new {loader}(context);
    }}

}}
'''

tmpl_operation_data_factory = '''package {package};

import android.support.annotation.NonNull;

import local_skill.IllegalStorageDataException;
import local_skill.ValidData;
import local_skill.operationplugin.OperationDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class {data_factory} implements OperationDataFactory<{data}> {{
    @NonNull
    @Override
    public Class<{data}> dataClass() {{
        return {data}.class;
    }}

    @ValidData
    @NonNull
    @Override
    public {data} dummyData() {{
        //TODO
    }}

    @ValidData
    @NonNull
    @Override
    public {data} parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {{
        return new {data}(data, format, version);
    }}
}}
'''

tmpl_operation_data = '''package {package};

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

import local_skill.IllegalStorageDataException;
import local_skill.dynamics.SolidDynamicsAssignment;
import local_skill.operationplugin.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class {data} implements OperationData {{
    {data}(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {{
        //TODO
    }}

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {{
        //TODO
    }}

    @SuppressWarnings({{"SimplifiableIfStatement", "RedundantIfStatement"}})
    @Override
    public boolean isValid() {{
        //TODO
    }}

    @SuppressWarnings({{"SimplifiableIfStatement", "RedundantIfStatement"}})
    @Override
    public boolean equals(Object obj) {{
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof {data}))
            return false;
        //TODO
        return true;
    }}

    @Nullable
    @Override
    public Set<String> placeholders() {{
        return null;
    }}

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {{
        return this;
    }}

    @Override
    public int describeContents() {{
        return 0;
    }}

    @Override
    public void writeToParcel(Parcel dest, int flags) {{
        //TODO
    }}

    public static final Creator<{data}> CREATOR
            = new Creator<{data}>() {{
        public {data} createFromParcel(Parcel in) {{
            return new {data}(in);
        }}

        public {data}[] newArray(int size) {{
            return new {data}[size];
        }}
    }};

    private {data}(Parcel in) {{
        //TODO
    }}
}}
'''

tmpl_operation_loader = '''package {package};

import android.content.Context;
import android.support.annotation.NonNull;

import local_skill.ValidData;
import skills.operation.OperationLoader;

public class {loader} extends OperationLoader<{data}> {{
    {loader}(Context context) {{
        super(context);
    }}

    @Override
    public boolean load(@ValidData @NonNull {data} data) {{
        //TODO
    }}
}}
'''

tmpl_event_plugin = '''package {package};

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import skills.PluginViewFragment;
import local_skill.ValidData;
import skills.event.AbstractSlot;
import local_skill.eventplugin.EventDataFactory;
import local_skill.eventplugin.EventPlugin;

public class {plugin} implements EventPlugin<{data}> {{

    @NonNull
    @Override
    public String id() {{
        return "{id}";
    }}

    @Override
    public int name() {{
        //TODO
    }}

    @Override
    public boolean isCompatible(@NonNull final Context context) {{
        return true;
    }}

    @Override
    public boolean checkPermissions(@NonNull Context context) {{
        return true;
    }}

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {{
    }}

    @NonNull
    @Override
    public EventDataFactory<{data}> dataFactory() {{
        return new {data_factory}();
    }}

    @NonNull
    @Override
    public PluginViewFragment<{data}> view() {{
        return new {view_fragment}();
    }}

    @Override
    public AbstractSlot<{data}> slot(@NonNull Context context, @ValidData @NonNull {data} data) {{
        return new {slot}(context, data);
    }}

    @Override
    public AbstractSlot<{data}> slot(@NonNull Context context, @NonNull {data} data, boolean retriggerable, boolean persistent) {{
        return new {slot}(context, data, retriggerable, persistent);
    }}

}}
'''

tmpl_event_data_factory = '''package {package};

import android.support.annotation.NonNull;

import local_skill.IllegalStorageDataException;
import local_skill.ValidData;
import local_skill.eventplugin.EventDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class {data_factory} implements EventDataFactory<{data}> {{
    @NonNull
    @Override
    public Class<{data}> dataClass() {{
        return {data}.class;
    }}

    @ValidData
    @NonNull
    @Override
    public {data} dummyData() {{
        //TODO
    }}

    @ValidData
    @NonNull
    @Override
    public {data} parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {{
        return new {data}(data, format, version);
    }}
}}
'''

tmpl_event_data = '''package {package};

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import local_skill.IllegalStorageDataException;
import local_skill.dynamics.Dynamics;
import skills.event.AbstractEventData;
import ryey.easer.plugin.PluginDataFormat;

public class {data} extends AbstractEventData {{

    {data}(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {{
        //TODO
    }}

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {{
        String res;
        //TODO
        return res;
    }}

    @Nullable
    @Override
    public Dynamics[] dynamics() {{
        return null;
    }}

    @SuppressWarnings({{"SimplifiableIfStatement", "RedundantIfStatement"}})
    @Override
    public boolean isValid() {{
        //TODO
        return true;
    }}

    @SuppressWarnings({{"SimplifiableIfStatement", "RedundantIfStatement"}})
    @Override
    public boolean equals(Object obj) {{
        if (obj == null || !(obj instanceof {data}))
            return false;
        //TODO
        return true;
    }}

    @Override
    public int describeContents() {{
        return 0;
    }}

    @Override
    public void writeToParcel(Parcel dest, int flags) {{
        //TODO
    }}

    public static final Creator<{data}> CREATOR
            = new Creator<{data}>() {{
        public {data} createFromParcel(Parcel in) {{
            return new {data}(in);
        }}

        public {data}[] newArray(int size) {{
            return new {data}[size];
        }}
    }};

    private {data}(Parcel in) {{
        //TODO
    }}
}}
'''

tmpl_event_slot = '''package {package};

import android.content.Context;

import skills.event.AbstractSlot;

public class {slot} extends AbstractSlot<{data}> {{

    {slot}(Context context, {data} data) {{
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }}

    {slot}(Context context, {data} data, boolean retriggerable, boolean persistent) {{
        super(context, data, retriggerable, persistent);
    }}

    @Override
    public void listen() {{
        //TODO
    }}

    @Override
    public void cancel() {{
        //TODO
    }}

}}
'''

tmpl_condition_plugin = '''package {package};

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;

import skills.PluginViewFragment;
import local_skill.ValidData;
import local_skill.conditionplugin.ConditionDataFactory;
import local_skill.conditionplugin.ConditionPlugin;
import local_skill.conditionplugin.Tracker;

public class {plugin} implements ConditionPlugin<{data}> {{

    @NonNull
    @Override
    public String id() {{
        return "{id}";
    }}

    @Override
    public int name() {{
        //TODO
    }}

    @Override
    public boolean isCompatible(@NonNull final Context context) {{
        return true;
    }}

    @Override
    public boolean checkPermissions(@NonNull Context context) {{
        return true;
    }}

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {{
    }}

    @NonNull
    @Override
    public ConditionDataFactory<{data}> dataFactory() {{
        return new {data_factory}();
    }}

    @NonNull
    @Override
    public PluginViewFragment<{data}> view() {{
        return new {view_fragment}();
    }}

    @NonNull
    @Override
    public Tracker<{data}> tracker(@NonNull Context context,
                                                 @ValidData @NonNull {data} data,
                                                 @NonNull PendingIntent event_positive,
                                                 @NonNull PendingIntent event_negative) {{
        return new {tracker}(context, data, event_positive, event_negative);
    }}

}}
'''

tmpl_condition_data_factory = '''package {package};

import android.support.annotation.NonNull;

import local_skill.IllegalStorageDataException;
import local_skill.ValidData;
import local_skill.conditionplugin.ConditionDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class {data_factory} implements ConditionDataFactory<{data}> {{
    @NonNull
    @Override
    public Class<{data}> dataClass() {{
        return {data}.class;
    }}

    @ValidData
    @NonNull
    @Override
    public {data} dummyData() {{
        //TODO
    }}

    @ValidData
    @NonNull
    @Override
    public {data} parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {{
        return new {data}(data, format, version);
    }}
}}
'''

tmpl_condition_data = '''package {package};

import android.os.Parcel;
import android.support.annotation.NonNull;

import local_skill.IllegalStorageDataException;
import local_skill.conditionplugin.ConditionData;
import ryey.easer.plugin.PluginDataFormat;

public class {data} implements ConditionData {{

    {data}(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {{
        switch (format) {{
            default:
                //TODO
        }}
    }}

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {{
        String res;
        switch (format) {{
            default:
                //TODO
        }}
        return res;
    }}

    @SuppressWarnings({{"SimplifiableIfStatement", "RedundantIfStatement"}})
    @Override
    public boolean isValid() {{
        //TODO
        return false;
    }}

    @SuppressWarnings({{"SimplifiableIfStatement", "RedundantIfStatement"}})
    @Override
    public boolean equals(Object obj) {{
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof {data}))
            return false;
        //TODO
        return true;
    }}

    @Override
    public int describeContents() {{
        return 0;
    }}

    @Override
    public void writeToParcel(Parcel dest, int flags) {{
        //TODO
    }}

    public static final Creator<{data}> CREATOR
            = new Creator<{data}>() {{
        public {data} createFromParcel(Parcel in) {{
            return new {data}(in);
        }}

        public {data}[] newArray(int size) {{
            return new {data}[size];
        }}
    }};

    private {data}(Parcel in) {{
        //TODO
    }}
}}
'''

tmpl_condition_tracker = '''package {package};

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;

import skills.condition.SkeletonTracker;

public class {tracker} extends SkeletonTracker<{data}> {{

    {tracker}(Context context, {data} data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {{
        super(context, data, event_positive, event_negative);
    }}

    @Override
    public void start() {{
        //TODO
    }}

    @Override
    public void stop() {{
        //TODO
    }}

    @Override
    public Boolean state() {{
        //TODO
    }}
}}
'''
