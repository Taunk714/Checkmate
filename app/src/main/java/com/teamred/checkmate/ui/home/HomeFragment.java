package com.teamred.checkmate.ui.home;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.teamred.checkmate.R;
import com.teamred.checkmate.ui.calendar.CalendarActivity;
import com.teamred.checkmate.ui.group.CreateGroupActivity;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.databinding.FragmentHomeBinding;
import com.teamred.checkmate.ui.group.GroupDetailFragment;
import com.teamred.checkmate.ui.login.LoginActivity;
import com.teamred.checkmate.MyGroupsActivity;

import java.text.DateFormatSymbols;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //private Button calenderHeatmap;

//    String[] testArray = {"Android","IPhone","WindowsMobile","Blackberry",
//            "WebOS","Ubuntu","Windows7","Max OS X"};
//
//    ListView lvMonth;
//    String[] months;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AlgoliaDataSource.initAlgolia();

        //calenderHeatmap = (Button) getView().findViewById(R.id.calendarheatmap);
        Button calendarHeatmap = binding.calendarheatmap;

        Button myGroups = binding.myGroupsBtn;

        myGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyGroupsActivity.class);
                startActivity(i);
            }
        });

        calendarHeatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CalendarActivity.class);
                startActivity(i);
            }
        });

//        Button tempGroupDetailBtn = binding.groupDetailButton;
//        tempGroupDetailBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment fragment = new GroupDetailFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("groupDocID", "0JG8VeJxHYJ83y9nZF3Y");
//                fragment.setArguments(bundle);
//                FragmentManager manager = getParentFragmentManager();
//
//                manager.beginTransaction()
//                        .replace(R.id.navigation_host, fragment, null)
//                        .setReorderingAllowed(true)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

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


        return root;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        lvMonth = view.findViewById(R.id.lvMonth);
//        months = new DateFormatSymbols().getMonths();
//        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
//                android.R.layout.simple_list_item_1, testArray);
//        lvMonth.setAdapter(monthAdapter);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}