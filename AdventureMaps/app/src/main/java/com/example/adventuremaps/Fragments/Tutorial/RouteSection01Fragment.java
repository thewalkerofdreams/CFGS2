package com.example.adventuremaps.Fragments.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.adventuremaps.R;

public class RouteSection01Fragment extends Fragment {

    public RouteSection01Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragment
        View rootView = inflater.inflate(R.layout.fragment_routes_section_01, container, false);

        return rootView;
    }

}