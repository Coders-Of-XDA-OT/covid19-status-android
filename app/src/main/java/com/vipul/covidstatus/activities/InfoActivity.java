package com.vipul.covidstatus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vipul.covidstatus.BuildConfig;
import com.vipul.covidstatus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class InfoActivity extends AppCompatActivity {

    String version;
    String updateUrl;
    String updateChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Objects.requireNonNull(getSupportActionBar()).setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void openWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vipuljha.com")));
    }

    public void openGit(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/lordarcadius")));
    }

    public void openInsta(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/lordarcadius")));
    }

    public void openTwitter(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/lordarcadius")));
    }

    public void checkUpdate(View view){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://vipul-api.netlify.app/api/version.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    version = response.getString("version");
                    updateUrl = response.getString("url");
                    updateChanges = response.getString("changes");

                    if (!version.equals(String.valueOf(BuildConfig.VERSION_NAME))){
                        new AlertDialog.Builder(InfoActivity.this)
                                .setTitle("v"+version+" update available!")
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
                    } else {
                        Toast.makeText(InfoActivity.this, "No update available!", Toast.LENGTH_SHORT).show();
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

    public void showChanges(View view){
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
    }
}
