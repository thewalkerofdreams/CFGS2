package com.iesnervion.mynotes.ui.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.iesnervion.mynotes.Entidades.ClsNota;
import com.iesnervion.mynotes.R;

public class papeleraNota extends AppCompatActivity {
    TextView titulo, contenido;
    ClsNota nota;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papelera_nota);

        titulo = findViewById(R.id.titlePapelera);
        contenido = findViewById(R.id.contenidoPapelera);
        contenido.setMovementMethod(new ScrollingMovementMethod());
        inicializarFireBase();

        //Obtengo la nota seleccionada.
        nota = (ClsNota) getIntent().getSerializableExtra("notaSelected");

        //Establecemos el contenido de las vistas.
        titulo.setText(nota.getTitulo());
        contenido.setText(nota.getContenido());
    }

    /**
     * Restaura la nota seleccionada.
     * @param v Vista sobre la que se ha pulsado, en este caso el boton de restaurar.
     */
    public void restaurarNota(View v){
        //La tenemos que pasar a la base de datos principal
        String currentUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(currentUser).child(nota.getID()).setValue(nota);

        eliminarNote();
        Toast.makeText(this, R.string.restaurada, Toast.LENGTH_SHORT).show();
    }

    /**
     * Muestra un mensaje de alerta, avisando que se va a eliminar la nota.
     * @param v Vista sobre la que se ha pulsado, en este caso el boton de eliminar.
     */
    public void eliminarNota(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.alertaElimTitle);
        builder.setMessage(R.string.alertaElimContenido);
        builder.setPositiveButton(R.string.alertaElimConfirm,
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Eliminar la nota de la base de datos.
                eliminarNote();
                Toast.makeText(getApplication(), R.string.borrada, Toast.LENGTH_SHORT).show();
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

    private void inicializarFireBase(){
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Elimina una nota de forma definitva.
     */
    private void eliminarNote(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference itemsRef = rootRef.collection("usuarios");
        Query query = itemsRef.whereEqualTo("id", nota.getID());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        itemsRef.document(document.getId()).delete();
                    }
                }
            }
        });
        finish();
    }
}
