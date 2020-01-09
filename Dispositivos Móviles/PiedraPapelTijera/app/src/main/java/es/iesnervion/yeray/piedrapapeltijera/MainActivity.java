package es.iesnervion.yeray.piedrapapeltijera;

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
    * Nombre: throwGameActivity
    * Comentario: Este método nos permite lanzar la actividad GameActivity.
    * Cabecera: public void throwGameActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad GameActivity.
    * */
    public void throwGameActivity(View v){
        startActivity(new Intent(this, GameActivity.class));
    }

    /*
     * Interfaz
     * Nombre: throwRecordsActivity
     * Comentario: Este método nos permite lanzar la actividad RecordsActivity.
     * Cabecera: public void throwRecordsActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad RecordsActivity.
     * */
    public void throwRecordsActivity(View v){
        startActivity(new Intent(this, RecordsActivity.class));
    }
}
