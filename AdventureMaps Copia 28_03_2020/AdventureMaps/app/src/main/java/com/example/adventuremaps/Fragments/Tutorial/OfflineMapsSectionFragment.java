package com.example.adventuremaps.Fragments.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adventuremaps.R;

public class OfflineMapsSectionFragment extends RootFragment {

    public OfflineMapsSectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragment
        View rootView = inflater.inflate(R.layout.fragment_offline_maps_section, container, false);

        return rootView;
    }

}