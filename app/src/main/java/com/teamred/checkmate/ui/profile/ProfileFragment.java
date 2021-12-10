package com.teamred.checkmate.ui.profile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.alibaba.fastjson.JSON;
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
import com.teamred.checkmate.services.NoteReviewReceiver;
import com.teamred.checkmate.ui.chat.FriendlyMessageAdapter;
import com.teamred.checkmate.ui.group.CreateGroupActivity;
import com.teamred.checkmate.ui.group.GroupDetailFragment;
import com.teamred.checkmate.ui.login.AfterRegisterActivity;
import com.teamred.checkmate.ui.login.LoginActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
            = { "None yet. Add some!" };

//    Map<String, Object> userMap = new HashMap<>();
//    User user = new User();
//    String name = user.getName();
    // String username = user.getUsername();
//    String profile_pic = user.getPhotoUrl();
//    String userN;
    List<Object> savedP = new ArrayList<>();
    List<String> savedPNames = new ArrayList<>();
    List<String> savedPIDs = new ArrayList<>();



    String groupIDPostBelongsTo = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        list = binding.reminderlist;

        /*ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, items);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(arr);*/

        // firebase auth to check current user

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (Constant.getInstance().getCurrentUser() != null) {
            User me = Constant.getInstance().getCurrentUser();
            binding.usernameTv.setText(me.getUsername());
            if (me.getPhotoUrl() != null){
                FriendlyMessageAdapter.loadImageIntoView(binding.pPic, me.getPhotoUrl());
            }
            if (me.getGroupJoined() == null || me.getGroupJoined().size() == 0) {
                binding.numgroupsjoined.setText(0 + " groups joined");
            } else {
                binding.numgroupsjoined.setText(me.getGroupJoined().size() + " groups joined");
            }

            FirebaseFirestore.getInstance()
                    .collection("user").document(me.getUid()).collection("savedPosts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("ptest3", document.getId() + " => " + document.getData());

                                    groupIDPostBelongsTo = document.getData().get("groupIDPostBelongsTo").toString();

                                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("groups").document(document.getData().get("groupIDPostBelongsTo").toString()).collection("posts").document(document.getId());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d("ptest4", "DocumentSnapshot data: " + document.getData());
                                                    savedP.add(document.getData());
                                                    savedPNames.add(document.getData().get("postTitle").toString());
                                                    savedPIDs.add(document.getId());
//                                                    userMap.put("savedPosts", savedP);
//                                                    userMap.put("savedPostNames", savedPNames);
                                                    Log.d("ptest4", JSON.toJSONString(me));


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
        }else if(fbUser != null){
            // User is signed in
            User me = new User();
            Log.d("ptest", fbUser.getUid());
            Log.d("ptest", fbUser.getEmail());

            String[] arrOfStr = fbUser.getEmail().split("@", 2);
//            userN = arrOfStr[0];
            me.setUid(fbUser.getUid());
            // userMap.put("username", userN);
            // Log.d("ptest", user.getPhotoUrl().toString());
            // Log.d("ptest", user.getDisplayName());

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("user").document(fbUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);

                            binding.usernameTv.setText(user.getUsername());
                            if (user.getPhotoUrl() != null){
                                FriendlyMessageAdapter.loadImageIntoView(binding.pPic, user.getPhotoUrl());
                            }
                            if (user.getGroupJoined() == null || user.getGroupJoined().size() == 0) {
                                binding.numgroupsjoined.setText(0 + " groups joined");
                            } else {
                                binding.numgroupsjoined.setText(user.getGroupJoined().size() + " groups joined");
                            }

                            db.collection("user").document(user.getUid()).collection("savedPosts")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("ptest3", document.getId() + " => " + document.getData());

                                                    groupIDPostBelongsTo = document.getData().get("groupIDPostBelongsTo").toString();

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
                                                                    savedPIDs.add(document.getId());
//                                                                    userMap.put("savedPosts", savedP);
//                                                                    userMap.put("savedPostNames", savedPNames);
                                                                    Log.d("ptest4", JSON.toJSONString(user));



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

        binding.signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Constant.getInstance().setCurrentUser(null);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        // list.isItemChecked(position)

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                // set reminder for next time to review
                //Object selectedFromList = (list.getItemAtPosition(position));
                AlarmManager alarmManager;

                Intent i = new Intent(getContext(), NoteReviewReceiver.class);
                //PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                i.putExtra("Post_id", savedPIDs.get(position));
                i.putExtra("Group_id", groupIDPostBelongsTo);
                i.putExtra("post_title", savedPNames.get(position));
                PendingIntent pi = PendingIntent.getBroadcast(getContext(), UUID.randomUUID().hashCode(),
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                long timeAtSwitchOn = System.currentTimeMillis();

                long tenSeconds = 1000 * 10;

                if (list.isItemChecked(position)) {
                    Toast.makeText(getContext(), "Reminder Set!", Toast.LENGTH_SHORT).show();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtSwitchOn + tenSeconds, pi);
                } else {
                    Toast.makeText(getContext(), "Reminder Canceled.", Toast.LENGTH_SHORT).show();
                    alarmManager.cancel(pi);
                }
            } //end onItemClick
        });


        edit = binding.editProfileBtn;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AfterRegisterActivity.class);
                Toast.makeText(getContext(), "Edit Profile", Toast.LENGTH_LONG).show();
                startActivity(i);
                getActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set this stuff
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}