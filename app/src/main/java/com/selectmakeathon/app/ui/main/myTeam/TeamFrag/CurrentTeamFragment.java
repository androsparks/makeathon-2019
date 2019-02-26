package com.selectmakeathon.app.ui.main.myTeam.TeamFrag;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;
import com.selectmakeathon.app.ui.main.problems.ProbFragmentPack.HealthFrag;
import com.selectmakeathon.app.util.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private ListAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private TextView TeamNameHolder;


    public CurrentTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_team, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.ListMembers);
        TeamNameHolder = view.findViewById(R.id.TeamNameText);

        currentTeam = getTeamModel().getTeamMembers();
        teamname = getTeamModel().getTeamName();
        TeamNameHolder.setText(teamname);
        for (int i = 0; i < getTeamModel().getTeamMembers().size(); i++) {
            in = getTeamModel().getTeamMembers().get(i);
            name = in.getName();
            regno = in.getRegNo();
            outp.setName(name);
            outp.setRegNo(regno);
            toAdapter.add(outp);
        }
        mListAdapter = new ListAdapter(toAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mListAdapter);
    }

    private TeamModel getTeamModel() {
        return ((MyTeamActivity) getActivity()).teamModel;
    }
    private UserModel getUserModel() {
        return ((MyTeamActivity) getActivity()).userModel;
    }

    public class ListAdapter extends RecyclerView.Adapter<CurrentTeamFragment.ListAdapter.ViewHolder> {
        private List<UserModel> dataList;


        public ListAdapter(List<UserModel> data) {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameHolder;
            TextView regHolder;
            public ViewHolder(View itemView) {
                super(itemView);
                this.nameHolder.findViewById(R.id.MemberNameText);
                this.regHolder.findViewById(R.id.MemberRgNo);
            }
        }

        public CurrentTeamFragment.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_statement_card, parent, false);
            CurrentTeamFragment.ListAdapter.ViewHolder viewHolder = new CurrentTeamFragment.ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        public void onBindViewHolder(CurrentTeamFragment.ListAdapter.ViewHolder holder, final int position) {
            holder.nameHolder.setText(dataList.get(position).getName());
            holder.regHolder.setText(dataList.get(position).getRegNo());
        }

        public int getItemCount() {
            return dataList.size();
        }


    }
}