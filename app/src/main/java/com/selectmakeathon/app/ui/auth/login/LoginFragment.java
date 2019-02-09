package com.selectmakeathon.app.ui.auth.login;


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
import com.selectmakeathon.app.ui.main.MainActivity;

public class LoginFragment extends Fragment {

    private Button loginButton;
    private Button signupButton;

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

        initViews(view);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).startMainActivity();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).updateFragment(AuthActivity.AuthFragment.OTP);
            }
        });
    }

    private void initViews(View view) {
        loginButton = view.findViewById(R.id.auth_button_login);
        signupButton = view.findViewById(R.id.auth_button_signup);
    }
}
