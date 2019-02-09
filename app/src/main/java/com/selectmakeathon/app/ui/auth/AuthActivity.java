package com.selectmakeathon.app.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.login.LoginFragment;
import com.selectmakeathon.app.ui.auth.otp.OtpFragment;
import com.selectmakeathon.app.ui.auth.signup.SignupFragment;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.util.Constants;

import static com.selectmakeathon.app.ui.auth.AuthActivity.AuthFragment.*;

public class AuthActivity extends AppCompatActivity {

    public enum AuthFragment {
        LOGIN,
        OTP,
        SIGNUP
    }

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        updateFragment(LOGIN);
    }

    public void updateFragment(AuthFragment authFragment) {
        Fragment fragment;

        switch (authFragment) {
            case LOGIN :
                fragment = LoginFragment.newInstance();
                break;
            case OTP :
                fragment = OtpFragment.newInstance();
                break;
            case SIGNUP :
                fragment = SignupFragment.newInstance();
                break;
            default:
                fragment = LoginFragment.newInstance();
                break;
        }

        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container_auth, fragment)
                    .addToBackStack(backStateName)
                    .commit();
        }
    }

    public void startMainActivity() {
        prefEditor.putBoolean(Constants.PREF_IS_FIRST_TIME, false).apply();

        TaskStackBuilder.create(AuthActivity.this)
                .addNextIntentWithParentStack(new Intent(AuthActivity.this, MainActivity.class))
                .startActivities();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}
