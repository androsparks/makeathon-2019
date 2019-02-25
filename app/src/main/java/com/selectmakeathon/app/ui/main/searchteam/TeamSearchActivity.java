package com.selectmakeathon.app.ui.main.searchteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class TeamSearchActivity extends AppCompatActivity implements OnTeamSelectListener {

    ImageView backButton;
    ImageView buttonSearch;
    EditText inputQuery;
    Button buttonAddTeam;
    RecyclerView rvTeams;

    LinearLayout teamStaticLayout;
    TextView teamNameStatic;
    TextView teamLeaderStatic;
    TextView memberCountStatic;
    Button clearButtonStatic;

    TeamAdapter teamAdapter;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    ArrayList<TeamModel> teamModels = new ArrayList<>();
    UserModel userModel;

    boolean showRv = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_search);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        String userName = prefs.getString(Constants.PREF_USER_ID, "");
        if (!userName.isEmpty()) {

            reference.child("users").child(userName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        userModel = dataSnapshot.getValue(UserModel.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        initViews();
        initRv();

        startAnimation();

        reference.child("teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                teamModels.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    teamModels.add(child.getValue(TeamModel.class));
                }

                updateUI();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String query = inputQuery.getText()
                        .toString()
                        .trim()
                        .toLowerCase()
                        .replace(" ", "_");

                if (!query.isEmpty()) {

                    startAnimation();

                    reference.child("teams").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(query)) {
                                TeamModel searchTeamModel = dataSnapshot.child(query).getValue(TeamModel.class);

                                teamNameStatic.setText(searchTeamModel.getTeamName());
                                teamLeaderStatic.setText(searchTeamModel.getTeamLeader());
                                memberCountStatic.setText(searchTeamModel.getTeamMembers().size());

                                showRv = false;

                            }
                            updateUI();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        clearButtonStatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRv = true;
                inputQuery.setText(null);
                updateUI();
            }
        });
    }

    private void initRv() {
        teamAdapter = new TeamAdapter(this);

        rvTeams.setLayoutManager(new LinearLayoutManager(this));
        rvTeams.setAdapter(teamAdapter);
    }

    private void initViews() {
        backButton = findViewById(R.id.image_team_search_back);
        buttonSearch = findViewById(R.id.image_team_search);
        inputQuery  = findViewById(R.id.input_team_query);
        buttonAddTeam = findViewById(R.id.button_team_addteam);
        rvTeams = findViewById(R.id.rv_teams);

        teamStaticLayout = findViewById(R.id.layout_team_static);
        teamNameStatic = findViewById(R.id.item_team_teamname_static);
        teamLeaderStatic = findViewById(R.id.item_team_leadername_static);
        memberCountStatic = findViewById(R.id.item_team_member_count_static);
        clearButtonStatic = findViewById(R.id.button_team_clear_static);
    }

    public void updateUI() {
        stopAnimation();

        if (showRv) {
            rvTeams.setVisibility(View.VISIBLE);
            teamStaticLayout.setVisibility(View.GONE);
        } else {
            rvTeams.setVisibility(View.GONE);
            teamStaticLayout.setVisibility(View.VISIBLE);
        }

        teamAdapter.setTeamModels(teamModels);
    }

    public void startAnimation() {

    }

    public void stopAnimation() {

    }

    @Override
    public void onTeamSelect(int position) {

        TeamModel teamModel = teamModels.get(position);

        /*TODO: Show dialog box*/
        /*TODO: Update firebase*/

    }
}
