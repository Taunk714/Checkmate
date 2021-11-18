package com.teamred.checkmate.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import com.teamred.checkmate.ui.NoteListViewAdapter;
import com.teamred.checkmate.ui.notifications.NotificationsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentSearchBinding binding;

    private Note[] noteList;

    private ListAdapter noteAdapter;
    private ListView listView;
    private SearchView searchKeywords;
    private Spinner searchType;
    private String[] queryType;
    private EditText filter;

    private Button numDesc;
    private Button numAsc;
    private Button authorDesc;
    private Button authorAsc;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        queryType = getResources().getStringArray(R.array.search_type);

        // binding
        searchKeywords = binding.searchKeywords;
        listView = binding.searchResultList;
        searchType = binding.searchType;
        filter = binding.searchFilter;

        numAsc = binding.btnNumberAsc;
        numDesc = binding.btnNumberDesc;
        authorAsc = binding.btnAuthorAsc;
        authorDesc = binding.btnAuthorDesc;



//        Note test = new Note("12345",new String[]{"sdfgg"},"sedfddd");
//        test.setAuthor("Mr. TEST");
//        noteList = new Note[]{test, test,test,test, test,test,test, test,test};

        if (noteList != null && noteList.length > 0){
            noteAdapter = new NoteListViewAdapter(getContext(), noteList);
            listView.setAdapter(noteAdapter);
        }

        // enter keywords and search
        searchKeywords.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // search algolia
                Toast.makeText(getContext(), "search algolia "+ s, Toast.LENGTH_LONG).show();
                String filters = filter.getText().toString();
                AlgoliaDataSource.getInstance(getContext()).searchNote(SearchFragment.this, "demo", s, queryType, filters);
//                updateSearchResult(demos);
                searchKeywords.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // dropdown spinner menu. Select the field you want to search
        searchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String current = getResources().getStringArray(R.array.search_type)[position];
                String[] arr = getResources().getStringArray(R.array.search_type);
                List<String> list = new ArrayList<>();
                if (position == 0){
                    queryType = new String[]{"title", "tags", "content"};
                }else{
                    queryType = new String[]{current.toLowerCase()};
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        numDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlgoliaDataSource.getInstance(getContext()).setCustomRanking(SearchFragment.this, "desc", "number");
            }
        });

        numAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlgoliaDataSource.getInstance(getContext()).setCustomRanking(SearchFragment.this, "asc", "number");
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     *  update the listview based on the search result.
     * @param noteList content of the listview
     */
    public void updateSearchResult(Note[] noteList){
        noteAdapter = new NoteListViewAdapter(getContext(), noteList);
        listView.setAdapter(noteAdapter);
    }


}