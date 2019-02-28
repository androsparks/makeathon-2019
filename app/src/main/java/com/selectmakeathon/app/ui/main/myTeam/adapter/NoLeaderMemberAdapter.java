package com.selectmakeathon.app.ui.main.myTeam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoLeaderMemberAdapter extends RecyclerView.Adapter<NoLeaderMemberAdapter.NoLeaderMemberViewHolder> {

    List<UserModel> noLeaderMemberList = new ArrayList<>();

    public NoLeaderMemberAdapter(List<UserModel> noLeaderMemberList) {
        if (noLeaderMemberList != null) {
            this.noLeaderMemberList = noLeaderMemberList;
        }
    }

    @NonNull
    @Override
    public NoLeaderMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_no_leader_member, parent, false);
        return new NoLeaderMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoLeaderMemberViewHolder holder, int position) {
        UserModel currentUser = noLeaderMemberList.get(position);
        holder.noLeaderMemberName.setText(currentUser.getName());
        holder.noLeaderMemberReg.setText(currentUser.getRegNo());
    }

    @Override
    public int getItemCount() {
        if (noLeaderMemberList == null) return 0;
        return noLeaderMemberList.size();
    }

    class NoLeaderMemberViewHolder extends RecyclerView.ViewHolder {

        TextView noLeaderMemberName, noLeaderMemberReg;

        public NoLeaderMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            noLeaderMemberName = itemView.findViewById(R.id.tv_no_leader_member_name);
            noLeaderMemberReg = itemView.findViewById(R.id.tv_no_leader_member_reg);
        }
    }

}
