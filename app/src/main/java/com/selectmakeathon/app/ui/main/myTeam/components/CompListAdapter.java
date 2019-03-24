package com.selectmakeathon.app.ui.main.myTeam.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.Component;
import com.selectmakeathon.app.model.ComponentRequestModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompListAdapter extends RecyclerView.Adapter<CompListAdapter.ViewHolder>{
    private ArrayList<ComponentRequestModel> mComponentRequestModelArrayList=new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompListAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_req_final, parent, false));
    }

    public CompListAdapter(ArrayList<ComponentRequestModel> componentRequestModelArrayList) {
        mComponentRequestModelArrayList = componentRequestModelArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(mComponentRequestModelArrayList.get(position).getStatus()==ComponentRequestModel.ComponentRequestStatus.PENDING)
        {
            holder.componentStatus.setText("PENDING");
        }
        else
        {
            holder.componentStatus.setText("APPROVED");
        }

        holder.componentCount.setText(Integer.toString(mComponentRequestModelArrayList.get(position).getCount()));
        holder.componentName.setText(mComponentRequestModelArrayList.get(position).getComponentName());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView componentName;
        TextView componentCount;
        TextView componentStatus;

        ViewHolder(View itemView) {
            super(itemView);
            componentName = (TextView) itemView.findViewById(R.id.finame);
            componentCount=(TextView)itemView.findViewById(R.id.ficount);
            componentStatus=(TextView)itemView.findViewById(R.id.fistatus);
        }
    }
}
