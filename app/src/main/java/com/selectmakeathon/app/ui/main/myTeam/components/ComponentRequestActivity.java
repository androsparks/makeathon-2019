package com.selectmakeathon.app.ui.main.myTeam.components;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.ComponentRequestModel;
import com.selectmakeathon.app.model.TeamModel;

import java.util.ArrayList;

public class ComponentRequestActivity extends AppCompatActivity {

    FloatingActionButton fab;

    String teamId = "";
    TeamModel teamModel;
    private ArrayList<String> components = new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_request);

        initViews();

        teamId = getIntent().getStringExtra("TEAM_ID");

        reference.child("teams").child(teamId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    teamModel = dataSnapshot.getValue(TeamModel.class);
                    teamModel.getComponentRequests(); //TODO: Use this
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("components").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    components.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        components.add(childSnapshot.getValue(String.class));
                    }
                    fab.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentRequestModel componentRequestModel = new ComponentRequestModel();
                componentRequestModel.setComponentName("Sample component");
                componentRequestModel.setCount(2);
//
//                String id = reference.push().getKey();
//                componentRequestModel.setId(id);

                teamModel.getComponentRequests().add(componentRequestModel);

                reference
                        .child("teams")
                        .child(teamId)
                        .setValue(teamModel);
            }
        });
    }

    private void initViews() {
        fab = findViewById(R.id.fab_component_request);
    }
}
