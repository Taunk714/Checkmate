package com.teamred.checkmate.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.model.Note;
import com.teamred.checkmate.data.model.Ranking;
import com.teamred.checkmate.databinding.FragmentSearchGroupBinding;
import com.teamred.checkmate.ui.NoteListViewAdapter;
import com.teamred.checkmate.ui.notifications.NotificationsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupFragment extends Fragment implements FilterDialogFragment.FilterDialogListener{

    private NotificationsViewModel notificationsViewModel;
    private FragmentSearchGroupBinding binding;

//    private Note[] noteList;

    private ListAdapter noteAdapter;
    private ListView listView;
    private SearchView searchKeywords;
    private Spinner searchType;
    private String[] queryType;
    private Button filter;
    private Spinner ranking;

    private boolean[] groupStatusSelected = new boolean[]{true, true};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentSearchGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        queryType = getResources().getStringArray(R.array.search_type);

        // binding
        searchKeywords = binding.searchKeywords;
        listView = binding.searchResultList;
        searchType = binding.searchType;
        filter = binding.btnFilter;
        ranking = binding.spnRanking;



//
//        if (noteList != null && noteList.length > 0){
//            noteAdapter = new NoteListViewAdapter(getContext(), noteList);
//            listView.setAdapter(noteAdapter);
//        }

        // enter keywords and search
        searchKeywords.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // search algolia
                Toast.makeText(getContext(), "search algolia "+ s, Toast.LENGTH_LONG).show();
                String filters = filter.getText().toString();
                AlgoliaDataSource.getInstance(getContext()).searchNote(SearchGroupFragment.this, "group", s, queryType, filters);
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
                    queryType = new String[]{"groupName", "creator", "description", "tags"};
                }else{
                    queryType = new String[]{current.toLowerCase()};
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Ranking[] rankingAdapter = new Ranking[]{
                Ranking.Default,
                Ranking.Newest,
                Ranking.Oldest,
                Ranking.Popular};

        ranking.setAdapter(new ArrayAdapter<Ranking>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                rankingAdapter));

        ranking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Ranking selected = rankingAdapter[position];
                AlgoliaDataSource.getInstance(getContext()).setCustomRanking(
                        SearchGroupFragment.this,
                        selected.getOrder(),
                        selected.getAttr());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Dia
                new FilterDialogFragment().show(getChildFragmentManager(), " FilterDialogFragment");
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

    public boolean[] getGroupStatusSelected() {
        return groupStatusSelected;
    }

    public void setGroupStatusSelected(boolean[] groupStatusSelected) {
        this.groupStatusSelected = groupStatusSelected;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) dialog;
//        filterDialogFragment.setGroupStatusSelect(groupStatusSelected);
        groupStatusSelected = filterDialogFragment.getGroupStatusSelect().clone();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) dialog;
        filterDialogFragment.setGroupStatusSelect(groupStatusSelected);
    }
}