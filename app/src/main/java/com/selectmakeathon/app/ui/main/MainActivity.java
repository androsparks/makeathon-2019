package com.selectmakeathon.app.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

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

        CountdownView countdownView1 = (CountdownView) findViewById(R.id.countdownView);
        countdownView1.setTag("Lets Begin !");
        long timer = (long) 5 * 60 * 60 * 1000;
        countdownView1.start(timer);

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
