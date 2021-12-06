package com.teamred.checkmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

public class MyGroupsActivity extends AppCompatActivity {
    String[] testArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    ListView lvMonth;
    String[] months;
    ArrayList<String> groups = new ArrayList<>();
    String[] groupsPrint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        lvMonth = findViewById(R.id.lvMonth);
        months = new DateFormatSymbols().getMonths();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("test", document.getId() + " => " + document.getData().get("groupName"));
                                System.out.println(document.getData().get("groupName"));
                                groups.add(document.getData().get("groupName").toString());
                            }
                            groupsPrint = groups.toArray(new String[groups.size()]);

                            System.out.println("check here");
                            System.out.println(Arrays.toString(groupsPrint));
                            System.out.println(groupsPrint);

                            ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getApplication().getApplicationContext(),
                                    android.R.layout.simple_list_item_1, groupsPrint);
                            lvMonth.setAdapter(monthAdapter);
                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(getApplicationContext(),"Back Button Clicked", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MyGroupsActivity.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
}