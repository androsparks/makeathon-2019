package com.selectmakeathon.app.ui.main.qa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selectmakeathon.app.R;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_qa, parent, false);
        return new QaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QaViewHolder holder, int position) {
        QaModel qaModel = qaModelList.get(position);
        holder.qaQuestionTextView.setText(qaModel.getQuestion());
        holder.qaAnswerTextView.setText(qaModel.getAnswer());
    }

    @Override
    public int getItemCount() {
        return qaModelList.size();
    }

    public class QaViewHolder extends RecyclerView.ViewHolder {

        TextView qaQuestionTextView, qaAnswerTextView;

        public QaViewHolder(@NonNull View itemView) {
            super(itemView);
            qaQuestionTextView = itemView.findViewById(R.id.tv_qa_question);
            qaAnswerTextView = itemView.findViewById(R.id.tv_qa_answer);
        }
    }

}
