package com.teamred.checkmate.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.Searchable;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.Ranking;
import com.teamred.checkmate.databinding.FragmentGroupDetailBinding;

import org.json.JSONObject;

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

    private String creatorId;

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
            creatorId = arguments.getString("creatorId");
            desc.setText(arguments.getString("desc"));
            subtopics = arguments.getStringArray("subtopics");
        }

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.getMaxLines() == 3){
                    desc.setMaxLines(99);
                }else{
                    desc.setMaxLines(3);
                }
            }
        });


        searchKeywords = binding.searchNote;
        String numThreads = subtopics.length + " Available Threads";
        binding.numberOfThread.setText(numThreads);
        binding.noteListFilter.setAdapter(
                new ArrayAdapter<String>(
                        getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        generateAdapterArray(subtopics)));

        creator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to user profile
                // uid is creatorId
                String uid = creatorId;
            }
        });

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
                AlgoliaDataSource.getInstance().setCustomRanking(
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

    private String[] generateAdapterArray(String[] subtopics){
        String[] ret = new String[1 + subtopics.length];
        ret[0] = "All";
        if (subtopics.length == 0){
            return ret;
        }
        System.arraycopy(subtopics, 0, ret, 1, subtopics.length);
        return ret;
    }
}
