package com.teamred.checkmate.ui.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.databinding.ActivityCreateNoteBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    ActivityCreateNoteBinding binding;

    final String TAG = "Note";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String postTitle = binding.postTitle.getText().toString();
                final String subTopic = binding.subTopic.getText().toString();
                final String tags = binding.tags.getText().toString();
                final String content = binding.postContent.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("postTitle", postTitle);
                data.put("subTopic", subTopic);
                data.put("tags", tags);
                data.put("content", content);
                data.put("createdDate", Calendar.getInstance().getTime());

                CollectionReference posts = db.collection("Groups").document("0JG8VeJxHYJ83y9nZF3Y").collection("posts");
                posts.add(data);
            }
        });

        DocumentReference post = db.collection("Groups").document("0JG8VeJxHYJ83y9nZF3Y").collection("posts").document("H0uC69kU01vIfJlTvuFL");

        post.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Timestamp createdDate = (Timestamp)document.getData().get("createdDate");
                        Log.d(TAG, createdDate.toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}