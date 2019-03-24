package com.selectmakeathon.app.ui.main.idea.bottomsheet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.main.idea.bottomsheet.util.OnComponentSelectedListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ComponentsBottomSheetAdapter extends RecyclerView.Adapter<ComponentsBottomSheetAdapter.ComponentsBottomViewHolder> {

    ArrayList<String> components;
    OnComponentSelectedListener listener;

    public ComponentsBottomSheetAdapter(ArrayList<String> components, OnComponentSelectedListener listener) {
        this.components = components;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComponentsBottomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_component_bottomsheet, parent, false);
        return new ComponentsBottomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComponentsBottomViewHolder holder, final int position) {

        holder.textView.setText(components.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelected(components.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    class ComponentsBottomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ComponentsBottomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_component_bottomsheet);
        }
    }

}
