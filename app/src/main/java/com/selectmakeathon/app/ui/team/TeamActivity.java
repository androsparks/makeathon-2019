package com.selectmakeathon.app.ui.team;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTeamReference, mUserReference;
    private String teamName, teamId;
    private EditText teamNameEditText;
    private List<UserModel> initialMembers;
    UserModel teamLeader;
    TeamModel mainTeam;
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
        teamNameEditText = findViewById(R.id.et_team_name);

        addMemberInterface = new AddMemberInterface() {
            @Override
            public void addMember(UserModel member) {

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

                    } else {
                        Toast.makeText(TeamActivity.this, "Can't find the User, Please Login again", Toast.LENGTH_SHORT).show();
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
                AddMemberBottomSheet memberBottomSheet = new AddMemberBottomSheet();
                memberBottomSheet.show(getSupportFragmentManager(), "AddMember");
            }
        });

        find

    }

    void createTeam(final String teamName, final UserModel teamLeader){
        teamId = teamName.toLowerCase().replace(' ', '_');
        mTeamReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(teamId)){
                    //Team Already Exists, take Action
                    Toast.makeText(TeamActivity.this, "Team Already Exists, Please use a different name", Toast.LENGTH_SHORT).show();
                } else {
                    TeamModel teamModel = new TeamModel(teamName, teamId, teamLeader, initialMembers, null, null, false);
                    mTeamReference.child(teamId).setValue(teamModel);
                    mainTeam = teamModel;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void addMember(UserModel newMember){
        if (initialMembers == null || initialMembers.size() == 0){
            initialMembers = new ArrayList<>();
            initialMembers.add(newMember);
        } else {
            initialMembers.add(newMember);
        }
    }

    public static AddMemberInterface getAddMemberInterface() {
        return addMemberInterface;
    }

    public static void setAddMemberInterface(AddMemberInterface addMemberInterface) {
        TeamActivity.addMemberInterface = addMemberInterface;
    }
}
