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

import java.util.Objects;

import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_CONFIRMED;
import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_NAME;
import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_ACTIVE;
import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_DECEASED;
import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_NEW_CONFIRMED;
import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_TESTS;
import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_NEW_DECEASED;
import static com.vipul.covidstatus.CountrywiseDataActivity.COUNTRY_RECOVERED;


public class PerCountryData extends AppCompatActivity {
    TextView perCountryConfirmed, perCountryActive, perCountryDeceased, perCountryNewConfirmed, perCountryTests, perCountryNewDeceased, perCountryRecovered;
    PieChart mPieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_country_data);

        Intent intent = getIntent();
        String countryName = intent.getStringExtra(COUNTRY_NAME);
        String countryConfirmed = intent.getStringExtra(COUNTRY_CONFIRMED);
        String countryActive = intent.getStringExtra(COUNTRY_ACTIVE);
        String countryDeceased = intent.getStringExtra(COUNTRY_DECEASED);
        String countryRecovery = intent.getStringExtra(COUNTRY_RECOVERED);
        String countryNewConfirmed = intent.getStringExtra(COUNTRY_NEW_CONFIRMED);
        String countryNewDeceased = intent.getStringExtra(COUNTRY_NEW_DECEASED);
        String countryTests = intent.getStringExtra(COUNTRY_TESTS);
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

        mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(countryActive), Color.parseColor("#007afe")));
        mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(countryRecovery), Color.parseColor("#08a045")));
        mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(countryDeceased), Color.parseColor("#F6404F")));

        mPieChart.startAnimation();

        perCountryConfirmed.append(countryConfirmed);
        perCountryActive.append(countryActive);
        perCountryDeceased.append(countryDeceased);
        perCountryTests.append(countryTests);
        perCountryNewConfirmed.append("[+"+countryNewConfirmed+"]");
        perCountryNewDeceased.append("[+"+countryNewDeceased+"]");
        perCountryRecovered.append(countryRecovery);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
