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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.LoggedInUser;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.databinding.FragmentProfileBinding;
import com.teamred.checkmate.ui.group.GroupDetailFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    Map<String, Object> userMap = new HashMap<>();
    User user = new User();
    String name = user.getName();
    String username = user.getUsername();
    String profile_pic = user.getPhotoUrl();
    String userN;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        list = binding.reminderlist;
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, items);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(arr);

        // firebase auth to check current user

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d("ptest", user.getUid());
            Log.d("ptest", user.getEmail());

            String[] arrOfStr = user.getEmail().split("@", 2);
            userN = arrOfStr[0];

            userMap.put("uid", user.getUid());
            userMap.put("email", user.getEmail());
            userMap.put("username", userN);
            // Log.d("ptest", user.getPhotoUrl().toString());
            // Log.d("ptest", user.getDisplayName());

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("user").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("ptest2", "DocumentSnapshot data: " + document.getData().get("groupJoined"));
                            userMap.put("groupJoined", document.getData().get("groupJoined"));
                            userMap.put("numGroups", document.getData().get("groupJoined").toString().split(",").length);
                            Log.d("ptest2", userMap.toString());
                        } else {
                            Log.d("ptest2", "No such document");
                        }
                    } else {
                        Log.d("ptest2", "get failed with ", task.getException());
                    }
                }
            });

            // db.collection("user")
            //         .get()
            //         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            //             @Override
            //             public void onComplete(@NonNull Task<QuerySnapshot> task) {
            //                 if (task.isSuccessful()) {
            //                     for (QueryDocumentSnapshot document : task.getResult()) {
            //                         Log.d("ptest", document.getId() + " => " + document.getData().get("name"));
            //                     }
            //                 } else {
            //                     Log.w("ptest", "Error getting documents.", task.getException());
            //                 }
            //             }
            //         });

        } else {
            // No user is signed in
        }






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