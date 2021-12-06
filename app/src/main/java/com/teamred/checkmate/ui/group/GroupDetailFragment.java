package com.teamred.checkmate.ui.group;

import static com.teamred.checkmate.data.model.Group.isJoined;
import static com.teamred.checkmate.data.model.Group.joinGroup;
import static com.teamred.checkmate.data.model.Group.removeGroup;

import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.Searchable;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.data.model.Post;
import com.teamred.checkmate.data.model.Ranking;
import com.teamred.checkmate.databinding.FragmentGroupDetailBinding;
import com.teamred.checkmate.ui.PostListViewAdapter;
import com.teamred.checkmate.ui.notes.CreateNoteFragment;
import com.teamred.checkmate.ui.notes.PostFragment;
import com.teamred.checkmate.ui.notes.PostListViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupDetailFragment extends Fragment implements Searchable {

    private FragmentGroupDetailBinding binding;

    private List<Post> postList;

    private ListAdapter noteAdapter;
    private ListView listView;
    private TextView title;
    private TextView creator;
    private TextView desc;
    private SearchView searchKeywords;
    private Spinner searchType;
    private String[] queryType;
    private Spinner filter;
    private Spinner ranking;

    private boolean joined;

    private String creatorId;

    private Group group;

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
        listView = binding.noteListView;

        Bundle arguments = getArguments();
        if (arguments!= null){
            group = JSON.parseObject(arguments.getString("group"), Group.class);
            title.setText(group.getGroupName());
            creator.setText(group.getCreator());
            desc.setText(group.getDescription());

            PostListViewModel postListModel = new ViewModelProvider(requireActivity()).get(PostListViewModel.class);
            postListModel.init(group.getObjectID());

            postListModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
                // Update UI
                noteAdapter = new PostListViewAdapter(getContext(), posts.toArray((new Post[posts.size()])), postListModel);
                listView.setAdapter(noteAdapter);
            });
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
        String numThreads = group.getSubTopics().size() + " Available Threads";
        binding.numberOfThread.setText(numThreads);
        binding.noteListFilter.setAdapter(
                new ArrayAdapter<String>(
                        getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        generateAdapterArray(group.getSubTopics())));

        creator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to user profile
                // uid is creatorId
                String uid = creatorId;
            }
        });


        binding.createNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Create Note fragment
                Fragment createNoteFragment = CreateNoteFragment.newInstance(group.getObjectID());

                FragmentManager manager = getParentFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.navigation_host, createNoteFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });



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

        joined = isJoined(group.getObjectID(), Constant.getInstance().getCurrentUser().getGroupJoined());
        if (joined){
            disableJoined();
        }else{
            enableJoined();
        }

        binding.joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.joinGroupButton.setEnabled(false);
                if (joined){
                    removeGroup(Constant.getInstance().getCurrentUser(), group.getObjectID());
                    FirebaseFirestore.getInstance().collection("user").document(Constant.getInstance().getCurrentUser().getUid()).set(JSON.toJSON(Constant.getInstance().getCurrentUser()));
                    group.removeMember();
                    Group.update(group);
                    binding.joinGroupButton.setEnabled(true);
                    enableJoined();

                }else{
                    joinGroup(Constant.getInstance().getCurrentUser(), group.getObjectID());
                    FirebaseFirestore.getInstance().collection("user").document(Constant.getInstance().getCurrentUser().getUid()).set(JSON.toJSON(Constant.getInstance().getCurrentUser()));
                    Group.update(group);
                    binding.joinGroupButton.setEnabled(true);
                    disableJoined();

                }
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

    private String[] generateAdapterArray(List<String> subtopics){
        String[] ret = new String[1 + subtopics.size()];
        ret[0] = "All";
        if (subtopics.size() == 0){
            return ret;
        }
        System.arraycopy(subtopics, 0, ret, 1, subtopics.size());
        return ret;
    }

    private void enableJoined(){
        binding.joinGroupButton.setText("JOINED");
        binding.joinGroupButton.setBackgroundResource(R.color.background);
    }

    private void disableJoined(){
        binding.joinGroupButton.setText("JOINED");
        binding.joinGroupButton.setBackgroundResource(R.color.background);
    }
}
