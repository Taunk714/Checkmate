package com.teamred.checkmate.ui.chat;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.FriendlyMessage;
import com.teamred.checkmate.databinding.ImageMessageBinding;
import com.teamred.checkmate.databinding.MessageBinding;

import java.util.HashMap;
import java.util.Map;

public class FriendlyMessageAdapter extends FirebaseRecyclerAdapter<FriendlyMessage, RecyclerView.ViewHolder> {
    private FirebaseRecyclerOptions<FriendlyMessage> options;
    private String currentUserName;
    private String otherUsername;
    private String currentUID;
    private String otherUID;
    private Map<String, String> map = new HashMap<>();


    public FriendlyMessageAdapter(@NonNull FirebaseRecyclerOptions<FriendlyMessage> options,
                                  String currentUserName, String otherUsername,
                                  String currentUID, String otherUID) {
        super(options);
        this.options = options;
        map.put(currentUID, currentUserName);
        map.put(otherUID, otherUsername);
        this.currentUserName = currentUserName;
        this.otherUsername = otherUsername;
        this.currentUID = currentUID;
        this.otherUID = otherUID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_TEXT) {
            View view = inflater.inflate(R.layout.message, parent, false);
            return new MessageViewHolder(MessageBinding.bind(view));
        } else {
            View view = inflater.inflate(R.layout.image_message, parent, false);
            return new ImageMessageViewHolder(ImageMessageBinding.bind(view));
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull FriendlyMessage model) {
        if (options.getSnapshots().get(position).getText() != null){
//            holder.b
            ((MessageViewHolder) holder).bind(model);
        }else{
            ((ImageMessageViewHolder) holder).bind(model);
        }
    }

    public int getItemViewType(int position){
        if (options.getSnapshots().get(position).getText() != null){
            return VIEW_TYPE_TEXT;
        }else{
            return VIEW_TYPE_IMAGE;
        }
    }

    private static String ANONYMOUS = "anonymous";
    private static int VIEW_TYPE_TEXT = 1;
    private static int VIEW_TYPE_IMAGE = 2;





    class MessageViewHolder extends RecyclerView.ViewHolder {

        private MessageBinding binding;
        public MessageViewHolder(MessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FriendlyMessage item){
            binding.messageTextView.setText(item.getText());
            setTextColor(item.getFrom(), binding.messageTextView);

            if (item.getFrom() == null){
                binding.messengerTextView.setText(ANONYMOUS);
            }else{
                binding.messengerTextView.setText(map.get(item.getFrom()));
            }

            if (item.getPhotoUrl() != null) {
                loadImageIntoView(binding.messengerImageView, item.getPhotoUrl());
            } else {
                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp);
            }
        }

        private void setTextColor(String userName, TextView textView) {
            if (!userName.equals(ANONYMOUS)
                    && currentUserName.equals(userName)
                    && userName != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue);
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_gray);
                textView.setTextColor(Color.BLACK);
            }
        }
    }

    class ImageMessageViewHolder extends RecyclerView.ViewHolder{

        private ImageMessageBinding binding;
        public ImageMessageViewHolder(ImageMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FriendlyMessage item){
            loadImageIntoView(binding.messageImageView, item.getImageUrl());

            if (item.getFrom() == null){
                binding.messengerTextView.setText(ANONYMOUS);
            }else{
                binding.messengerTextView.setText(map.get(item.getFrom()));
            }
            if (item.getPhotoUrl() != null) {
                loadImageIntoView(binding.messengerImageView, item.getImageUrl());
            } else {
                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp);
            }
        }
    }

    public static void loadImageIntoView(ImageView view, String url) {
        if (url.startsWith("gs://")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
//            val storageReference = storageReference1;
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String s = uri.toString();
                    Glide.with(view.getContext()).load(s).into(view);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Getting download url was not successful.", e);
                }
            });
        } else {
            Glide.with(view.getContext()).load(url).into(view);
        }
    }

    private static String TAG = "MessageAdapter";
}
