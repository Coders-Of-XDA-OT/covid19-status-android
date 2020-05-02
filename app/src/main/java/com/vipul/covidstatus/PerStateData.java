package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import static com.vipul.covidstatus.StatewiseDataActivity.STATE_CONFIRMED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NAME;

public class PerStateData extends AppCompatActivity {

    TextView perStateName,perStateConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_state_data);

        Intent intent = getIntent();
        String stateName = intent.getStringExtra(STATE_NAME);
        String stateConfirmed = intent.getStringExtra(STATE_CONFIRMED);

        perStateName = findViewById(R.id.perstate_name_textview);
        perStateConfirmed = findViewById(R.id.perstate_confirmed_textview);

        perStateName.setText(stateName);
        perStateConfirmed.setText(stateConfirmed);
    }
}
