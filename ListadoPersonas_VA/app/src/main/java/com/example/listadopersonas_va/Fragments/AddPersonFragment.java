package com.example.listadopersonas_va.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.listadopersonas_va.DDBB.AppDataBase;
import com.example.listadopersonas_va.DDBB_Entities.ClsPersona;
import com.example.listadopersonas_va.R;
import com.example.listadopersonas_va.ViewModels.MainPageVM;

import java.util.GregorianCalendar;

public class AddPersonFragment extends Fragment implements View.OnClickListener {

    EditText nombre, apellidos, telefono;
    DatePicker fechaNacimiento;
    GregorianCalendar fechaNacimientoEnGC;
    ClsPersona personaCreada = new ClsPersona();
    Button addButton;
    MainPageVM viewModel;
    public AddPersonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_person, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(MainPageVM.class);

        nombre = view.findViewById(R.id.EditFirstNameCreate);
        apellidos = view.findViewById(R.id.EditLastNameCreate);
        telefono = view.findViewById(R.id.EditTelefonoCreate);
        fechaNacimiento = view.findViewById(R.id.calendario);    //link con su xml
        fechaNacimiento.setMaxDate(viewModel.getFechaActual().getTimeInMillis());  //Le indicamos la fecha máxima que puede introducir

        addButton = view.findViewById(R.id.btnSavePersonCreate);
        addButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        fechaNacimientoEnGC = new GregorianCalendar(fechaNacimiento.getYear(), fechaNacimiento.getMonth(), fechaNacimiento.getDayOfMonth());

        personaCreada.set_nombre(nombre.getText().toString());
        personaCreada.set_apellidos(apellidos.getText().toString());
        personaCreada.set_telefono(telefono.getText().toString());
        personaCreada.set_fechaNacimiento(fechaNacimientoEnGC);

        if(!personaCreada.get_nombre().isEmpty() && !personaCreada.get_apellidos().isEmpty() && !personaCreada.get_telefono().isEmpty()){
            //Insertamos la persona en la base de datos
            AppDataBase.getDataBase(getActivity().getBaseContext()).clsPersonaDao().insertPerson(personaCreada);
            //actualizamos la lista de contactos del viewModel
            viewModel.cargarListadoPersonas();
            Toast.makeText(getContext(), R.string.person_inserted, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), R.string.all_fields_are_required, Toast.LENGTH_SHORT).show();
        }
    }
}