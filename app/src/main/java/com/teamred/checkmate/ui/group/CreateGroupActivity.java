package com.teamred.checkmate.ui.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.FireStoreDataSource;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.databinding.ActivityCreateGroupBinding;

import java.util.Date;

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
//        TextView postTag = binding.postTag;
        TextView groupDescription = binding.groupDescription;
        TextView groupTag = binding.groupTags;
        Button create = binding.createGroup;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(groupName.getText());
                String[] tags = groupTag.getText().toString().split(";");
                String description = String.valueOf(groupDescription.getText());
                Toast.makeText(getApplicationContext(), "send group info to algolia", Toast.LENGTH_LONG).show();
                // upload to firebase
                Group group = new Group();
                group.setGroupName(name);
                group.setDescription(description);
//                group.setSubTopics(new String[]{});
                group.setCreator(LoginDataSource.getUserResult().getUsername());
                group.setCreateDate(new Date());
                group.setUpdateDate(new Date());

                Task<DocumentReference> documentReferenceTask = FireStoreDataSource.addGroup(group);
                documentReferenceTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        String id = task.getResult().getId();
                        group.setId(id);
                        String s = JSON.toJSONString(group);
                        AlgoliaDataSource.getInstance().addRecord(CheckmateKey.GROUP_ALGOLIA, s);
                        finish();
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