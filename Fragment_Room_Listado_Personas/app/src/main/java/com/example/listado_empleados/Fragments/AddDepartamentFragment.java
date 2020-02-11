package com.example.listado_empleados.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.listado_empleados.Activities.MainActivity;
import com.example.listado_empleados.DDBB.AppDataBase;
import com.example.listado_empleados.DDBB_Entities.ClsDepartamento;
import com.example.listado_empleados.R;
import com.example.listado_empleados.ViewModels.MainActivityVM;

public class AddDepartamentFragment extends Fragment {

    private EditText editName;
    private MainActivityVM viewModel;
    private Button btnCreate, btnCancel;

    public AddDepartamentFragment(){//Constructor por defecto.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_departament, container, false);//Inflamos el layout del fragment
        viewModel = ViewModelProviders.of(getActivity()).get(MainActivityVM.class);//Instanciamos el viewModel

        editName = view.findViewById(R.id.EditDepartamentNameAddFragment02);
        btnCreate = view.findViewById(R.id.btnSaveDepartamentAddFragment);
        btnCancel = view.findViewById(R.id.btnCancelDepartamentAddFragment);

        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewModel.setNewDepartamentName(editName.getText().toString());
                if(!viewModel.getNewDepartamentName().isEmpty()){
                    AppDataBase.getDataBase(getContext()).clsDepartamentoDao().insertDepartament(new ClsDepartamento(viewModel.getNewDepartamentName()));
                    Toast.makeText(getContext(), R.string.departament_inserted, Toast.LENGTH_SHORT).show();
                    viewModel.loadDepartamentList();//Cargamos la nueva lista de departamentos en el viewModel
                    ((MainActivity)getActivity()).removeYourFragments();//Cerramos el fragmento
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
        return view;
    }

}
