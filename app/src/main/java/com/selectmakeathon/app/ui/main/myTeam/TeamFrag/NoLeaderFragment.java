package com.selectmakeathon.app.ui.main.myTeam.TeamFrag;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;
import com.selectmakeathon.app.ui.main.myTeam.adapter.NoLeaderMemberAdapter;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoLeaderFragment extends Fragment {

    RecyclerView noLeaderMemberRecyclerView;

    public NoLeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_no_leader, container, false);
        noLeaderMemberRecyclerView = view.findViewById(R.id.rv_no_leader_members);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Use this
        List<UserModel> registeredMembers = getTeamModel().getTeamMembers();
        NoLeaderMemberAdapter adapter = new NoLeaderMemberAdapter(registeredMembers);
        noLeaderMemberRecyclerView.setAdapter(adapter);
    }

    private TeamModel getTeamModel() {
        return ((MyTeamActivity)getActivity()).teamModel;
    }

    private UserModel getUserModel() {
        return ((MyTeamActivity)getActivity()).userModel;
    }

}
