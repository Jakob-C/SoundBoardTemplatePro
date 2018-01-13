package com.pentasounds.soundboardtemplatepro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pentasounds.soundboardtemplatepro.tabs.Tab1;
import com.pentasounds.soundboardtemplatepro.tabs.Tab2;
import com.pentasounds.soundboardtemplatepro.tabs.Tab3;

/**
 * Created by Ratan on 7/27/2015.
 */
public class TabFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;

    public InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getText(R.string.interstitial_ad_unit_id) + "");
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //Ads loaded
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //Ads closed
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                //Ads couldn't loaded
            }
        });
        mInterstitialAd.loadAd(adRequest);


        /**
         *Inflate tab_layout and setup Views.
         */
            View x =  inflater.inflate(R.layout.tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {


                if(position == 1||position == 2){
                    if(mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();

                        AdRequest adRequest = new AdRequest.Builder().build();
                        mInterstitialAd = new InterstitialAd(getContext());
                        mInterstitialAd.setAdUnitId(getText(R.string.interstitial_ad_unit_id) + "");
                        mInterstitialAd.loadAd(adRequest);
                    }
                }
            }
        });


        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                   }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {

            if(position == 0){
                return new Tab1();
            }
            if(position == 1){
                return new Tab2();
            }
            if(position == 2){
                return new Tab3();
            }


        return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return getText(R.string.tab1);
                case 1 :
                    return getText(R.string.tab2);
                case 2 :
                    return getText(R.string.tab3);

            }
                return null;
        }
    }

}
