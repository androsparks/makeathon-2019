package com.selectmakeathon.app.ui.main.sidenav;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.NavModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SideNavAdapter extends RecyclerView.Adapter<SideNavAdapter.SideNavViewHolder> {

    Context context;
    ArrayList<NavModel> navModels;
    SideNavListener listener;

    int selectedPosition = 0;

    public SideNavAdapter(Context context, ArrayList<NavModel> navModels, SideNavListener listener) {
        this.context = context;
        this.navModels = navModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SideNavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav, parent, false);
        return new SideNavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SideNavViewHolder holder, final int position) {

        final NavModel navModel = navModels.get(position);

        holder.navIcon.setImageResource(navModel.getIconId());
        holder.navText.setText(navModel.getText());

        if (position == selectedPosition) {

            int colRes = context.getResources().getColor(R.color.colorPrimary);

            holder.layout.setBackgroundResource(R.drawable.bg_nav_selected);
            holder.navText.setTextColor(colRes);
            ImageViewCompat.setImageTintList(holder.navIcon, ColorStateList.valueOf(colRes));

        } else {

            int colRes = Color.WHITE;

            holder.layout.setBackgroundResource(0);
            holder.navText.setTextColor(colRes);
            ImageViewCompat.setImageTintList(holder.navIcon, ColorStateList.valueOf(colRes));

        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNavItemSelected(navModel.getNavItem(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return navModels.size();
    }

    public void update(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public static class SideNavViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        ImageView navIcon;
        TextView navText;

        public SideNavViewHolder(@NonNull View itemView) {

            super(itemView);

            navIcon = itemView.findViewById(R.id.nav_item_icon);
            navText = itemView.findViewById(R.id.nav_item_text);
            layout = itemView.findViewById(R.id.nav_item_layout);

        }

    }

}
