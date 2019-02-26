package com.selectmakeathon.app.ui.main.myTeam.TeamFrag;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PendingTeamFragment extends Fragment {

    TextView textEmptyListPlaceHolder;
    RecyclerView rvPendingMembers;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public PendingTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

    }

    private void initViews(View view) {
        textEmptyListPlaceHolder = view.findViewById(R.id.text_empty_pending_members_placeholder);
        rvPendingMembers = view.findViewById(R.id.rv_pending_members);
    }

    private TeamModel getTeamModel() {
        return ((MyTeamActivity)getActivity()).teamModel;
    }

    private UserModel getUserModel() {
        return ((MyTeamActivity)getActivity()).userModel;
    }
}
