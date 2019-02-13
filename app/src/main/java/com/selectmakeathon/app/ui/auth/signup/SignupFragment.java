package com.selectmakeathon.app.ui.auth.signup;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.util.Constants;
import com.selectmakeathon.app.util.FormUtil;
import com.selectmakeathon.app.util.HashUtil;

import java.util.regex.Pattern;

import static com.selectmakeathon.app.util.FormUtil.emailValid;
import static com.selectmakeathon.app.util.FormUtil.isEmpty;
import static com.selectmakeathon.app.util.FormUtil.phoneValid;

public class SignupFragment extends Fragment {

    private static String REGNO_PATTERN = "^[0-9]{2}[A-Z]{3}[0-9]{4}$";

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
    private TextInputLayout branchInput;
    private ChipGroup gender;
    private LinearLayout layoutGenderError;
    private TextInputLayout skillsetInput;
    private LinearLayout layoutVitianYes;
    private LinearLayout layoutHostelerYes;

    private ImageView backButton;

    private boolean isVitianSelected = false;
    private boolean isHostelerSelected = false;
    private boolean isGenderSelected = false;

    private String phoneNumber = "";

    private Button registerButton;

    private UserModel userModel = new UserModel();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;

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

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefEditor = prefs.edit();


        initViews(view);

        phoneNumber = prefs.getString(Constants.PREF_PHONE_NUMBER, "");

        whatsappNum.getEditText().setText(phoneNumber);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).updateFragment(AuthActivity.AuthFragment.OTP);
            }
        });

        isVitian.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                isVitianSelected = true;

                if (checkedId == R.id.chip_vitian_yes) {
                    userModel.setVitian(true);
                    collegeNameInput.setVisibility(View.GONE);
                    layoutVitianYes.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.chip_vitian_no) {
                    userModel.setVitian(false);
                    collegeNameInput.setVisibility(View.VISIBLE);
                    layoutVitianYes.setVisibility(View.GONE);
                }
            }
        });

        isHosteler.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                isHostelerSelected = true;

                if (checkedId == R.id.chip_hostel_yes) {
                    userModel.setHosteler(true);
                    layoutHostelerYes.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.chip_hostel_no) {
                    userModel.setHosteler(false);
                    layoutHostelerYes.setVisibility(View.GONE);
                }
            }
        });

        gender.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                isGenderSelected = true;

                if (checkedId == R.id.chip_gender_male) {
                    userModel.setGender("Male");
                } else if (checkedId == R.id.chip_gender_female) {
                    userModel.setGender("Female");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {

                    userModel.setName(nameInput.getEditText().getText().toString());
                    userModel.setEmail(emailId.getEditText().getText().toString());
                    userModel.setRegNo(regNo.getEditText().getText().toString().trim().toUpperCase());

                    String hashPassword = HashUtil.get_SHA_512_SecurePassword(password.getEditText().getText().toString());
                    userModel.setHashPassword(hashPassword);

                    userModel.setWhatsNo(whatsappNum.getEditText().getText().toString());
                    userModel.setPhNo(phoneNumber);
                    userModel.setCollegeName(collegeNameInput.getEditText().getText().toString());
                    userModel.setHostelBlock(blockInput.getEditText().getText().toString());
                    userModel.setRoomNo(roomNoInput.getEditText().getText().toString());
                    userModel.setBranch(branchInput.getEditText().getText().toString());
                    userModel.setSkillSet(skillsetInput.getEditText().getText().toString());

                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(userModel.getRegNo())) {
                                Toast.makeText(getContext(), "User already exits", Toast.LENGTH_SHORT).show();
                            } else {
                                confirmRegNo();
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

    private void confirmRegNo() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm registration number")
                .setMessage(userModel.getRegNo())
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        register();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void register() {
        prefEditor.putString(Constants.PREF_USER_ID, userModel.getRegNo()).apply();
        usersRef.child(userModel.getRegNo()).setValue(userModel);
        Toast.makeText(getContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
        ((AuthActivity) getActivity()).startMainActivity();
    }

    private boolean isValid() {

        boolean valid = true;

        if (isEmpty(nameInput)) {
            nameInput.setError("Enter a valid name");
            valid = false;
        } else {
            nameInput.setError(null);
        }

        if (!emailValid(emailId)) {
            emailId.setError("Enter a valid email");
            valid = false;
        } else {
            emailId.setError(null);
        }

        if (isEmpty(regNo)) {
            regNo.setError("Enter a valid registration number");
            valid = false;
        } else {
            regNo.setError(null);

            if (isVitianSelected && userModel.isVitian()) {
                if (!Pattern.matches(REGNO_PATTERN, regNo.getEditText().getText().toString())) {
                    regNo.setError("Enter a valid VIT registration number");
                    valid = false;
                } else {
                    regNo.setError(null);
                }
            }
        }

        if (isEmpty(password)) {
            password.setError("Enter a valid password");
            valid = false;
        } else {
            password.setError(null);
        }

        if (isEmpty(confirmPassword)) {
            confirmPassword.setError("Enter a valid registration number");
            valid = false;
        } else {
            confirmPassword.setError(null);
        }

        if (!isEmpty(password) && !isEmpty(confirmPassword)) {
            boolean isPasswordMatching = password.getEditText().getText().toString()
                    .equals(confirmPassword.getEditText().getText().toString());

            if (!isPasswordMatching) {
                confirmPassword.setError("Password does not match");
                valid = false;
            } else {
                confirmPassword.setError(null);
            }

        } else {
            valid = false;
        }

        if (!phoneValid(whatsappNum)) {
            whatsappNum.setError("Enter a valid phone number");
            valid = false;
        } else  {
            whatsappNum.setError(null);
        }

        if (!isVitianSelected) {
            layoutVitianError.setVisibility(View.VISIBLE);
            valid = false;
        } else {

            layoutVitianError.setVisibility(View.GONE);

            if (!userModel.isVitian()) {

                if (isEmpty(collegeNameInput)) {
                    collegeNameInput.setError("Please enter valid college name");
                    valid = false;
                } else {
                    collegeNameInput.setError(null);
                }

            } else {

                if (!isHostelerSelected) {
                    layoutHostelerError.setVisibility(View.VISIBLE);
                    valid = false;
                } else {

                    layoutHostelerError.setVisibility(View.GONE);

                    if (userModel.isHosteler()) {

                        if (isEmpty(blockInput)) {
                            blockInput.setError("Enter valid block");
                            valid = false;
                        } else {
                            blockInput.setError(null);
                        }

                        if (isEmpty(roomNoInput)) {
                            roomNoInput.setError("Enter valid room no");
                            valid = false;
                        } else {
                            roomNoInput.setError(null);
                        }

                    }

                }

            }
        }

        if (isEmpty(branchInput)) {
            branchInput.setError("Enter a valid branch");
            valid = false;
        } else {
            branchInput.setError(null);
        }

        if (!isGenderSelected) {
            layoutGenderError.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            layoutGenderError.setVisibility(View.INVISIBLE);
        }

        if (isEmpty(skillsetInput)) {
            skillsetInput.setError("Enter a valid skillset");
            valid = false;
        } else {
            skillsetInput.setError(null);
        }


        return valid;
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
        branchInput = view.findViewById(R.id.input_branch);
        gender = view.findViewById(R.id.chip_group_gender);
        skillsetInput = view.findViewById(R.id.input_skillset);
        layoutVitianError = view.findViewById(R.id.layout_error_vitian);
        layoutHostelerError = view.findViewById(R.id.layout_error_hosteler);
        layoutGenderError = view.findViewById(R.id.layout_error_gender);
        layoutVitianYes = view.findViewById(R.id.layout_vitian_yes);
        layoutHostelerYes = view.findViewById(R.id.layout_hosteler_yes);

        backButton = view.findViewById(R.id.signup_button_back);

        registerButton = view.findViewById(R.id.auth_button_register);
    }
}
