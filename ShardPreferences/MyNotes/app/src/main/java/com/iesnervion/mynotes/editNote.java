package com.iesnervion.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iesnervion.mynotes.Entidades.ClsNota;

public class editNote extends AppCompatActivity {
    EditText titulo;
    EditText contenido;
    private DatabaseReference mDatabase;
    ClsNota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        //Vinculos entre java y xml
        titulo = findViewById(R.id.etTituloEdit);
        contenido = findViewById(R.id.etContenidoEdit);

        //Obtengo la nota seleccionada.
        nota = (ClsNota) getIntent().getSerializableExtra("notaSelected");

        titulo.setText(nota.getTitulo());
        contenido.setText(nota.getContenido());

        inicializarFireBase();
    }

    private void inicializarFireBase(){
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Actualiza una nota en la base de datos.
     * @param v Vista sobre la que se ha pulsado, en este caso sobre el boton editar.
     */
    public void editarNota(View v){
        nota.setTitulo(titulo.getText().toString());
        nota.setContenido(contenido.getText().toString());

        //Obtengo el ID del usuario.
        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(currentUser).child(nota.getID()).setValue(nota);

        Toast.makeText(this, R.string.toastGuardado, Toast.LENGTH_SHORT).show();
    }
}
