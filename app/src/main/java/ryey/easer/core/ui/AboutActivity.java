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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import ryey.easer.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        FloatingActionButton fabGit = findViewById(R.id.fabGithub);
        fabGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myHubGit();

            }
        });

        FloatingActionButton fabWord = findViewById(R.id.fabWord);
        fabWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myWebsite();

            }
        });

        FloatingActionButton fabBack = findViewById(R.id.fabReturn);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goBack();

            }
        });
    }

    // When they click on the github icon.
    public void myHubGit () {

        goToUrl ( "https://github.com/renyuneyun/Easer");

    }

    // When they click on the wordpress icon.
    public void myWebsite () {

        goToUrl ( "http://renyuneyun.is-programmer.com/");

    }

    // When they click on the license.
    public void howL (View view) {

        goToUrl ( "https://www.gnu.org/licenses/gpl.txt");

    }

    // To launch one of the above URL's.
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    // To kill this activity and go back to Activity Main.
    public void goBack () {
        finish();
    }


}
