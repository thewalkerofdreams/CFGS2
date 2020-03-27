package com.example.adventuremaps.Fragments.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adventuremaps.R;

public class B3Fragment extends RootFragment {


    public B3Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragmento
        return inflater.inflate(R.layout.fragment_b3, container, false);
    }

}