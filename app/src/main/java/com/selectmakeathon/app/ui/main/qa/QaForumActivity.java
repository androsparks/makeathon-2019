package com.selectmakeathon.app.ui.main.qa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.QaModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QaForumActivity extends AppCompatActivity {

    FloatingActionButton fab;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userReference = reference.child("users");
    DatabaseReference qaReference = reference.child("qa_forum");
    UserModel userModel;
    static SendQaInterface sendQaInterface;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    RecyclerView qaRecyclerView;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_forum);

        initViews();

        String userId = sharedPreferences.getString(Constants.PREF_USER_ID, null);
        fetchUser(userId);

        sendQaInterface = new SendQaInterface() {
            @Override
            public void sendQuestion(String question) {
                addQa(question);
                bottomSheetDialogFragment.dismiss();
            }
        };

        qaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<QaModel> qaModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QaModel model = snapshot.getValue(QaModel.class);
                    qaModelList.add(model);
                }
                setAdapter(qaModelList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = new QaBottomSheet();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Qa");
            }
        });

    }

    private void initViews() {
        fab = findViewById(R.id.fab_add_question);
        qaRecyclerView = findViewById(R.id.rv_qa_forum);
    }

    private void fetchUser(String userId) {
        if (userId != null) {
            userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel = dataSnapshot.getValue(UserModel.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void addQa(String question){
        String id = reference.child("qa_forum").push().getKey();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
        String time = simpleDateFormat.format(date);
        QaModel qaModel = new QaModel(id, userModel.getTeamName(), userModel.getName(), time, question, "Please wait while we answer your question");
        qaReference.child(qaModel.getId()).setValue(qaModel);
    }

    private void setAdapter(List<QaModel> qaModels){
        QaAdapter adapter = new QaAdapter(qaModels);
        qaRecyclerView.setAdapter(adapter);
    }

    public static SendQaInterface getSendQaInterface() {
        return sendQaInterface;
    }

    public static void setSendQaInterface(SendQaInterface sendQaInterface) {
        QaForumActivity.sendQaInterface = sendQaInterface;
    }
}
