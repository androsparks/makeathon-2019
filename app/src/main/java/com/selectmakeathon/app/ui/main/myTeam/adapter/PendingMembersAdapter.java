package com.selectmakeathon.app.ui.main.myTeam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.myTeam.PendingMemberListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PendingMembersAdapter extends RecyclerView.Adapter<PendingMembersAdapter.PendingMemberViewHolder> {

    PendingMemberListener listener;
    List<UserModel> pendingUsers = new ArrayList<>();

    public PendingMembersAdapter(PendingMemberListener listener) {
        this.listener = listener;
    }

    public void setPendingUsers(List<UserModel> pendingUsers) {
        if (pendingUsers != null) {
            this.pendingUsers = pendingUsers;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PendingMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_request, parent, false);
        return new PendingMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingMemberViewHolder holder, final int position) {

        final UserModel userModel = pendingUsers.get(position);

        if (userModel.isJoined()) {
            try {
                pendingUsers.remove(position);
//            notifyItemRemoved(position);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.userName.setText(userModel.getName());
        holder.userId.setText(userModel.getRegNo());

        holder.imageAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAcceptUser(userModel);
            }
        });

        holder.imageReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRejectUser(userModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingUsers.size();
    }

    class PendingMemberViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userId;
        ImageView imageAccept;
        ImageView imageReject;

        public PendingMemberViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.NewMemberText);
            userId = itemView.findViewById(R.id.NewMemberRegText);
            imageAccept = itemView.findViewById(R.id.image_pending_member_accept);
            imageReject = itemView.findViewById(R.id.image_pending_member_reject);
        }
    }

}
