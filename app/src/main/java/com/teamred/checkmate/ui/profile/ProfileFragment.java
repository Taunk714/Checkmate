package com.teamred.checkmate.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.model.LoggedInUser;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentProfileBinding;
import com.teamred.checkmate.ui.group.CreateGroupActivity;
import com.teamred.checkmate.ui.group.GroupDetailFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ProfileFragment extends Fragment {

private FragmentProfileBinding binding;
private FrameLayout relative;
private ListView list;
private TextView tName;
private TextView tUsername;
private TextView tAbout_me;
private Button edit;
 String items[]
            = { "Algorithms", "Data Structures",
            "Languages", "CS501",
            "CS412", "INTRO TO CS",
            "Data Science", "CS Subjects",
            "Web Technologies" };

    Map<String, Object> userMap = new HashMap<>();
    User user = new User();
    String name = user.getName();
    // String username = user.getUsername();
    String profile_pic = user.getPhotoUrl();
    String userN;
    List<Object> savedP = new ArrayList<>();
    List<String> savedPNames = new ArrayList<>();

    TextView usernameTxtView;
    // String username;
    TextView numGroupsTxtView;
    // Integer numGroups;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        list = binding.reminderlist;

        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, items);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(arr);

        // firebase auth to check current user

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d("ptest", user.getUid());
            Log.d("ptest", user.getEmail());

            String[] arrOfStr = user.getEmail().split("@", 2);
            userN = arrOfStr[0];

            userMap.put("uid", user.getUid());
            userMap.put("email", user.getEmail());
            userMap.put("username", userN);
            // Log.d("ptest", user.getPhotoUrl().toString());
            // Log.d("ptest", user.getDisplayName());

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("user").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("ptest2", "DocumentSnapshot data: " + document.getData().get("groupJoined"));
                            userMap.put("groupJoined", document.getData().get("groupJoined"));
                            userMap.put("numGroups", document.getData().get("groupJoined").toString().split(",").length);
                            Log.d("ptest2", userMap.toString());

                            db.collection("user").document(user.getUid()).collection("savedPosts")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("ptest3", document.getId() + " => " + document.getData());

                                                    DocumentReference docRef = db.collection("groups").document(document.getData().get("groupIDPostBelongsTo").toString()).collection("posts").document(document.getId());
                                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {
                                                                    Log.d("ptest4", "DocumentSnapshot data: " + document.getData());
                                                                    savedP.add(document.getData());
                                                                    savedPNames.add(document.getData().get("postTitle").toString());
                                                                    userMap.put("savedPosts", savedP);
                                                                    userMap.put("savedPostNames", savedPNames);
                                                                    Log.d("ptest4", userMap.toString());

                                                                    usernameTxtView.setText(userMap.get("username").toString());
                                                                    numGroupsTxtView.setText(userMap.get("numGroups").toString() + " groups joined");

                                                                    // work on getting things to review today working
                                                                    // savedPNames.toArray();

                                                                    ArrayAdapter<String> reviewAdapter = new ArrayAdapter<String>(getContext(),
                                                                            android.R.layout.simple_list_item_multiple_choice, savedPNames);
                                                                    list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                                                    list.setAdapter(reviewAdapter);

                                                                    // ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getApplication().getApplicationContext(),
                                                                    //         android.R.layout.simple_list_item_1, groupsPrint);
                                                                    // lvMonth.setAdapter(monthAdapter);
                                                                } else {
                                                                    Log.d("ptest4", "No such document");
                                                                }
                                                            } else {
                                                                Log.d("ptest4", "get failed with ", task.getException());
                                                            }
                                                        }
                                                    });



                                                }
                                            } else {
                                                Log.d("ptest3", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                            // CollectionReference docRef = db.collection("user").document(user.getUid()).collection("savedPosts");
                            // docRef.get().addOnCompleteListener(new OnCompleteListener<CollectionSnapshot>() {
                            //     @Override
                            //     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //         if (task.isSuccessful()) {
                            //             DocumentSnapshot document = task.getResult();
                            //             if (document.exists()) {
                            //                 Log.d("ptest2", "DocumentSnapshot data: " + document.getData().get("groupJoined"));
                            //                 userMap.put("groupJoined", document.getData().get("groupJoined"));
                            //                 userMap.put("numGroups", document.getData().get("groupJoined").toString().split(",").length);
                            //                 Log.d("ptest2", userMap.toString());
                            //             } else {
                            //                 Log.d("ptest2", "No such document");
                            //             }
                            //         } else {
                            //             Log.d("ptest2", "get failed with ", task.getException());
                            //         }
                            //     }
                            // });



                        } else {
                            Log.d("ptest2", "No such document");
                        }
                    } else {
                        Log.d("ptest2", "get failed with ", task.getException());
                    }
                }
            });

            // db.collection("user")
            //         .get()
            //         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            //             @Override
            //             public void onComplete(@NonNull Task<QuerySnapshot> task) {
            //                 if (task.isSuccessful()) {
            //                     for (QueryDocumentSnapshot document : task.getResult()) {
            //                         Log.d("ptest", document.getId() + " => " + document.getData().get("name"));
            //                     }
            //                 } else {
            //                     Log.w("ptest", "Error getting documents.", task.getException());
            //                 }
            //             }
            //         });

        } else {
            // No user is signed in
        }







        edit = binding.editProfileBtn;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                Toast.makeText(getContext(), "Edit Profile", Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set this stuff
        usernameTxtView = (TextView) getView().findViewById(R.id.username_tv);
        numGroupsTxtView = (TextView) getView().findViewById(R.id.numgroupsjoined);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}