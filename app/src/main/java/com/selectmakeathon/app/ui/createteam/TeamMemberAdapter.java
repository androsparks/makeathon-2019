package com.selectmakeathon.app.ui.createteam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.UserModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder> {

    List<UserModel> memberList;

    public TeamMemberAdapter(List<UserModel> memberList) {
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_view_team_member, parent, false);
        return new TeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position) {
        UserModel member = memberList.get(position);
        holder.memberNameTextView.setText(member.getName());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    class TeamMemberViewHolder extends RecyclerView.ViewHolder{

        TextView memberNameTextView;

        TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            memberNameTextView = itemView.findViewById(R.id.tv_member_name);
        }
    }

}
