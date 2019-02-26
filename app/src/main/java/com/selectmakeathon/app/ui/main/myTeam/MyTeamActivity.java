package com.selectmakeathon.app.ui.main.myTeam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.util.Constants;

public class MyTeamActivity extends AppCompatActivity {

    private TabLayout allTabs;
    private ViewPager tviewPager;
    private TabItem mMt;
    private TabItem mPt;

    public String teamName;
    public TeamModel teamModel = new TeamModel();
    public String userName;
    public UserModel userModel = new UserModel();

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        /*TODO: Remove this */
        teamName = prefs.getString(Constants.PREF_TEAM_ID, "team_null_proxy");
        userName = prefs.getString(Constants.PREF_USER_ID, "16BCE0587");

        reference.child("teams").child(teamName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    teamModel = dataSnapshot.getValue(TeamModel.class);
                    initViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("users").child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userModel = dataSnapshot.getValue(UserModel.class);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initViews() {
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

