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

package ryey.easer.core.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl;

import ryey.easer.R;
import ryey.easer.commons.ui.CommonBaseActivity;
import ryey.easer.core.ui.setting.SettingsActivity;
import ryey.easer.core.ui.version_n_info.AboutActivity;
import ryey.easer.core.ui.version_n_info.Info;
import ryey.easer.core.ui.version_n_info.Version;

public class MainActivity extends CommonBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String FRAGMENT_OUTLINE = "ryey.easer.FRAGMENT.OUTLINE";
    private static final String FRAGMENT_PIVOT = "ryey.easer.FRAGMENT.PIVOT";
    private static final String FRAGMENT_DATA = "ryey.easer.FRAGMENT.DATA";
    private static final String FRAGMENT_LOG = "ryey.easer.FRAGMENT.LOG";

    private static final NavTag navTag = new NavTag();

    private final LocaleHelperActivityDelegate localeDelegate = new LocaleHelperActivityDelegateImpl();

    @Override
    protected void attachBaseContext(@NonNull Context newBase) {
        super.attachBaseContext(localeDelegate.attachBaseContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localeDelegate.onCreate(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_outline);
            Fragment fragment = new OutlineFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment, FRAGMENT_OUTLINE)
                    .commit();
        }

        Info.INSTANCE.welcome(this);
        Version.INSTANCE.dataVersionChange(this);
        Version.INSTANCE.nearFutureChange(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        localeDelegate.onPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        localeDelegate.onResumed(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().popBackStack(0, 0); // The -1'st is the Outline. We rely on super.onBackPressed() to pop the 0th.
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_outline);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        changeUIView(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void changeUIView(@IdRes int id) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment;
        String tag = navTag.findTag(id);
        String bs_tag = tag;

        invalidateOptionsMenu();

        if (id == R.id.nav_outline) {
            fragment = manager.findFragmentByTag(tag);
            if (fragment == null)
                fragment = new OutlineFragment();
            manager.beginTransaction()
                    .replace(R.id.content_main, fragment, tag)
                    .addToBackStack(bs_tag)
                    .commit();
        } else if (id == R.id.nav_pivot) {
            fragment = manager.findFragmentByTag(tag);
            if (fragment == null)
                fragment = new PivotFragment();
            manager.beginTransaction()
                    .replace(R.id.content_main, fragment, tag)
                    .addToBackStack(bs_tag)
                    .commit();
        } else if (id == R.id.nav_data) {
            fragment = manager.findFragmentByTag(tag);
            if (fragment == null)
                fragment = new DataCollectionFragment();
            manager.beginTransaction()
                    .replace(R.id.content_main, fragment, tag)
                    .addToBackStack(bs_tag)
                    .commit();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log) {
            fragment = manager.findFragmentByTag(tag);
            if (fragment == null)
                fragment = ActivityHistoryFragment.full();
            manager.beginTransaction()
                    .replace(R.id.content_main, fragment, tag)
                    .addToBackStack(bs_tag)
                    .commit();
        }
    }

    private static class NavTag {
        private static final int[] nav_ids = {
                R.id.nav_outline,
                R.id.nav_pivot,
                R.id.nav_data,
                R.id.nav_log,
        };
        private static final String[] fragment_tags = {
                FRAGMENT_OUTLINE,
                FRAGMENT_PIVOT,
                FRAGMENT_DATA,
                FRAGMENT_LOG,
        };

        private @Nullable Integer findId(String tag) {
            for (int i = 0; i < nav_ids.length; i++) {
                if (tag.equals(fragment_tags[i]))
                    return nav_ids[i];
            }
            return null;
        }
        private @Nullable String findTag(int id) {
            for (int i = 0; i < fragment_tags.length; i++) {
                if (id == nav_ids[i])
                    return fragment_tags[i];
            }
            return null;
        }
    }
}
