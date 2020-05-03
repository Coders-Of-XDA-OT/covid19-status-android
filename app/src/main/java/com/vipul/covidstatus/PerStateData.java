package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import static com.vipul.covidstatus.StatewiseDataActivity.STATE_CONFIRMED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NAME;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_ACTIVE;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_RECOVERED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_DECEASED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NEW_CONFIRMED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NEW_RECOVERED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NEW_DECEASED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_LAST_UPDATE;

public class PerStateData extends AppCompatActivity {

    TextView perStateName,perStateConfirmed, perStateActive, perStateRecovered, perStateDeceased, perStateNewConfirmed, perStateNewRecovered, perStateNewDeceased, perStateUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_state_data);

        Intent intent = getIntent();
        String stateName = intent.getStringExtra(STATE_NAME);
        String stateConfirmed = intent.getStringExtra(STATE_CONFIRMED);
        String stateActive = intent.getStringExtra(STATE_ACTIVE);
        String stateRecovered = intent.getStringExtra(STATE_RECOVERED);
        String stateDeceased = intent.getStringExtra(STATE_DECEASED);
        String stateNewConfirmed = intent.getStringExtra(STATE_NEW_CONFIRMED);
        String stateNewRecovered = intent.getStringExtra(STATE_NEW_RECOVERED);
        String stateNewDeceased = intent.getStringExtra(STATE_NEW_DECEASED);
        String stateLastUpdate = intent.getStringExtra(STATE_LAST_UPDATE);

        perStateName = findViewById(R.id.perstate_name_textview);
        perStateConfirmed = findViewById(R.id.perstate_confirmed_textview);
        perStateActive = findViewById(R.id.perstate_active_textview);
        perStateRecovered = findViewById(R.id.perstate_recovered_textview);
        perStateDeceased = findViewById(R.id.perstate_deceased_textview);
        perStateUpdate = findViewById(R.id.perstate_lastupdate);

        perStateName.setText(stateName);
        perStateConfirmed.setText(stateConfirmed);
        perStateActive.setText(stateActive);
        perStateRecovered.setText(stateRecovered);
        perStateDeceased.setText(stateDeceased);
        perStateUpdate.setText(stateLastUpdate);
    }
}
