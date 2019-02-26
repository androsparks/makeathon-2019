package com.selectmakeathon.app.ui.main.myTeam.TeamFrag;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.ProblemStatements;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.idea.AbstractActivity;
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;
import com.selectmakeathon.app.ui.main.myTeam.adapter.NoLeaderMemberAdapter;
import com.selectmakeathon.app.ui.main.problems.ProbFragmentPack.HealthFrag;
import com.selectmakeathon.app.ui.main.problems.ProblemActivity;
import com.selectmakeathon.app.util.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CurrentTeamFragment extends androidx.fragment.app.Fragment {

    private TeamModel model;
    private List<UserModel> currentTeam, toAdapter;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    private String teamnameId, teamname, name, regno;
    private UserModel in, outp;
    private RecyclerView mRecyclerView;
    private TextView TeamNameHolder;
    private Button submitButton;


    public CurrentTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_current_team, container, false);
        mRecyclerView = view.findViewById(R.id.ListMembers);
        submitButton = view.findViewById(R.id.btn_team_submit);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefEditor = prefs.edit();

        boolean isSubmitted = prefs.getBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, false);

        if (isSubmitted) {
            submitButton.setText("Edit Abstract");
        } else {
            submitButton.setText("Submit Abstract");
        }
        //System.out.println(getUserModel().isLeader());
        if(getUserModel().isLeader())
        {
            submitButton.setVisibility(View.VISIBLE);
        }
        else {
            submitButton.setVisibility(View.GONE);

        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TeamNameHolder = view.findViewById(R.id.TeamNameText);
        List<UserModel> registeredMembers = getTeamModel().getTeamMembers();
        CurrentTeamAdapter adapter = new CurrentTeamAdapter(registeredMembers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSubmitted = prefs.getBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, false);

                if (isSubmitted) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Edit Abstract")
                            .setMessage("Do you want to change problem statement?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), ProblemActivity.class);
                                    intent.putExtra("CONTINUE", true);
                                    intent.putExtra("TEAM_ID", getTeamModel().getTeamId());
                                    intent.putExtra("IS_EXTERNAL", !getUserModel().isVitian());
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), AbstractActivity.class);
                                    intent.putExtra("TEAM_ID", getTeamModel().getTeamId());
                                    intent.putExtra("IS_EXTERNAL", !getUserModel().isVitian());
                                    startActivity(intent);
                                }
                            })
                            .create().show();
                } else {
                    Intent intent = new Intent(getActivity(), ProblemActivity.class);
                    intent.putExtra("CONTINUE", true);
                    intent.putExtra("TEAM_ID", getTeamModel().getTeamId());
                    intent.putExtra("IS_EXTERNAL", !getUserModel().isVitian());
                    startActivity(intent);
                }
            }
        });
    }

    private TeamModel getTeamModel() {
        return ((MyTeamActivity) getActivity()).teamModel;
    }
    private UserModel getUserModel() {
        return ((MyTeamActivity) getActivity()).userModel;
    }
}

class CurrentTeamAdapter extends RecyclerView.Adapter<CurrentTeamAdapter.LeaderMemberViewHolder> {

    List<UserModel> LeaderMemberList;

    public CurrentTeamAdapter(List<UserModel> LeaderMemberList) {
        this.LeaderMemberList = LeaderMemberList;
    }

    @NonNull
    @Override
    public CurrentTeamAdapter.LeaderMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_member, parent, false);
        return new CurrentTeamAdapter.LeaderMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentTeamAdapter.LeaderMemberViewHolder holder, int position) {
        UserModel currentUser = LeaderMemberList.get(position);
        holder.LeaderMemberName.setText(currentUser.getName());
        holder.LeaderMemberReg.setText(currentUser.getRegNo());
    }

    @Override
    public int getItemCount() {
        return LeaderMemberList.size();
    }

    class LeaderMemberViewHolder extends RecyclerView.ViewHolder {

        TextView LeaderMemberName, LeaderMemberReg;

        public LeaderMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            LeaderMemberName = itemView.findViewById(R.id.MemberNameText);
            LeaderMemberReg = itemView.findViewById(R.id.MemberRgNo);
        }
    }

}
