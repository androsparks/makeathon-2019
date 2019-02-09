package com.selectmakeathon.app.ui.auth.otp;

import android.content.Context;
import android.net.Uri;
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


public class OtpFragment extends Fragment {

    private Button otpNextButton;

    public OtpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OtpFragment newInstance() {
        return new OtpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        otpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).updateFragment(AuthActivity.AuthFragment.SIGNUP);
            }
        });
    }

    private void initViews(View view) {
        otpNextButton = view.findViewById(R.id.auth_button_otp_next);
    }
}
