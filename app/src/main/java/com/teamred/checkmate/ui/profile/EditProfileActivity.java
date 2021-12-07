package com.teamred.checkmate.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
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
import com.teamred.checkmate.databinding.FragmentProfileBinding;
import com.teamred.checkmate.databinding.FragmentProfileEdit1Binding;

import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

    private FragmentProfileEdit1Binding binding;
    private EditText photo;
    private EditText tName;
    private EditText tUsername;
    private Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        ActionBar supportActionBar = getSupportActionBar();
//        supportActionBar.setDisplayHomeAsUpEnabled(true);

        binding = FragmentProfileEdit1Binding.inflate(getLayoutInflater());
//        View root = binding.getRoot();
        setContentView(binding.getRoot());

//        TextView postTag = binding.postTag;

        View root = binding.getRoot();

        //photo = binding.photourl;

        //tName = binding.eName;


        tUsername = binding.eUsername;



        save = binding.save;


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String name = String.valueOf(tName.getText());
                String username = tUsername.getText().toString();
                //String uPhoto = photo.getText().toString();
                Toast.makeText(getApplicationContext(), "Status: saved", Toast.LENGTH_LONG).show();
                // upload to firebase
                User user = new User();
                //user.setName(name);
                user.setUsername(username);
                //user.setPhotoUrl(uPhoto);

                Task<Void> voidTask = FireStoreDataSource.updateUser(user.getUid(), user.getUsername());
                voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
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