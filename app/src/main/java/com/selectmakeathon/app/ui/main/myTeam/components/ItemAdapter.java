package com.selectmakeathon.app.ui.main.myTeam.components;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<String> displaynow;
    public List<ComponentRequestModel> list=new ArrayList<>();
    private int count = 0;
    private int i = 0;
    private String teamId;
    TeamModel teamModel;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public ItemAdapter(ArrayList<String> displaynow, String teamId) {
        this.displaynow = displaynow;
        this.teamId = teamId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        reference.child("teams").child(teamId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    teamModel = dataSnapshot.getValue(TeamModel.class);
                    list=teamModel.getComponentRequests(); //TODO: Use this

                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String cname = displaynow.get(position);
        final ComponentRequestModel c=new ComponentRequestModel();
        holder.componentName.setText(cname);

        holder.addC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.custom);

                final TextView dialogC = (TextView) dialog.findViewById(R.id.AddCName);
                final TextView dialogN = (TextView) dialog.findViewById(R.id.AddCCount);
                final ImageView increment = (ImageView) dialog.findViewById(R.id.inc);
                final ImageView decrement = (ImageView) dialog.findViewById(R.id.decr);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonAddNow);

                dialogC.setText(displaynow.get(position));
                c.setComponentName(displaynow.get(position));

                increment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (count >= 0 && count <= 10) {
                                count++;
                                System.out.println(count);
                                dialogN.setText(Integer.toString(count));
                                c.setCount(Integer.parseInt(dialogN.getText().toString()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                decrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (count > 0) {
                                count--;
                                System.out.println(count);
                                dialogN.setText(Integer.toString(count));
                                c.setCount(Integer.parseInt(dialogN.getText().toString()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                c.setCount(count);


                // dialogbutton should add data to Firebase but fails to do so
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isPresent = false;
                        for (ComponentRequestModel a : list) {
                            if (a.getComponentName().equals(c.getComponentName())) {
                                isPresent = true;
                                Toast.makeText(holder.itemView.getContext(), "Component Already Requested", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                        if (!isPresent) {
                            teamModel.getComponentRequests().add(c);
                            reference.child("teams").child(teamId).setValue(teamModel);
                            list.add(c);
                        }
                        dialog.dismiss();
                    }
                });
                count = 0;
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return displaynow.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView componentName;
        ImageView addC;
        String item;

        ViewHolder(View itemView) {
            super(itemView);
            componentName = (TextView) itemView.findViewById(R.id.ComponentName_text);
            addC = (ImageView) itemView.findViewById(R.id.addComp);

        }
    }
}
