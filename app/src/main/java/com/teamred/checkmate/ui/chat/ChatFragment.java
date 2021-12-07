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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.Chat;
import com.teamred.checkmate.data.model.FriendlyMessage;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentChatBinding;
import com.teamred.checkmate.databinding.FragmentChatMainBinding;
import com.teamred.checkmate.ui.group.GroupDetailFragment;
import com.teamred.checkmate.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Map;

public class ChatFragment extends Fragment {

    private FragmentChatMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdb;
    private ChatAdapter adapter;
    private LinearLayoutManager manager;

    private String MESSAGE_REF;

    public static String CHAT_LIST= "chatList";

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
        DatabaseReference messagesRef = mdb.getReference().child(CHAT_LIST).child(Constant.getInstance().getCurrentUser().getUid());
//        Query messagesRef = FirebaseStorage.getInstance().getReference().child(MESSAGES_CHILD);

        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(messagesRef, Chat.class)
                .build();

        adapter = new ChatAdapter(options, getParentFragmentManager());
        manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(false);
        binding.chatListview.setLayoutManager(manager);
        binding.chatListview.setAdapter(adapter);

        return root;
    }

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

}
