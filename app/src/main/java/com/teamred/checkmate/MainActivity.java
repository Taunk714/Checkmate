package com.teamred.checkmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.databinding.ActivityBottomMenuMainBinding;
import com.teamred.checkmate.ui.login.LoginActivity;

import com.teamred.checkmate.databinding.ActivityBottomMenuMainBinding;
import com.teamred.checkmate.ui.search.FilterDialogFragment;
import com.teamred.checkmate.ui.search.SearchGroupFragment;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {

    private ActivityBottomMenuMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBottomMenuMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_host);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
//        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//
//
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_notification, R.id.navigation_search)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
//        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) dialog;
//        filterDialogFragment.setGroupStatusSelect(groupStatusSelected);
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        SearchGroupFragment fragmentById = (SearchGroupFragment) (getSupportFragmentManager().findFragmentById(R.id.navigation_search));
        SearchGroupFragment fragmentById = (SearchGroupFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_host).getChildFragmentManager().getPrimaryNavigationFragment();
        assert fragmentById != null;
        fragmentById.setGroupStatusSelected(filterDialogFragment.getGroupStatusSelect().clone());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) dialog;

        SearchGroupFragment fragmentById = (SearchGroupFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_host).getChildFragmentManager().getPrimaryNavigationFragment();
        assert fragmentById != null;
        filterDialogFragment.setGroupStatusSelect(fragmentById.getGroupStatusSelected());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Constant.getInstance().setCurrentUser(null);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}