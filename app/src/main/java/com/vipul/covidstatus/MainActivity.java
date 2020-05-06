package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    String confirmed;
    String active;
    String date;
    String recovered;
    String deaths;
    String newConfirmed;
    String newDeaths;
    String newRecovered;
    String totalTests;
    String newTests;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    private long backPressTime;
    private Toast backToast;

    TextView textView_confirmed, textView_confirmed_new, textView_active, textView_active_new, textView_recovered, textView_recovered_new, textView_death, textView_death_new, textView_tests, textView_date, textView_tests_new;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_confirmed = findViewById(R.id.confirmed_textView);
        textView_confirmed_new = findViewById(R.id.confirmed_new_textView);
        textView_active = findViewById(R.id.active_textView);
        textView_active_new = findViewById(R.id.active_new_textView);
        textView_recovered = findViewById(R.id.recovered_textView);
        textView_recovered_new = findViewById(R.id.recovered_new_textView);
        textView_death = findViewById(R.id.death_textView);
        textView_death_new = findViewById(R.id.death_new_textView);
        textView_tests = findViewById(R.id.tests_textView);
        textView_date = findViewById(R.id.date_textView);
        textView_tests_new = findViewById(R.id.tests_new_textView);
        swipeRefreshLayout = findViewById(R.id.main_refreshLayout);

        showProgressDialog();
        fetchData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (backPressTime + 200 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressTime = System.currentTimeMillis();
    }

    public void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.covid19india.org/data.json";
        final PieChart mPieChart = findViewById(R.id.piechart);
        mPieChart.clearChart();
        //Fetching the API from URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Since the objects of JSON are in an Array we need to define the array from which we can fetch objects
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    JSONObject statewise = jsonArray.getJSONObject(0);


                    if (isRefreshed) {
                        //Inserting the fetched data into variables
                        confirmed = statewise.getString("confirmed");
                        active = statewise.getString("active");
                        date = statewise.getString("lastupdatedtime");
                        recovered = statewise.getString("recovered");
                        deaths = statewise.getString("deaths");
                        newConfirmed = statewise.getString("deltaconfirmed");
                        newDeaths = statewise.getString("deltadeaths");
                        newRecovered = statewise.getString("deltarecovered");
                        Runnable progressRunnable = new Runnable() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                progressDialog.cancel();
                                textView_confirmed.setText(confirmed);
                                textView_confirmed_new.setText("[+" + newConfirmed + "]");
                                textView_active.setText(active);
                                //We need to calculate new active cases since it doesn't exist in API
                                int newActive = (Integer.parseInt(newConfirmed)) - ((Integer.parseInt(newRecovered)) + Integer.parseInt(newDeaths));
                                textView_active_new.setText("[+" + newActive + "]");
                                textView_recovered.setText(recovered);
                                textView_recovered_new.setText("[+" + newRecovered + "]");
                                textView_death.setText(deaths);
                                textView_death_new.setText("[+" + newDeaths + "]");
                                textView_date.setText(date);

                                mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(active), Color.parseColor("#007afe")));
                                mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered), Color.parseColor("#08a045")));
                                mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(deaths), Color.parseColor("#F6404F")));

                                mPieChart.startAnimation();
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 0);
                    } else {
                        //Inserting the fetched data into variables
                        confirmed = statewise.getString("confirmed");
                        active = statewise.getString("active");
                        date = statewise.getString("lastupdatedtime");
                        recovered = statewise.getString("recovered");
                        deaths = statewise.getString("deaths");
                        newConfirmed = statewise.getString("deltaconfirmed");
                        newDeaths = statewise.getString("deltadeaths");
                        newRecovered = statewise.getString("deltarecovered");
                        if (!date.isEmpty()) {
                            Runnable progressRunnable = new Runnable() {

                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    progressDialog.cancel();
                                    textView_confirmed.setText(confirmed);
                                    textView_confirmed_new.setText("[+" + newConfirmed + "]");
                                    textView_active.setText(active);
                                    //We need to calculate new active cases since it doesn't exist in API
                                    int newActive = (Integer.parseInt(newConfirmed)) - ((Integer.parseInt(newRecovered)) + Integer.parseInt(newDeaths));
                                    textView_active_new.setText("[+" + newActive + "]");
                                    textView_recovered.setText(recovered);
                                    textView_recovered_new.setText("[+" + newRecovered + "]");
                                    textView_death.setText(deaths);
                                    textView_death_new.setText("[+" + newDeaths + "]");
                                    textView_date.setText(date);

                                    mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(active), Color.parseColor("#007afe")));
                                    mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered), Color.parseColor("#08a045")));
                                    mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(deaths), Color.parseColor("#F6404F")));

                                    mPieChart.startAnimation();
                                }
                            };
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 1000);
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

        JsonObjectRequest jsonObjectRequestTests = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("tested");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject statewise = jsonArray.getJSONObject(i);
                        totalTests = statewise.getString("totalsamplestested");
                    }

                    for (int i = 0; i < jsonArray.length() - 1; i++) {
                        JSONObject statewise = jsonArray.getJSONObject(i);
                        newTests = statewise.getString("totalsamplestested");
                    }
                    if (totalTests.isEmpty()) {
                        for (int i = 0; i < jsonArray.length() - 1; i++) {
                            JSONObject statewise = jsonArray.getJSONObject(i);
                            totalTests = statewise.getString("totalsamplestested");
                        }
                        textView_tests.setText(totalTests);
                        for (int i = 0; i < jsonArray.length() - 2; i++) {
                            JSONObject statewise = jsonArray.getJSONObject(i);
                            newTests = statewise.getString("totalsamplestested");
                        }
                        int testsNew = (Integer.parseInt(totalTests)) - (Integer.parseInt(newTests));
                        textView_tests_new.setText("[+" + testsNew + "]");
                    } else {
                        textView_tests.setText(totalTests);
                        if (newTests.isEmpty()) {
                            for (int i = 0; i < jsonArray.length() - 2; i++) {
                                JSONObject statewise = jsonArray.getJSONObject(i);
                                newTests = statewise.getString("totalsamplestested");
                            }
                        }
                        int testsNew = (Integer.parseInt(totalTests)) - (Integer.parseInt(newTests));
                        textView_tests_new.setText("[+" + testsNew + "]");
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
        requestQueue.add(jsonObjectRequestTests);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                if (confirmation != 1) {
                    progressDialog.cancel();
                    Toast.makeText(MainActivity.this, "Internet slow/not available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 8000);
    }

    public void openStatewise(View view) {
        Intent intent = new Intent(this, StatewiseDataActivity.class);
        startActivity(intent);
    }

    public void openMoreInfo(View view) {
        Intent intent = new Intent(this, CountrywiseDataActivity.class);
        startActivity(intent);
    }

}
