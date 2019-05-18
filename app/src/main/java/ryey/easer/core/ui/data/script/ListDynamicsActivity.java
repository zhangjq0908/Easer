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

package ryey.easer.core.ui.data.script;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.collection.ArrayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ryey.easer.R;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.dynamics.DynamicsLink;
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.ui.CommonBaseActivity;
import ryey.easer.core.dynamics.CoreDynamics;

public class ListDynamicsActivity extends CommonBaseActivity {

    public static final String EXTRA_PLUGIN_TYPE = "ryey.easer.core.ui.data.extra.PLUGIN_TYPE";
    public static final String EXTRA_PLUGIN_DATA = "ryey.easer.core.ui.data.extra.PLUGIN_DATA";
    public static final String EXTRA_DYNAMICS_LINK = "ryey.easer.core.ui.data.extra.DYNAMICS_LINK";
    public static final String EXTRA_PLACEHOLDERS = "ryey.easer.core.ui.data.extras.PLACEHOLDERS";

    public static final String PLUGIN_TYPE_EVENT = "event";
    public static final String PLUGIN_TYPE_CONDITION = "condition";

    private static final int REQ_CODE = 100;

    String plugin_type;
    EventData eventData;

    private ArrayList<String> placeholders;
    private Map<String, Dynamics> dynamicsMap = new ArrayMap<>();
    private List<Dynamics> knownDynamics = new ArrayList<>();
    List<LinkItem> dynamicsLinkList;
    DynamicsLinkAdapter adapter;
    private ListView listView;

    {
        knownDynamics.addAll(Arrays.asList(CoreDynamics.coreDynamics()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dynamics);
        setFinishOnTouchOutside(true);

        listView = findViewById(R.id.list);
        Intent intent = getIntent();

        placeholders = intent.getStringArrayListExtra(EXTRA_PLACEHOLDERS);
        plugin_type = intent.getStringExtra(EXTRA_PLUGIN_TYPE);
        if (PLUGIN_TYPE_EVENT.equals(plugin_type)) {
            eventData = intent.getParcelableExtra(EXTRA_PLUGIN_DATA);
            Dynamics[] dataDynamics = eventData.dynamics();
            if (dataDynamics != null)
                this.knownDynamics.addAll(Arrays.asList(dataDynamics));
        }
        for (Dynamics dynamics : knownDynamics) {
            dynamicsMap.put(dynamics.id(), dynamics);
        }
        DynamicsLink dynamicsLink = intent.getParcelableExtra(EXTRA_DYNAMICS_LINK);
        if (dynamicsLink == null)
            dynamicsLink = new DynamicsLink();

        Map<String, String> identityMap = dynamicsLink.identityMap();
        dynamicsLinkList = new ArrayList<>(identityMap.size());
        for (String placeholder : identityMap.keySet()) {
            String dynamics = identityMap.get(placeholder);
            String name = null;
            if (dynamicsMap.containsKey(dynamics)) {
                name = getString(dynamicsMap.get(dynamics).nameRes());
            }
            dynamicsLinkList.add(new LinkItem(placeholder, dynamics, name));
        }

        adapter = new DynamicsLinkAdapter(this, dynamicsLinkList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        Button button_add = findViewById(R.id.button);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDynamicsActivity.this, EditDynamicsActivity.class);
                ArrayList<String> availablePlaceholders = new ArrayList<>(placeholders.size());
                availablePlaceholders.addAll(placeholders);
                for (LinkItem item : dynamicsLinkList) {
                    availablePlaceholders.remove(item.placeholder);
                }
                intent.putStringArrayListExtra(EXTRA_PLACEHOLDERS, availablePlaceholders);
                intent.putExtra(EXTRA_PLUGIN_TYPE, plugin_type);
                if (PLUGIN_TYPE_EVENT.equals(plugin_type))
                    intent.putExtra(EXTRA_PLUGIN_DATA, eventData);
                startActivityForResult(intent, REQ_CODE);
            }
        });

        Button button_ok = findViewById(R.id.btn_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DynamicsLink link = new DynamicsLink();
                for (LinkItem item : dynamicsLinkList) {
                    link.put(item.placeholder, item.propertyId);
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DYNAMICS_LINK, link);
                setResult(RESULT_OK, intent);
                ListDynamicsActivity.this.finish();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v == listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_context, menu);
            menu.removeItem(R.id.action_edit);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        LinkItem linkItem = (LinkItem) listView.getItemAtPosition(info.position);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setMessage(String.format(getString(R.string.prompt_delete), linkItem.placeholder))
                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dynamicsLinkList.remove(info.position);
                                adapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                LinkItem linkItem = data.getParcelableExtra(EditDynamicsActivity.EXTRA_LINK_ITEM);
                dynamicsLinkList.add(linkItem);
                adapter.notifyDataSetChanged();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    static class LinkItem implements Parcelable {
        @NonNull final String placeholder;
        @NonNull final String propertyId;
        @Nullable final String propertyName;

        LinkItem(@NonNull String placeholder, @NonNull String propertyId, @Nullable String propertyName) {
            this.placeholder = placeholder;
            this.propertyId = propertyId;
            this.propertyName = propertyName;
        }

        LinkItem(Parcel in) {
            placeholder = in.readString();
            propertyId = in.readString();
            propertyName = in.readString();
        }

        public static final Creator<LinkItem> CREATOR = new Creator<LinkItem>() {
            @Override
            public LinkItem createFromParcel(Parcel in) {
                return new LinkItem(in);
            }

            @Override
            public LinkItem[] newArray(int size) {
                return new LinkItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(placeholder);
            parcel.writeString(propertyId);
            parcel.writeString(propertyName);
        }
    }

    private static class DynamicsLinkAdapter extends ArrayAdapter<LinkItem> {

        DynamicsLinkAdapter(@NonNull Context context, @NonNull List<LinkItem> objects) {
            super(context, R.layout.item_dynamics_link, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_dynamics_link, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_placeholder = view.findViewById(R.id.tv_placeholder);
                viewHolder.tv_dynamics = view.findViewById(R.id.tv_dynamics_name);
                viewHolder.tv_dynamics_id = view.findViewById(R.id.tv_dynamics_id);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            LinkItem item = getItem(position);
            if (item == null)
                return view;
            viewHolder.tv_placeholder.setText(item.placeholder);
            if (item.propertyName != null) {
                viewHolder.tv_dynamics.setText(item.propertyName);
                viewHolder.tv_dynamics_id.setText(item.propertyId);
                viewHolder.tv_dynamics_id.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_dynamics.setText(item.propertyId);
                viewHolder.tv_dynamics_id.setVisibility(View.GONE);
            }
            return view;
        }

        private static class ViewHolder {
            TextView tv_placeholder;
            TextView tv_dynamics;
            TextView tv_dynamics_id;
        }
    }

    public static class EditDynamicsActivity extends CommonBaseActivity {

        public static final String EXTRA_LINK_ITEM = "ryey.easer.core.ui.data.extras.LINK_ITEM";

        Spinner spinner_placeholder, spinner_dynamics;
        ArrayAdapter<String> adapter_placeholder;
        ArrayAdapter<DynamicsWrapper> adapter_dynamics;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_dynamics);
            spinner_placeholder = findViewById(R.id.spinner_placeholder);
            spinner_dynamics = findViewById(R.id.spinner_dynamics);

            ArrayList<String> placeholders = getIntent().getStringArrayListExtra(EXTRA_PLACEHOLDERS);
            adapter_placeholder = new ArrayAdapter<>(this, R.layout.spinner_simple, placeholders);
            spinner_placeholder.setAdapter(adapter_placeholder);

            List<Dynamics> allDynamics = new ArrayList<>();
            allDynamics.addAll(Arrays.asList(CoreDynamics.coreDynamics()));
            String plugin_type = getIntent().getStringExtra(EXTRA_PLUGIN_TYPE);
            if (PLUGIN_TYPE_EVENT.equals(plugin_type)) {
                EventData eventData = getIntent().getParcelableExtra(EXTRA_PLUGIN_DATA);
                Dynamics[] p_properties = eventData.dynamics();
                if (p_properties != null)
                    allDynamics.addAll(Arrays.asList(p_properties));
            }
            List<DynamicsWrapper> dynamicsList = new ArrayList<>();
            for (Dynamics dynamics : allDynamics) {
                DynamicsWrapper wrapper = new DynamicsWrapper(dynamics.id(), getString(dynamics.nameRes()));
                dynamicsList.add(wrapper);
            }
            adapter_dynamics = new ArrayAdapter<>(this, R.layout.spinner_simple, dynamicsList);
            spinner_dynamics.setAdapter(adapter_dynamics);

            Button button_ok = findViewById(R.id.btn_ok);
            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String placeholder = (String) spinner_placeholder.getSelectedItem();
                    if (placeholder == null) {
                        toastInvalid();
                        return;
                    }
                    DynamicsWrapper wrapper = (DynamicsWrapper) spinner_dynamics.getSelectedItem();
                    if (wrapper == null) {
                        toastInvalid();
                        return;
                    }
                    Intent intent = new Intent();
                    LinkItem item = new LinkItem(placeholder, wrapper.id, wrapper.name);
                    intent.putExtra(EXTRA_LINK_ITEM, item);
                    setResult(RESULT_OK, intent);
                    EditDynamicsActivity.this.finish();
                }

                private void toastInvalid() {
                    Toast.makeText(EditDynamicsActivity.this, R.string.prompt_data_illegal, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public static final class DynamicsWrapper {
            @NonNull
            public final String id;
            @Nullable
            public final String name;

            DynamicsWrapper(@NonNull String id, @Nullable String name) {
                this.id = id;
                this.name = name;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == this)
                    return true;
                if (obj == null || !(obj instanceof DynamicsWrapper))
                    return false;
                return id.equals(((DynamicsWrapper) obj).id);
            }

            @Override
            public String toString() {
                if (name != null)
                    return name;
                else
                    return id;
            }
        }
    }
}
