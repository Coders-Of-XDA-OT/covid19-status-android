package com.vipul.covidstatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
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
    PieChart mPieChart;

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

        Objects.requireNonNull(getSupportActionBar()).setTitle(stateName);
        perStateConfirmed = findViewById(R.id.perstate_confirmed_textview);
        perStateActive = findViewById(R.id.perstate_active_textView);
        perStateRecovered = findViewById(R.id.perstate_recovered_textView);
        perStateDeceased = findViewById(R.id.perstate_death_textView);
        perStateUpdate = findViewById(R.id.perstate_lastupdate_textView);
        perStateNewConfirmed = findViewById(R.id.perstate_confirmed_new_textView);
        perStateNewRecovered = findViewById(R.id.perstate_recovered_new_textView);
        perStateNewDeceased = findViewById(R.id.perstate_death_new_textView);
        mPieChart = findViewById(R.id.piechart_perstate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String activeCopy = stateActive;
        String recoveredCopy = stateRecovery;
        String deceasedCopy = stateDeceased;

        int stateActiveInt = Integer.parseInt(stateActive);
        stateActive = NumberFormat.getInstance().format(stateActiveInt);

        int stateDeceasedInt = Integer.parseInt(stateDeceased);
        stateDeceased = NumberFormat.getInstance().format(stateDeceasedInt);

        int stateRecoveredInt = Integer.parseInt(stateRecovery);
        stateRecovery = NumberFormat.getInstance().format(stateRecoveredInt);

        //assert stateActive != null;
        mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(activeCopy), Color.parseColor("#007afe")));
        mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recoveredCopy), Color.parseColor("#08a045")));
        mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(deceasedCopy), Color.parseColor("#F6404F")));

        mPieChart.startAnimation();

        MainActivity object = new MainActivity();
        String formatDate = object.formatDate(stateLastUpdate, 0);
        perStateConfirmed.setText(stateConfirmed);
        perStateActive.setText(stateActive);
        perStateDeceased.setText(stateDeceased);
        perStateUpdate.setText(formatDate);
        perStateNewConfirmed.setText("+" + stateNewConfirmed);
        perStateNewRecovered.setText("+" + stateNewRecovered);
        perStateNewDeceased.setText("+" + stateNewDeceased);
        perStateRecovered.setText(stateRecovery);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
