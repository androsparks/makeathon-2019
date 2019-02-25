package com.selectmakeathon.app.ui.main.rules;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selectmakeathon.app.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class RulesFragment extends androidx.fragment.app.Fragment {


    public RulesFragment() {
        // Required empty public constructor
    }

    public static RulesFragment newInstance() {
        return new RulesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules, container, false);
    }
}
