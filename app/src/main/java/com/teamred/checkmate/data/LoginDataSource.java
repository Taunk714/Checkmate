package com.teamred.checkmate.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.teamred.checkmate.data.model.LoggedInUser;
import com.teamred.checkmate.data.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

// auto-generated, we don't need this file

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {


    private static final String TAG = "USER";

//    private static User user = new User();

    public static MutableLiveData<User> currentUserResult = new MutableLiveData<>();

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }


//    private void createAccount(String email, String password){
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//    }
//
//    private void signIn(String email, String password) {
//        // [START sign_in_with_email]
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//        // [END sign_in_with_email]
//    }

//    private void sendEmailVerification() {
//        // Send verification email
//        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // Email sent
//                    }
//                });
//        // [END send_email_verification]
//    }

    public static void getUser(){
        if(currentUserResult.getValue() == null){
            getUser(FirebaseAuth.getInstance().getUid());
        }
//        return user;
    }

    public static User getUserResult(){
        return currentUserResult.getValue();
    }

    public static void setUser(Map<String, Object> data){
        User user = new User();
        user.setName((String) data.get("name"));
        user.setGroupJoined(((ArrayList<String>) data.get("groupJoined")));
        user.setPhotoUrl((String) data.get("photoUrl"));
        user.setUid((String) data.get("uid"));
        user.setUsername((String) data.get("username"));
        currentUserResult.setValue(user);
    }


    public static Task<DocumentSnapshot> getUser(String uid){
        User user = new User();
        Source source = Source.SERVER;
        Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().collection("user").document(uid).get();
        documentSnapshotTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String, Object> data = task.getResult().getData();
                    user.setName((String) data.get("name"));
                    user.setGroupJoined(((ArrayList<String>) data.get("groupJoined")));
                    user.setPhotoUrl((String) data.get("photoUrl"));
                    user.setUid(uid);
                    user.setUsername((String) data.get("username"));
                    Constant.getInstance().setCurrentUser(user);
                    currentUserResult.setValue(user);
                    Log.i(TAG, "Fetch algolia key");
//                    AlgoliaDataSource.
                }else{
                    Log.i(TAG, "Get User Error");
                }

            }
        });
        return documentSnapshotTask;
    }

    public static Task<DocumentSnapshot> getTargetUser(String uid){
        User user = new User();
        Source source = Source.SERVER;
        Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().collection("user").document(uid).get();
        return documentSnapshotTask;
    }

    public static User getChatUser(String uid){
        User user = new User();
        Source source = Source.SERVER;
        Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().collection("user").document(uid).get();
        documentSnapshotTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String, Object> data = task.getResult().getData();
                    user.setName((String) data.get("name"));
                    user.setGroupJoined(((ArrayList<String>) data.get("groupJoined")));
                    user.setPhotoUrl((String) data.get("photoUrl"));
                    user.setUid(uid);
                    user.setUsername((String) data.get("username"));
                    Log.i(TAG, "Get chat user");
//                    AlgoliaDataSource.
                }else{
                    Log.i(TAG, "Get User Error");
                }

            }
        });
        return user;
    }

    public static User generateUser(FirebaseUser firebaseUser){
        User user = new User();
        user.setUid(firebaseUser.getUid());
        user.setUsername(firebaseUser.getEmail());
        user.setGroupJoined(new ArrayList<>());
        user.setPhotoUrl(firebaseUser.getPhotoUrl() == null? null:firebaseUser.getPhotoUrl().toString());
        user.setName(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        return user;
    }

    public static Task<DocumentSnapshot> getUserTask(String uid){
        return FirebaseFirestore.getInstance().collection("user").document(uid).get();
    }

    public static void addUser(FirebaseUser firebaseUser){
        User user = new User();
        user.setUid(firebaseUser.getUid());
        user.setUsername(firebaseUser.getEmail());
        user.setGroupJoined(new ArrayList<>());
        user.setPhotoUrl(firebaseUser.getPhotoUrl() == null? null:firebaseUser.getPhotoUrl().toString());
        user.setName(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());

        FirebaseFirestore.getInstance()
            .collection("user")
            .document(firebaseUser.getUid())
            .set(JSON.toJSON(user))
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.i(TAG, "add to firebase");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "fail to add new user to firebase");
                }
            });

    }

    public static Task<Void> updateUser(String uid, String username){
        return FirebaseFirestore.getInstance()
                .collection("user")
                .document(uid)
                .update("username", username);
    }


}