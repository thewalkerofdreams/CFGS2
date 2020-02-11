package com.example.listadopersonas_va.Fragments;

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

import com.example.listadopersonas_va.DDBB_Entities.ClsPersona;
import com.example.listadopersonas_va.R;
import com.example.listadopersonas_va.ViewModels.MainPageVM;

public class DetailsPersonFragment extends Fragment {

    TextView nombreCompleto, fechaNacimiento, telefono;
    MainPageVM viewModel;

    public DetailsPersonFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_details, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(MainPageVM.class);

        nombreCompleto = view.findViewById(R.id.txtViewNombreCompletoDetails);
        fechaNacimiento = view.findViewById(R.id.txtViewFechaNacimientoDetails);
        telefono = view.findViewById(R.id.txtViewTelefonoDetails);

        if(viewModel.get_personSelected().getValue() != null){
            nombreCompleto.setText(viewModel.get_personSelected().getValue().get_nombre() +" " +viewModel.get_personSelected().getValue().get_apellidos());
            fechaNacimiento.setText(viewModel.get_personSelected().getValue().obtenerFechaNacimientoCorta());
            telefono.setText(viewModel.get_personSelected().getValue().get_telefono());
        }

        /*El observer*/
        final Observer<ClsPersona> contactObserver = new Observer<ClsPersona>() {
            @Override
            public void onChanged(ClsPersona person) {

                if(viewModel.get_personSelected().getValue() != null){
                    nombreCompleto.setText(viewModel.get_personSelected().getValue().get_nombre() +" " +viewModel.get_personSelected().getValue().get_apellidos());
                    fechaNacimiento.setText(viewModel.get_personSelected().getValue().obtenerFechaNacimientoCorta());
                    telefono.setText(viewModel.get_personSelected().getValue().get_telefono());
                }
            }
        };

        //Observo el LiveData con ese observer que acabo de crear
        viewModel.get_personSelected().observe(getActivity(), contactObserver);

        return view;
    }
}
