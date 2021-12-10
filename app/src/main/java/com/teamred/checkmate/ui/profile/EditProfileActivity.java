package com.teamred.checkmate.ui.profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teamred.checkmate.MainActivity;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.FireStoreDataSource;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.ActivityCreateGroupBinding;
import com.teamred.checkmate.databinding.FragmentProfileBinding;
import com.teamred.checkmate.databinding.FragmentProfileEdit1Binding;
import com.teamred.checkmate.ui.login.LoginActivity;

import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private FragmentProfileEdit1Binding binding;
    private EditText imageName;
    private EditText tName;
    private ImageView imageToUpload;
    private EditText tUsername;
    private Button upload;
    private Button save;
    private FrameLayout image;
    private Uri imageUri;


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

        imageToUpload = binding.imageToUpload;

        imageName = binding.imageName;

        //tName = binding.eName;


        tUsername = binding.eUsername;


        upload = binding.upload;

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });


        save = binding.save;


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String name = String.valueOf(tName.getText());
                String username = tUsername.getText().toString();
                String uPhoto = "uploads/"+ imageName.getText().toString()+".jpg";
                Toast.makeText(getApplicationContext(), "Status: saved", Toast.LENGTH_LONG).show();
                // upload to firebase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //user.setName(name);
                // user.setUsername(username);
                //user.setPhotoUrl(uPhoto);
                Task<Void> voidTask0 = FireStoreDataSource.updatePhotoUrl(user.getUid(), uPhoto);
                voidTask0.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Task<Void> voidTask = FireStoreDataSource.updateUsername(user.getUid(), username);
                        voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("sending info");
        pd.show();
        if(imageUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(imageName.getText().toString() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            Log.d("DownloadUrl", url);
                            pd.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && data != null){
            Uri selectedImage = data.getData();
            imageUri = selectedImage;
            imageToUpload.setImageURI(selectedImage);
            uploadImage();
        }
    }
}