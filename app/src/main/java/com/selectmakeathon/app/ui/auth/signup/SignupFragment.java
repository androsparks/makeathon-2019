package com.selectmakeathon.app.ui.auth.signup;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.AuthActivity;

public class SignupFragment extends Fragment {

    private Button registerButton;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).startMainActivity();
            }
        });
    }

    private void initViews(View view) {
        registerButton = view.findViewById(R.id.auth_button_register);
    }
}
