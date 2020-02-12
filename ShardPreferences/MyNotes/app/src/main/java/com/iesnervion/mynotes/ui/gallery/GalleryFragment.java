package com.iesnervion.mynotes.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.iesnervion.mynotes.Adaptadores.CustomAdapter;
import com.iesnervion.mynotes.Entidades.ClsNota;
import com.iesnervion.mynotes.R;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private ListView listaNotas;
    private ClsNota notaSeleccionada;
    private PapeleraVM vm;
    private CustomAdapter customAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        listaNotas = root.findViewById(R.id.lvListaNotasBin);

        vm = new ViewModelProvider(getActivity()).get(PapeleraVM.class);

        //Creamos el Observer
        androidx.lifecycle.Observer<ArrayList<ClsNota>> listaObserver = new Observer<ArrayList<ClsNota>>() {
            @Override
            public void onChanged(ArrayList<ClsNota> listContact) {
                //Actualizar la UI
                customAdapter = new CustomAdapter(getActivity(), vm.getListaNotas().getValue());
                listaNotas.setAdapter(customAdapter);
            }
        };

        //Observamos los cambios de la lista
        vm.getListaNotas().observe(getActivity(), listaObserver);

        //Establecemos el adaptador
        listaNotas.setAdapter(customAdapter);

        //Para obtener el item de la lista sobre el que se ha pulsado.
        listaNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Obtenemos la nota sobre la que se ha hecho click.
                notaSeleccionada = (ClsNota) adapterView.getItemAtPosition(i);
                //Pasamos el objeto atraves de un Intent
                Intent intent = new Intent(getContext(), papeleraNota.class);
                intent.putExtra("notaSelected", notaSeleccionada);
                //Esto nos debe abrir la activity de crearNota
                startActivity(intent);
            }
        });

        return root;
    }

}