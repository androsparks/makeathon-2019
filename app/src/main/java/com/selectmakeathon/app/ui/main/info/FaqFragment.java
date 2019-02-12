package com.selectmakeathon.app.ui.main.info;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selectmakeathon.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaqFragment extends androidx.fragment.app.Fragment {


    public FaqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

}
