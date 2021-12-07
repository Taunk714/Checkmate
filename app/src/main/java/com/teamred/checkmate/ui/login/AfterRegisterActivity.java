package com.teamred.checkmate.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.teamred.checkmate.MainActivity;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.ActivityAfterRegisterBinding;
import com.teamred.checkmate.ui.chat.FriendlyMessageAdapter;
import com.teamred.checkmate.ui.chat.MyOpenDocumentContract;

public class AfterRegisterActivity extends AppCompatActivity {

    private EditText nickname_editor;
    private Button nickname_button;

    private ActivityAfterRegisterBinding binding;

    private User user;

    private ActivityResultLauncher<String[]> openDocument = registerForActivityResult(new MyOpenDocumentContract(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            onImageSelected(result);
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);
        binding = ActivityAfterRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nickname_button = binding.nicknameConfirm;
        nickname_editor = binding.nicknameEditor;

        if (Constant.getInstance().getCurrentUser() != null){
            user = Constant.getInstance().getCurrentUser();
        }else{
            user = LoginDataSource.generateUser(FirebaseAuth.getInstance().getCurrentUser());
        }


        nickname_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    nickname_button.setEnabled(true);
                }else{
                    nickname_button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nickname_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                user.setUsername(nickname_editor.getText().toString());
                FirebaseFirestore.getInstance()
                        .collection("user")
                        .document(currentUser.getUid())
                        .set(JSON.toJSON(user))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i(TAG, "add to firebase");
                                Intent intent = new Intent(AfterRegisterActivity.this, MainActivity.class);
                                Constant.getInstance().setCurrentUser(user);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "fail to add new user to firebase");
                    }
                });
            }
        });

        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDocument.launch(new String[]{"image/*"});
            }
        });
    }

    private void onImageSelected(Uri uri) {
        // TODO: implement
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        FriendlyMessage friendlyMessage = new FriendlyMessage(null, getUserName(), getPhotoUrl(), LOADING_IMAGE_URL);
        FirebaseStorage.getInstance()
                .getReference("avatar")
                .child(currentUser.getUid())
                .putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FriendlyMessageAdapter.loadImageIntoView(binding.avatar, uri.toString());
                                user.setPhotoUrl(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Image upload task was unsuccessful.",
                                e);
                    }
                });

    }

    private static final String TAG = "RegisterSetting";


}
