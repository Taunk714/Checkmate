package com.teamred.checkmate.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.LoggedInUser;
import com.teamred.checkmate.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

private FragmentProfileBinding binding;
private FrameLayout relative;
private ListView list;
private TextView tName;
private TextView tUsername;
private TextView tAbout_me;
 String tutorials[]
            = { "Algorithms", "Data Structures",
            "Languages", "CS501",
            "CS412", "INTRO TO CS",
            "Data Science", "CS Subjects",
            "Web Technologies" };
    LoggedInUser user = new LoggedInUser("Full Name", "Username");
    String name = user.getUserId();
    String username = user.getDisplayName();
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        relative = binding.backgroundLayout;
        relative.setBackgroundResource(R.drawable.background);

        tName = binding.Name;
        tName.setText(name);

        tUsername = binding.Username;
        tUsername.setText(username);

        tAbout_me = binding.aboutme;
        tAbout_me.setText("About me: ");

        list = binding.l;
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, tutorials);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(arr);




        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}