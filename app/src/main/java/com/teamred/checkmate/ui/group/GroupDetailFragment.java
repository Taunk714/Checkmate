package com.teamred.checkmate.ui.group;

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

    private String[] subtopics;
    private boolean[] groupStatusSelected = new boolean[]{true, true};


    /* Actual stuff */
    private String TAG = "GroupDETAIL";
    private Group groupRef; // selected Group


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGroupDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle arguments = getArguments();
        if (arguments != null) {
            String groupDocID = arguments.getString("groupDocID");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Groups").document(groupDocID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Map<String, Object> data = document.getData();
                            groupRef = new Group(groupDocID, data.get("groupName").toString(),
                                    (String[]) data.get("tags"),
                                    data.get("creatorUsername").toString(),
                                    data.get("description").toString());

                            binding.groupDetailTitle.setText(groupRef.getGroupName());
                            binding.groupDetailCreator.setText(groupRef.getCreatorUsername());
                            binding.groupDetailDescription.setText(groupRef.getDescription());
                            binding.groupDetailNumMembers.setText(data.get("numMembers").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                    PostListViewModel postListModel = new ViewModelProvider(requireActivity()).get(PostListViewModel.class);
                    postListModel.init(groupDocID);

                    postListModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
                        // Update UI
                        noteAdapter = new PostListViewAdapter(getContext(), posts.toArray((new Post[posts.size()])), postListModel);
                        listView.setAdapter(noteAdapter);
                    });
                }
            });
        }


        searchKeywords = binding.searchNote;
        listView = binding.noteListView;
//        searchType = binding.searchType;
        filter = binding.noteListFilter;
        ranking = binding.noteListRanking;

        binding.createNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Create Note fragment
                Fragment createNoteFragment = CreateNoteFragment.newInstance(groupRef.getGroupDocumentID());

                FragmentManager manager = getParentFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.navigation_host, createNoteFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });


// enter keywords and search
        searchKeywords.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
// search algolia
                Toast.makeText(getContext(), "search algolia " + s, Toast.LENGTH_LONG).show();
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
                        title + ":note",
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

    private void updateResult() {

    }

    @Override
    public void updateSearchResult(JSONObject content) {

    }
}
