package com.selectmakeathon.app.ui.main.problems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.Problems;
import com.selectmakeathon.app.ui.main.idea.AbstractActivity;
import com.selectmakeathon.app.ui.main.problems.ProbFragmentPack.ProbHomeFrag;

import java.util.ArrayList;
import java.util.List;

public class ProblemActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    public boolean toContinue;
    public String teamId;
    public boolean isExternal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        toContinue = getIntent().getBooleanExtra("CONTINUE", false);
        teamId = getIntent().getStringExtra("TEAM_ID");
        isExternal = getIntent().getBooleanExtra("IS_EXTERNAL", false);

        ImageView backButton = findViewById(R.id.problems_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final FragmentManager manager=getSupportFragmentManager();
        ProbHomeFrag fragment=new ProbHomeFrag();
        manager.beginTransaction()
                .replace(R.id.fragholder,fragment)
                .commit();

        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference();

    }

    public void sendToAbstract(String Id)
    {
        if (toContinue) {
            Intent intent = new Intent(ProblemActivity.this, AbstractActivity.class);
            intent.putExtra("probId", Id);
            intent.putExtra("TEAM_ID", teamId);
            intent.putExtra("IS_EXTERNAL", isExternal);
            startActivity(intent);
            finish();
        }
    }


}
