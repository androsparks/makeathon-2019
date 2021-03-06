package com.selectmakeathon.app.ui.main.idea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.selectmakeathon.app.BuildConfig;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.AbstractModel;
import com.selectmakeathon.app.model.Component;
import com.selectmakeathon.app.ui.main.idea.bottomsheet.ComponentsBottomSheetFragment;
import com.selectmakeathon.app.ui.main.idea.helper.SimpleItemTouchHelperCallback;
import com.selectmakeathon.app.util.Constants;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AbstractActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    private static final String TAG = "AbstractActivity";

    private View loadingContainer;
    private LottieAnimationView loadingAnimation;
    private ConstraintLayout containerAbstract;

    private TextView abstractSubmitButton;
    private TextView textProbId;
    private TextView textDeadline;
    private ImageView backButton;

    private LinearLayout layoutAbstractEdit;
    private TextInputLayout inputAbstract;
    private TextInputLayout inputUniqueness;
    private TextInputLayout inputUseCases;
    private TextView textAttachmentError;
    private TextView buttonAttachment;
    private ImageView imageAttachment;
    private TextView textComponentsError;
    private RecyclerView rvComponents;
    private TextView buttonAddComponent;
    private TextInputLayout inputOtherComponents;

    private LinearLayout layoutAbstractStatic;
    private TextView textAbstract;
    private TextView textUniqueness;
    private TextView textUseCases;
    private TextView textAttachment;
    private ImageView imageAttachmentStatic;
    private RecyclerView rvComponentsStatic;
    private TextView textOtherComponentsTitleStatic;
    private TextView textOtherComponentsBodyStatic;

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private boolean isEditModeOn = true;
    private boolean isExternalEnabled;
    private boolean isInternalEnabled;
    private boolean isExternal;
    private String deadline;

    private AbstractModel abstractModel;
    private String teamId;
    private ArrayList<String> components = new ArrayList<>();
    private Uri filePath;
    private String problemId;

    private ComponentAdapter componentAdapter;
    private StaticComponentsAdapter staticComponentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract);

        initViews();
        startAnimation();
        initRv();

        initRemoteConfig();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        isEditModeOn = !prefs.getBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, false);

        problemId = getIntent().getStringExtra("probId");
        teamId = getIntent().getStringExtra("TEAM_ID");
        isExternal = getIntent().getBooleanExtra("IS_EXTERNAL", false);

        if (problemId != null) {
            reference
                    .child("teams")
                    .child(teamId)
                    .child("abstract")
                    .child("problemStatementId")
                    .setValue(problemId);
        }

        textProbId.setText(problemId);

        reference.child("teams").child(teamId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    abstractModel = dataSnapshot.child("abstract").getValue(AbstractModel.class);
                    if (abstractModel == null) {
                        abstractModel = new AbstractModel();
                        problemId = abstractModel.getProblemStatementId();
                    }
                    updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("components").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    components.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        components.add(childSnapshot.getValue(String.class));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        updateUI();


//        AbstractModel abstractModel = new AbstractModel();
//        FirebaseDatabase.getInstance().getReference().child("teams").child("teamname").child("abstract").setValue(abstractModel);


        abstractSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetAvailable(AbstractActivity.this)) {

                    if ((isExternal && isExternalEnabled) || (!isExternal && isInternalEnabled)) {

                        if (isEditModeOn) {
                            if (isValid()) {
                                uploadImage();
                            }
                        } else {
                            isEditModeOn = true;
                            updateUI();
                        }

                    } else {
                        showToast("Crossed deadline");
                    }

                } else {
                    showToast("Please check your internet connection");
                }

            }
        });

        buttonAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        buttonAddComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentsBottomSheetFragment componentsBottomSheetFragment
                        = ComponentsBottomSheetFragment.newInstance(components);
                componentsBottomSheetFragment.show(
                        getSupportFragmentManager(),
                        componentsBottomSheetFragment.getTag()
                );
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void uploadImage() {

        if (filePath != null) {

            startAnimation();

            storageRef.child(teamId)
                    .putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageRef.child(teamId).getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            abstractModel.setAttachmentUrl(uri.toString());

                                            submitAbstract();
                                            isEditModeOn = false;

//                                            updateUI();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("File not uploaded");
                            updateUI();
                        }
                    });
        } else {
            submitAbstract();
            isEditModeOn = false;
        }
    }

    private void initRv() {
        componentAdapter = new ComponentAdapter();
        rvComponents.setHasFixedSize(true);
        rvComponents.setLayoutManager(new LinearLayoutManager(this));
        rvComponents.setAdapter(componentAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(componentAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvComponents);

        staticComponentsAdapter = new StaticComponentsAdapter();
        rvComponentsStatic.setHasFixedSize(true);
        rvComponentsStatic.setLayoutManager(new LinearLayoutManager(this));
        rvComponentsStatic.setAdapter(staticComponentsAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            Cursor returnCursor = getContentResolver().query(filePath, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
            long size = returnCursor.getLong(sizeIndex);

            if (size > 2000000) {
                filePath = null;
                imageAttachment.setVisibility(View.VISIBLE);
                showToast("Image size should be less than 2MB");
            } else {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    imageAttachment.setVisibility(View.VISIBLE);
                    imageAttachment.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateData() {

        if (abstractModel != null) {

            textProbId.setText(abstractModel.getProblemStatementId());

            inputAbstract.getEditText().setText(abstractModel.getIdeaAbstract());
            inputUniqueness.getEditText().setText(abstractModel.getIdeaUniquness());
            inputUseCases.getEditText().setText(abstractModel.getIdeaUseCases());
            componentAdapter.setComponents(abstractModel.getComponents());
            inputOtherComponents.getEditText().setText(abstractModel.getExtraComponents());

            if (abstractModel.getAttachmentUrl() != null) {
                imageAttachment.setVisibility(View.VISIBLE);
                Picasso.get().load(abstractModel.getAttachmentUrl()).into(imageAttachment);
            }

            textAbstract.setText(abstractModel.getIdeaAbstract());
            textUniqueness.setText(abstractModel.getIdeaUniquness());
            textUseCases.setText(abstractModel.getIdeaUseCases());
            staticComponentsAdapter.setComponents(abstractModel.getComponents());

            textOtherComponentsTitleStatic.setVisibility(View.GONE);
            textOtherComponentsBodyStatic.setVisibility(View.GONE);
            if (abstractModel.getExtraComponents() != null) {
                if (!abstractModel.getExtraComponents().isEmpty()) {
                    textOtherComponentsTitleStatic.setVisibility(View.VISIBLE);
                    textOtherComponentsBodyStatic.setVisibility(View.VISIBLE);
                    textOtherComponentsBodyStatic.setText(abstractModel.getExtraComponents());
                }
            }

            Picasso.get().load(abstractModel.getAttachmentUrl()).into(imageAttachmentStatic);

        }

        stopAnimation();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isValid() {

        boolean isValid = true;

        String abstractText = inputAbstract.getEditText().getText().toString();
        int lengthAbstractText = abstractText.split(" ").length;
        if (abstractText.isEmpty()) {
            isValid = false;
            inputAbstract.setError("Cannot be empty");
        } else if (lengthAbstractText < 150 || lengthAbstractText > 200) {
            isValid = false;
            inputAbstract.setError("" + lengthAbstractText + " words - Not in specified range");
        } else {
            inputAbstract.setError(null);
        }

        String uniqueText = inputUniqueness.getEditText().getText().toString();
        int lengthUnique = uniqueText.split(" ").length;
        if (uniqueText.isEmpty()) {
            isValid = false;
            inputUniqueness.setError("Cannot be empty");
        } else if (lengthUnique < 50 || lengthUnique > 60) {
            isValid = false;
            inputUniqueness.setError("" + lengthUnique + " words - Not in specified range");
        } else {
            inputUniqueness.setError(null);
        }

        String useCasesText = inputUseCases.getEditText().getText().toString();
        int lengthUseCases = useCasesText.split(" ").length;
        if (useCasesText.isEmpty()) {
            isValid = false;
            inputUseCases.setError("Cannot be empty");
        } else if (lengthUseCases < 80 || lengthUseCases > 100) {
            isValid = false;
            inputUseCases.setError("" + lengthUseCases + " words - Not in specified range");
        } else {
            inputUseCases.setError(null);
        }

        if (abstractModel.getAttachmentUrl() == null && filePath == null) {
            isValid = false;
            textAttachmentError.setVisibility(View.VISIBLE);
        } else {
            textAttachmentError.setVisibility(View.GONE);
        }

        if (componentAdapter.getItemCount() == 0) {
            isValid = false;
            textComponentsError.setVisibility(View.VISIBLE);
        } else {
            textComponentsError.setVisibility(View.GONE);
        }

        return isValid;
    }

    private void submitAbstract() {

        abstractModel.setIdeaAbstract(inputAbstract.getEditText().getText().toString());
        abstractModel.setIdeaUniquness(inputUniqueness.getEditText().getText().toString());
        abstractModel.setIdeaUseCases(inputUseCases.getEditText().getText().toString());
        abstractModel.setComponents(componentAdapter.getComponents());
        abstractModel.setExtraComponents(inputOtherComponents.getEditText().getText().toString());

        reference.child("teams").child(teamId).child("abstract").setValue(abstractModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isEditModeOn = false;
                prefEditor.putBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, true).apply();
                updateUI();
            }
        });

//        stopAnimation();
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

        if (isExternal) {
            deadline = firebaseRemoteConfig.getString(Constants.EXTERNAL_DATE_CONFIG_KEY);
        } else {
            deadline = firebaseRemoteConfig.getString(Constants.INTERNAL_DATE_CONFIG_KEY);
        }

        textDeadline.setText("Deadline - " + deadline);
    }

    private void updateUI() {

        stopAnimation();

        updateData();

        if (isEditModeOn) {
            layoutAbstractEdit.setVisibility(View.VISIBLE);
            layoutAbstractStatic.setVisibility(View.GONE);
            abstractSubmitButton.setText("Submit");
        } else {
            layoutAbstractEdit.setVisibility(View.GONE);
            layoutAbstractStatic.setVisibility(View.VISIBLE);
            abstractSubmitButton.setText("Edit");
        }

//        stopAnimation();
    }

    private void initViews() {

        backButton = findViewById(R.id.activity_abstract_back_button);

        loadingContainer = findViewById(R.id.loading_animation_container_abstract);
        loadingAnimation = findViewById(R.id.lottie_loading_animation_abstract);
        containerAbstract = findViewById(R.id.abstract_container);

        abstractSubmitButton = findViewById(R.id.abstract_button_submit);
        textProbId = findViewById(R.id.text_abstract_probid);
        textDeadline = findViewById(R.id.text_abstract_deadline);

        layoutAbstractEdit = findViewById(R.id.abstract_layout_edit);
        inputAbstract = findViewById(R.id.abstract_input_abstract);
        inputUniqueness = findViewById(R.id.abstract_input_uniqueness);
        inputUseCases = findViewById(R.id.abstract_input_usercases);
        textAttachmentError = findViewById(R.id.text_error_abstract_attachment);
        buttonAttachment = findViewById(R.id.abstract_text_attachment_button);
        imageAttachment = findViewById(R.id.abstract_image_attachment);
        textComponentsError = findViewById(R.id.text_error_abstract_components);
        rvComponents = findViewById(R.id.abstract_rv_components);
        buttonAddComponent = findViewById(R.id.abstract_text_add_component);
        inputOtherComponents = findViewById(R.id.abstract_input_other_components);

        layoutAbstractStatic = findViewById(R.id.abstract_layout_static);
        textAbstract = findViewById(R.id.abstract_text_abstract);
        textUniqueness = findViewById(R.id.abstract_text_uniqueness);
        textUseCases = findViewById(R.id.abstract_text_usecases);
        textAttachment = findViewById(R.id.abstract_text_attachment_static);
        imageAttachmentStatic = findViewById(R.id.abstract_image_static);
        rvComponentsStatic = findViewById(R.id.abstract_rv_components_static);
        textOtherComponentsTitleStatic = findViewById(R.id.text_extra_components_title_static);
        textOtherComponentsBodyStatic = findViewById(R.id.text_extra_components_body_static);

    }

    public void onComponentSelected(String component) {

        componentAdapter.addComponent(new Component(component, 1));

    }

    public void startAnimation(){
        loadingContainer.setVisibility(View.VISIBLE);
        containerAbstract.setVisibility(View.GONE);
        loadingAnimation.playAnimation();
    }

    public void stopAnimation(){
        containerAbstract.setVisibility(View.VISIBLE);
        loadingContainer.setVisibility(View.GONE);
        loadingAnimation.pauseAnimation();
    }

    public boolean isInternetAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }
}
