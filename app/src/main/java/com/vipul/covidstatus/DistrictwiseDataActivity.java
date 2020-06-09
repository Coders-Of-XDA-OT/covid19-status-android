package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NAME;

public class DistrictwiseDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districtwise_data);

        Intent intent = getIntent();
        String stateName = intent.getStringExtra(STATE_NAME);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setTitle(stateName);

    }
}