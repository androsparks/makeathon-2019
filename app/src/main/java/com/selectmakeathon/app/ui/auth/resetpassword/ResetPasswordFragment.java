package com.selectmakeathon.app.ui.auth.resetpassword;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.ui.auth.login.LoginFragment;
import com.selectmakeathon.app.util.HashUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.selectmakeathon.app.util.FormUtil.isEmpty;

public class ResetPasswordFragment extends Fragment {

    private static final String ARG_PHONENUM = "PHONE_NUMBER";

    private TextInputLayout inputRegno;
    private TextInputLayout inputPassword;
    private TextInputLayout inputConfirmPassword;

    private Button resetButton;
    private ImageView backButton;
    private TextView textPhNo;

    private String phoneNumber;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance(String phoneNumber) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONENUM, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString(ARG_PHONENUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        textPhNo.setText("Phone Number: " + phoneNumber);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogin();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {

                    final String regNo = inputRegno.getEditText().getText().toString().trim().toUpperCase();
                    final String password = inputPassword.getEditText().getText().toString();

                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(regNo)) {
                                if (dataSnapshot.child(regNo).child("phNo").getValue(String.class).equals(phoneNumber)) {
                                    String hashPassword = HashUtil.get_SHA_512_SecurePassword(password);
                                    usersRef.child(regNo).child("hashPassword").setValue(hashPassword);
                                    showToast("Password reset successfully");
                                    showLogin();
                                } else {
                                    showToast("Phone number does not exist");
                                }
                            } else {
                                showToast("Registration number does not exist");
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

    private void showLogin() {
        ((AuthActivity)getActivity()).updateFragment(LoginFragment.newInstance());
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValid() {

        boolean isValid = true;

        if (isEmpty(inputRegno)) {
            inputRegno.setError("Enter a valid registration number");
            isValid = false;
        } else {
            inputRegno.setError(null);
        }

        if (isEmpty(inputPassword)) {
            inputPassword.setError("Enter a valid password");
            isValid = false;
        } else {
            inputPassword.setError(null);
        }

        if (isEmpty(inputConfirmPassword)) {
            inputConfirmPassword.setError("Enter a valid password");
            isValid = false;
        } else {
            inputConfirmPassword.setError(null);
        }

        if (!isEmpty(inputPassword) && !isEmpty(inputConfirmPassword)) {

            boolean isPasswordMatching = inputPassword.getEditText().getText().toString()
                    .equals(inputConfirmPassword.getEditText().getText().toString());

            if (!isPasswordMatching) {
                inputConfirmPassword.setError("Password does not match");
                isValid = false;
            } else {
                inputConfirmPassword.setError(null);
            }

        } else {
            isValid = false;
        }

        return isValid;
    }

    private void initViews(View view) {
        inputRegno = view.findViewById(R.id.reset_input_regno);
        inputPassword = view.findViewById(R.id.reset_input_password);
        inputConfirmPassword = view.findViewById(R.id.reset_input_confirm_password);

        resetButton = view.findViewById(R.id.button_reset_password);
        backButton = view.findViewById(R.id.reset_button_back);
        textPhNo = view.findViewById(R.id.reset_text_phonenum);
    }

}
