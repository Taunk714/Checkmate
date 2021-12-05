package com.teamred.checkmate.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //private Button calenderHeatmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AlgoliaDataSource.initAlgolia();

        //calenderHeatmap = (Button) getView().findViewById(R.id.calendarheatmap);
        Button calendarHeatmap = binding.calendarheatmap;

        calendarHeatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CalendarActivity.class);
                startActivity(i);
            }
        });

        Button tempGroupDetailBtn = binding.groupDetailButton;
        tempGroupDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GroupDetailFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main2, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

                Bundle bundle = new Bundle();
                bundle.putString("groupDocID", "0JG8VeJxHYJ83y9nZF3Y");
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), fragment, "findThisFragment")
                        .commit();
            }
        });

//        FloatingActionButton btnAddPost = binding.btnAddPost;
//        btnAddPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent i = new Intent(getActivity(), AddPostActivity.class);
//
//                if (FirebaseAuth.getInstance().getCurrentUser() == null){
//                    Intent login = new Intent(getActivity(), LoginActivity.class);
//                    Toast.makeText(getContext(), "Interceptor! Jump to login", Toast.LENGTH_LONG).show();
//                    login.putExtra("callback", CreateGroupActivity.class.getCanonicalName());
//                    startActivity(login);
//                }else{
//                    Intent i = new Intent(getActivity(), CreateGroupActivity.class);
//                    Toast.makeText(getContext(), "click create group", Toast.LENGTH_LONG).show();
//                    startActivity(i);
//                }
//            }
//        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}