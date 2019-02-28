package com.selectmakeathon.app.ui.main.myTeam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;
import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.CurrentTeamFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class CurrentTeamAdapter extends RecyclerView.Adapter<CurrentTeamAdapter.LeaderMemberViewHolder> {

    List<UserModel> LeaderMemberList;
    Boolean mBoolean;

    public CurrentTeamAdapter(List<UserModel> leaderMemberList, Boolean aBoolean) {
        LeaderMemberList = leaderMemberList;
        mBoolean = aBoolean;
    }

    @NonNull
    @Override
    public CurrentTeamAdapter.LeaderMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_member, parent, false);
        return new CurrentTeamAdapter.LeaderMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CurrentTeamAdapter.LeaderMemberViewHolder holder, final int position) {
        UserModel currentUser = LeaderMemberList.get(position);
        holder.LeaderMemberName.setText(currentUser.getName());
        holder.LeaderMemberReg.setText(currentUser.getRegNo());

        if (mBoolean && currentUser.isLeader()) {
            holder.DeleteMember.setVisibility(View.GONE);
        } else if (mBoolean) {
            holder.DeleteMember.setVisibility(View.VISIBLE);
        } else {
            holder.DeleteMember.setVisibility(View.GONE);
        }
        holder.DeleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(position);


            }
        });

    }

    @Override
    public int getItemCount() {
        return LeaderMemberList.size();
    }

    class LeaderMemberViewHolder extends RecyclerView.ViewHolder {

        TextView LeaderMemberName, LeaderMemberReg;
        ImageView DeleteMember;

        public LeaderMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            DeleteMember = itemView.findViewById(R.id.delete_member);
            LeaderMemberName = itemView.findViewById(R.id.MemberNameText);
            LeaderMemberReg = itemView.findViewById(R.id.MemberRgNo);
        }
    }
}
