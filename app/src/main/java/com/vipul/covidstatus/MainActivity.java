package com.vipul.covidstatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.vipul.covidstatus.activities.InfoActivity;
import com.vipul.covidstatus.activities.StatewiseDataActivity;
import com.vipul.covidstatus.activities.WorldDataActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    String oldTests;
    int testsInt;
    String totalTestsCopy;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    private long backPressTime;
    private Toast backToast;
    String version;
    String updateVersion;
    String updateUrl;
    String updateChanges;

    TextView textView_confirmed, textView_confirmed_new, textView_active, textView_active_new, textView_recovered, textView_recovered_new, textView_death, textView_death_new, textView_tests, textView_date, textView_tests_new, textview_time;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        version = String.valueOf(R.string.version);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isFirstStart = sharedPreferences.getBoolean("initialStart", true);
        String appVersion = sharedPreferences.getString("appVersion", version);

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
        textview_time = findViewById(R.id.time_textView);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Covid-19 Status (India)");

        fetchUpdate();
        showProgressDialog();
        fetchData();
        if (isFirstStart || !appVersion.equals(BuildConfig.VERSION_NAME)) {
            showChanges();
        }

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

    private void showChanges() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.changelogTitle)
                .setMessage(R.string.changelog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("initialStart", false);
        editor.putString("appVersion", BuildConfig.VERSION_NAME);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.info) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (backPressTime + 800 > System.currentTimeMillis()) {
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

                                String activeCopy = active;
                                String deathsCopy = deaths;
                                String recoveredCopy = recovered;
                                String confirmedNewCopy = newConfirmed;


                                int confirmedInt = Integer.parseInt(confirmed);
                                confirmed = NumberFormat.getInstance().format(confirmedInt);
                                textView_confirmed.setText(confirmed);

                                int newConfirmedInt = Integer.parseInt(newConfirmed);
                                newConfirmed = NumberFormat.getInstance().format(newConfirmedInt);
                                textView_confirmed_new.setText("+" + newConfirmed);

                                int activeInt = Integer.parseInt(active);
                                active = NumberFormat.getInstance().format(activeInt);
                                textView_active.setText(active);

//                                //We need to calculate new active cases since it doesn't exist in API
//                                int newActive = (Integer.parseInt(confirmedNewCopy)) - ((Integer.parseInt(newRecovered)) + Integer.parseInt(newDeaths));
//                                textView_active_new.setText("+" + NumberFormat.getInstance().format(newActive));

                                int recoveredInt = Integer.parseInt(recovered);
                                recovered = NumberFormat.getInstance().format(recoveredInt);
                                textView_recovered.setText(recovered);

                                int recoveredNewInt = Integer.parseInt(newRecovered);
                                newRecovered = NumberFormat.getInstance().format(recoveredNewInt);
                                textView_recovered_new.setText("+" + newRecovered);

                                int deathsInt = Integer.parseInt(deaths);
                                deaths = NumberFormat.getInstance().format(deathsInt);
                                textView_death.setText(deaths);

                                int deathsNewInt = Integer.parseInt(newDeaths);
                                newDeaths = NumberFormat.getInstance().format(deathsNewInt);
                                textView_death_new.setText("+" + newDeaths);

                                String dateFormat = formatDate(date, 1);
                                textView_date.setText(dateFormat);

                                String timeFormat = formatDate(date, 2);
                                textview_time.setText(timeFormat);

                                mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(activeCopy), Color.parseColor("#007afe")));
                                mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recoveredCopy), Color.parseColor("#08a045")));
                                mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(deathsCopy), Color.parseColor("#F6404F")));

                                mPieChart.startAnimation();
                                fetchTests();
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

                                    String activeCopy = active;
                                    String deathsCopy = deaths;
                                    String recoveredCopy = recovered;
                                    String confirmedNewCopy = newConfirmed;

                                    int confirmedInt = Integer.parseInt(confirmed);
                                    confirmed = NumberFormat.getInstance().format(confirmedInt);
                                    textView_confirmed.setText(confirmed);

                                    int newConfirmedInt = Integer.parseInt(newConfirmed);
                                    newConfirmed = NumberFormat.getInstance().format(newConfirmedInt);
                                    textView_confirmed_new.setText("+" + newConfirmed);

                                    int activeInt = Integer.parseInt(active);
                                    active = NumberFormat.getInstance().format(activeInt);
                                    textView_active.setText(active);

//                                    //We need to calculate new active cases since it doesn't exist in API
//                                    int newActive = (Integer.parseInt(confirmedNewCopy)) - ((Integer.parseInt(newRecovered)) + Integer.parseInt(newDeaths));
//                                    textView_active_new.setText("+" + NumberFormat.getInstance().format(newActive));

                                    int recoveredInt = Integer.parseInt(recovered);
                                    recovered = NumberFormat.getInstance().format(recoveredInt);
                                    textView_recovered.setText(recovered);

                                    int recoveredNewInt = Integer.parseInt(newRecovered);
                                    newRecovered = NumberFormat.getInstance().format(recoveredNewInt);
                                    textView_recovered_new.setText("+" + newRecovered);

                                    int deathsInt = Integer.parseInt(deaths);
                                    deaths = NumberFormat.getInstance().format(deathsInt);
                                    textView_death.setText(deaths);

                                    int deathsNewInt = Integer.parseInt(newDeaths);
                                    newDeaths = NumberFormat.getInstance().format(deathsNewInt);
                                    textView_death_new.setText("+" + newDeaths);


                                    String dateFormat = formatDate(date, 1);
                                    textView_date.setText(dateFormat);

                                    String timeFormat = formatDate(date, 2);
                                    textview_time.setText(timeFormat);

                                    mPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(activeCopy), Color.parseColor("#007afe")));
                                    mPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recoveredCopy), Color.parseColor("#08a045")));
                                    mPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(deathsCopy), Color.parseColor("#F6404F")));

                                    mPieChart.startAnimation();
                                    fetchTests();
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
    }

    public String formatDate(String date, int testCase) {
        Date mDate = null;
        String dateFormat;
        try {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).parse(date);
            if (testCase == 0) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(mDate);
                return dateFormat;
            } else if (testCase == 1) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(mDate);
                return dateFormat;
            } else if (testCase == 2) {
                dateFormat = new SimpleDateFormat("hh:mm a", Locale.US).format(mDate);
                return dateFormat;
            } else {
                Log.d("error", "Wrong input! Choose from 0 to 2");
                return "Error";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public void fetchTests() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.covid19india.org/data.json";
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
                        oldTests = statewise.getString("totalsamplestested");
                    }
                    if (totalTests.isEmpty()) {
                        for (int i = 0; i < jsonArray.length() - 1; i++) {
                            JSONObject statewise = jsonArray.getJSONObject(i);
                            totalTests = statewise.getString("totalsamplestested");
                        }
                        totalTestsCopy = totalTests;
                        testsInt = Integer.parseInt(totalTests);
                        totalTests = NumberFormat.getInstance().format(testsInt);
                        textView_tests.setText(totalTests);


                        for (int i = 0; i < jsonArray.length() - 2; i++) {
                            JSONObject statewise = jsonArray.getJSONObject(i);
                            oldTests = statewise.getString("totalsamplestested");
                        }
                        int testsNew = (Integer.parseInt(totalTestsCopy)) - (Integer.parseInt(oldTests));
                        textView_tests_new.setText("[+" + NumberFormat.getInstance().format(testsNew) + "]");

                    } else {
                        totalTestsCopy = totalTests;
                        testsInt = Integer.parseInt(totalTests);
                        totalTests = NumberFormat.getInstance().format(testsInt);
                        textView_tests.setText(totalTests);

                        if (oldTests.isEmpty()) {
                            for (int i = 0; i < jsonArray.length() - 2; i++) {
                                JSONObject statewise = jsonArray.getJSONObject(i);
                                oldTests = statewise.getString("totalsamplestested");
                            }
                        }
                        long testsNew = (Integer.parseInt(totalTestsCopy)) - (Integer.parseInt(oldTests));
                        textView_tests_new.setText("+" + NumberFormat.getInstance().format(testsNew));
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
        Intent intent = new Intent(this, WorldDataActivity.class);
        startActivity(intent);
    }

    public void fetchUpdate() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://vipul-api.netlify.app/api/version.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    updateVersion = response.getString("version");
                    updateUrl = response.getString("url");
                    updateChanges = response.getString("changes");

                    if (!updateVersion.equals(String.valueOf(BuildConfig.VERSION_NAME))) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("v" + updateVersion + " update available!")
                                .setMessage(updateChanges)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(updateUrl));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .create().show();
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

}
