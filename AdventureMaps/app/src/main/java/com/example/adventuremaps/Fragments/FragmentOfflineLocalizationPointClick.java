package com.example.adventuremaps.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;

public class FragmentOfflineLocalizationPointClick extends Fragment {

    private MainTabbetActivityVM viewModel;
    private Button btnDelete, btnDetails;
    //Constructor por defecto.
    public FragmentOfflineLocalizationPointClick(){
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_click_localization_point, container, false);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        btnDelete = view.findViewById(R.id.btnDeleteLocalizationPointFragmentStart);
        btnDetails = view.findViewById(R.id.btnDetailsLocalizationPointFragmentStart);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
