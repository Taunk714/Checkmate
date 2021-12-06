package com.teamred.checkmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormatSymbols;

public class MyGroupsActivity extends AppCompatActivity {
    String[] testArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    ListView lvMonth;
    String[] months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        lvMonth = findViewById(R.id.lvMonth);
        months = new DateFormatSymbols().getMonths();
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, testArray);
        lvMonth.setAdapter(monthAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(getApplicationContext(),"Back Button Clicked", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MyGroupsActivity.this, MainActivity2.class));
        return super.onOptionsItemSelected(item);
    }
}