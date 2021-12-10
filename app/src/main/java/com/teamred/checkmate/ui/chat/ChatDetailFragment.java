package com.teamred.checkmate.ui.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.Chat;
import com.teamred.checkmate.data.model.FriendlyMessage;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentChatBinding;
import com.teamred.checkmate.databinding.FragmentHomeBinding;
import com.teamred.checkmate.ui.calendar.CalendarActivity;
import com.teamred.checkmate.ui.group.CreateGroupActivity;
import com.teamred.checkmate.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Map;

public class ChatDetailFragment extends Fragment {
    private FragmentChatBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdb;
    private FriendlyMessageAdapter adapter;
    private LinearLayoutManager manager;

    private String MESSAGE_REF;

    private User otherUser = new User();

//    private String otherUserUid = null;
//    private
    private ActivityResultLauncher<String[]> openDocument = registerForActivityResult(new MyOpenDocumentContract(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            onImageSelected(result);
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return root;
        }

        Bundle arguments = getArguments();
        assert arguments != null;
        String otherUserString = arguments.getString("otherUser");
        otherUser = JSON.parseObject(otherUserString, User.class);
        binding.chatWithWhom.setText(otherUser.getUsername());
        MESSAGE_REF = generateMessageRef(otherUser.getUid(), Constant.getInstance().getCurrentUser().getUid());

        mdb = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = mdb.getReference().child(MESSAGES_CHILD).child(MESSAGE_REF);
//        DatabaseReference chatRef = mdb.getReference().child(ChatFragment.CHAT_LIST).child(Constant.getInstance().getCurrentUser().getUid()).child(otherUser.getUid());

        FirebaseRecyclerOptions<FriendlyMessage> options = new FirebaseRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(messagesRef, FriendlyMessage.class)
                .build();

        adapter = new FriendlyMessageAdapter(options, getUserName(), otherUser.getUsername(), Constant.getInstance().getCurrentUser().getUid(), otherUser.getUid());
        binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        binding.messageRecyclerView.setLayoutManager(manager);
        binding.messageRecyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(
                new MyScrollToButtonObserver<FriendlyMessageAdapter>(binding.messageRecyclerView, adapter, manager)
        );

        binding.messageEditText.addTextChangedListener(
                new MyButtonObserver(binding.sendButton)
        );

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(
                        FirebaseAuth.getInstance().getUid(),
                        otherUser.getUid(),
                        binding.messageEditText.getText().toString(),
                        getUserName(),
                        getPhotoUrl(),
                        null);
                mdb.getReference()
                        .child(MESSAGES_CHILD)
                        .child(MESSAGE_REF)
                        .push()
                        .setValue(friendlyMessage);
                binding.messageEditText.setText("");
                Chat chat = new Chat(
                        Constant.getInstance().getCurrentUser().getUid(),
                        otherUser.getUid(),
                        friendlyMessage.getText());
                mdb.getReference()
                        .child(ChatFragment.CHAT_LIST)
                        .child(Constant.getInstance().getCurrentUser().getUid())
                        .child(otherUser.getUid())
                        .setValue(chat);
                Chat otherChat = new Chat(
                        otherUser.getUid(),
                        Constant.getInstance().getCurrentUser().getUid(),
                        friendlyMessage.getText(), 1);
                mdb.getReference()
                        .child(ChatFragment.CHAT_LIST)
                        .child(otherUser.getUid())
                        .child(Constant.getInstance().getCurrentUser().getUid())
                        .setValue(otherChat);
            }
        });



        return root;
    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        re
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onStart() {
        super.onStart();

    }

    public void onPause() {
        adapter.stopListening();
        super.onPause();
    }
//
    public void onResume(){
        super.onResume();
        adapter.startListening();
    }

    private void onImageSelected(Uri uri) {
        // TODO: implement
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FriendlyMessage friendlyMessage = new FriendlyMessage(
                FirebaseAuth.getInstance().getUid(),
                otherUser.getUid(),
                null,
                getUserName(),
                getPhotoUrl(),
                LOADING_IMAGE_URL);
        mdb.getReference().child(MESSAGES_CHILD).child(MESSAGE_REF).push().setValue(friendlyMessage,
                new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null){
                            Log.w(TAG, "Unable to write message to database.",
                                    error.toException());
                            return;
                        }

                        String key = ref.getKey();
                        StorageReference child =
                                FirebaseStorage.getInstance()
                                        .getReference(currentUser.getUid())
                                        .child(key)
                                        .child(uri.getLastPathSegment());
                        putImageInStorage(child, uri, key);

                    }
                });

    }
//
    private void putImageInStorage(StorageReference reference, Uri uri, String key) {
        // Upload the image to Cloud Storage
        // TODO: implement
        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot
                                .getMetadata()
                                .getReference()
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FriendlyMessage friendlyMessage = new FriendlyMessage(
                                        FirebaseAuth.getInstance().getUid(),
                                        otherUser.getUid(),
                                        null,
                                        getUserName(),
                                        getPhotoUrl(),
                                        uri.toString());
                                mdb.getReference()
                                        .child(MESSAGES_CHILD)
                                        .child(MESSAGE_REF)
                                        .child(key)
                                        .setValue(friendlyMessage);
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

    private String getUserName() {
        User currentUser = LoginDataSource.getUserResult();
        if (currentUser != null) {
            return currentUser.getUsername();
        } else {
            return ANONYMOUS;
        }
    }

    private String getPhotoUrl(){
        try {
            return LoginDataSource.getUserResult().getPhotoUrl().toString();
        }catch (Exception e){
            return null;
        }
    }

    private String generateMessageRef(String uid1, String uid2){
        if (uid1.compareTo(uid2) > 0){
            return uid1 + "+" + uid2;
        }else{
            return uid2 + "+" + uid1;
        }
    }

    private static String TAG = "ChatDetailFragment";
    private static String MESSAGES_CHILD = "messages";
    private static String ANONYMOUS = "anonymous";
    private static String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";

}
