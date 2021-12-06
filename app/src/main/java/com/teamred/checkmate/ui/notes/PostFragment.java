package com.teamred.checkmate.ui.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamred.checkmate.data.model.Post;
import com.teamred.checkmate.databinding.FragmentPostBinding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private Post post;
    final String TAG = "PostFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PostListViewModel postListModel = new ViewModelProvider(requireActivity()).get(PostListViewModel.class);
        this.post = postListModel.getSelected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d(TAG, this.post.getTitle());
        binding.postTitle.setText(this.post.getTitle());
        binding.postAuthorTV.setText(this.post.getAuthor());
        binding.postDate.setText(this.post.getCreateDate().toString());
        binding.postContentTV.setText(this.post.getContent());
        binding.postSubTopic.setText(this.post.getsubtopic());

        // TODO: Use state of logged in user for document path
        DocumentReference userPost = db.collection("user").document("c1tw3AB25SRVEqBNBw6GXoDc6X23").collection("savedPosts").document(post.getPostID());
        userPost.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // set the checked for "reminder switch" to be true
                        binding.reminderSwitch.setChecked(true);
                    } else {
                        binding.reminderSwitch.setChecked(false);
                    }
                }
            }
        });

        binding.reminderSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if state is checked, then add this to user's posts collection and set the
                // reminder state to true, along with the date that this was saved.
                // if user unchecks or rechecks this, the entry will be replaced
                if (binding.reminderSwitch.isChecked()) {
                    Log.d(TAG, "onClick: Is checked");

                    // TODO: Use state of logged in user for isAuthor.
                    final boolean isAuthor = post.getAuthor() == "";

                    Map<String, Object> data = new HashMap<>();
                    data.put("dateLastReviewed", Calendar.getInstance().getTime());
                    data.put("isAuthor", isAuthor);
                    data.put("isStarred", true);
                    data.put("starredDate", Calendar.getInstance().getTime());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // TODO: Use state of logged in user for document path
                    CollectionReference posts = db.collection("user").document("c1tw3AB25SRVEqBNBw6GXoDc6X23").collection("savedPosts");
                    posts.document(post.getPostID()).set(data);
                } else {
                    Log.d(TAG, "onClick: Is not checked");
                    db.collection("user").document("c1tw3AB25SRVEqBNBw6GXoDc6X23")
                            .collection("savedPosts").document(post.getPostID()).delete().addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i(TAG, "onSuccess: Post reference successfully removed from the user's sub-collection");
                                }
                            }
                    );
                }
            }
        });

        binding.openNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getOnenoteAppURL()));
                startActivity(browserIntent);
            }
        });

        binding.webView.getSettings().setJavaScriptEnabled(true);
        if (post.getOnenoteWebURL() != null) {
            binding.webView.loadUrl(post.getOnenoteWebURL());
            binding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }
            });
        }

        if (post.getOnenoteAppURL() == null) {
            binding.openNoteBtn.setVisibility(View.GONE);
        }

        return root;
    }
}