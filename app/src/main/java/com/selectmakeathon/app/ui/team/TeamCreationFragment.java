package com.selectmakeathon.app.ui.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TeamCreationFragment extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTeamReference;
    private String teamName, teamId, teamLeader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_creation, container, false);

        return view;
    }

    void createTeam(){
        teamId = teamName.toLowerCase().replace(' ', '_');
        mTeamReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(teamId)){
                    //Team Already Exists, take Action
                    Toast.makeText(getContext(), "Team Already Exists, Please use a different name", Toast.LENGTH_SHORT).show();
                } else {
                    TeamModel teamModel = new TeamModel(teamName, teamId, teamLeader, null, null, null, false);
                    mTeamReference.child(teamId).setValue(teamModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
