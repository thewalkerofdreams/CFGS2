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

public class crearNota extends AppCompatActivity {
    EditText titulo;
    EditText contenido;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);

        //Vinculos entre java y xml
        titulo = findViewById(R.id.etTitulo);
        contenido = findViewById(R.id.etContenido);

        inicializarFireBase();
    }

    /**
     * Inicializa la base de datos.
     */
    private void inicializarFireBase(){
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Guarda una nota en la base de datos.
     * @param view Vista sobre la que se ha pulsado.
     */
    public void guardarNota(View view){
        ClsNota nota = new ClsNota();

        if(!titulo.getText().toString().isEmpty()){ //Para comprobar si el titulo esta vacio, en cuyo caso se le deja el por defecto.
            nota.setTitulo(titulo.getText().toString());
        }
        nota.setContenido(contenido.getText().toString());

        //Obtengo el ID del usuario.
        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(currentUser).child(nota.getID()).setValue(nota); //El ID de la nota es aleatorio.

        //Mostramos un mensaje de guardado satisfactorio
        Toast.makeText(this, R.string.toastCreado, Toast.LENGTH_SHORT).show();
        finish();

    }
}