package com.teamred.checkmate.ui.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.databinding.FragmentChatBinding;
import com.teamred.checkmate.databinding.FragmentHomeBinding;
import com.teamred.checkmate.ui.calendar.CalendarActivity;
import com.teamred.checkmate.ui.group.CreateGroupActivity;
import com.teamred.checkmate.ui.login.LoginActivity;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
//    private

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // auth to check whether user is signed in
        //calenderHeatmap = (Button) getView().findViewById(R.id.calendarheatmap);
        binding.messageEditText.addTextChangedListener(new MyButtonObserver(binding.sendButton));

        registerForActivityResult(new MyOpenDocumentContract(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
//                onImageSelected(result);
            }
        });
//        binding.addMessageImageView.setOnClickListener(
//
//        );




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    private Boolean onOptionsItemSelected(MenuItem item) {
//        return when (item.itemId) {
//            R.id.sign_out_menu -> {
//                signOut()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    private void onImageSelected(Uri uri) {
//        // TODO: implement
//    }
//
//    private void putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
//        // Upload the image to Cloud Storage
//        // TODO: implement
//    }
}
