package com.teamred.checkmate.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.model.Note;
import com.teamred.checkmate.databinding.FragmentNotificationsBinding;
import com.teamred.checkmate.databinding.FragmentSearchBarBinding;
import com.teamred.checkmate.databinding.FragmentSearchBinding;
import com.teamred.checkmate.ui.notifications.NotificationsViewModel;

public class SearchFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentSearchBinding binding;

    private Note[] noteList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textRecommendation;
        SearchView searchKeywords = binding.searchBarFrag.findViewById(R.id.search_keywords);
        ListView listView = binding.searchResultList;
//        ListAdapter noteAdapter = new ArrayAdapter<Note>(getContext(), noteList);
        if (noteList != null && noteList.length > 0){

        }
        searchKeywords.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // search algolia
                Toast.makeText(getContext(), "search algolia "+ s, Toast.LENGTH_LONG).show();
                AlgoliaDataSource.getInstance().searchNote(SearchFragment.this, "demo", s);
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