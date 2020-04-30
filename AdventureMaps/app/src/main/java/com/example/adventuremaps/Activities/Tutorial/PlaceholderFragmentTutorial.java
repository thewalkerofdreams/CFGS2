package com.example.adventuremaps.Activities.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection01;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection02;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection03;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection04;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection05;
import com.example.adventuremaps.Fragments.Tutorial.OfflineMapsSection01Fragment;
import com.example.adventuremaps.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentTutorial extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Fragment newInstance(int index) {
        Fragment fragment = null;

        switch (index){
            case 1:
                fragment = new CommonFragmentSection01();//Cargamos la sección del mapa inicial
                break;
            case 2:
                fragment = new CommonFragmentSection02();//Cargamos la sección de los marcadores
                break;
            case 3:
                fragment = new CommonFragmentSection03();//Cargamos la sección de las localizaciones
                break;
            case 4:
                fragment = new CommonFragmentSection04();//Cargamos la sección de las rutas
                break;
            case 5:
                fragment = new CommonFragmentSection05();//Cargamos la sección de los mapas offline
                break;
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageViewModelTutorial pageViewModel = ViewModelProviders.of(this).get(PageViewModelTutorial.class);
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