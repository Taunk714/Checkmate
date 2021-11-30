package com.teamred.checkmate.ui.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.firebase.auth.FirebaseAuth;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.data.model.Note;
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
                group.setTags(tags);
                group.setCreator(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                group.setCreateDate(new Date());
                group.setUpdateDate(new Date());
                String s = JSON.toJSONString(group);
                AlgoliaDataSource.getInstance(getApplicationContext()).addRecord("group", s);
                finish();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}