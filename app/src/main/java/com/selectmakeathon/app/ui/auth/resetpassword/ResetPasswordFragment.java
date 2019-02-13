package com.selectmakeathon.app.ui.auth.resetpassword;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.selectmakeathon.app.R;

import androidx.fragment.app.Fragment;

public class ResetPasswordFragment extends Fragment {

    private static final String ARG_PHONENUM = "PHONE_NUMBER";

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

    public void updateFirebase() {
        /*TODO: update firebase*/
    }

}
