package es.iesnervion.yeray.clienteapirest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Intefaz
     * Nombre: throwInsertBookActivity
     * Comentario: Este método nos permite lanzar la actividad InsertBookActivity.
     * Cabecera: public void throwInsertBookActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad InsertBookActivity.
     * */
    public void throwInsertBookActivity(View v){
        startActivity(new Intent(this, InsertBookActivity.class));
    }

    /**
     * Intefaz
     * Nombre: throwDeleteBookActivity
     * Comentario: Este método nos permite lanzar la actividad DeleteBookActivity.
     * Cabecera: public void throwDeleteBookActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad DeleteBookActivity.
     * */
    public void throwDeleteBookActivity(View v){
        startActivity(new Intent(this, DeleteBookActivity.class));
    }

    /**
     * Intefaz
     * Nombre: throwFindBookActivity
     * Comentario: Este método nos permite lanzar la actividad FindBookActivity.
     * Cabecera: public void throwFindBookActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad FindBookActivity.
     * */
    public void throwFindBookActivity(View v){
        startActivity(new Intent(this, FindBookActivity.class));
    }

    /**
     * Intefaz
     * Nombre: throwUpdateBookActivity
     * Comentario: Este método nos permite lanzar la actividad UpdateBookActivity.
     * Cabecera: public void throwUpdateBookActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad UpdateBookActivity.
     * */
    public void throwUpdateBookActivity(View v){
        startActivity(new Intent(this, UpdateBookActivity.class));
    }
}
