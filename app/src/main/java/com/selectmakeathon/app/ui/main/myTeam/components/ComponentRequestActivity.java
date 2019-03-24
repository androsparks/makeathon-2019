package com.selectmakeathon.app.ui.main.myTeam.components;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.ComponentRequestModel;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.ui.main.idea.bottomsheet.ComponentsBottomSheetFragment;

import java.util.ArrayList;

public class ComponentRequestActivity extends AppCompatActivity{

    FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;
    BottomSheetBehavior behavior;
    RecyclerView recyclerView,finarec;
    public ItemAdapter mAdapter;

    private ArrayList<ComponentRequestModel> aL=new ArrayList<>();
    CompListAdapter mCompListAdapter;

    String teamId = "";
    TeamModel teamModel;
    private ArrayList<String> components = new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_request);
        fab = findViewById(R.id.fab_component_request);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        final View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentRequestModel componentRequestModel = new ComponentRequestModel();
                if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
//
//                String id = reference.push().getKey();
//                componentRequestModel.setId(id);

                teamModel.getComponentRequests().add(componentRequestModel);
                reference.child("teams").child(teamId).setValue(teamModel);
            }
        });
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        finarec=(RecyclerView)findViewById(R.id.finalCompRec);

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
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ComponentRequestActivity.this));
                mAdapter = new ItemAdapter(components,teamId);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
