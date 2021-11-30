package com.teamred.checkmate.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.teamred.checkmate.R;
import com.teamred.checkmate.Searchable;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.model.Ranking;
import com.teamred.checkmate.databinding.FragmentGroupDetailBinding;
import com.teamred.checkmate.databinding.FragmentSearchGroupBinding;
import com.teamred.checkmate.ui.notifications.NotificationsViewModel;
import com.teamred.checkmate.ui.search.FilterDialogFragment;
import com.teamred.checkmate.ui.search.SearchGroupFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailFragment extends Fragment implements Searchable {

    private FragmentGroupDetailBinding binding;

//    private Note[] noteList;

    private ListAdapter noteAdapter;
//    private ListAdapter groupAdapter;
    private ListView listView;
    private TextView title;
    private TextView creator;
    private TextView desc;
    private SearchView searchKeywords;
    private Spinner searchType;
    private String[] queryType;
    private Spinner filter;
    private Spinner ranking;

    private String[] subtopics;
    private boolean[] groupStatusSelected = new boolean[]{true, true};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentGroupDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // binding
        title = binding.groupDetailTitle;
        creator = binding.groupDetailCreator;
        desc = binding.groupDetailDesc;

        ranking = binding.noteListRanking;
        filter = binding.noteListFilter;

        Bundle arguments = getArguments();
        if (arguments!= null){
            title.setText(arguments.getString("title"));
            creator.setText(arguments.getString("creator"));
            desc.setText(arguments.getString("desc"));
            subtopics = arguments.getStringArray("subtopics");

        }


        searchKeywords = binding.searchNote;
//        listView = binding.searchResultList;
//        searchType = binding.searchType;
//        filter = binding.btnFilter;
//        ranking = binding.spnRanking;




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
//                String filters = generateFilterString();
//                AlgoliaDataSource.getInstance(getContext()).searchGroup(SearchGroupFragment.this, "group", s, queryType, filters);
////                updateSearchResult(demos);
//                searchKeywords.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

//        // dropdown spinner menu. Select the field you want to search
//        searchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String current = getResources().getStringArray(R.array.search_type)[position];
//                String[] arr = getResources().getStringArray(R.array.search_type);
//                List<String> list = new ArrayList<>();
//                if (position == 0){
//                    queryType = new String[]{"groupName", "creator", "description", "tags"};
//                }else{
//                    queryType = new String[]{current.toLowerCase()};
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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
                        GroupDetailFragment.this,
                        title+":note",
                        selected.getOrder(),
                        selected.getAttr());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateResult(){

    }

    @Override
    public void updateSearchResult(JSONObject content) {

    }
}
