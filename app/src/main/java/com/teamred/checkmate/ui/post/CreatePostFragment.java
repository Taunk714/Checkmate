package com.teamred.checkmate.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.google.firebase.auth.FirebaseAuth;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.model.Note;
import com.teamred.checkmate.databinding.FragmentAddPostBinding;

public class CreatePostFragment extends Fragment {
    private FragmentAddPostBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel =
//                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView postTitle = binding.postTitle;
        TextView postTag = binding.postTag;
        TextView post = binding.postContent;
        TextView tag = binding.tagEditor;
        Button submit = binding.submitPost;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = String.valueOf(postTitle.getText());
                String[] tags = tag.getText().toString().split(";");
                String content = String.valueOf(post.getText());
                Toast.makeText(getActivity().getApplicationContext(), "send to firebase", Toast.LENGTH_LONG).show();
                // upload to firebase
                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setTags(tags);
                note.setAuthor(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                String s = JSON.toJSONString(note);
                AlgoliaDataSource.getInstance().addRecord("demo", s);
                getActivity().finish();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
