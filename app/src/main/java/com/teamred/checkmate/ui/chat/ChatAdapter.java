package com.teamred.checkmate.ui.chat;

import static com.teamred.checkmate.ui.chat.ChatFragment.CHAT_LIST;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.Chat;
import com.teamred.checkmate.data.model.FriendlyMessage;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.ChatRowBinding;
import com.teamred.checkmate.databinding.ImageMessageBinding;
import com.teamred.checkmate.databinding.MessageBinding;

import java.util.Map;

public class ChatAdapter extends FirebaseRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder> {
    private FirebaseRecyclerOptions<Chat> options;
    private FragmentManager fm;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirebaseRecyclerOptions<Chat> options, FragmentManager fm) {
        super(options);
        this.options = options;
        this.fm = fm;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_row, parent, false);
        return new ChatViewHolder(ChatRowBinding.bind(view));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Chat model) {
        Chat chat = options.getSnapshots().get(position);
        holder.bind(model);
        holder.binding.bigConsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setUnread(0);
                FirebaseDatabase.getInstance()
                    .getReference()
                    .child(CHAT_LIST)
                    .child(Constant.getInstance().getCurrentUser().getUid())
                    .child(chat.getOtherUser()).setValue(chat);

                FragmentTransaction ft = fm.beginTransaction();
                Task<DocumentSnapshot> userTask = LoginDataSource.getUserTask(model.getOtherUser());
                userTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser", JSON.toJSONString(user));
                        ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
                        chatDetailFragment.setArguments(bundle);
                        ft.replace(R.id.navigation_host, chatDetailFragment, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });

    }


    class ChatViewHolder extends RecyclerView.ViewHolder {

        private ChatRowBinding binding;

        public ChatViewHolder(ChatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat item) {

//            User user = null;
            LoginDataSource.getTargetUser(item.getOtherUser()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User user= task.getResult().toObject(User.class);
                    if (user.getUsername().isEmpty()) {
                        binding.otherUsername.setText(ANONYMOUS);
                    } else {
                        binding.otherUsername.setText(user.getUsername());
                    }

                    if (user.getPhotoUrl() != null) {
                        FriendlyMessageAdapter.loadImageIntoView(binding.chatAvatar, user.getPhotoUrl());
                    } else {
                        binding.chatAvatar.setImageResource(R.drawable.ic_account_circle_black_36dp);
                    }
                    binding.lastMessage.setText(item.getLastMessage());

                    String unread = "    ";
                    binding.unreadNumber.setText(unread);
                    if (item.getUnread() == 0) {
                        binding.unreadNumber.setVisibility(View.INVISIBLE);
                        binding.lastMessage.setTextColor(R.color.colorPrimaryDark);
                    } else {
                        binding.unreadNumber.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        public ChatRowBinding getBinding() {
            return binding;
        }
    }



        private String ANONYMOUS = "anonymous";
}
