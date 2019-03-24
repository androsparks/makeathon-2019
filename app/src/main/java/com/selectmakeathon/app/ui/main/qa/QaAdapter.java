package com.selectmakeathon.app.ui.main.qa;

import android.view.View;
import android.view.ViewGroup;

import com.selectmakeathon.app.model.QaModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QaAdapter extends RecyclerView.Adapter<QaAdapter.QaViewHolder> {

    List<QaModel> qaModelList;

    public QaAdapter(List<QaModel> qaModelList) {
        this.qaModelList = qaModelList;
    }

    @NonNull
    @Override
    public QaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull QaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return qaModelList.size();
    }

    public class QaViewHolder extends RecyclerView.ViewHolder {

        public QaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
