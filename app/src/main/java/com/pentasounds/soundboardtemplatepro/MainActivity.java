package com.pentasounds.soundboardtemplatepro;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.pentasounds.soundboardtemplatepro.tabs.Tab3;
import com.pentasounds.soundboardtemplatepro.tabs.Tab1;
import com.pentasounds.soundboardtemplatepro.tabs.Tab2;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public MediaPlayer mp;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    public static boolean isTesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        isTesting=false;

        final File FILES_PATH = new File(Environment.getExternalStorageDirectory(), "Android/data/"+ getText(R.string.package_name) +"/files");


        if (Environment.MEDIA_MOUNTED.equals(

                Environment.getExternalStorageState())) {

            if (!FILES_PATH.mkdirs()) {

                Log.w("error", "Could not create " + FILES_PATH);

            }

        } else {

            Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();

            finish();

        }


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         *
         *
         *
         */
        mNavigationView.setItemIconTintList(null);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                }



                if(menuItem.getItemId() == R.id.teilen){
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.text_fuers_teilen_vor_name) + " " +  getText(R.string.app_name) + " " + getText(R.string.text_fuers_teilen_nach_name) +"\n\n" + getText(R.string.link_zur_app));
                    startActivity(Intent.createChooser(shareIntent,  "Teilen über..."));
                }






                if (menuItem.getItemId() == R.id.soundboardapps) {
                    String url = "https://play.google.com/store/apps/developer?id=PentaSounds";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    onPause();
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.button) {
                    String url = "https://play.google.com/store/apps/developer?id=PentaButtons";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    onPause();
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.instagram) {
                    String url = "https://www.redirectioninstapenta.lima-city.de";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    onPause();
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.email) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","pentasounds@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_TEXT,"[Packagename:" + getText(R.string.package_name) +  "  ---Diese Info nicht löschen---]\n\n\n\n\n (Deine Nachricht)");
                    startActivity(Intent.createChooser(emailIntent, "E-Mail senden..."));
                }


                if (menuItem.getItemId() == R.id.rechtliches) {
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                    a_builder.setMessage(R.string.rechtliches)
                            .setCancelable(true)
                            .setNegativeButton("Verstanden", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Rechtliches");
                    alert.show();
                }




                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


    }




    public void TabOneItemClicked(int position) {

        cleanUpMediaPlayer();
        mp=MediaPlayer.create(MainActivity.this, Tab1.soundfiles[position]);
        mp.start();
    }

    public void TabTwoItemClicked(int position) {
        cleanUpMediaPlayer();
        mp=MediaPlayer.create(MainActivity.this, Tab2.soundfiles[position]);
        mp.start();

    }
    public void TabThreeItemClicked ( int position){
        cleanUpMediaPlayer();
        mp=MediaPlayer.create(MainActivity.this, Tab3.soundfiles[position]);
        mp.start();

    }



    public void cleanUpMediaPlayer() {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
                mp = null;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Ein Fehler ist aufgetreten, versuche die App neu zu starten.", Toast.LENGTH_LONG).show();

            }
        }
    }



}
