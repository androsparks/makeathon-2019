package com.selectmakeathon.app.ui.main.myTeam.TeamFrag;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;
import com.selectmakeathon.app.ui.main.myTeam.adapter.NoLeaderMemberAdapter;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoLeaderFragment extends androidx.fragment.app.Fragment {

    RecyclerView noLeaderMemberRecyclerView;
    Button leaveGroup;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public NoLeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_no_leader, container, false);
        noLeaderMemberRecyclerView = view.findViewById(R.id.rv_no_leader_members);
        leaveGroup = view.findViewById(R.id.button_leave_group);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<UserModel> registeredMembers = getTeamModel().getTeamMembers();
        NoLeaderMemberAdapter adapter = new NoLeaderMemberAdapter(registeredMembers);
        noLeaderMemberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noLeaderMemberRecyclerView.setAdapter(adapter);


        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeUser(getUserModel());
            }
        });
    }

    private void removeUser(UserModel userModel) {

        TeamModel teamModel = getTeamModel();

        for (int i=0; i<teamModel.getTeamMembers().size(); i++) {
            if (teamModel.getTeamMembers().get(i).getRegNo().matches(userModel.getRegNo())) {
                teamModel.getTeamMembers().remove(i);
                break;
            }
        }

        userModel.setJoined(false);
        userModel.setTeamName("");

        reference.child("teams").child(getTeamModel().getTeamId()).setValue(getTeamModel());
        reference.child("users").child(userModel.getRegNo()).setValue(userModel);

        ((MyTeamActivity)getActivity()).finish();
    }

    private TeamModel getTeamModel() {
        return ((MyTeamActivity)getActivity()).teamModel;
    }

    private UserModel getUserModel() {
        return ((MyTeamActivity)getActivity()).userModel;
    }

}
