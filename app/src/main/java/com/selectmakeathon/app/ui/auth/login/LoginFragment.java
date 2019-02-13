package com.selectmakeathon.app.ui.auth.login;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.util.Constants;
import com.selectmakeathon.app.util.HashUtil;

import static com.selectmakeathon.app.util.FormUtil.isEmpty;

public class LoginFragment extends Fragment {

    private TextInputLayout userNameInput;
    private TextInputLayout passwordInput;

    private Button loginButton;
    private Button signupButton;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefEditor = prefs.edit();

        initViews(view);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).updateFragment(AuthActivity.AuthFragment.OTP);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {

                    final String userName = userNameInput.getEditText().getText().toString().trim().toUpperCase();
                    String password = passwordInput.getEditText().getText().toString();

                    final String passwordHash = HashUtil.get_SHA_512_SecurePassword(password);

                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(userName)) {
                                if (dataSnapshot.child(userName).child("hashPassword")
                                        .getValue(String.class).equals(passwordHash)) {
                                    prefEditor.putString(Constants.PREF_USER_ID, userName).apply();
                                    showToast("Successfully logged in");
                                    ((AuthActivity)getActivity()).startMainActivity();
                                } else {
                                    showToast("Incorrect password");
                                }
                            } else {
                                showToast("Username does not exist");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValid() {

        boolean valid = true;

        if (isEmpty(userNameInput)) {
            userNameInput.setError("Enter a valid registration number");
            valid = false;
        } else {
            userNameInput.setError(null);
        }

        if (isEmpty(passwordInput)) {
            passwordInput.setError("Enter a valid password");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        return valid;
    }

    private void initViews(View view) {

        userNameInput = view.findViewById(R.id.login_input_regno);
        passwordInput = view.findViewById(R.id.login_input_password);

        loginButton = view.findViewById(R.id.auth_button_login);
        signupButton = view.findViewById(R.id.auth_button_signup);
    }
}
