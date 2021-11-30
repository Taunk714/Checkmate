package com.teamred.checkmate;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.teamred.checkmate.databinding.ActivityBottomMenuBinding;
import com.teamred.checkmate.ui.search.FilterDialogFragment;
import com.teamred.checkmate.ui.search.SearchGroupFragment;

public class MainActivity2 extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {

private ActivityBottomMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityBottomMenuBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_notification, R.id.navigation_search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) dialog;
//        filterDialogFragment.setGroupStatusSelect(groupStatusSelected);
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        SearchGroupFragment fragmentById = (SearchGroupFragment) (getSupportFragmentManager().findFragmentById(R.id.navigation_search));
        SearchGroupFragment fragmentById = (SearchGroupFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main2).getChildFragmentManager().getPrimaryNavigationFragment();
        assert fragmentById != null;
        fragmentById.setGroupStatusSelected(filterDialogFragment.getGroupStatusSelect().clone());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) dialog;

        SearchGroupFragment fragmentById = (SearchGroupFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main2).getChildFragmentManager().getPrimaryNavigationFragment();
        assert fragmentById != null;
        filterDialogFragment.setGroupStatusSelect(fragmentById.getGroupStatusSelected());
    }

}