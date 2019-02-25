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

    private LinearLayout layoutAbstractEdit;
    private TextInputLayout inputAbstract;
    private TextInputLayout inputUniqueness;
    private TextView buttonAttachment;
    private ImageView imageAttachment;
    private RecyclerView rvComponents;
    private TextView buttonAddComponent;

    private LinearLayout layoutAbstractStatic;
    private TextView textAbstract;
    private TextView textUniqueness;
    private TextView textAttachment;
    private ImageView imageAttachmentStatic;
    private RecyclerView rvComponentsStatic;

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private boolean isEditModeOn = true;
    private boolean isExternalEnabled;
    private boolean isInternalEnabled;
    private boolean isExternal;

    private AbstractModel abstractModel = new AbstractModel();
    private String teamId;
    private ArrayList<String> components = new ArrayList<>();
    private Uri filePath;

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
        teamId = prefs.getString(Constants.PREF_TEAM_ID, "teamname");

        reference.child("teams").child(teamId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    /*TODO: Get team model and extract abstract model from it*/
                    abstractModel = dataSnapshot.child("abstract").getValue(AbstractModel.class);
                    isExternal = false; /*TODO: This value is only for testing purpose*/
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
                        }

                    } else {
                        showToast("Cannot submit. Deadline over");
                    }

                } else {
                    showToast("Please check your internet connection");
                }

                updateUI();

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
            showToast("File not selected");
            updateUI();
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

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imageAttachment.setVisibility(View.VISIBLE);
                imageAttachment.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateData() {

        inputAbstract.getEditText().setText(abstractModel.getIdeaAbstract());
        inputUniqueness.getEditText().setText(abstractModel.getIdeaUniquness());
        componentAdapter.setComponents(abstractModel.getComponents());

        imageAttachment.setVisibility(View.VISIBLE);
        Picasso.get().load(abstractModel.getAttachmentUrl()).into(imageAttachment);

        textAbstract.setText(abstractModel.getIdeaAbstract());
        textUniqueness.setText(abstractModel.getIdeaUniquness());
        staticComponentsAdapter.setComponents(abstractModel.getComponents());
        Picasso.get().load(abstractModel.getAttachmentUrl()).into(imageAttachmentStatic);

        stopAnimation();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isValid() {

        boolean isValid = true;

//        String abstractText = inputAbstract.getEditText().getText().toString();
        String abstractText = "";
        int lengthAbstractText = abstractText.split(" ").length;

        return isValid;
    }

    private void submitAbstract() {

        abstractModel.setIdeaAbstract(inputAbstract.getEditText().getText().toString());
        abstractModel.setIdeaUniquness(inputUniqueness.getEditText().getText().toString());
        abstractModel.setComponents(componentAdapter.getComponents());

        reference.child("teams").child(teamId).child("abstract").setValue(abstractModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isEditModeOn = false;
                prefEditor.putBoolean(Constants.PREF_IS_ABSTRACT_SUBMITTED, true);
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

        loadingContainer = findViewById(R.id.loading_animation_container_abstract);
        loadingAnimation = findViewById(R.id.lottie_loading_animation_abstract);
        containerAbstract = findViewById(R.id.abstract_container);

        abstractSubmitButton = findViewById(R.id.abstract_button_submit);

        layoutAbstractEdit = findViewById(R.id.abstract_layout_edit);
        inputAbstract = findViewById(R.id.abstract_input_abstract);
        inputUniqueness = findViewById(R.id.abstract_input_uniqueness);
        buttonAttachment = findViewById(R.id.abstract_text_attachment_button);
        imageAttachment = findViewById(R.id.abstract_image_attachment);
        rvComponents = findViewById(R.id.abstract_rv_components);
        buttonAddComponent = findViewById(R.id.abstract_text_add_component);

        layoutAbstractStatic = findViewById(R.id.abstract_layout_static);
        textAbstract = findViewById(R.id.abstract_text_abstract);
        textUniqueness = findViewById(R.id.abstract_text_uniqueness);
        textAttachment = findViewById(R.id.abstract_text_attachment_static);
        imageAttachmentStatic = findViewById(R.id.abstract_image_static);
        rvComponentsStatic = findViewById(R.id.abstract_rv_components_static);

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
