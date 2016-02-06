package com.livestrong.tracker.googlefitapp;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;


import com.livestrong.tracker.googlefitmodule.Interfaces.LSGoogleFitConnectionListener;
import com.livestrong.tracker.googlefitmodule.main.LSGoogleFitManager;
import com.livestrong.tracker.googlefitmodule.main.LSGoogleFitServiceReciever;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by shambhavipunja on 1/25/16.
 */
public class MainActivity extends AppCompatActivity implements LSGoogleFitConnectionListener, ViewPager.OnPageChangeListener {
    private PagerAdapter mPageAdapter;
    private ViewPager mViewPager;
    //private ActionBar mActionBar;
    //private Date mDate;
    private static final int NUM_PAGES = 30;
    public static final int TODAY_POSITION = NUM_PAGES / 2 ;
    public static final int OFF_SCREEN_PAGE = 1;
    public static final String TODAY = "Today";
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd, yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setTitle(TODAY);
        LSGoogleFitManager.initialize(this, this);
        LSGoogleFitManager.getLsGoogleFitManager().registerLSReciever(LSGoogleFitServiceReciever.ACTION_RESP);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mPageAdapter =  new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(TODAY_POSITION);
        mViewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE);
        mViewPager.addOnPageChangeListener(this);




    }

    @Override
    protected void onStart() {
        super.onStart();
        LSGoogleFitManager.getLsGoogleFitManager().startLSGoogleFitService();
    }

    @Override
    protected void onDestroy() {
        LSGoogleFitManager.getLsGoogleFitManager().unregisterLSReciever();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void setActionBar(String title){
        this.getSupportActionBar().setTitle(title);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == TODAY_POSITION){
            setActionBar(TODAY);
        }else {
           setActionBar(mDateFormat.format(getDateForPage(position)));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public Date getDateForPage(int page)
    {
        page = TODAY_POSITION - page;
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.add(Calendar.DATE, -page);
        return calendar.getTime();
    }

    @Override
    public void connectionStatus(String connectionStatus) {

    }

    @Override
    public void subscribeStatus(String subscribeStatus) {

    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return NUM_PAGES;
        }


    }
}
