package com.selectmakeathon.app.ui.main.myTeam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.NoLeaderFragment;
import com.selectmakeathon.app.util.Constants;

public class MyTeamActivity extends AppCompatActivity {

    private TabLayout allTabs;
    private ViewPager tviewPager;
    private TabItem mMt;
    private TabItem mPt;
    private TextView teamDisplayName;
    private FrameLayout layoutNoLeader;
    private LinearLayout layoutLeader;
    private ImageView backButton;
    private ImageView deleteButton;

    public String teamName;
    public TeamModel teamModel = new TeamModel();
    public String userName;
    public UserModel userModel = new UserModel();

    private RelativeLayout loadingLayout;
    private LinearLayout containerLayout;

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        teamDisplayName=findViewById(R.id.TeamNameHolder);

        backButton = findViewById(R.id.nav_home_team);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteButton = findViewById(R.id.image_delete_team);

        /*TODO: Remove default values */
        teamName = prefs.getString(Constants.PREF_TEAM_ID, "");
        userName = prefs.getString(Constants.PREF_USER_ID, "");

        loadingLayout = findViewById(R.id.layout_myteam_loading_container);
        containerLayout = findViewById(R.id.layout_myteam_main_container);
        startAnimation();

        reference.child("users").child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    fetchTeamModel();
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchTeamModel() {
        reference.child("teams").child(userModel.getTeamName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    teamModel = dataSnapshot.getValue(TeamModel.class);

                    if (teamModel.isSelected()) {
                        teamDisplayName.setText("Congratulations !! " + teamModel.getTeamName() + " is selected for next round");
                    } else {
                        teamDisplayName.setText(teamModel.getTeamName());
                    }

                    initViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initViews() {

        stopAnimation();

        layoutNoLeader = findViewById(R.id.team_layout_is_no_leader);
        layoutLeader = findViewById(R.id.team_layout_isleader);

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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_layout_is_no_leader, new NoLeaderFragment())
                .commit();

        if (userModel.isLeader()) {
            layoutLeader.setVisibility(View.VISIBLE);
            layoutNoLeader.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            layoutLeader.setVisibility(View.GONE);
            layoutNoLeader.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MyTeamActivity.this)
                        .setTitle("Delete Team")
                        .setMessage("Are you sure you want to delete team")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTeam();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();

            }
        });
    }

    private void deleteTeam() {

        for (UserModel userModel : teamModel.getTeamMembers()) {

            if (userModel.isLeader()) {
                userModel.setLeader(false);
            }
            userModel.setJoined(false);
            userModel.setTeamName("");

            reference.child("users").child(userModel.getRegNo()).setValue(userModel);

        }

        if (prefs.getBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, false)) {
            FirebaseStorage.getInstance()
                    .getReference(teamModel.getTeamId())
                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }

        prefEditor.putBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, false).apply();
        reference.child("teams").child(teamModel.getTeamId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });

    }

    public void startAnimation() {
        loadingLayout.setVisibility(View.VISIBLE);
        containerLayout.setVisibility(View.GONE);
    }

    public void stopAnimation() {
        loadingLayout.setVisibility(View.GONE);
        containerLayout.setVisibility(View.VISIBLE);
    }

}

