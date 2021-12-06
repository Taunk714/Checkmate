package com.teamred.checkmate.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.Chat;
import com.teamred.checkmate.data.model.ChatViewHolder;
import com.teamred.checkmate.databinding.ChatRowBinding;
import com.teamred.checkmate.databinding.ImageMessageBinding;
import com.teamred.checkmate.databinding.MessageBinding;

public class ChatAdapter extends FirebaseRecyclerAdapter<Chat, ChatViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirebaseRecyclerOptions<Chat> options) {
        super(options);
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
        holder.bind(model);
    }
}
