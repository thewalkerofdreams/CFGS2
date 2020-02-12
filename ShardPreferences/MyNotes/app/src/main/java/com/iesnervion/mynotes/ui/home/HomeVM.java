package com.iesnervion.mynotes.ui.home;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iesnervion.mynotes.Entidades.ClsNota;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeVM extends ViewModel {
    //Propiedades privadas
    private MutableLiveData<ArrayList<ClsNota>> listaNotas;
    private DatabaseReference mDatabase;

    //Constructor por defecto
    public HomeVM(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //Propiedades publicas
    public LiveData<ArrayList<ClsNota>> getListaNotas(){
        if(listaNotas == null){
            listaNotas = new MutableLiveData<>();
            cargarNotas();
        }
        return listaNotas;
    }

    /**
     * Obtiene las notas del usuario de la base de datos FireBase.
     */
    private void cargarNotas(){
        //Obtenemos la ID del usuario
        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ClsNota> notasAux = new ArrayList<>();
                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    ClsNota nota = objSnapshot.getValue(ClsNota.class);
                    notasAux.add(nota);
                }

                listaNotas.setValue(notasAux);

                //Ordenamos por fecha, asi la mas reciente sale primero
                Collections.sort(listaNotas.getValue(), new Comparator<ClsNota>() {
                    public int compare(ClsNota o1, ClsNota o2) {
                        return o1.getFecha().compareTo(o2.getFecha());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
