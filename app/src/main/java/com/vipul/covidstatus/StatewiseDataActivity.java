package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class StatewiseDataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<StatewiseModel>statewiseModel;
    private static String dataURL = "https://api.covid19india.org/data.json";

    StatewiseAdapter statewiseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statewise_data);

        recyclerView.findViewById(R.id.statewise_recyclerview);

        statewiseModel = new ArrayList<>();

        extractData();

    }

    private void extractData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, dataURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    for (int i=1; i<jsonArray.length(); i++){
                        JSONObject statewise = jsonArray.getJSONObject(i);
                        StatewiseModel statewiseModel = new StatewiseModel();
                        statewiseModel.setState(statewise.getString("state"));
                        statewiseModel.setConfirmed(statewise.getString("confirmed"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                statewiseAdapter = new StatewiseAdapter(getApplicationContext(), statewiseModel);
                recyclerView.setAdapter(statewiseAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dataerror", "onErrorResponse: "+error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
