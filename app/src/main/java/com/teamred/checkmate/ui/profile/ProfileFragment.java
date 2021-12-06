package com.teamred.checkmate.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.model.LoggedInUser;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentProfileBinding;
import com.teamred.checkmate.ui.group.CreateGroupActivity;
import com.teamred.checkmate.ui.group.GroupDetailFragment;

import java.util.concurrent.ThreadLocalRandom;

public class ProfileFragment extends Fragment {

private FragmentProfileBinding binding;
private FrameLayout relative;
private ListView list;
private TextView tName;
private TextView tUsername;
private TextView tAbout_me;
private Button edit;
 String items[]
            = { "Algorithms", "Data Structures",
            "Languages", "CS501",
            "CS412", "INTRO TO CS",
            "Data Science", "CS Subjects",
            "Web Technologies" };
    User user = Constant.getInstance().getCurrentUser();
    //String name = user.getName();
    String username = user.getUsername();
    //String profile_pic = user.getPhotoUrl();
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        relative = binding.backgroundLayout;
        relative.setBackgroundResource(R.drawable.background);

        tName = binding.Name;
        //tName.setText(name);

        tUsername = binding.Username;
        tUsername.setText(username);

        tAbout_me = binding.aboutme;
        //tAbout_me.setText("About me: ");

        list = binding.l;
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, items);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(arr);




        edit = binding.edit;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("profile", "edit profile click");
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                Toast.makeText(getContext(), "Edit Profile", Toast.LENGTH_LONG).show();
                startActivity(i);
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