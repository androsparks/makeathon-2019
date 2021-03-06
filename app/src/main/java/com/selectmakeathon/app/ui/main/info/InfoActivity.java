package com.selectmakeathon.app.ui.main.info;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.main.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class InfoActivity extends FragmentActivity /*implements ActionBar.TabListener*/ {

    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private TabItem mAbout;
    private TabItem mFaq;
    private ImageView backButton;
    private TabItem mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TabLayout mTabLayout = findViewById(R.id.TabsLay);
        TabItem mAbout = findViewById(R.id.item1);
        TabItem mFaq = findViewById(R.id.item3);
        TabItem mContact = findViewById(R.id.item2);
        final ViewPager viewPager = findViewById(R.id.ViewPagerMain);
        backButton=(ImageView)findViewById(R.id.back_home_info);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent backHome=new Intent(InfoActivity.this, MainActivity.class);
//                startActivity(backHome);
                finish();
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        PagerMyAdapter pagerAdapter = new PagerMyAdapter(getSupportFragmentManager(),3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


    }
}
