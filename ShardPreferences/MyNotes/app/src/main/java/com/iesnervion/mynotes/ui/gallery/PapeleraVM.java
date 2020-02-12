package com.iesnervion.mynotes.ui.gallery;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iesnervion.mynotes.Entidades.ClsNota;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PapeleraVM extends ViewModel {
    //Propiedades privadas
    private MutableLiveData<ArrayList<ClsNota>> listaNotas;
    private FirebaseFirestore db;

    //Constructor por defecto
    public PapeleraVM(){
        db = FirebaseFirestore.getInstance();
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
     * Obtiene las notas (si las hay) del usuario de la base de datos.
     */
    private void cargarNotas(){
        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("usuarios").whereEqualTo("usuario", currentUser)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    ArrayList<ClsNota> notas = new ArrayList<>();
                    for (QueryDocumentSnapshot dc : snapshots){//for (DocumentChange dc : snapshots.getDocumentChanges()){
                        notas.add(new ClsNota(dc.getString("id"), dc.getString("titulo"), dc.getString("fecha"), dc.getString("contenido")));
                    }
                    listaNotas.setValue(notas);
                }
            });
    }

}
