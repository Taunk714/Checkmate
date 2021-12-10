package com.teamred.checkmate.ui.notes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.SavedPost;
import com.teamred.checkmate.data.model.Post;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentPostBinding;
import com.teamred.checkmate.services.NoteReviewReceiver;
import com.teamred.checkmate.ui.chat.ChatDetailFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostFragment extends Fragment {

    PostsViewModel postListModel;
    private FragmentPostBinding binding;
    private Post post;
    final String TAG = "PostFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private User postAuthor;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
            this.post = JSON.parseObject(arguments.getString("post"), Post.class);
        }else{
            postListModel = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);
            this.post = postListModel.getSelected();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d(TAG, this.post.getPostTitle());
        binding.postTitle.setText(this.post.getPostTitle());
        Task<DocumentSnapshot> targetUser = LoginDataSource.getTargetUser(this.post.getAuthor());
        targetUser.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                postAuthor = task.getResult().toObject(User.class);
                binding.postAuthorTV.setText(postAuthor.getUsername());
            }
        });

        binding.postDate.setText(this.post.getCreateDate().toString());
        binding.postContentTV.setText(this.post.getContent());
        binding.postSubTopic.setText(this.post.getSubtopic());

        User currentUser = Constant.getInstance().getCurrentUser();

        /* Checks to see if the user has saved the post */
        DocumentReference userSavedPost = db.collection("user").document(currentUser.getUid()).collection("savedPosts").document(post.getPostID());
        userSavedPost.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String d = (String) document.get("d");
                    if (document.exists()) {
                        // set the checked for "reminder switch" to be true
                        binding.reminderSwitch.setChecked(true);
                    } else {
                        binding.reminderSwitch.setChecked(false);
                    }
                }
            }
        });

        binding.postAuthorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to user profile
                // uid is creatorId
                if (postAuthor == null){
                    // go to profile
                    Toast.makeText(getContext(), "Wait a sec!", Toast.LENGTH_SHORT).show();
                } else if (postAuthor.getUid().equals(Constant.getInstance().getCurrentUser().getUid())) {
                    Toast.makeText(getContext(), "You can't talk with yourself", Toast.LENGTH_SHORT).show();
                } else {
                    FragmentManager fm = getParentFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("otherUser", JSON.toJSONString(postAuthor));
                    ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
                    chatDetailFragment.setArguments(bundle);
                    ft.replace(R.id.navigation_host, chatDetailFragment)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();

                }

            }
        });


        binding.reminderSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if state is checked, then add this to user's posts collection and set the
                // reminder state to true, along with the date that this was saved.
                // if user unchecks or rechecks this, the entry will be replaced
                User currentUser = Constant.getInstance().getCurrentUser();

                // set reminder for next time to review
                AlarmManager alarmManager;

                Intent i = new Intent(getContext(), NoteReviewReceiver.class);
                //PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                i.putExtra("Group_id", postListModel.getGroupID());
                i.putExtra("Post_id", post.getPostID());
                i.putExtra("post_title", post.getPostTitle());
                PendingIntent pi = PendingIntent.getBroadcast(getContext(), UUID.randomUUID().hashCode(),
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                long timeAtSwitchOn = System.currentTimeMillis();

                long tenSeconds = 1000 * 10;

                if (binding.reminderSwitch.isChecked()) {
                    Log.d(TAG, "onClick: Is checked");
                    final boolean isAuthor = post.getAuthor() == currentUser.getUsername();
                    SavedPost savedPost = new SavedPost();
                    savedPost.setReviewCount(0);
                    savedPost.setPostTitle(post.getPostTitle());
                    savedPost.setGroupId(postListModel.getGroupID());
                    savedPost.setPostId(post.getPostID());
                    savedPost.setAuthor(isAuthor);
                    savedPost.setLastReview(new Date());
//                    savedPost.setLiked();

//                    Map<String, Object> data = new HashMap<>();
//                    data.put("dateLastReviewed", Calendar.getInstance().getTime());
//                    data.put("isAuthor", isAuthor);
//                    data.put("isStarred", true);
//                    data.put("starredDate", Calendar.getInstance().getTime());
//                    data.put("numTimesReviewed", 0);
//                    data.put("groupIDPostBelongsTo", postListModel.getGroupID());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    CollectionReference posts = db.collection(CheckmateKey.USER_FIREBASE)
                            .document(currentUser.getUid())
                            .collection(CheckmateKey.SAVE_POST);
                    posts.document(post.getPostID()).set(JSON.toJSON(savedPost));


                    Toast.makeText(getContext(), "Reminder Set!", Toast.LENGTH_SHORT).show();

                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtSwitchOn + tenSeconds, pi);

                } else {
                    Toast.makeText(getContext(), "Reminder Canceled.", Toast.LENGTH_SHORT).show();
                    alarmManager.cancel(pi);

                    Log.d(TAG, "onClick: Is not checked");
                    db.collection("user").document(currentUser.getUid())
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