package com.selectmakeathon.app.ui.main.qa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.QaModel;

public class QaForumActivity extends AppCompatActivity {

    FloatingActionButton fab;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_forum);

        initViews();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = reference.child("qa_forum").push().getKey();
                QaModel qaModel = new QaModel();
                qaModel.setId(id);
                qaModel.setQuestion("Some sample question");

                reference.child("qa_forum").child(id).setValue(qaModel);
            }
        });

    }

    private void initViews() {
        fab = findViewById(R.id.fab_add_question);
    }
}
