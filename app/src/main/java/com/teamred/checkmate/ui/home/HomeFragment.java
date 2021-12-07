package com.teamred.checkmate.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.ui.GroupListViewAdapter;
import com.teamred.checkmate.ui.calendar.CalendarActivity;
import com.teamred.checkmate.ui.group.CreateGroupActivity;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.databinding.FragmentHomeBinding;
import com.teamred.checkmate.ui.group.GroupDetailFragment;
import com.teamred.checkmate.ui.login.LoginActivity;
import com.teamred.checkmate.MyGroupsActivity;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME";
    private FragmentHomeBinding binding;
    private List<Group> allGroups;
    private GroupListViewAdapter groupAdapter;
    private ListView listview;

    //private Button calenderHeatmap;

//    String[] testArray = {"Android","IPhone","WindowsMobile","Blackberry",
//            "WebOS","Ubuntu","Windows7","Max OS X"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AlgoliaDataSource.initAlgolia();

        //calenderHeatmap = (Button) getView().findViewById(R.id.calendarheatmap);
        Button calendarHeatmap = binding.calendarheatmap;
        listview = binding.allgroupsListview;
        calendarHeatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CalendarActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton btnAddPost = binding.btnAddGroup;
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getContext(), "Interceptor! Jump to login", Toast.LENGTH_LONG).show();
                    login.putExtra("callback", CreateGroupActivity.class.getCanonicalName());
                    startActivity(login);
                }else{
                    Intent i = new Intent(getActivity(), CreateGroupActivity.class);
                    startActivity(i);
                }
            }
        });

        // Get list view for all groups available
        allGroups = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CheckmateKey.GROUP_FIREBASE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allGroups = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                Map<String, Object> data = document.getData();
//                                ArrayList<String> tags = (ArrayList<String>) data.get("tags");
                                Group g = document.toObject(Group.class);
//                                document.
//                                g.setCreatorId((String) data.get("creatorId"));
                                allGroups.add(g);
                                Log.i(TAG, "onComplete: GOT GROUP" + g.getTags().toString());
                            }
                            groupAdapter = new GroupListViewAdapter(getContext(), allGroups.toArray(new Group[allGroups.size()]), getParentFragmentManager());
                            listview.setAdapter(groupAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}