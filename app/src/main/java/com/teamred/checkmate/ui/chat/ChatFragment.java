package com.teamred.checkmate.ui.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.Chat;
import com.teamred.checkmate.data.model.FriendlyMessage;
import com.teamred.checkmate.databinding.FragmentChatBinding;
import com.teamred.checkmate.databinding.FragmentChatMainBinding;
import com.teamred.checkmate.ui.login.LoginActivity;

public class ChatFragment extends Fragment {

    private FragmentChatMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdb;
    private ChatAdapter adapter;
    private LinearLayoutManager manager;

    private String MESSAGE_REF;

    private String CHAT_LIST= "chatList";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return root;
        }

//        Bundle arguments = getArguments();
//        assert arguments != null;
//        this.otherUserUid = arguments.getString("otherUserUid");
//        LoginDataSource.getUser(this.otherUserUid);
//        MESSAGE_REF = generateMessageRef(otherUserUid, Constant.getInstance().getCurrentUser().getUid());

        mdb = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = mdb.getReference().child(CHAT_LIST);
//        Query messagesRef = FirebaseStorage.getInstance().getReference().child(MESSAGES_CHILD);

        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(messagesRef, Chat.class)
                .build();

        adapter = new ChatAdapter(options);
        manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        binding.chatListview.setLayoutManager(manager);
        binding.chatListview.setAdapter(adapter);

        adapter.registerAdapterDataObserver(
                new MyScrollToButtonObserver<ChatAdapter>(binding.chatListview, adapter, manager)
        );

//        binding.sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FriendlyMessage friendlyMessage = new FriendlyMessage(
//                        FirebaseAuth.getInstance().getUid(),
//                        otherUserUid,
//                        binding.messageEditText.getText().toString(),
//                        getUserName(),
//                        getPhotoUrl(),
//                        null);
//                mdb.getReference()
//                        .child(MESSAGES_CHILD)
//                        .child(MESSAGE_REF)
//                        .push()
//                        .setValue(friendlyMessage);
//                binding.messageEditText.setText("");
//            }
//        });
//
//        binding.addMessageImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDocument.launch(new String[]{"image/*"});
//            }
//        });

        return root;
    }
}
