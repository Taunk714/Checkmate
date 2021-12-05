package com.teamred.checkmate;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

        // nav Controller refers to the fragment which will contain our different app views
//        NavController navController = Navigation.findNavController(this, R.id.navigation_host);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_host);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        /**
         * commented out the following to make UI cleaner. No need for action bar.
         */
//      AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_profile, R.id.navigation_search).build();
//      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
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

}