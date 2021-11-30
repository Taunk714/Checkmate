package com.teamred.checkmate.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
//
//        registerForActivityResult(new MyOpen)
//        binding.addMessageImageView.setOnClickListener(
//
//        );

//
//        FloatingActionButton btnAddPost = binding.btnAddPost;
//        btnAddPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent i = new Intent(getActivity(), AddPostActivity.class);
//
//                if (FirebaseAuth.getInstance().getCurrentUser() == null){
//                    Intent login = new Intent(getActivity(), LoginActivity.class);
//                    Toast.makeText(getContext(), "Interceptor! Jump to login", Toast.LENGTH_LONG).show();
//                    login.putExtra("callback", CreateGroupActivity.class.getCanonicalName());
//                    startActivity(login);
//                }else{
//                    Intent i = new Intent(getActivity(), CreateGroupActivity.class);
//                    Toast.makeText(getContext(), "click create group", Toast.LENGTH_LONG).show();
//                    startActivity(i);
//                }
//            }
//        });
//

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
