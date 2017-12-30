package com.sanzhs.dota2helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.sanzhs.dota2helper.fragment.Fragment1;
import com.sanzhs.dota2helper.fragment.Fragment2;
import com.sanzhs.dota2helper.fragment.Fragment3;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView left_navigation;
    private Toolbar toolbar;
    private ViewPager vp;
    private FragmentPagerAdapter mAdapter;
    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        Intent intent = getIntent();
        Log.i("mainactivity", String.valueOf(intent.getExtras() == null));

        findViewById();
        initView(intent.getExtras());
        setListeners();
    }

    private void findViewById(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        left_navigation = (NavigationView) findViewById(R.id.nav_view);

        vp = (ViewPager) findViewById(R.id.vpContainer);
        bottom_navigation = (BottomNavigationView) findViewById(R.id.navigation);
    }

    private void initView(Bundle savedInstanceState){
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final ArrayList<Fragment> fgList = new ArrayList<>(3);
        Fragment1 fragment1 = new Fragment1();
        fragment1.setArguments(savedInstanceState);
        fgList.add(fragment1);
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
        left_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_search:
                        Intent intent = new Intent();
                        intent.setClass(getApplication(), SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_gallery:
                        Log.i("mainActivity", "gallery");
                        break;
                    case R.id.nav_slideshow:
                        Log.i("mainActivity", "slide");
                        break;
                    case R.id.nav_tools:
                        Log.i("mainActivity", "tools");
                        break;
                    case R.id.nav_share:
                        Log.i("mainActivity", "share");
                        break;
                    case R.id.nav_send:
                        Log.i("mainActivity", "send");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Log.i("mainActivity","home selected");
                        vp.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        Log.i("mainActivity","dashboard selected");
                        vp.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        Log.i("mainActivity","notifications selected");
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
                Log.i("mainActivity","page " + position + " selected");
                bottom_navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
