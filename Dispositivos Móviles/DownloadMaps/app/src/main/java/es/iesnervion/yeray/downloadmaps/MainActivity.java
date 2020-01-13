package es.iesnervion.yeray.downloadmaps;

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

    /*
    * Interfaz
    * Nombre: throwDownloadActivity
    * Comentario: Este método nos permite lanzar la actividad DownloadActivity.
    * Cabecera: public void throwDownloadActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad DownloadActivity.
    * */
    public void throwDownloadActivity(View v){
        startActivity(new Intent(this, DownloadActivity.class));
    }

    /*
     * Interfaz
     * Nombre: throwMapListActivity
     * Comentario: Este método nos permite lanzar la actividad MapListActivity.
     * Cabecera: public void throwMapListActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad MapListActivity.
     * */
    public void throwMapListActivity(View v){
        startActivity(new Intent(this, MapListActivity.class));
    }
}
