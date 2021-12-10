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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.SavedPost;
import com.teamred.checkmate.data.model.ReviewRecord;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentProfileBinding;
import com.teamred.checkmate.services.NoteReviewReceiver;
import com.teamred.checkmate.ui.chat.FriendlyMessageAdapter;
import com.teamred.checkmate.ui.login.AfterRegisterActivity;
import com.teamred.checkmate.ui.login.LoginActivity;
import com.teamred.checkmate.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
    List<SavedPost> savedP = new ArrayList<>();
    List<SavedPost> review = new LinkedList<>();
    List<SavedPost> noTask = new LinkedList<>();
//    List<String> savedPNames = new ArrayList<>();
//    List<String> savedPIDs = new ArrayList<>();



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
                    .collection("user").document(me.getUid()).collection(CheckmateKey.SAVE_POST)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    SavedPost savedPost = document.toObject(SavedPost.class);
                                    Log.d("ptest3", document.getId() + " => " + document.getData());
                                    savedP.add(savedPost);
                                    if (calDiff(savedPost.getLastReview(), new Date()) >= CheckmateKey.REVIEW_INTERNAL){
                                        review.add(savedPost);
                                    }else{
                                        noTask.add(savedPost);
                                    }

                                }

                                ArrayAdapter<SavedPost> reviewAdapter = new ArrayAdapter<>(getContext(),
                                        android.R.layout.simple_list_item_multiple_choice, review);
                                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                list.setAdapter(reviewAdapter);

                                ArrayAdapter<SavedPost> noTaskAdapter = new ArrayAdapter<>(getContext(),
                                        android.R.layout.simple_list_item_1, noTask);
                                binding.listviewDone.setAdapter(noTaskAdapter);
                            } else {
                                Log.d("ptest3", "Error getting documents: ", task.getException());
                            }
                        }
                    });
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

         binding.reminderlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 // go
                 //Object selectedFromList = (list.getItemAtPosition(position));
                 AlarmManager alarmManager;

                 Intent i = new Intent(getContext(), NoteReviewReceiver.class);
                 //PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                 SavedPost savedPost = review.get(position);
                 i.putExtra("Post_id", savedPost.getPostId());
                 i.putExtra("Group_id", savedPost.getGroupId());
                 i.putExtra("post_title", savedPost.getPostTitle());

                 savedPost.setReviewCount(savedPost.getReviewCount()+1);
                 savedPost.setLastReview(new Date());

                 FirebaseFirestore.getInstance()
                         .collection(CheckmateKey.USER_FIREBASE)
                         .document(Constant.getInstance().getCurrentUser().getUid())
                         .collection(CheckmateKey.SAVE_POST)
                         .document(savedPost.getPostId())
                         .update("reviewCount", savedPost.getReviewCount()
                                 , "lastReview", savedPost.getLastReview())
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 String simpleDateString = DateUtil.getSimpleDateString(savedPost.getLastReview());
                                 ReviewRecord reviewRecord = new ReviewRecord();
                                 reviewRecord.setTime(savedPost.getLastReview());
                                 reviewRecord.setTimes(savedPost.getReviewCount());
                                 reviewRecord.setPostTitle(savedPost.getPostTitle());
                                 FirebaseFirestore.getInstance().collection(CheckmateKey.REVIEW_RECORD)
                                         .document(Constant.getInstance().getCurrentUser().getUid())
                                         .collection(simpleDateString).add(JSON.toJSON(reviewRecord));
                             }
                         });



                 PendingIntent pi = PendingIntent.getBroadcast(getContext(), UUID.randomUUID().hashCode(),
                         i, PendingIntent.FLAG_UPDATE_CURRENT);

                 alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                 long timeAtSwitchOn = System.currentTimeMillis();

                 long tenSeconds = 1000 * 10;

                 if (list.isItemChecked(position)) {
                     Toast.makeText(getContext(), "Review!", Toast.LENGTH_SHORT).show();
                     alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtSwitchOn + tenSeconds, pi);
                 } else {
                     Toast.makeText(getContext(), "Reminder Canceled.", Toast.LENGTH_SHORT).show();
                     alarmManager.cancel(pi);
                 }
                 review.remove(savedPost);
                 noTask.add(savedPost);
                 ArrayAdapter<SavedPost> reviewAdapter = new ArrayAdapter<>(getContext(),
                         android.R.layout.simple_list_item_multiple_choice, review);
                 list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                 list.setAdapter(reviewAdapter);

                 ArrayAdapter<SavedPost> noTaskAdapter = new ArrayAdapter<>(getContext(),
                         android.R.layout.simple_list_item_1, noTask);
                 binding.listviewDone.setAdapter(noTaskAdapter);
                 return true;
             }
         });

        // list.isItemChecked(position)

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                // set reminder for next time to review
               // go to post

            } //end onItemClick
        });


        edit = binding.editProfileBtn;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AfterRegisterActivity.class);
//                Toast.makeText(getContext(), "Edit Profile", Toast.LENGTH_LONG).show();
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

    public int calDiff(Date date, Date latter){
        String simpleDateString = DateUtil.getSimpleDateString(date);
        String simpleDateString1 = DateUtil.getSimpleDateString(latter);
        Date a = DateUtil.parse(simpleDateString);
        Date b = DateUtil.parse(simpleDateString1);
        return (int) Math.abs((a.getTime()-b.getTime())/(1000*3600*24));
    }

}