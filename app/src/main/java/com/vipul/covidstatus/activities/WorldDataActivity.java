package com.vipul.covidstatus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vipul.covidstatus.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Objects;

public class WorldDataActivity extends AppCompatActivity {

    String totalCases;
    String newCases;
    String totalActive;
    String totalRecovered;
    String newRecovered;
    String totalDeceased;
    String newDeceased;
    String tests;

    TextView textView_confirmed, textView_confirmed_new, textView_totalActive, textView_totalRecovered, textView_totalRecovered_new, textView_death, textView_death_new, textView_tests;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    public static int confirmation = 0;
    public static boolean isRefreshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_data);

        textView_confirmed = findViewById(R.id.world_confirmed_textview);
        textView_confirmed_new = findViewById(R.id.world_confirmed_new_textView);
        textView_totalActive = findViewById(R.id.world_active_textView);
        textView_totalRecovered = findViewById(R.id.world_recovered_textView);
        textView_totalRecovered_new = findViewById(R.id.world_recovered_new_textView);
        textView_death = findViewById(R.id.world_death_textView);
        textView_death_new = findViewById(R.id.world_death_new_textView);
        textView_tests = findViewById(R.id.world_tests_textView);
        swipeRefreshLayout = findViewById(R.id.main_refreshLayout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Covid-19 Status (World)");

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        showProgressDialog();
        fetchData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(WorldDataActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://corona.lmao.ninja/v2/all";
       final PieChart mPieChart = findViewById(R.id.piechart_world);
        mPieChart.clearChart();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (isRefreshed){
                        totalCases = response.getString("cases");
                        newCases = response.getString("todayCases");
                        totalActive = response.getString("active");
                        totalRecovered = response.getString("recovered");
                        newRecovered = response.getString("todayRecovered");
                        totalDeceased = response.getString("deaths");
                        newDeceased = response.getString("todayDeaths");
                        tests = response.getString("tests");
                        textView_confirmed.setText(totalCases);

                        Runnable progressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                                String totalActiveCopy = totalActive;
                                String totalDeceasedCopy = totalDeceased;
                                String totalRecoveredCopy = totalRecovered;

                                int confirmedInt = Integer.parseInt(totalCases);
                                totalCases = NumberFormat.getInstance().format(confirmedInt);
                                textView_confirmed.setText(totalCases);

                                int newCasesInt = Integer.parseInt(newCases);
                                newCases = NumberFormat.getInstance().format(newCasesInt);
                                textView_confirmed_new.setText("+" + newCases);

                                int totalActiveInt = Integer.parseInt(totalActive);
                                totalActive = NumberFormat.getInstance().format(totalActiveInt);
                                textView_totalActive.setText(totalActive);

                                int totalRecoveredInt = Integer.parseInt(totalRecovered);
                                totalRecovered = NumberFormat.getInstance().format(totalRecoveredInt);
                                textView_totalRecovered.setText(totalRecovered);

                                int totalRecoveredNewInt = Integer.parseInt(newRecovered);
                                newRecovered = NumberFormat.getInstance().format(totalRecoveredNewInt);
                                textView_totalRecovered_new.setText("+" + newRecovered);

                                int totalDeceasedInt = Integer.parseInt(totalDeceased);
                                totalDeceased = NumberFormat.getInstance().format(totalDeceasedInt);
                                textView_death.setText(totalDeceased);

                                int totalDeceasedNewInt = Integer.parseInt(newDeceased);
                                newDeceased = NumberFormat.getInstance().format(totalDeceasedNewInt);
                                textView_death_new.setText("+" + newDeceased);

                                int testsInt = Integer.parseInt(tests);
                                tests = NumberFormat.getInstance().format(testsInt);
                                textView_tests.setText(tests);

                                mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(totalActiveCopy), Color.parseColor("#007afe")));
                                mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(totalRecoveredCopy), Color.parseColor("#08a045")));
                                mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(totalDeceasedCopy), Color.parseColor("#F6404F")));

                                mPieChart.startAnimation();
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 0);
                    } else {
                        totalCases = response.getString("cases");
                        newCases = response.getString("todayCases");
                        totalActive = response.getString("active");
                        totalRecovered = response.getString("recovered");
                        newRecovered = response.getString("todayRecovered");
                        totalDeceased = response.getString("deaths");
                        newDeceased = response.getString("todayDeaths");
                        tests = response.getString("tests");
                        textView_confirmed.setText(totalCases);

                        if (!tests.isEmpty()){
                            Runnable progressRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.cancel();
                                    String totalActiveCopy = totalActive;
                                    String totalDeceasedCopy = totalDeceased;
                                    String totalRecoveredCopy = totalRecovered;

                                    int confirmedInt = Integer.parseInt(totalCases);
                                    totalCases = NumberFormat.getInstance().format(confirmedInt);
                                    textView_confirmed.setText(totalCases);

                                    int newCasesInt = Integer.parseInt(newCases);
                                    newCases = NumberFormat.getInstance().format(newCasesInt);
                                    textView_confirmed_new.setText("+" + newCases);

                                    int totalActiveInt = Integer.parseInt(totalActive);
                                    totalActive = NumberFormat.getInstance().format(totalActiveInt);
                                    textView_totalActive.setText(totalActive);

                                    int totalRecoveredInt = Integer.parseInt(totalRecovered);
                                    totalRecovered = NumberFormat.getInstance().format(totalRecoveredInt);
                                    textView_totalRecovered.setText(totalRecovered);

                                    int totalRecoveredNewInt = Integer.parseInt(newRecovered);
                                    newRecovered = NumberFormat.getInstance().format(totalRecoveredNewInt);
                                    textView_totalRecovered_new.setText("+" + newRecovered);

                                    int totalDeceasedInt = Integer.parseInt(totalDeceased);
                                    totalDeceased = NumberFormat.getInstance().format(totalDeceasedInt);
                                    textView_death.setText(totalDeceased);

                                    int totalDeceasedNewInt = Integer.parseInt(newDeceased);
                                    newDeceased = NumberFormat.getInstance().format(totalDeceasedNewInt);
                                    textView_death_new.setText("+" + newDeceased);

                                    int testsInt = Integer.parseInt(tests);
                                    tests = NumberFormat.getInstance().format(testsInt);
                                    textView_tests.setText(tests);

                                    mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(totalActiveCopy), Color.parseColor("#007afe")));
                                    mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(totalRecoveredCopy), Color.parseColor("#08a045")));
                                    mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(totalDeceasedCopy), Color.parseColor("#F6404F")));

                                    mPieChart.startAnimation();
                                }
                            };
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 0);
                            confirmation = 1;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(WorldDataActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (confirmation != 1) {
                    progressDialog.cancel();
                    Toast.makeText(WorldDataActivity.this, "Internet slow/not available", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 8000);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void openCountryData(View view) {
        Intent intent = new Intent(this, CountrywiseDataActivity.class);
        startActivity(intent);
    }
}