package com.selectmakeathon.app.ui.main.idea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.Component;
import com.selectmakeathon.app.ui.main.idea.helper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ComponentAdapter
        extends RecyclerView.Adapter<ComponentAdapter.ComponentsViewHolder>
        implements ItemTouchHelperAdapter {

    ArrayList<Component> components = new ArrayList<>();

    public void setComponents(ArrayList<Component> components) {
        if (components != null) {
            this.components = components;
            notifyDataSetChanged();
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {

        if (components.size() < 15) {

            boolean isPresent = false;
            for (Component c : components) {
                if (c.getName().equals(component.getName())) {
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent) {
                components.add(component);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(components, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(components, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
//        notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        components.remove(position);
//        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComponentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_component, parent, false);
        return new ComponentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ComponentsViewHolder holder, int position) {

        final Component component = components.get(position);

        holder.componentName.setText(component.getName());
        holder.componentCount.setText(String.valueOf(component.getCount()));

        holder.countDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (component.getCount() > 1) {
                    component.decrementCount();
                    holder.componentCount.setText(String.valueOf(component.getCount()));
                }
            }
        });

        holder.countInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (component.getCount() < 10) {
                    component.incrementCount();
                    holder.componentCount.setText(String.valueOf(component.getCount()));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    class ComponentsViewHolder extends RecyclerView.ViewHolder {

        TextView componentName;
        TextView componentCount;
        ImageView countInc;
        ImageView countDec;

        public ComponentsViewHolder(@NonNull View itemView) {
            super(itemView);

            componentName = itemView.findViewById(R.id.text_component_name);
            componentCount = itemView.findViewById(R.id.text_component_count);
            countInc = itemView.findViewById(R.id.button_component_increment);
            countDec = itemView.findViewById(R.id.button_component_decrement);

        }
    }
}
