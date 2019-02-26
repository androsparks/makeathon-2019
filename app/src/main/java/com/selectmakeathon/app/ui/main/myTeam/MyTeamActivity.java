package com.selectmakeathon.app.ui.main.myTeam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.ui.main.info.PagerMyAdapter;
import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.CurrentTeamFrag;
import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.PendingTeam;

public class MyTeamActivity extends AppCompatActivity {

    private TabLayout allTabs;
    private ViewPager tviewPager;
    private TabItem mMt;
    private TabItem mPt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        TabLayout allTabs = findViewById(R.id.TabsLayTeam);
        TabItem mMt = findViewById(R.id.titem1);
        TabItem mPt = findViewById(R.id.titem2);
        final ViewPager tviewPager = findViewById(R.id.ViewPagerTeam);


        allTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tviewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        MyTeamAdapter myTeamAdapter = new MyTeamAdapter(getSupportFragmentManager(),2);
        tviewPager.setAdapter(myTeamAdapter);
        tviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(allTabs));
    }


}

