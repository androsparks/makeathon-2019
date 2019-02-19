package com.selectmakeathon.app.ui.main.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.selectmakeathon.app.R;

public class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract);
    }
}
