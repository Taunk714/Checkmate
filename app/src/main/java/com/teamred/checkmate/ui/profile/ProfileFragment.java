package com.teamred.checkmate.ui.profile;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.LoggedInUser;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentProfileBinding;
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
    User user = new User();
    String name = user.getName();
    String username = user.getUsername();
    String profile_pic = user.getPhotoUrl();
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        list = binding.reminderlist;
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, items);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(arr);




        edit = binding.editProfileBtn;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("profile", "edit profile click");
                FragmentManager fm = null;
                FragmentTransaction ft = fm.beginTransaction();
                EditProfileFragment epf = new EditProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", String.valueOf(tName.getText()));
                bundle.putString("tAbout_me", String.valueOf(tAbout_me.getText()));
                epf.setArguments(bundle);
                ft.replace(R.id.nav_host_fragment_container, epf)
                        .commit();
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