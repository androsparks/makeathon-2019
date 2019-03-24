package com.selectmakeathon.app.ui.main.aboutteam;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.DevModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AboutTeamAdapter extends RecyclerView.Adapter<AboutTeamAdapter.AboutTeamViewHolder> {

    ArrayList<DevModel> devModels = new ArrayList<>();
    Context context;

    public AboutTeamAdapter(ArrayList<DevModel> devModels, Context context) {
        this.devModels = devModels;
        this.context = context;
    }

    @NonNull
    @Override
    public AboutTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dev_info, parent, false);
        return new AboutTeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutTeamViewHolder holder, int position) {

        final DevModel devModel = devModels.get(position);

        holder.textViewName.setText(devModel.getName());
        holder.textViewDesig.setText(devModel.getDesig());
        holder.imageViewLogo.setImageResource(devModel.getIconId());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl(devModel.getUrl());
            }
        });

    }

    private void openUrl(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    @Override
    public int getItemCount() {
        return devModels.size();
    }

    public class AboutTeamViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewDesig;
        ImageView imageViewLogo;
        CardView cardView;

        public AboutTeamViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_dev_name);
            textViewDesig = itemView.findViewById(R.id.text_dev_desig);
            imageViewLogo = itemView.findViewById(R.id.image_dev_icon);
            cardView = itemView.findViewById(R.id.card_dev_info);
        }
    }

}
