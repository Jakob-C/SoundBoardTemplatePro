package com.pentasounds.soundboardtemplatepro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarTabs();
        sidebar();
        externalStorageAccess();

    }


    public void sidebar(){
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()){
                    case R.id.nav_item_inbox:
                        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                        xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                        break;

                    case R.id.teilen:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
                        shareIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.text_fuers_teilen_vor_name) + " " +  getText(R.string.app_name) + " " + getText(R.string.text_fuers_teilen_nach_name) +"\n\n" + getText(R.string.link_zur_app));
                        startActivity(Intent.createChooser(shareIntent,  "Share via..."));
                        break;

                }
                return false;
            }

        });
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
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        }
    }


    public void externalStorageAccess(){
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
    }


    public void toolbarTabs(){
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }



}
