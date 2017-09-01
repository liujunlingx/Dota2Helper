package com.sanzhs.dota2helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.sanzhs.dota2helper.fragment.Fragment1;
import com.sanzhs.dota2helper.fragment.Fragment2;
import com.sanzhs.dota2helper.fragment.Fragment3;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private TextView mTextMessage;
    private ViewPager vp;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();
        initView();
        setListeners();
    }

    private void findViewById(){
        vp = (ViewPager) findViewById(R.id.vpContainer);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
    }

    private void initView(){
        final ArrayList<Fragment> fgList = new ArrayList<>(3);
        fgList.add(new Fragment1());
        fgList.add(new Fragment2());
        fgList.add(new Fragment3());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return fgList.get(position);
            }

            @Override
            public int getCount() {
                return fgList.size();
            }
        };
        vp.setAdapter(mAdapter);
        vp.setCurrentItem(0);
    }

    private void setListeners(){
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Log.i("tag","home selected");
                        vp.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        Log.i("tag","dashboard selected");
                        vp.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        Log.i("tag","notifications selected");
                        vp.setCurrentItem(2);
                        return true;
                }
                return false;
            }

        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("tag","page " + position + " selected");
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
