package com.selectmakeathon.app.ui.auth.otp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.util.Constants;

import java.util.concurrent.TimeUnit;


public class OtpFragment extends Fragment {

    private static final String TAG = "OTP-Fragment";
    private FirebaseAuth mAuth;
    String mVerificationId, phoneNumber, otpCode;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private Button nextButton, confirmButton;
    private TextView phoneNumberTextLabel, otpTextLabel;
    private PinView otpPinView;
    private EditText phoneNumberEditText;
    private View phoneNumberEditTextLayout;

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

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
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        nextButton = view.findViewById(R.id.auth_button_otp_next);
        confirmButton = view.findViewById(R.id.btn_confirm_otp);
        otpPinView = view.findViewById(R.id.pv_otp);
        phoneNumberEditText = view.findViewById(R.id.et_phone_number);
        phoneNumberTextLabel = view.findViewById(R.id.tv_phone_label);
        otpTextLabel = view.findViewById(R.id.tv_otp_label);
        phoneNumberEditTextLayout = view.findViewById(R.id.el_phone_number);
        mAuth = FirebaseAuth.getInstance();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = phoneNumberEditText.getText().toString();
                phoneNumber = "+91" + phoneNumber;
                verifyPhoneNnumberWithOtp(phoneNumber);
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
        ((AuthActivity)getActivity()).updateFragment(AuthActivity.AuthFragment.SIGNUP);
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

    void verifyPhoneNnumberWithOtp(String phoneNumber){

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
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);
                        Toast.makeText(getContext(), "Please enter correct Phone Number", Toast.LENGTH_SHORT).show();

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            // ...
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            // ...
                        }

                        // Show a message and update the UI
                        // ...
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
        ((AuthActivity)getActivity()).updateFragment(AuthActivity.AuthFragment.SIGNUP);
    }

}
