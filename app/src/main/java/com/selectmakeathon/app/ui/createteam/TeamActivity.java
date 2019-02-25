package com.selectmakeathon.app.ui.createteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.util.Constants;
import com.selectmakeathon.app.util.TeamMemberAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTeamReference, mUserReference;
    private TextInputLayout teamNameEditText;
    private RecyclerView teamMemberRecyclerView;
    private List<UserModel> initialMembers;
    UserModel teamLeader;
    private AddMemberBottomSheet memberBottomSheet;
    private SharedPreferences prefs;
    static AddMemberInterface addMemberInterface;
    private SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        mDatabase = FirebaseDatabase.getInstance();
        mTeamReference = mDatabase.getReference().child("teams");
        mUserReference = mDatabase.getReference().child("users");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();
        teamNameEditText = findViewById(R.id.el_team_name);
        teamMemberRecyclerView = findViewById(R.id.rv_team_members);

        addMemberInterface = new AddMemberInterface() {
            @Override
            public void addMember(final UserModel member) {
                dismissMemberBottomSheet();
                AlertDialog.Builder builder = new AlertDialog.Builder(TeamActivity.this);
                builder.setTitle("Confirm");
                String message = "Do you really wanna add " + member.getName() + "?";
                builder.setMessage(message);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        if (member.getRegNo().equals(teamLeader.getRegNo())){
                            Toast.makeText(TeamActivity.this, "We believe in Collaboration and not in Isolation. Please add members other than yourself!", Toast.LENGTH_SHORT).show();
                        } else {
                            addNewMember(member);
                            dialog.dismiss();
                        }
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        };

        final String teamLeaderRegNo = prefs.getString(Constants.PREF_USER_ID, null);
        if (teamLeaderRegNo == null){
            Toast.makeText(this, "Couldn't find User Data, Please Login again", Toast.LENGTH_SHORT).show();
            finishAfterTransition();
        } else {
            mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(teamLeaderRegNo)){
                        teamLeader = dataSnapshot.child(teamLeaderRegNo).getValue(UserModel.class);
                        if (teamLeader.isLeader() || teamLeader.isJoined()){
                            Toast.makeText(TeamActivity.this, "You are already part of another team", Toast.LENGTH_SHORT).show();
                            finishAfterTransition();
                        } else {
                            initialMembers = new ArrayList<>();
                            initialMembers.add(teamLeader);
                            updateMembersList();
                        }

                    } else {
                        Toast.makeText(TeamActivity.this, "Can't find the User, Please Login again", Toast.LENGTH_SHORT).show();
                        finishAfterTransition();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        findViewById(R.id.add_member_text_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMemberBottomSheet();
            }
        });

        findViewById(R.id.team_button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAfterTransition();
            }
        });

        findViewById(R.id.btn_team_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teamName = teamNameEditText.getEditText().getText().toString().trim();
                if (teamName == null || teamName.length() == 0){
                    teamNameEditText.setError("Please Enter a valid Team Name");
                } else {
                    createTeam(teamName, teamLeader);
                }
            }
        });

    }

    void createTeam(final String teamName, final UserModel teamLeader){
        final String teamId = teamName.toLowerCase().replace(' ', '_');
        mTeamReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(teamId)){
                    //Team Already Exists, take Action
                    Toast.makeText(TeamActivity.this, "Team Already Exists, Please use a different name", Toast.LENGTH_SHORT).show();
                } else {
                    teamLeader.setLeader(true);
                    mUserReference.child(teamLeader.getRegNo()).setValue(teamLeader);
                    for(int i = 1; i < initialMembers.size(); i++){
                        UserModel userModel = initialMembers.get(i);
                        userModel.setJoined(true);
                        putTeamMemberAsAdded(initialMembers.get(i));
                        initialMembers.set(i, userModel);
                    }
                    TeamModel teamModel = new TeamModel(teamName, teamId, teamLeader, initialMembers, null, null, false);
                    mTeamReference.child(teamId).setValue(teamModel);
                    launchAfterFinish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void addNewMember(UserModel newMember){
        if (initialMembers == null || initialMembers.size() == 0){
            initialMembers = new ArrayList<>();
            initialMembers.add(teamLeader);
            initialMembers.add(newMember);
        } else {
            initialMembers.add(newMember);
        }
        if (initialMembers.size() > 4){
            findViewById(R.id.add_member_text_button).setVisibility(View.GONE);
        }
        updateMembersList();
    }

    void putTeamMemberAsAdded(final UserModel acceptedMember){
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel thisMember = dataSnapshot.child(acceptedMember.getRegNo()).getValue(UserModel.class);
                thisMember.setJoined(true);
                mUserReference.child(thisMember.getRegNo()).setValue(thisMember);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static AddMemberInterface getAddMemberInterface() {
        return addMemberInterface;
    }


    void openAddMemberBottomSheet(){
        memberBottomSheet = new AddMemberBottomSheet();
        memberBottomSheet.show(getSupportFragmentManager(), "AddMember");
    }

    void dismissMemberBottomSheet(){
        memberBottomSheet.dismiss();
    }

    void updateMembersList(){
        TeamMemberAdapter adapter = new TeamMemberAdapter(initialMembers);
        teamMemberRecyclerView.setAdapter(adapter);
    }

    void launchAfterFinish(){
        //TODO : Handle further flow
    }

}
