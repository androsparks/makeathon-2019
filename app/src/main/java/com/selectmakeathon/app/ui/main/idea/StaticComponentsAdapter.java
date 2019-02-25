package com.selectmakeathon.app.ui.main.idea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.Component;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StaticComponentsAdapter
        extends RecyclerView.Adapter<StaticComponentsAdapter.StaticComponentsViewHolder> {

    ArrayList<Component> components = new ArrayList<>();

    public void setComponents(ArrayList<Component> components) {
        if (components != null) {
            this.components = components;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public StaticComponentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_component_static, parent, false);
        return new StaticComponentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaticComponentsViewHolder holder, int position) {
        final Component component = components.get(position);

        holder.componentName.setText(component.getName());
        holder.componentCount.setText(String.valueOf(component.getCount()));

    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    class StaticComponentsViewHolder extends RecyclerView.ViewHolder {

        TextView componentName;
        TextView componentCount;

        public StaticComponentsViewHolder(@NonNull View itemView) {
            super(itemView);

            componentName = itemView.findViewById(R.id.text_component_static_name);
            componentCount = itemView.findViewById(R.id.text_component_static_count);

        }
    }

}
