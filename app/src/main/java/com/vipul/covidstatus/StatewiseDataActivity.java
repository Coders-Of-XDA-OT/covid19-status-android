package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatewiseDataActivity extends AppCompatActivity  implements StatewiseAdapter.OnItemClickListner {

    public static final String STATE_NAME = "stateName";
    public static final String STATE_CONFIRMED = "stateConfirmed";

    private RecyclerView recyclerView;
    private StatewiseAdapter statewiseAdapter;
    private ArrayList<StatewiseModel>statewiseModelArrayList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statewise_data);
        recyclerView = findViewById(R.id.statewise_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        statewiseModelArrayList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        extractData();
    }

    private void extractData() {
        String dataURL = "https://api.covid19india.org/data.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, dataURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");

                    for (int i=1; i<jsonArray.length(); i++){
                        JSONObject statewise = jsonArray.getJSONObject(i);

                        String stateName = statewise.getString("state");
                        String stateConfirmed = statewise.getString("confirmed");
                        String stateActive = statewise.getString("active");
                        String stateDeceased = statewise.getString("deaths");
                        String stateRecovered = statewise.getString("recovered");
                        String stateNewConfirmed = statewise.getString("deltaconfirmed");
                        String stateNewRecovered = statewise.getString("deltarecovered");
                        String stateNewDeceased = statewise.getString("deltadeaths");
                        String stateLastUpdate = statewise.getString("lastupdatedtime");

                        statewiseModelArrayList.add(new StatewiseModel(stateName, stateConfirmed,stateActive, stateDeceased, stateRecovered, stateNewConfirmed, stateNewRecovered, stateNewDeceased, stateLastUpdate));
                    }

                    statewiseAdapter = new StatewiseAdapter(StatewiseDataActivity.this, statewiseModelArrayList);
                    recyclerView.setAdapter(statewiseAdapter);
                    statewiseAdapter.setOnItemClickListner(StatewiseDataActivity.this);

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

        startActivity(perStateIntent);
    }
}
