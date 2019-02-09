package com.selectmakeathon.app.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.util.Constants;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 500;

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                if (prefs.getBoolean(Constants.PREF_IS_LOGGED_IN, true)) {
                    intent = new Intent(SplashActivity.this, AuthActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }

                TaskStackBuilder.create(SplashActivity.this)
                        .addNextIntentWithParentStack(intent)
                        .startActivities();
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
