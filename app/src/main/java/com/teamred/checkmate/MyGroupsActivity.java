package com.teamred.checkmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_my_groups, testArray);
//
//        ListView listView = (ListView) findViewById(R.id.activity_listview);
//        listView.setAdapter(adapter);
    }
}