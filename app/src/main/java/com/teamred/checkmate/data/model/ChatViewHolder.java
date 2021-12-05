package com.teamred.checkmate.data.model;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.databinding.ChatRowBinding;
import com.teamred.checkmate.databinding.MessageBinding;
import com.teamred.checkmate.ui.chat.FriendlyMessageAdapter;

import java.util.Map;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    private ChatRowBinding binding;
    public ChatViewHolder(ChatRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Chat item){

        User user = null;
        LoginDataSource.getTargetUser(item.getOtherUser()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> data = task.getResult().getData();
                if (data.get("username").toString().trim().isEmpty()){
                    binding.otherUsername.setText(ANONYMOUS);
                }else{
                    binding.otherUsername.setText(data.get("username").toString());
                }

                if (data.get("photoUrl") != null) {
                    FriendlyMessageAdapter.loadImageIntoView(binding.chatAvatar, data.get("photoUrl").toString());
                } else {
                    binding.chatAvatar.setImageResource(R.drawable.ic_account_circle_black_36dp);
                }

                String unread = "  " +data.get("unread").toString()+"  ";
                binding.unreadNumber.setText(unread);
                if (Integer.parseInt(data.get("unread").toString()) == 0){
                    binding.unreadNumber.setVisibility(View.INVISIBLE);
                }else{
                    binding.unreadNumber.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String ANONYMOUS = "anonymous";
}
