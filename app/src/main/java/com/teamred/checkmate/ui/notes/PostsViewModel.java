package com.teamred.checkmate.ui.notes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.model.Post;
import com.teamred.checkmate.ui.PostListViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class expects there to be a group ID when the view model is Initialized
 */
public class PostsViewModel extends ViewModel {
    private MutableLiveData<List<Post>> posts;
    private Post selectedPost;
    private String groupID;
    DocumentReference docRef;
    final private String TAG = "PostsViewModel";

    public void init(String groupID) {
        this.groupID = groupID;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.docRef = db.collection(CheckmateKey.GROUP_FIREBASE).document(this.groupID);
        this.docRef.collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // Something was changed in the posts. Recreate our posts list.
                List<Post> postList = new ArrayList<Post>();
                for (QueryDocumentSnapshot document : value) {
                    Map<String, Object> data = document.getData();

                    String title = data.get("postTitle").toString();
                    List<String> tags = (ArrayList<String>) data.get("tags");
                    String author = data.get("author").toString();
                    String content = data.get("content").toString();
                    String subtopic = data.get("subTopic").toString();
                    String onenoteWebURL = data.get("onenoteWebURL") != null ? data.get("onenoteWebURL").toString() : null;
                    String onenoteAppURL = data.get("onenoteAppURL") != null ? data.get("onenoteAppURL").toString() : null;

                    Post n = new Post(document.getId(), title, subtopic, tags, author, content, onenoteWebURL, onenoteAppURL);

                    postList.add(n);
                }
                posts.setValue(postList);
            }
        });
    }

    public void select(int position) {
        selectedPost = posts.getValue().get(position);
    }

    public Post getSelected() {
        return selectedPost;
    }

    public LiveData<List<Post>> getPosts() {
        if (posts == null) {
            posts = new MutableLiveData<List<Post>>();
            loadPosts();
        }
        return posts;
    }

    private void loadPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(CheckmateKey.GROUP_FIREBASE).document(this.groupID);
        // Do an async operation to fetch posts.
        docRef.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Post> postList = new ArrayList<Post>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();

                                String title = data.get("postTitle").toString();
                                List<String> tags = (List<String>) data.get("tags");
                                String author = "TODO Author";
                                String content = data.get("content").toString();
                                String subtopic = data.get("subTopic").toString();
                                String onenoteWebURL = data.get("onenoteWebURL") != null ? data.get("onenoteWebURL").toString() : null;
                                String onenoteAppURL = data.get("onenoteAppURL") != null ? data.get("onenoteAppURL").toString() : null;

                                Post n = new Post(document.getId(), title, subtopic, tags, author, content, onenoteWebURL, onenoteAppURL);
                                postList.add(n);
                            }
                            posts.setValue(postList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public String getGroupID() {
        return groupID;
    }
}
