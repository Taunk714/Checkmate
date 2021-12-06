package com.teamred.checkmate.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.LoggedInUser;
import com.teamred.checkmate.databinding.FragmentProfileBinding;
import com.teamred.checkmate.databinding.FragmentProfileEdit1Binding;

public class EditProfileFragment extends Fragment {

private FragmentProfileEdit1Binding binding;
private FrameLayout relative;
private ListView list;
private EditText tName;
private EditText tUsername;
private EditText tAbout_me;
private Button edit;



    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileEdit1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tName = binding.eName;


        tUsername = binding.eUsername;


        tAbout_me = binding.eAboutme;


        edit = binding.save;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //user.setName(tName.getText().toString());

            }

        });




        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}