package com.selectmakeathon.app.ui.auth.signup;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.service.quicksettings.Tile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.auth.AuthActivity;

public class SignupFragment extends Fragment {

    private TextInputLayout nameInput;
    private TextInputLayout emailId;
    private TextInputLayout regNo;
    private TextInputLayout password;
    private TextInputLayout confirmPassword;
    private TextInputLayout whatsappNum;
    private ChipGroup isVitian;
    private LinearLayout layoutVitianError;
    private TextInputLayout collegeNameInput;
    private ChipGroup isHosteler;
    private LinearLayout layoutHostelerError;
    private TextInputLayout blockInput;
    private TextInputLayout roomNoInput;
    private ChipGroup gender;
    private LinearLayout layoutGenderError;
    private TextInputLayout skillsetInput;

    private Button registerButton;

    private UserModel userModel;

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
        nameInput = view.findViewById(R.id.input_name);
        emailId = view.findViewById(R.id.input_email);
        regNo = view.findViewById(R.id.input_regno);
        password = view.findViewById(R.id.input_password);
        confirmPassword = view.findViewById(R.id.input_password_confirm);
        whatsappNum = view.findViewById(R.id.input_whatsapp_number);
        isVitian = view.findViewById(R.id.chip_group_vitian);
        collegeNameInput = view.findViewById(R.id.input_college_name);
        isHosteler = view.findViewById(R.id.chip_group_hosteller);
        blockInput = view.findViewById(R.id.input_hostel_block);
        roomNoInput = view.findViewById(R.id.input_room_no);
        gender = view.findViewById(R.id.chip_group_gender);
        skillsetInput = view.findViewById(R.id.input_skillset);
        layoutVitianError = view.findViewById(R.id.layout_error_vitian);
        layoutHostelerError = view.findViewById(R.id.layout_error_hosteler);
        layoutGenderError = view.findViewById(R.id.layout_error_gender);

        registerButton = view.findViewById(R.id.auth_button_register);
    }
}
