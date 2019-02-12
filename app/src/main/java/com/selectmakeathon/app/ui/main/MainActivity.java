package com.selectmakeathon.app.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.ui.main.info.InfoActivity;
import com.selectmakeathon.app.util.Constants;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import cn.iwgang.countdownview.CountdownView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private Button logoutButton;
    private TextView infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String dateStop = "03/25/2019 18:00:00";
            Date dtstart = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateStart = dateFormat.format(dtstart);
            Date d1 = null;
            Date d2 = null;
            d1 = frmt.parse(dateStart);
            d2 = frmt.parse(dateStop);
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);


            CountdownView countdownView1 = (CountdownView) findViewById(R.id.countdownView);
            countdownView1.setTag("Lets Begin !");
            long timer = (long) diffDays * 24 * 60 * 60 * 1000;
            countdownView1.start(timer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        initViews();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAuthActivity();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(i);
            }
        });
    }

    public void startAuthActivity() {
        prefEditor.putBoolean(Constants.PREF_IS_LOGGED_IN, true).apply();

        TaskStackBuilder.create(MainActivity.this)
                .addNextIntentWithParentStack(new Intent(MainActivity.this, AuthActivity.class))
                .startActivities();
        finish();
    }

    private void initViews() {
        logoutButton = findViewById(R.id.main_button_logout);
        infoButton = findViewById(R.id.main_button_info);
    }
}
