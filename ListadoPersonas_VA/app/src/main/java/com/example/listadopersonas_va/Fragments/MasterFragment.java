package com.example.listadopersonas_va.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.listadopersonas_va.Adapters.AdapterPersonaList;
import com.example.listadopersonas_va.DDBB.AppDataBase;
import com.example.listadopersonas_va.DDBB_Entities.ClsPersona;
import com.example.listadopersonas_va.R;
import com.example.listadopersonas_va.ViewModels.MainPageVM;

import java.util.ArrayList;

public class MasterFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener
{
    MainPageVM viewModel;
    ListView listView;
    ImageButton btnAddContact, searchButton ;
    AutoCompleteTextView autoCompleteTextView;
    public MasterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.master_layout_main, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(MainPageVM.class);

        listView = view.findViewById(R.id.listViewContactos);
        searchButton = view.findViewById(R.id.searchButton);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteMainActivity);
        btnAddContact = view.findViewById(R.id.btnAddPerson);
        //Adapter
        final AdapterPersonaList adapter = new AdapterPersonaList(getActivity().getBaseContext(), R.layout.item_person_list, viewModel.getContactList().getValue());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        btnAddContact.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getBaseContext(), android.R.layout.simple_dropdown_item_1line, viewModel.getContactList().getValue());
        autoCompleteTextView.setAdapter(arrayAdapter);

        /*El observer*/
        final Observer<ArrayList<ClsPersona>> listObserver = new Observer<ArrayList<ClsPersona>>() {
            @Override
            public void onChanged(ArrayList<ClsPersona> personas) {
                listView.invalidate();//TODO Para que hacía esto??
                AdapterPersonaList adapter02 = new AdapterPersonaList(getActivity(), R.layout.item_person_list, viewModel.getContactList().getValue());
                listView.setAdapter(adapter02);
                // contactAdapter.notifyDataSetChanged(); //-> con esto se actualiza al cambiar de fragment
            }
        };

        //Observo el LiveData con ese observer que acabo de crear
        viewModel.getContactList().observe(getActivity(), listObserver);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClsPersona personaSeleccionada = null;
        //Cojo el contacto seleccionado
        if(position >= 0){
            personaSeleccionada = viewModel.getContactList().getValue().get(position);
        }
        //Le digo al view model que este es la persona seleccionada
        viewModel.set_personSelected(personaSeleccionada);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsPersona item = (ClsPersona) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_person);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.person_deleted, Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getActivity()).clsPersonaDao().deletePerson(item);
                reloadList();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddPerson:
                viewModel.setIsAddBtnPressed(true);
                break;
            case R.id.searchButton:
                /*ClsPersona personaSeleccionada = AppDatabase.getDatabase(getActivity().getBaseContext()).contactDAO()
                        .obtenerContactoPorNombre(autoCompleteTextView.getText().toString());

//                if(viewModel.getContactList().getValue().contains(contactoSeleccionado)){
//                    viewModel.setContactoSeleccionado(contactoSeleccionado);
//                }
                //Eso no funciona porque no es la misma referencia

                boolean encontrado = false;
                int index = 0;
                if(personaSeleccionada != null){
                    for(int i = 0; i < viewModel.getContactList().getValue().size() && !encontrado; i ++){

                        if(viewModel.getContactList().getValue().get(i).getId() == personaSeleccionada.getId()){
                            encontrado = true;
                            index = i;
                        }
                    }

                    viewModel.setContactoSeleccionado(viewModel.getContactList().getValue().get(index));
                }*/
                break;
        }
    }

    public void reloadList(){
        viewModel.cargarListadoPersonas();
        final AdapterPersonaList adapter = new AdapterPersonaList(getActivity().getBaseContext(), R.layout.item_person_list, viewModel.getContactList().getValue());
        listView.setAdapter(adapter);
    }
}
