package com.selectmakeathon.app.ui.main.idea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.AbstractModel;

public class AbstractActivity extends AppCompatActivity {

    private TextView abstractSubmitButton;

    private LinearLayout layoutAbstractEdit;
    private TextInputLayout inputAbstract;

    private LinearLayout layoutAbstractStatic;

    private boolean isEditModeOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract);

        initViews();

        updateUI();


//        AbstractModel abstractModel = new AbstractModel();
//        FirebaseDatabase.getInstance().getReference().child("teams").child("teamname").child("abstract").setValue(abstractModel);



        abstractSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditModeOn = !isEditModeOn;
                updateUI();

            }
        });

    }

    private void updateUI() {
        if (isEditModeOn) {
            layoutAbstractEdit.setVisibility(View.VISIBLE);
            layoutAbstractStatic.setVisibility(View.GONE);
            abstractSubmitButton.setText("Save");
        } else {
            layoutAbstractEdit.setVisibility(View.GONE);
            layoutAbstractStatic.setVisibility(View.VISIBLE);
            abstractSubmitButton.setText("Edit");
        }
    }

    private void initViews() {
        abstractSubmitButton = findViewById(R.id.abstract_button_submit);

        layoutAbstractEdit = findViewById(R.id.abstract_layout_edit);
        inputAbstract = findViewById(R.id.abstract_input_abstract);

        layoutAbstractStatic = findViewById(R.id.abstract_layout_static);
    }
}
