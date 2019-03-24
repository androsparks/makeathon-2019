package com.selectmakeathon.app.ui.main.qa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.selectmakeathon.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class QaBottomSheet extends BottomSheetDialogFragment {

    TextInputLayout textInputLayout;
    MaterialButton materialButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_qa, container, false);
        textInputLayout = view.findViewById(R.id.el_qa);
        materialButton = view.findViewById(R.id.btn_qa_add);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = textInputLayout.getEditText().getText().toString();
                if (question != null) {
                    QaForumActivity.getSendQaInterface().sendQuestion(question);
                } else {
                    Toast.makeText(getContext(),"Question field can't be left blank", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
