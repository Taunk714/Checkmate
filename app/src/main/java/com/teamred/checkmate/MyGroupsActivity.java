package com.teamred.checkmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.ui.GroupListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * Gets the joined groups of current user
 *
 * */
public class MyGroupsActivity extends AppCompatActivity {

    ListView lvGroup;
    ArrayList<String> groups = new ArrayList<>();
    String[] groupsPrint;
    private ListAdapter groupAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        lvGroup = findViewById(R.id.lvGroups);


        // Fetch all joined groups
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        groupAdapter = new GroupListViewAdapter(getApplicationContext(), groups, getSupportFragmentManager());
//        lvGroup.setAdapter(groupAdapter);


        db.collection(CheckmateKey.GROUP_FIREBASE)
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
                            lvGroup.setAdapter(monthAdapter);
                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }
}