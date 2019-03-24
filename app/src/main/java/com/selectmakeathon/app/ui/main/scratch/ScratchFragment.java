package com.selectmakeathon.app.ui.main.scratch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.selectmakeathon.app.BuildConfig;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.util.Constants;
import com.selectmakeathon.app.util.customview.ScratchTextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class ScratchFragment extends Fragment {

    private static final String ARG_USER_ID = "USER_ID";
    private String userId;
    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private boolean scratchEnabled = false;
    private boolean isScratched;
    private String uniqueKey;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;

    private ImageView navIcon;
    private ConstraintLayout placeHolderLayout;
    private LinearLayout containerLayout;
    private ConstraintLayout beforeLayout;
    private ConstraintLayout afterLayout;
    private ScratchTextView scratchTextView;
    private TextView textView;

    public ScratchFragment() {
        // Required empty public constructor
    }

    public static ScratchFragment newInstance(String userId) {
        ScratchFragment fragment = new ScratchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scratch, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefEditor = prefs.edit();

        initViews(view);
        setUniqueKey();

        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openSideNav();
            }
        });

        return view;
    }

    private void initViews(View view) {
        navIcon = view.findViewById(R.id.fragment_scratch_nav_icon);
        placeHolderLayout = view.findViewById(R.id.layout_scratch_placeholder);
        containerLayout = view.findViewById(R.id.layout_scratch_container);
        beforeLayout = view.findViewById(R.id.layout_scratch_before);
        afterLayout = view.findViewById(R.id.layout_scratch_after);
        scratchTextView = view.findViewById(R.id.scratchView);
        textView = view.findViewById(R.id.text_scratch_unique_key);

        scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
            @Override
            public void onRevealed(ScratchTextView scratchTextView) {

            }

            @Override
            public void onRevealPercentChangedListener(ScratchTextView scratchTextView, float v) {
                if (v > 0.8) {
                    prefEditor.putBoolean(Constants.PREF_IS_SCRATCHED, true).apply();
                }
            }
        });
    }

    private void setUniqueKey() {

        startLoading();

        uniqueKey = getUserModel().getScratchUniqueKey();
        if (uniqueKey == null) {
            String key = reference.child("scratch_keys").push().getKey();
            uniqueKey = key.substring(key.length() - 8, key.length());
            getUserModel().setScratchUniqueKey(uniqueKey);
            reference
                    .child("users")
                    .child(getUserModel().getRegNo())
                    .setValue(getUserModel());
            reference
                    .child("scratch_keys")
                    .child(uniqueKey)
                    .setValue(getUserModel().getRegNo());
        }

        initRemoteConfig();
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
                .addOnCompleteListener (getActivity(), new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task ) {
                        if ( task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched( );
                        }
                        updateConfig();
                    }
                });

    }

    private void updateConfig() {

        stopLoading();

        scratchEnabled = firebaseRemoteConfig.getBoolean(Constants.SCRATCH_ENABLED_CONFIG_KEY);
        isScratched = prefs.getBoolean(Constants.PREF_IS_SCRATCHED, false);

        updateUI();
    }

    private void updateUI() {

        scratchTextView.setText(uniqueKey);
        textView.setText(uniqueKey);

        if (scratchEnabled) {
            showLayout(containerLayout);
            hideLayout(placeHolderLayout);

            if (isScratched) {
                showLayout(afterLayout);
                hideLayout(beforeLayout);
            } else {
                showLayout(beforeLayout);
                hideLayout(afterLayout);
            }

        } else {
            showLayout(placeHolderLayout);
            hideLayout(containerLayout);
        }
    }

    private void hideLayout(View view) {
        view.setVisibility(View.GONE);
    }

    private void showLayout(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private UserModel getUserModel() {
        return ((MainActivity)getActivity()).userModel;
    }

    private void startLoading() {
        ((MainActivity)getActivity()).startAnimation();
    }

    private void stopLoading() {
        ((MainActivity)getActivity()).stopAnimation();
    }
}
