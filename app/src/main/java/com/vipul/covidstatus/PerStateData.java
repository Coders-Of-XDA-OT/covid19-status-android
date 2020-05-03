package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

import static com.vipul.covidstatus.StatewiseDataActivity.STATE_CONFIRMED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NAME;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_ACTIVE;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_DECEASED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NEW_CONFIRMED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NEW_RECOVERED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_NEW_DECEASED;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_LAST_UPDATE;
import static com.vipul.covidstatus.StatewiseDataActivity.STATE_RECOVERED;

public class PerStateData extends AppCompatActivity {

    TextView perStateConfirmed, perStateActive, perStateDeceased, perStateNewConfirmed, perStateNewRecovered, perStateNewDeceased, perStateUpdate, perStateRecovered, perstateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_state_data);

        Intent intent = getIntent();
        String stateName = intent.getStringExtra(STATE_NAME);
        String stateConfirmed = intent.getStringExtra(STATE_CONFIRMED);
        String stateActive = intent.getStringExtra(STATE_ACTIVE);
        String stateDeceased = intent.getStringExtra(STATE_DECEASED);
        String stateNewConfirmed = intent.getStringExtra(STATE_NEW_CONFIRMED);
        String stateNewRecovered = intent.getStringExtra(STATE_NEW_RECOVERED);
        String stateNewDeceased = intent.getStringExtra(STATE_NEW_DECEASED);
        String stateLastUpdate = intent.getStringExtra(STATE_LAST_UPDATE);
        String stateRecovery = intent.getStringExtra(STATE_RECOVERED);

        Objects.requireNonNull(getSupportActionBar()).hide();
        perStateConfirmed = findViewById(R.id.perstate_confirmed_textview);
        perStateActive = findViewById(R.id.perstate_active_textView);
        perStateRecovered = findViewById(R.id.perstate_recovered_textView);
        perStateDeceased = findViewById(R.id.perstate_death_textView);
        perStateUpdate = findViewById(R.id.perstate_lastupdate_textView);
        perStateNewConfirmed = findViewById(R.id.perstate_confirmed_new_textView);
        perStateNewRecovered = findViewById(R.id.perstate_recovered_new_textView);
        perStateNewDeceased = findViewById(R.id.perstate_death_new_textView);
        perstateName = findViewById(R.id.state_name_textview);


        perStateConfirmed.append(stateConfirmed);
        perStateActive.append(stateActive);
        perStateDeceased.append(stateDeceased);
        perStateUpdate.append(stateLastUpdate);
        perStateNewConfirmed.append("[+"+stateNewConfirmed+"]");
        perStateNewRecovered.append("[+"+stateNewRecovered+"]");
        perStateNewDeceased.append("[+"+stateNewDeceased+"]");
        perStateRecovered.append(stateRecovery);
        perstateName.append(stateName);
    }
}
