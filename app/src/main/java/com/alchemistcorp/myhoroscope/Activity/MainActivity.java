package com.alchemistcorp.myhoroscope.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alchemistcorp.myhoroscope.Fragments.ListZodiacsFragment;
import com.alchemistcorp.myhoroscope.Fragments.SplashScreenFragment;
import com.alchemistcorp.myhoroscope.R;
import com.alchemistcorp.myhoroscope.Utility;
import com.startapp.android.publish.StartAppSDK;

import java.lang.reflect.Field;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StartAppSDK.init(this,"204789210" , false);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        int day = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("date",-1);
        Fragment fragmentToLoad = null;
        if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == day)
            fragmentToLoad = new ListZodiacsFragment();
        else
            fragmentToLoad = new SplashScreenFragment();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
                if (stackHeight > 0) { // if we have something on the stack (doesn't include the current shown fragment)
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }

        });

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            Utility.setFont((TextView) f.get(toolbar),getAssets());
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }


        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,fragmentToLoad).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            return true;
        }
        if(id == R.id.about_us){
            final SpannableString s =
                    new SpannableString(Html.fromHtml("<div style=\"margin:8px;padding-left:10px\"><div style=\"padding:10px;\">This app is created by <strong>Alchemist Corp</strong>.</div><p><div>DISCLAIMER</div> The Zodiac photos have been taken from freevector.com ." +
                            "The Zodiac Vector Images were created by Akira and is distributed under the Creative Commons Attribution-ShareAlike 4.0 license (<a>https://creativecommons.org/licenses/by-sa/4.0/</a>). " +
                            "</p><p>Some graphics used are <a href=\"http://www.freepik.com\">Designed by Freepik</a></p></div></div>"));
            Linkify.addLinks(s, Linkify.WEB_URLS);
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("About Us");
            builder.setMessage(s);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //((TextView)builder.create().findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            builder.show();
            return true;
        }
        if(id == android.R.id.home) {
            getSupportFragmentManager().popBackStack(); return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        if(!getSupportFragmentManager().popBackStackImmediate()){
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.alchemistcorp.myhoroscope")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.alchemistcorp.myhoroscope")));
                    }
                }
            });
            builder.show();
        }

    }
}
