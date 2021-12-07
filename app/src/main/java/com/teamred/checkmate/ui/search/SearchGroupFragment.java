package com.teamred.checkmate.ui.search;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.Searchable;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.FireStoreDataSource;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.data.model.Ranking;
import com.teamred.checkmate.databinding.FragmentSearchGroupBinding;
import com.teamred.checkmate.ui.GroupListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupFragment extends Fragment implements FilterDialogFragment.FilterDialogListener, Searchable {

    private FragmentSearchGroupBinding binding;

//    private Note[] noteList;

    private ListAdapter groupAdapter;
    private ListView listView;
    private SearchView searchKeywords;
    private Spinner searchType;
    private String[] queryType;
    private Button filter;
    private Spinner ranking;

    private boolean[] groupStatusSelected = new boolean[]{true, true};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        queryType = getResources().getStringArray(R.array.search_type);

        // binding
        searchKeywords = binding.searchKeywords;
        listView = binding.searchResultList;
        searchType = binding.searchType;
        filter = binding.btnFilter;
        ranking = binding.spnRanking;

        // enter keywords and search
        searchKeywords.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // search algolia
                Toast.makeText(getContext(), "search algolia "+ s, Toast.LENGTH_LONG).show();
                String filters = generateFilterString();
                AlgoliaDataSource.getInstance().search(SearchGroupFragment.this, "group", s, queryType, filters);
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

        FireStoreDataSource.getGroups().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                ArrayList<Group> groups = new ArrayList<>();
                for (DocumentSnapshot document : documents) {
                    Group group = document.toObject(Group.class);
                    group.setObjectID(document.getId());
                    groups.add(group);

                }
                listView.setAdapter(new GroupListViewAdapter(getContext(), groups.toArray(new Group[0]), getParentFragmentManager()));

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
                AlgoliaDataSource.getInstance().setCustomRanking(
                        SearchGroupFragment.this,
                        "group",
                        selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Dia
                FilterDialogFragment f = new FilterDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putBooleanArray("groupStatusSelect", groupStatusSelected);
                f.setArguments(bundle);
                f.show(getChildFragmentManager(), " FilterDialogFragment");
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("group", "search group click");
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.nav_host_fragment_content_main, new GroupDetailFragment())
//                        .commit();
//            }
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     *  update the listview based on the search result.
     * @param content content of the listview
     */
    public void updateSearchResult(JSONObject content){
        if (content == null){
            return;
        }
        System.out.println(content);
        try {
            JSONArray hits = content.getJSONArray("hits");  // get result
            int size = hits.length();
            Group[] groups = new Group[size];
            for (int i = 0; i < hits.length(); i++) { // change json to object
                String hitObj = hits.getString(i);
                Group group = JSON.parseObject(hitObj, Group.class);
                groups[i] = group;
            }
            groupAdapter = new GroupListViewAdapter(getContext(), groups, getParentFragmentManager());
            listView.setAdapter(groupAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
//        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) dialog;
//        filterDialogFragment.setGroupStatusSelect(groupStatusSelected);
    }

    public String generateFilterString(){
        return  (groupStatusSelected[0]? "status = 0 ": "")+
                ((groupStatusSelected[0]&& groupStatusSelected[1])?" OR ":"")+
                (groupStatusSelected[1]?"status = 1": "");
    }


}