package com.vipul.covidstatus.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.vipul.covidstatus.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.Objects;

import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_CONFIRMED;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_NAME;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_ACTIVE;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_DECEASED;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_NEW_CONFIRMED;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_NEW_RECOVERED;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_TESTS;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_NEW_DECEASED;
import static com.vipul.covidstatus.activities.CountrywiseDataActivity.COUNTRY_RECOVERED;


public class PerCountryData extends AppCompatActivity {
    TextView perCountryConfirmed, perCountryActive, perCountryDeceased, perCountryNewConfirmed, perCountryTests, perCountryNewDeceased, perCountryRecovered, perCountryNewRecovered;
    PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_country_data);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        Intent intent = getIntent();
        String countryName = intent.getStringExtra(COUNTRY_NAME);
        String countryConfirmed = intent.getStringExtra(COUNTRY_CONFIRMED);
        String countryActive = intent.getStringExtra(COUNTRY_ACTIVE);
        String countryDeceased = intent.getStringExtra(COUNTRY_DECEASED);
        String countryRecovery = intent.getStringExtra(COUNTRY_RECOVERED);
        String countryNewConfirmed = intent.getStringExtra(COUNTRY_NEW_CONFIRMED);
        String countryNewDeceased = intent.getStringExtra(COUNTRY_NEW_DECEASED);
        String countryTests = intent.getStringExtra(COUNTRY_TESTS);
        String countryNewRecovered = intent.getStringExtra(COUNTRY_NEW_RECOVERED);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Objects.requireNonNull(getSupportActionBar()).setTitle(countryName);
        perCountryConfirmed = findViewById(R.id.percountry_confirmed_textview);
        perCountryActive = findViewById(R.id.percountry_active_textView);
        perCountryRecovered = findViewById(R.id.percountry_recovered_textView);
        perCountryDeceased = findViewById(R.id.percountry_death_textView);
        perCountryNewConfirmed = findViewById(R.id.percountry_confirmed_new_textView);
        perCountryTests = findViewById(R.id.percountry_tests_textView);
        perCountryNewDeceased = findViewById(R.id.percountry_death_new_textView);
        mPieChart = findViewById(R.id.piechart_percountry);
        perCountryNewRecovered = findViewById(R.id.percountry_recovered_new_textView);

        String activeCopy = countryActive;
        String recoveredCopy = countryRecovery;
        String deceasedCopy = countryDeceased;

        int activeInt = Integer.parseInt(countryActive);
        countryActive = NumberFormat.getInstance().format(activeInt);

        int recoveredInt = Integer.parseInt(countryRecovery);
        countryRecovery = NumberFormat.getInstance().format(recoveredInt);

        int deceasedInt = Integer.parseInt(countryDeceased);
        countryDeceased = NumberFormat.getInstance().format(deceasedInt);

        int confirmedInt = Integer.parseInt(countryConfirmed);
        countryConfirmed = NumberFormat.getInstance().format(confirmedInt);

        int testsInt = Integer.parseInt(countryTests);
        countryTests = NumberFormat.getInstance().format(testsInt);

        mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(activeCopy), Color.parseColor("#007afe")));
        mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recoveredCopy), Color.parseColor("#08a045")));
        mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(deceasedCopy), Color.parseColor("#F6404F")));

        mPieChart.startAnimation();

        perCountryConfirmed.setText(countryConfirmed);
        perCountryActive.setText(countryActive);
        perCountryDeceased.setText(countryDeceased);
        perCountryTests.setText(countryTests);
        perCountryNewConfirmed.setText("+" + countryNewConfirmed);
        perCountryNewDeceased.setText("+" + countryNewDeceased);
        perCountryRecovered.setText(countryRecovery);
        perCountryNewRecovered.setText("+"+countryNewRecovered);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
