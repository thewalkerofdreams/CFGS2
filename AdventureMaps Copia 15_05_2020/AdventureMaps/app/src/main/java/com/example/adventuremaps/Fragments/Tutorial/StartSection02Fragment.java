package com.example.adventuremaps.Fragments.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.adventuremaps.R;

public class StartSection02Fragment extends Fragment {

    public StartSection02Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_start_section_02, container, false);

        return rootView;
    }
}