package com.selectmakeathon.app.ui.main.myTeam.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.Component;
import com.selectmakeathon.app.model.ComponentRequestModel;
import com.selectmakeathon.app.model.TeamModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompListAdapter extends RecyclerView.Adapter<CompListAdapter.ViewHolder>{
    private List<ComponentRequestModel> mComponentRequestModelArrayList=new ArrayList<>();
    private String teamId="";
    TeamModel teamModel;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    public CompListAdapter(List<ComponentRequestModel> componentRequestModelArrayList, String teamId) {
        mComponentRequestModelArrayList = componentRequestModelArrayList;
        this.teamId = teamId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CompListAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_req_final, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(mComponentRequestModelArrayList.get(position).getStatus()==ComponentRequestModel.ComponentRequestStatus.PENDING)
        {
            holder.componentStatus.setText("PENDING");
        }
        else if(mComponentRequestModelArrayList.get(position).getStatus()==ComponentRequestModel.ComponentRequestStatus.APPROVED)
        {

        }

        holder.componentCount.setText(Integer.toString(mComponentRequestModelArrayList.get(position).getCount()));
        holder.componentName.setText(mComponentRequestModelArrayList.get(position).getComponentName());

    }

    @Override
    public int getItemCount() {
        return mComponentRequestModelArrayList.size();
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
