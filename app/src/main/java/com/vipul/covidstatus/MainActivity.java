package com.vipul.covidstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    TextView textView_confirmed, textView_confirmed_new, textView_active, textView_active_new, textView_recovered, textView_recovered_new, textView_death, textView_death_new, textView_tests, textView_date, textView_tests_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.covid19india.org/data.json";

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

        //Fetching the API from URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Since the objects of JSON are in an Array we need to define the array from which we can fetch objects
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    JSONObject statewise = jsonArray.getJSONObject(0);

                    //Inserting the fetched data into variables
                    confirmed = statewise.getString("confirmed");
                    active = statewise.getString("active");
                    date = statewise.getString("lastupdatedtime");
                    recovered = statewise.getString("recovered");
                    deaths = statewise.getString("deaths");
                    newConfirmed = statewise.getString("deltaconfirmed");
                    newDeaths = statewise.getString("deltadeaths");
                    newRecovered = statewise.getString("deltarecovered");

                    textView_confirmed.append(confirmed);
                    textView_confirmed_new.append("[+"+newConfirmed+"]");
                    textView_active.append(active);
                    //We need to calculate new active cases since it doesn't exist in API
                    int newActive = (Integer.parseInt(newConfirmed))-((Integer.parseInt(newRecovered))+Integer.parseInt(newDeaths));
                    textView_active_new.append("[+"+newActive+"]");
                    textView_recovered.append(recovered);
                    textView_recovered_new.append("[+"+newRecovered+"]");
                    textView_death.append(deaths);
                    textView_death_new.append("[+"+newDeaths+"]");
                    textView_date.append(date);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
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
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject statewise = jsonArray.getJSONObject(i);
                        totalTests = statewise.getString("totalsamplestested");
                    }

                    textView_tests.append(totalTests);

                    for (int i=0; i<jsonArray.length()-1; i++){
                        JSONObject statewise = jsonArray.getJSONObject(i);
                        newTests = statewise.getString("totalsamplestested");
                    }
                    int testsNew = (Integer.parseInt(totalTests))-(Integer.parseInt(newTests));
                    textView_tests_new.append("[+"+testsNew+"]");

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
}
