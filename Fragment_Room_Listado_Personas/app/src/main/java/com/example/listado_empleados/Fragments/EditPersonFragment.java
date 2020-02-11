package com.example.listado_empleados.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.listado_empleados.Activities.MainActivity;
import com.example.listado_empleados.DDBB.AppDataBase;
import com.example.listado_empleados.DDBB_Entities.ClsDepartamento;
import com.example.listado_empleados.DDBB_Entities.ClsPersona;
import com.example.listado_empleados.R;
import com.example.listado_empleados.ViewModels.MainActivityVM;

import java.util.ArrayList;

public class EditPersonFragment extends Fragment {

    private EditText editFirstName, editLastName, editPhone;
    private Spinner spinnerDepartament;
    private MainActivityVM viewModel;
    private Button btnCreate, btnCancel;
    private ArrayList<String> departamentosSpinner = new ArrayList<>();
    ArrayList<ClsDepartamento> departamentos = null;

    public EditPersonFragment(){//Constructor por defecto.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_person, container, false);//Inflamos el layout del fragment
        viewModel = ViewModelProviders.of(getActivity()).get(MainActivityVM.class);//Instanciamos el viewModel
        departamentos = viewModel.get_departamentList().getValue();//Obtenemos los departamentos

        editFirstName = view.findViewById(R.id.EditPersonFirstNameAddFragment);
        editLastName = view.findViewById(R.id.EditPersonLastNameAddFragment);
        editPhone = view.findViewById(R.id.EditPersonPhoneAddFragment);
        spinnerDepartament = view.findViewById(R.id.SpinnerDepartamentNameAddFragment);
        btnCreate = view.findViewById(R.id.btnSavePersonAddFragment);
        btnCancel = view.findViewById(R.id.btnCancelPersonAddFragment);

        //Insertamos los valores por defecto de la persona seleccionada para la modificación en los textBoxs
        editFirstName.setText(viewModel.get_selectedEmployee().get_nombre());
        editLastName.setText(viewModel.get_selectedEmployee().get_apellidos());
        editPhone.setText(viewModel.get_selectedEmployee().get_telefono());

        loadSpinner();//Cargamos el spinner
        viewModel.setNewPersonDepartament(viewModel.get_selectedEmployee().get_idDepartamento());

        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewModel.setNewPersonFirstName(editFirstName.getText().toString());
                viewModel.setNewPersonLastName(editLastName.getText().toString());
                viewModel.setNewPersonPhone(editPhone.getText().toString());
                if(!viewModel.getNewPersonFirstName().isEmpty() && !viewModel.getNewPersonLastName().isEmpty() && !viewModel.getNewPersonPhone().isEmpty()){
                    AppDataBase.getDataBase(getContext()).clsPersonaDao().updatePerson(new ClsPersona(viewModel.get_selectedEmployee().get_id(), viewModel.getNewPersonFirstName(),
                            viewModel.getNewPersonLastName(), viewModel.getNewPersonPhone(), viewModel.getNewPersonDepartament()));
                    Toast.makeText(getContext(), R.string.person_inserted, Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).removeYourFragments();//Cerramos el fragment
                    //((MainActivity)getActivity()).reloadList();//Recargamos la lista
                }else{
                    Toast.makeText(getContext(), R.string.all_fields_are_required, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Tenemos que cerrar el fragmento
                ((MainActivity)getActivity()).removeYourFragments();
            }
        });

        spinnerDepartament.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setNewPersonDepartament(departamentos.get(position).get_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //No hacemos nada
            }
        });

        return view;
    }

    private void loadSpinner(){
        for(int i = 0; i < departamentos.size(); i++){//Cargamos los datos para el spinner
            departamentosSpinner.add(departamentos.get(i).get_nombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, departamentosSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartament.setAdapter(adapter);
    }

}