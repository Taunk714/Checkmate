package com.teamred.checkmate.ui.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamred.checkmate.R;
import com.teamred.checkmate.databinding.FragmentCreateNoteBinding;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateNoteFragment extends Fragment {

    FragmentCreateNoteBinding binding;

    final String TAG = "CreateNoteFragment";

    private String groupID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CreateNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param groupID GroupID
     * @return A new instance of fragment CreateNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNoteFragment newInstance(String groupID) {
        CreateNoteFragment fragment = new CreateNoteFragment();
        Bundle args = new Bundle();
        args.putString("groupID", groupID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupID = getArguments().getString("groupID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateNoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String postTitle = binding.postTitle.getText().toString();
                final String subTopic = binding.subTopic.getText().toString();
                final List<String> tags = Arrays.asList(binding.tags.getText().toString().split(",").clone());
                final String content = binding.postContent.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("postTitle", postTitle);
                data.put("subTopic", subTopic);
                data.put("tags", tags);
                data.put("content", content);
                data.put("createdDate", Calendar.getInstance().getTime());

                CollectionReference posts = db.collection("Groups").document(groupID).collection("posts");
                posts.add(data);

                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}