package com.vipul.covidstatus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vipul.covidstatus.data.PerStateData;
import com.vipul.covidstatus.R;
import com.vipul.covidstatus.adapters.StatewiseAdapter;
import com.vipul.covidstatus.models.StatewiseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

public class StatewiseDataActivity extends AppCompatActivity implements StatewiseAdapter.OnItemClickListner {

    public static final String STATE_NAME = "stateName";
    public static final String STATE_CONFIRMED = "stateConfirmed";
    public static final String STATE_ACTIVE = "stateActive";
    public static final String STATE_DECEASED = "stateDeaceased";
    public static final String STATE_NEW_CONFIRMED = "stateNewConfirmed";
    public static final String STATE_NEW_RECOVERED = "stateNewRecovered";
    public static final String STATE_NEW_DECEASED = "stateNewDeceased";
    public static final String STATE_LAST_UPDATE = "stateLastUpdate";
    public static final String STATE_RECOVERED = "stateRecovered";

    private RecyclerView recyclerView;
    private StatewiseAdapter statewiseAdapter;
    private ArrayList<StatewiseModel> statewiseModelArrayList;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    public static int confirmation = 0;
    public static String testValue;
    public static boolean isRefreshed;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText search;
    String stateLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("State Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_statewise_data);
        recyclerView = findViewById(R.id.statewise_recyclerview);
        swipeRefreshLayout = findViewById(R.id.statewise_refresh);
        search = findViewById(R.id.search_editText);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        statewiseModelArrayList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        showProgressDialog();
        extractData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                extractData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(StatewiseDataActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {
        ArrayList<StatewiseModel> filteredList = new ArrayList<>();
        for (StatewiseModel item : statewiseModelArrayList) {
            if (item.getState().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        statewiseAdapter.filterList(filteredList);
    }

    private void extractData() {
        String dataURL = "https://api.covid19india.org/data.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, dataURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    statewiseModelArrayList.clear();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject statewise = jsonArray.getJSONObject(i);

                        String stateName = statewise.getString("state");
                        String stateConfirmed = statewise.getString("confirmed");
                        String stateActive = statewise.getString("active");
                        String stateDeceased = statewise.getString("deaths");
                        String stateRecovered = statewise.getString("recovered");
                        String stateNewConfirmed = statewise.getString("deltaconfirmed");
                        String stateNewRecovered = statewise.getString("deltarecovered");
                        String stateNewDeceased = statewise.getString("deltadeaths");
                        stateLastUpdate = statewise.getString("lastupdatedtime");
                        testValue = stateLastUpdate;

                        int stateConfirmedInt = Integer.parseInt(stateConfirmed);
                        stateConfirmed = NumberFormat.getInstance().format(stateConfirmedInt);

                        int stateNewConfirmedInt = Integer.parseInt(stateNewConfirmed);
                        stateNewConfirmed = NumberFormat.getInstance().format(stateNewConfirmedInt);

                        int stateNewRecoveredInt = Integer.parseInt(stateNewRecovered);
                        stateNewRecovered = NumberFormat.getInstance().format(stateNewRecoveredInt);

                        int stateNewDeceasedInt = Integer.parseInt(stateNewDeceased);
                        stateNewDeceased = NumberFormat.getInstance().format(stateNewDeceasedInt);


                        statewiseModelArrayList.add(new StatewiseModel(stateName, stateConfirmed, stateActive, stateDeceased, stateNewConfirmed, stateNewRecovered, stateNewDeceased, stateLastUpdate, stateRecovered));
                    }

                    if (!testValue.isEmpty()) {
                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                progressDialog.cancel();
                                statewiseAdapter = new StatewiseAdapter(StatewiseDataActivity.this, statewiseModelArrayList);
                                recyclerView.setAdapter(statewiseAdapter);
                                statewiseAdapter.setOnItemClickListner(StatewiseDataActivity.this);
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 500);
                        confirmation = 1;
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

    @Override
    public void onItemClick(int position) {
        Intent perStateIntent = new Intent(this, PerStateData.class);
        StatewiseModel clickedItem = statewiseModelArrayList.get(position);

        perStateIntent.putExtra(STATE_NAME, clickedItem.getState());
        perStateIntent.putExtra(STATE_CONFIRMED, clickedItem.getConfirmed());
        perStateIntent.putExtra(STATE_ACTIVE, clickedItem.getActive());
        perStateIntent.putExtra(STATE_DECEASED, clickedItem.getDeceased());
        perStateIntent.putExtra(STATE_NEW_CONFIRMED, clickedItem.getNewConfirmed());
        perStateIntent.putExtra(STATE_NEW_RECOVERED, clickedItem.getNewRecovered());
        perStateIntent.putExtra(STATE_NEW_DECEASED, clickedItem.getNewDeceased());
        perStateIntent.putExtra(STATE_LAST_UPDATE, clickedItem.getLastupdate());
        perStateIntent.putExtra(STATE_RECOVERED, clickedItem.getRecovered());


        startActivity(perStateIntent);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(StatewiseDataActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                if (confirmation != 1) {
                    progressDialog.cancel();
                    Toast.makeText(StatewiseDataActivity.this, "Internet slow/not available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 8000);
    }
}
