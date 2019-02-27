package com.selectmakeathon.app.ui.main.myTeam.TeamFrag;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;
import com.selectmakeathon.app.ui.main.myTeam.PendingMemberListener;
import com.selectmakeathon.app.ui.main.myTeam.adapter.PendingMembersAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PendingTeamFragment extends Fragment implements PendingMemberListener {

    ConstraintLayout placeholderLayout;
    RecyclerView rvPendingMembers;
    PendingMembersAdapter adapter;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userReference;

    public PendingTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        return inflater.inflate(R.layout.fragment_pending_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initRv();

        updateUI();

    }

    private void updateUI() {

        if (getTeamModel().getMemberRequests() == null
                || getTeamModel().getMemberRequests().isEmpty()
                || getTeamModel().getMemberRequests().size() == 0) {
            placeholderLayout.setVisibility(View.VISIBLE);
            rvPendingMembers.setVisibility(View.GONE);
        } else {
            placeholderLayout.setVisibility(View.GONE);
            rvPendingMembers.setVisibility(View.VISIBLE);
        }

        adapter.setPendingUsers(getTeamModel().getMemberRequests());

    }

    private void initRv() {
        adapter = new PendingMembersAdapter(this);

        rvPendingMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendingMembers.setAdapter(adapter);
    }

    private void initViews(View view) {
        placeholderLayout = view.findViewById(R.id.layout_pending_placeholder);
        rvPendingMembers = view.findViewById(R.id.rv_pending_members);
    }

    private TeamModel getTeamModel() {
        return ((MyTeamActivity)getActivity()).teamModel;
    }

    private UserModel getUserModel() {
        return ((MyTeamActivity)getActivity()).userModel;
    }

    @Override
    public void onAcceptUser(final UserModel userModel) {

        if (getTeamModel().getTeamMembers().size() < 5) {

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel user = dataSnapshot.child(userModel.getRegNo()).getValue(UserModel.class);
                    if (user.isJoined() || user.isLeader()){
                        Toast.makeText(getContext(), "User is already part of another team!", Toast.LENGTH_SHORT).show();
                        //TODO : Remove the user from pending requests
                    } else {
                        userModel.setJoined(true);
                        userModel.setTeamName(getTeamModel().getTeamId());


                        getTeamModel().getMemberRequests().remove(userModel);
                        getTeamModel().getTeamMembers().add(userModel);


                        reference.child("teams").child(getTeamModel().getTeamId()).setValue(getTeamModel());
                        reference.child("users").child(userModel.getRegNo()).setValue(userModel);

                        updateUI();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(getContext(), "Cannot add more members", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRejectUser(UserModel userModel) {
        getTeamModel().getMemberRequests().remove(userModel);
        reference.child("teams").child(getTeamModel().getTeamId()).setValue(getTeamModel());
        updateUI();
    }
}
