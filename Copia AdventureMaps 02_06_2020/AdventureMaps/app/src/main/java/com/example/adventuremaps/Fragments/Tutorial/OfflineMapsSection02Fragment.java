package com.example.adventuremaps.Fragments.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.adventuremaps.R;

public class OfflineMapsSection02Fragment extends Fragment {

    public OfflineMapsSection02Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragment
        return inflater.inflate(R.layout.fragment_offline_maps_section_02, container, false);
    }
}
