/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import ryey.easer.R;
import ryey.easer.core.ui.edit.EventListFragment;
import ryey.easer.core.ui.edit.ProfileListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String FRAGMENT_OUTLINE = "ryey.easer.FRAGMENT.OUTLINE";
    private static final String FRAGMENT_PROFILE = "ryey.easer.FRAGMENT.PROFILE";
    private static final String FRAGMENT_EVENT = "ryey.easer.FRAGMENT.EVENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
  
        if (savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_outline);
            Fragment fragment = new OutlineFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment, FRAGMENT_OUTLINE).commit();
        }

        // Show Welcome page at first launch
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.key_pref_welcome), true)) {
            ((TextView) new AlertDialog.Builder(this)
                    .setTitle(R.string.title_welcome_message)
                    .setMessage(R.string.welcome_message)
                    .setPositiveButton(R.string.button_understand, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                                    .edit()
                                    .putBoolean(getString(R.string.key_pref_welcome), false)
                                    .apply();
                        }
                    })
                    .setNegativeButton(R.string.button_read_next_time, null)
                    .show()
                    .findViewById(android.R.id.message))
                    .setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Logger.v("item: " + item.getTitle());

        int id = item.getItemId();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment;

        if (id == R.id.nav_outline) {
            fragment = manager.findFragmentByTag(FRAGMENT_OUTLINE);
            if (fragment == null)
                fragment = new OutlineFragment();
            manager.beginTransaction().replace(R.id.content_main, fragment, FRAGMENT_OUTLINE).commit();
        } else if (id == R.id.nav_profile) {
            fragment = manager.findFragmentByTag(FRAGMENT_PROFILE);
            if (fragment == null)
                fragment = new ProfileListFragment();
            manager.beginTransaction().replace(R.id.content_main, fragment, FRAGMENT_PROFILE).commit();
        } else if (id == R.id.nav_event) {
            fragment = manager.findFragmentByTag(FRAGMENT_EVENT);
            if (fragment == null)
                fragment = new EventListFragment();
            manager.beginTransaction().replace(R.id.content_main, fragment, FRAGMENT_EVENT).commit();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
