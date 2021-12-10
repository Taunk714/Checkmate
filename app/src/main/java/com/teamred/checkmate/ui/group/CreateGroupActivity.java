package com.teamred.checkmate.ui.group;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.alibaba.fastjson.JSON;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.FireStoreDataSource;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.ActivityCreateGroupBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {

    private ActivityCreateGroupBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        ActionBar supportActionBar = getSupportActionBar();
//        supportActionBar.setDisplayHomeAsUpEnabled(true);

        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
//        View root = binding.getRoot();
        setContentView(binding.getRoot());

        final TextView groupName = binding.groupName;
        TextView groupDescription = binding.groupDescription;
        TextView groupTag = binding.groupTags;
        Button create = binding.createGroup;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(groupName.getText());
                List<String> tags = Arrays.asList(groupTag.getText().toString().split("[,; ]"));
                String description = String.valueOf(groupDescription.getText());
                Toast.makeText(getApplicationContext(), "searching...", Toast.LENGTH_LONG).show();
                // upload to firebase
                Group group = new Group();
                group.setGroupName(name);
                group.setDescription(description);
                group.setSubTopics(new ArrayList<>());
//                group.setCreator(Constant.getInstance().getCurrentUser().getUsername());
                group.setCreatorId(Constant.getInstance().getCurrentUser().getUid());
                group.setCreateDate(new Date());
                group.setUpdateDate(new Date());
                group.setTags(tags);

                Task<DocumentReference> documentReferenceTask = FireStoreDataSource.addGroup(group);
                documentReferenceTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        String id = task.getResult().getId();
                        group.setObjectID(id);
                        FirebaseFirestore.getInstance().collection(CheckmateKey.GROUP_FIREBASE).document(id).update("objectID", id);
                        String s = JSON.toJSONString(group);
                        AlgoliaDataSource.getInstance().addRecord(CheckmateKey.GROUP_ALGOLIA, s, new CompletionHandler() {
                            @Override
                            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                                if (e == null){
                                    Group.joinGroup(Constant.getInstance().getCurrentUser(), group.getObjectID());
                                    User currentUser = Constant.getInstance().getCurrentUser();
                                    FirebaseFirestore.getInstance().collection("user")
                                            .document(currentUser.getUid())
                                            .update("groupJoined", currentUser.getGroupJoined()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            finish();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}