/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.commons;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SwitchItemLayout extends LinearLayout {
    boolean outher = false;
    CheckBox mCheckBox;
    ContentLayout content;

    public SwitchItemLayout(Context context, ContentLayout contentLayout) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        init(context, contentLayout);
    }

    protected void init(Context context, ContentLayout contentLayout) {
        outher = false;
        setOrientation(HORIZONTAL);
        mCheckBox = new CheckBox(context);
        super.addView(mCheckBox);
        content = contentLayout;
        addView(content);
        content.setEnabled(mCheckBox.isChecked());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                content.setEnabled(b);
            }
        });
        outher = true;
    }

    public void fill(StorageData data) {
        if (data == null) {
            mCheckBox.setChecked(false);
        } else {
            mCheckBox.setChecked(true);
            content.fill(data);
        }
    }

    public StorageData getData() {
        if (mCheckBox.isChecked()) {
            return content.getData();
        } else {
            return null;
        }
    }

    @Override
    public void addView(View child) {
        if (outher) {
            content.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        if (outher) {
            content.addView(child, width, height);
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (outher) {
            content.addView(child, index);
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (outher) {
            content.addView(child, params);
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (outher) {
            content.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    public static abstract class ContentLayout extends LinearLayout {
        public ContentLayout(Context context) {
            super(context);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            setOrientation(VERTICAL);
        }

        public abstract void fill(StorageData data);
        public abstract StorageData getData();

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            View child;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                child.setEnabled(enabled);
            }
        }
    }

    public static abstract class LabeledContentLayout extends ContentLayout {
        TextView textView;
        public LabeledContentLayout(Context context) {
            super(context);
            textView = new TextView(context);
            addView(textView);
        }

        protected void setDesc(String description) {
            textView.setText(description);
        }
    }
}
