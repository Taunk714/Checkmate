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

import com.teamred.checkmate.databinding.FragmentAddPostBinding;
import com.teamred.checkmate.databinding.FragmentSearchBinding;
import com.teamred.checkmate.databinding.FragmentSlideshowBinding;
import com.teamred.checkmate.ui.notifications.NotificationsViewModel;

public class AddPostFragment extends Fragment {
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
        Button submit = binding.submitPost;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = String.valueOf(postTitle.getText());
                String content = String.valueOf(post.getText());
                Toast.makeText(getContext(), "send to firebase", Toast.LENGTH_LONG).show();
                // upload to firebase
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
