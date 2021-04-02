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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vipul.covidstatus.data.PerDistrictData;
import com.vipul.covidstatus.R;
import com.vipul.covidstatus.adapters.DistrictAdapter;
import com.vipul.covidstatus.models.DistrictModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static com.vipul.covidstatus.activities.StatewiseDataActivity.STATE_NAME;

public class DistrictwiseDataActivity extends AppCompatActivity {

    public static final String DISTRICT_NAME = "districtName";
    public static final String DISTRICT_CONFIRMED = "districtConfirmed";
    public static final String DISTRICT_ACTIVE = "districtActive";
    public static final String DISTRICT_DECEASED = "districtDeaceased";
    public static final String DISTRICT_NEW_CONFIRMED = "districtNewConfirmed";
    public static final String DISTRICT_NEW_RECOVERED = "districtNewRecovered";
    public static final String DISTRICT_NEW_DECEASED = "districtNewDeceased";
    public static final String DISTRICT_RECOVERED = "districtRecovered";
    public static final String DISTRICT_LAST_UPDATE = "districtUpdate";

    private RecyclerView recyclerView;
    private DistrictAdapter districtAdapter;
    private ArrayList<DistrictModel> districtModelArrayList;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText search;
    String stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districtwise_data);

        Intent intent = getIntent();
        stateName = intent.getStringExtra(STATE_NAME);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setTitle(stateName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.district_recyclerview);
        swipeRefreshLayout = findViewById(R.id.district_refresh);
        search = findViewById(R.id.search_editText);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        districtModelArrayList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        showProgressDialog();
        extractData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                extractData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(DistrictwiseDataActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
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
        ArrayList<DistrictModel> filteredList = new ArrayList<>();
        for (DistrictModel item : districtModelArrayList) {
            if (item.getDistrict().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        districtAdapter.filterList(filteredList);
    }

    private void extractData() {
        String dataURL = "https://api.covid19india.org/v2/state_district_wise.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, dataURL, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray jsonArray) {
                try {
                    districtModelArrayList.clear();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (stateName.toLowerCase().equals(jsonObject.getString("state").toLowerCase())) {
                            JSONArray jsonArray2 = jsonObject.getJSONArray("districtData");
                            for (int j = 0; j < jsonArray2.length(); j++) {
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                                String districtName = jsonObject2.getString("district");
                                String districtConfirmed = jsonObject2.getString("confirmed");
                                String districtActive = jsonObject2.getString("active");
                                String districtDeceased = jsonObject2.getString("deceased");
                                String districtRecovered = jsonObject2.getString("recovered");

                                JSONObject jsonObject3 = jsonObject2.getJSONObject("delta");
                                String districtNewConfirmed = jsonObject3.getString("confirmed");
                                String districtNewRecovered = jsonObject3.getString("recovered");
                                String districtNewDeceased = jsonObject3.getString("deceased");

                                districtModelArrayList.add(new DistrictModel(districtName, districtConfirmed, districtActive, districtRecovered, districtDeceased, districtNewConfirmed, districtNewRecovered, districtNewDeceased));
                                int newConfirmedInt = Integer.parseInt(districtNewConfirmed);
                                districtNewConfirmed = NumberFormat.getInstance().format(newConfirmedInt);

                                int newDeceasedInt = Integer.parseInt(districtNewDeceased);
                                districtNewDeceased = NumberFormat.getInstance().format(newDeceasedInt);


                                Collections.sort(districtModelArrayList, new Comparator<DistrictModel>() {
                                    @Override
                                    public int compare(DistrictModel o1, DistrictModel o2) {
                                        if (Integer.parseInt(o1.getConfirmed())>Integer.parseInt(o2.getConfirmed())){
                                            return -1;
                                        } else {
                                            return 1;
                                        }
                                    }
                                });
                            }
                        }

                    }
                        Runnable progressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                                districtAdapter = new DistrictAdapter(DistrictwiseDataActivity.this, districtModelArrayList);
                                recyclerView.setAdapter(districtAdapter);
                                //districtAdapter.setOnItemClickListner(DistrictwiseDataActivity.this);
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 500);
                        confirmation = 1;
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
        requestQueue.add(jsonArrayRequest);
    }

    public void onItemClick(int position) {
        Intent perDistrictIntent = new Intent(this, PerDistrictData.class);
        DistrictModel clickedItem = districtModelArrayList.get(position);

        perDistrictIntent.putExtra(DISTRICT_NAME, clickedItem.getDistrict());
        perDistrictIntent.putExtra(DISTRICT_CONFIRMED, clickedItem.getConfirmed());
        perDistrictIntent.putExtra(DISTRICT_ACTIVE, clickedItem.getActive());
        perDistrictIntent.putExtra(DISTRICT_RECOVERED, clickedItem.getRecovered());
        perDistrictIntent.putExtra(DISTRICT_DECEASED, clickedItem.getDeceased());
        perDistrictIntent.putExtra(DISTRICT_NEW_CONFIRMED, clickedItem.getNewConfirmed());
        perDistrictIntent.putExtra(DISTRICT_NEW_DECEASED, clickedItem.getNewDeceased());
        perDistrictIntent.putExtra(DISTRICT_NEW_RECOVERED, clickedItem.getNewRecovered());
        //perDistrictIntent.putExtra(DISTRICT_LAST_UPDATE, clickedItem.getDate());


        startActivity(perDistrictIntent);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(DistrictwiseDataActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                if (confirmation != 1) {
                    progressDialog.cancel();
                    Toast.makeText(DistrictwiseDataActivity.this, "Internet slow/not available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 8000);
    }

}