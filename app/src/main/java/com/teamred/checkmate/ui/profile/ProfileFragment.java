package com.teamred.checkmate.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.teamred.checkmate.R;
import com.teamred.checkmate.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

private FragmentProfileBinding binding;
private LinearLayout relative;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        relative = binding.backgroundLayout;
        //relative.setBackgroundResource(R.drawable.background);





        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}