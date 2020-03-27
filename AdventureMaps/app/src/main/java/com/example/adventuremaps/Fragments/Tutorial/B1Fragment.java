package com.example.adventuremaps.Fragments.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adventuremaps.R;

public class B1Fragment extends RootFragment {


    public B1Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_b1, container, false);

        return rootView;
    }

}