package com.selectmakeathon.app.ui.main.problems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
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
        Intent intent=new Intent(ProblemActivity.this, AbstractActivity.class);
        intent.putExtra("probId",Id);
        startActivity(intent);
        finish();
    }


}
