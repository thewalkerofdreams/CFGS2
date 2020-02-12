package com.iesnervion.mynotes.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iesnervion.mynotes.Adaptadores.CustomAdapter;
import com.iesnervion.mynotes.Entidades.ClsNota;
import com.iesnervion.mynotes.R;
import com.iesnervion.mynotes.editNote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment{

    private ListView lista;
    private DatabaseReference mDatabase;
    private ClsNota notaSeleccionada;
    private CustomAdapter customAdapter;
    private HomeVM vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        lista = root.findViewById(R.id.lvListaNotas);

        //Cargamos las notas en la lista.
        vm = new ViewModelProvider(getActivity()).get(HomeVM.class);

        //Creamos el Observer
        androidx.lifecycle.Observer<ArrayList<ClsNota>> listaObserver = new Observer<ArrayList<ClsNota>>() {
            @Override
            public void onChanged(ArrayList<ClsNota> listContact) {
                //Actualizar la UI
                customAdapter = new CustomAdapter(getActivity(), vm.getListaNotas().getValue() );
                lista.setAdapter(customAdapter);
            }
        };

        //Observamos los cambios de la lista
        vm.getListaNotas().observe(getActivity(), listaObserver);

        //Manejamos el click simple sobre algun elemento de la lista.
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Obtenemos la nota sobre la que se ha hecho click.
                notaSeleccionada = (ClsNota) adapterView.getItemAtPosition(i);
                //Pasamos el objeto atraves de un Intent
                Intent intent = new Intent(getContext(), editNote.class);
                intent.putExtra("notaSelected", notaSeleccionada);
                //Esto nos debe abrir la activity de crearNota
                startActivity(intent);
            }
        });

        //Manejamos la pulsacion larga sobre un elemento de la lista
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                notaSeleccionada = (ClsNota) adapterView.getItemAtPosition(i);
                MostrarDialogoBorrado();
                return true;
            }
        });

        //Establecemos el adaptador
        lista.setAdapter(customAdapter);

        return root;
    }

    /**
     * Muestra un dialogo que indica si quiere llevar a cabo la accion seleccionada, en este caso eliminar.
     * En caso afirmativo eliminará la nota seleccionada, en caso contrario no hará nada, simplemente desaparece el dialogo.
     */
    private void MostrarDialogoBorrado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.alertaElimTitulo);
        builder.setMessage(R.string.alertaElimDescripcion);
        builder.setPositiveButton(R.string.alertaElimConfirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        //Aqui tenemos que pasar la nota a la pestaña de Papelera
                        moverPapelera();
                        mDatabase.child(currentUser).child(notaSeleccionada.getID()).removeValue();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Mueve a la papelera la nota. Lo que hace es insertarla en la base de datos que tengo destinada a papelera.
     */
    private void  moverPapelera(){
        // Access a Cloud Firestore instance from your Activity
        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Para introducir los datos en FireBase Cloud FireStore
        Map<String, Object> nota = new HashMap<>();
        nota.put("id", notaSeleccionada.getID());
        nota.put("titulo", notaSeleccionada.getTitulo());
        nota.put("fecha", notaSeleccionada.getFecha());
        nota.put("contenido", notaSeleccionada.getContenido());
        nota.put("usuario", currentUser);

        //Añadimos la nota a la coleccion usuarios.
        db.collection("usuarios").add(nota)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(), R.string.movedToRecicle, Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), R.string.deleteNoteFailed, Toast.LENGTH_SHORT).show();
                }
            });
    }

    //No usados, pero mejor guardarlos

    /**
     * Obtiene las notas del usuario.
     */
    /*private void cargarListaNotas(){
        //Obtenemos la ID del usuario
        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaNotas.clear(); //Las tenemos que limpiar porque sino luego se concatenan
                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    ClsNota nota = objSnapshot.getValue(ClsNota.class);
                    listaNotas.add(nota);
                }

                //Ordenamos por fecha, asi la mas reciente sale primero
                Collections.sort(listaNotas, new Comparator<ClsNota>() {
                    public int compare(ClsNota o1, ClsNota o2) {
                        return o2.getFecha().compareTo(o1.getFecha());
                    }
                });

                customAdapter = new CustomAdapter(getActivity(), listaNotas );
                lista.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

}