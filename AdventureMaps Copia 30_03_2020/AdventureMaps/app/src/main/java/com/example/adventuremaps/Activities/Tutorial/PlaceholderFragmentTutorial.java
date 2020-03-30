package com.example.adventuremaps.Activities.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Fragments.FragmentLocalizations;
import com.example.adventuremaps.Fragments.FragmentMaps;
import com.example.adventuremaps.Fragments.FragmentRoutes;
import com.example.adventuremaps.Fragments.FragmentStart;
import com.example.adventuremaps.Fragments.FragmentUser;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection01;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection02;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection03;
import com.example.adventuremaps.Fragments.Tutorial.OfflineMapsSectionFragment;
import com.example.adventuremaps.Fragments.Tutorial.RoutesSectionFragment;
import com.example.adventuremaps.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentTutorial extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModelTutorial pageViewModel;

    public static Fragment newInstance(int index) {
        Fragment fragment = null;

        switch (index){
            case 1:
                fragment = new CommonFragmentSection01();
                break;
            case 2:
                fragment = new CommonFragmentSection02();
                break;
            case 3:
                fragment = new CommonFragmentSection03();
                break;
            case 4:
                fragment = new RoutesSectionFragment();
                break;
            case 5:
                fragment = new OfflineMapsSectionFragment();
                break;
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModelTutorial.class);
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
        View root = inflater.inflate(R.layout.fragment_main_tabbet, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}