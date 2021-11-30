package com.teamred.checkmate.ui.calendar;

import android.os.Bundle;
/*import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;*/
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.teamred.checkmate.R;

import org.jetbrains.annotations.Nullable;

public class Tab2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_event,container,false);
        return v;
    }
}
