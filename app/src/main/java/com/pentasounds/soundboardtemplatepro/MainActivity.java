package com.pentasounds.soundboardtemplatepro;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public MediaPlayer mp;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    public static boolean isTesting;

    public static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        isTesting=false;

        //zufallszehl holen
        SharedPreferences prefs = getSharedPreferences("values", 0);
        user_id = prefs.getString("user_id", "");
        if(user_id.equals("")){
            Random rnd = new Random();
            int zufallszahl;

            for (int i=0; i<10; i++){
                zufallszahl = 1 + rnd.nextInt(9);
                user_id=user_id+zufallszahl;
            }
            SharedPreferences prefs_writing = getSharedPreferences("values", 0);
            SharedPreferences.Editor editor =prefs_writing.edit();
            editor.putString("user_id", user_id);
            editor.commit();
        }

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

        saveToWeb("app_start");


//        ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                1);
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
                    saveToWeb("share_app");

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.text_fuers_teilen_vor_name) + " " +  getText(R.string.app_name) + " " + getText(R.string.text_fuers_teilen_nach_name) +"\n\n" + getText(R.string.link_zur_app));
                    startActivity(Intent.createChooser(shareIntent,  "Teilen über..."));
                }






                if (menuItem.getItemId() == R.id.soundboardapps) {
                    saveToWeb("playstore_soundboardapps");

                    String url = "https://play.google.com/store/apps/developer?id=PentaSounds";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    onPause();
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.buttonapps) {
                    saveToWeb("playstore_buttonapps");

                    String url = "https://play.google.com/store/apps/developer?id=PentaButtons";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    onPause();
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.instagram) {

                    saveToWeb("instagram");

                    AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                    a_builder.setMessage(R.string.instagram_text)
                            .setCancelable(true)
                            .setPositiveButton("Ja will ich", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String url = "https://www.redirection.lima-city.de/links/instagram.html";

                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    onPause();
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Instagram");
                    alert.show();
                }

//                if (menuItem.getItemId() == R.id.paypaldialog) {
//                    saveToWeb("paypal_dialog");
//
//
//
//                    AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
//                    a_builder.setMessage(R.string.paypal_text)
//                            .setCancelable(true)
//                            .setPositiveButton("Zu PayPal", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    saveToWeb("paypal");
//                                    String url = "https://www.redirection.lima-city.de/links/paypal.html";
//
//                                    Intent intent = new Intent();
//                                    intent.setAction(Intent.ACTION_VIEW);
//                                    intent.setData(Uri.parse(url));
//                                    onPause();
//                                    startActivity(intent);
//                                }
//                            })
//                            .setNegativeButton("Ne, bin gerade Broke", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int i) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alert = a_builder.create();
//                    alert.setTitle("Unterstützung");
//                    alert.show();
//                }



                if (menuItem.getItemId() == R.id.email) {
                    saveToWeb("email");
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","pentasounds@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_TEXT,"[Packagename:" + getText(R.string.package_name) +  "  ---Diese Info nicht löschen---]\n\n\n\n\n (Deine Nachricht)");
                    startActivity(Intent.createChooser(emailIntent, "E-Mail senden..."));
                }


                if (menuItem.getItemId() == R.id.rechtliches) {
                    saveToWeb("rechtliches");
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

    public void saveToWeb(final String type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String textparam = "text1=" + URLEncoder.encode(";User;" + user_id + ";action;" +type +";", "UTF-8");
                    URL scripturl = new URL(getText(R.string.stats_link).toString());
                    HttpURLConnection connection = (HttpURLConnection) scripturl.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setFixedLengthStreamingMode(textparam.getBytes().length);
                    OutputStreamWriter contentWriter = new OutputStreamWriter(connection.getOutputStream());
                    contentWriter.write(textparam);
                    contentWriter.flush();
                    contentWriter.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("###","no internet conection");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("###","no internet conection");
                }
            }
        }).start();

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
