package com.selectmakeathon.app.ui.main.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import cn.iwgang.countdownview.CountdownView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.ui.main.info.InfoActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private Button logoutButton;
    private TextView infoButton;
    private ImageView navIcon;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        startCountdown(view);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).startAuthActivity();
            }
        });

        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openSideNav();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openSideNav();
            }
        });
    }

    private void initViews(View view) {
        logoutButton = view.findViewById(R.id.main_button_logout);
        infoButton = view.findViewById(R.id.main_button_info);
        navIcon = view.findViewById(R.id.main_nav_icon);
    }

    private void startCountdown(View view) {
        try {
            String dateStop = "03/25/2019 17:30:00";
            Date dtstart = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateStart = dateFormat.format(dtstart);
            Date d1 = null;
            Date d2 = null;
            d1 = frmt.parse(dateStart);
            d2 = frmt.parse(dateStop);
            long diff = d2.getTime() - d1.getTime();

            CountdownView countdownView1 = (CountdownView) view.findViewById(R.id.countdownView);

            countdownView1.setTag("Lets Begin !");

            countdownView1.start(diff);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
