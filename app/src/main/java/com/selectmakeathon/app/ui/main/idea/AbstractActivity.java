package com.selectmakeathon.app.ui.main.idea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.selectmakeathon.app.BuildConfig;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.AbstractModel;
import com.selectmakeathon.app.util.Constants;

public class AbstractActivity extends AppCompatActivity {

    private TextView abstractSubmitButton;

    private LinearLayout layoutAbstractEdit;
    private TextInputLayout inputAbstract;
    private TextInputLayout inputUniqueness;
    private TextView buttonAttachment;
    private RecyclerView rvComponents;
    private TextView buttonAddComponent;

    private LinearLayout layoutAbstractStatic;
    private TextView textAbstract;
    private TextView textUniqueness;
    private TextView textAttachment;
    private RecyclerView rvComponentsStatic;

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private boolean isEditModeOn = true;
    private boolean isExternalEnabled;
    private boolean isInternalEnabled;
    private boolean isExternal;

    private AbstractModel abstractModel = new AbstractModel();
    private String teamId;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract);

        initViews();

        initRemoteConfig();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        teamId = prefs.getString(Constants.PREF_TEAM_ID, "teamname");

        reference.child("teams").child(teamId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    /*TODO: Get team model and extract abstract model from it*/
                    abstractModel = dataSnapshot.child("abstract").getValue(AbstractModel.class);
                    isExternal = false; /*TODO: This value if only for testing purpose*/
                    updateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        isEditModeOn = !prefs.getBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, false);
        updateUI();


//        AbstractModel abstractModel = new AbstractModel();
//        FirebaseDatabase.getInstance().getReference().child("teams").child("teamname").child("abstract").setValue(abstractModel);


        abstractSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEditModeOn) {

                    if ((isExternal && isExternalEnabled) || (!isExternal && isInternalEnabled)) { /*TODO: Do this check first*/
                        if (isValid()) {
                            submitAbstract();
                            isEditModeOn = false;
                        }
                    } else {
                        Toast.makeText ( AbstractActivity.this, "Cannot submit. Deadline over",
                                Toast.LENGTH_SHORT).show( );
                    }

                } else {
                    updateStaticFields();
                    isEditModeOn = true;
                }

                updateUI();

            }
        });

    }

    private void updateData() {
        inputAbstract.getEditText().setText(abstractModel.getIdeaAbstract());
        inputUniqueness.getEditText().setText(abstractModel.getIdeaUniquness());

        textAbstract.setText(abstractModel.getIdeaAbstract());
        textUniqueness.setText(abstractModel.getIdeaUniquness());
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void updateStaticFields() {
        /*TODO: Update all TextViews*/
    }

    private boolean isValid() {
        return true;
    }

    private void submitAbstract() {

        abstractModel.setIdeaAbstract(inputAbstract.getEditText().getText().toString());
        abstractModel.setIdeaUniquness(inputUniqueness.getEditText().getText().toString());

        reference.child("teams").child(teamId).child("abstract").setValue(abstractModel);
        /*TODO: Update Firebase*/
        /*TODO: Update local abstractModel*/
    }

    private void initRemoteConfig() {

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder( )
                .setDeveloperModeEnabled ( BuildConfig.DEBUG )
                .build();
        firebaseRemoteConfig.setConfigSettings ( configSettings );

        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        fetchConfig();
    }

    private void fetchConfig() {

        long cacheExpiration = 3600;
        if (firebaseRemoteConfig.getInfo( ).getConfigSettings( ).isDeveloperModeEnabled( )) {
            cacheExpiration = 0;
        }

        firebaseRemoteConfig.fetch ( cacheExpiration )
                .addOnCompleteListener (this, new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task ) {
                        if ( task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched( );
                        }
                        updateConfig( );
                    }
                });

    }

    private void updateConfig() {
        isExternalEnabled = firebaseRemoteConfig.getBoolean(Constants.EXTERNAL_CONFIG_KEY);
        isInternalEnabled = firebaseRemoteConfig.getBoolean(Constants.INTERNAL_CONFIG_KEY);
    }

    private void updateUI() {
        if (isEditModeOn) {
            layoutAbstractEdit.setVisibility(View.VISIBLE);
            layoutAbstractStatic.setVisibility(View.GONE);
            abstractSubmitButton.setText("Submit");
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
        inputUniqueness = findViewById(R.id.abstract_input_uniqueness);
        buttonAttachment = findViewById(R.id.abstract_text_attachment_button);
        rvComponents = findViewById(R.id.abstract_rv_components);
        buttonAddComponent = findViewById(R.id.abstract_text_add_component);

        layoutAbstractStatic = findViewById(R.id.abstract_layout_static);
        textAbstract = findViewById(R.id.abstract_text_abstract);
        textUniqueness = findViewById(R.id.abstract_text_uniqueness);
        textAttachment = findViewById(R.id.abstract_text_attachment_static);
        rvComponentsStatic = findViewById(R.id.abstract_rv_components_static);
    }
}
