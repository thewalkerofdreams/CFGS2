package com.example.adventuremaps.Activities.ui.MainTabbet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Fragments.FragmentLocalizations;
import com.example.adventuremaps.Fragments.FragmentMaps;
import com.example.adventuremaps.Fragments.FragmentRoutes;
import com.example.adventuremaps.Fragments.FragmentStart;
import com.example.adventuremaps.Fragments.FragmentUser;
import com.example.adventuremaps.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static Fragment newInstance(int index) {
        Fragment fragment = null;

        switch (index){
            case 1:
                fragment = new FragmentStart();
                break;
            case 2:
                fragment = new FragmentLocalizations();
                break;
            case 3:
                fragment = new FragmentRoutes();
                break;
            case 4:
                fragment = new FragmentMaps();
                break;
            case 5:
                fragment = new FragmentUser();
                break;
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tabbet, container, false);
    }
}