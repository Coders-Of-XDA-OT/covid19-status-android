package com.vipul.covidstatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Objects.requireNonNull(getSupportActionBar()).setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
}
