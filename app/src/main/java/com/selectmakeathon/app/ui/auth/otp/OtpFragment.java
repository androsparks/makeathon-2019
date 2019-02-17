package com.selectmakeathon.app.ui.auth.otp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.VerifiedPhoneNumberModel;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.ui.auth.login.LoginFragment;
import com.selectmakeathon.app.ui.auth.resetpassword.ResetPasswordFragment;
import com.selectmakeathon.app.ui.auth.signup.SignupFragment;
import com.selectmakeathon.app.util.Constants;

import java.util.concurrent.TimeUnit;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.selectmakeathon.app.util.FormUtil.isEmpty;
import static com.selectmakeathon.app.util.FormUtil.phoneValid;


public class OtpFragment extends Fragment {

    private static final String ARG_IS_RESET_PASSWORD = "IS_RESET_PASSWORD";

    private static final String TAG = "OTP-Fragment";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference verifiedPhoneNumberRef = database.getReference("verified");
    String mVerificationId, phoneNumber, otpCode;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private Button nextButton, confirmButton;
    private TextView phoneNumberTextLabel, otpTextLabel;
    private PinView otpPinView;
    private EditText phoneNumberEditText;
    private TextInputLayout phoneNumberEditTextLayout;

    private ImageView backButton;

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    boolean isResetPassword;

    public OtpFragment() {
        // Required empty public constructor
    }

    public static OtpFragment newInstance(boolean isResetPassword) {
        OtpFragment fragment = new OtpFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_RESET_PASSWORD, isResetPassword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isResetPassword = getArguments().getBoolean(ARG_IS_RESET_PASSWORD) ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        nextButton = view.findViewById(R.id.auth_button_otp_next);
        confirmButton = view.findViewById(R.id.btn_confirm_otp);
        otpPinView = view.findViewById(R.id.pv_otp);
        phoneNumberEditText = view.findViewById(R.id.et_phone_number);
        phoneNumberTextLabel = view.findViewById(R.id.tv_phone_label);
        otpTextLabel = view.findViewById(R.id.tv_otp_label);
        phoneNumberEditTextLayout = view.findViewById(R.id.el_phone_number);
        backButton = view.findViewById(R.id.otp_button_back);

        mAuth = FirebaseAuth.getInstance();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmpty(phoneNumberEditTextLayout) || !phoneValid(phoneNumberEditTextLayout)) {

                    phoneNumberEditTextLayout.setError("Enter a valid phone number");

                } else {

                    phoneNumberEditTextLayout.setError(null);

                    hideSoftKeyBoard();
                    phoneNumber = phoneNumberEditText.getText().toString();
                    phoneNumber = "+91" + phoneNumber;

                    AuthActivity.startAnimation();

                    if (isResetPassword) {
                        verifyPhoneNumberWithOtp(phoneNumber);
                    } else {
                        verifyIfPhoneNumberExists(phoneNumber);
                    }
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).updateFragment(LoginFragment.newInstance());
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    otpCode = otpPinView.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
                    signInWithPhoneAuthCredential(credential);
                } catch (NullPointerException e){
                    Toast.makeText(getContext(), "Please enter a Valid OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefEditor = prefs.edit();

        return view;
    }

    void goToSignUp(){
        ((AuthActivity)getActivity()).updateFragment(SignupFragment.newInstance());
    }

    void goToResetPassword() {
        ((AuthActivity)getActivity()).updateFragment(ResetPasswordFragment.newInstance(phoneNumber));
    }

    void switchToOtp(){
        nextButton.setVisibility(View.GONE);
        phoneNumberTextLabel.setVisibility(View.GONE);
        phoneNumberEditText.setVisibility(View.GONE);
        phoneNumberEditTextLayout.setVisibility(View.GONE);

        confirmButton.setVisibility(View.VISIBLE);
        otpTextLabel.setVisibility(View.VISIBLE);
        otpPinView.setVisibility(View.VISIBLE);
    }

    void verifyPhoneNumberWithOtp(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),      // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        AuthActivity.stopAnimation();
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);
                        AuthActivity.stopAnimation();

                        //e.toString() produced long toast message
//                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "An error occurred \nCheck your number", Toast.LENGTH_SHORT).show();

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {

                        } else if (e instanceof FirebaseTooManyRequestsException) {

                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);

                        Toast.makeText(getActivity(), "Code Sent, Waiting for Verification", Toast.LENGTH_SHORT).show();
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:" + verificationId);
                        AuthActivity.stopAnimation();
                        switchToOtp();

                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;
                        mResendToken = token;

                        // ...
                    }
                });     // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            proceedWithLogin();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getContext(), "The code doesn't match.\nPlease check the code again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    void proceedWithLogin(){
        //Perform next sequence of actions
        prefEditor.putString(Constants.PREF_PHONE_NUMBER, phoneNumber).commit();
        Toast.makeText(getContext(), "Successfully Authenticated", Toast.LENGTH_SHORT).show();
        if (isResetPassword) {
            goToResetPassword();
        } else {
            goToSignUp();
        }
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    void verifyIfPhoneNumberExists(final String number){
        verifiedPhoneNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VerifiedPhoneNumberModel verifiedPhoneNumber =
                        dataSnapshot.getValue(VerifiedPhoneNumberModel.class);
                if (verifiedPhoneNumber != null && verifiedPhoneNumber.getVerifiedPhoneNumbers().contains(number)){
                    AuthActivity.stopAnimation();
                    Toast.makeText(getContext(), "Number already Registered.\nPlease go to Login",
                            Toast.LENGTH_LONG).show();
                } else {
                    verifyPhoneNumberWithOtp(number);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AuthActivity.stopAnimation();
            }
        });
    }

}
