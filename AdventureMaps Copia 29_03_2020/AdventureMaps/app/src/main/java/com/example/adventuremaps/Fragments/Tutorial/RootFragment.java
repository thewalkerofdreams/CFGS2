package com.example.adventuremaps.Fragments.Tutorial;

import androidx.fragment.app.Fragment;

import com.example.adventuremaps.Activities.Tutorial.BackPressImpl;
import com.example.adventuremaps.Activities.Tutorial.OnBackPressListener;

public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}