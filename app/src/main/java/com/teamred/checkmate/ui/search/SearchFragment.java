package com.teamred.checkmate.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.teamred.checkmate.R;
import com.teamred.checkmate.databinding.FragmentNotificationsBinding;
import com.teamred.checkmate.databinding.FragmentSearchBarBinding;
import com.teamred.checkmate.databinding.FragmentSearchBinding;
import com.teamred.checkmate.ui.notifications.NotificationsViewModel;

public class SearchFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentSearchBarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentSearchBarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textRecommendation;
        SearchView searchKeywords = binding.searchKeywords;
        searchKeywords.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // search algolia
                Toast.makeText(getContext(), "search algolia", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
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