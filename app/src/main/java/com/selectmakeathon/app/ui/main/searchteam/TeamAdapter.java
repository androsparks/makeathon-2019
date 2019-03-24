package com.selectmakeathon.app.ui.main.searchteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.TeamModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    OnTeamSelectListener listener;
    ArrayList<TeamModel> teamModels = new ArrayList<>();

    public TeamAdapter(OnTeamSelectListener listener) {
        this.listener = listener;
    }

    public void setTeamModels(ArrayList<TeamModel> teamModels) {
        this.teamModels = teamModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team_info, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, final int position) {

        TeamModel teamModel = teamModels.get(position);

        holder.teamName.setText(teamModel.getTeamName());
        holder.leaderName.setText(teamModel.getTeamLeader().getName());
        holder.memberCount.setText(
                String.valueOf(teamModel.getTeamMembers().size())
        );

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTeamSelect(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamModels.size();
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {

        public TextView teamName;
        public TextView leaderName;
        public TextView memberCount;
        public CardView cardView;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            teamName = itemView.findViewById(R.id.item_team_teamname);
            leaderName = itemView.findViewById(R.id.item_team_leadername);
            memberCount = itemView.findViewById(R.id.item_team_member_count);
            cardView = itemView.findViewById(R.id.item_team_card);

        }
    }

}
