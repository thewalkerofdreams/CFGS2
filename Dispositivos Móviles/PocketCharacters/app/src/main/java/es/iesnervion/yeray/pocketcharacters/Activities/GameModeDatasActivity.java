package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import es.iesnervion.yeray.pocketcharacters.R;

public class GameModeDatasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_datas);
    }

    /*
    * Interfaz
    * Nombre: throwObjectListSimpleActivity
    * Comentario: Este método nos permite lanzar la actividad ObjectListSimpleActivity.
    * Cabecera: public void throwObjectListSimpleActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad ObjectListSimpleActivity.
    * */
    public void throwObjectListSimpleActivity(View v){
        startActivity(new Intent(this, ObjectListSimpleActivity.class).putExtra("GameMode", getIntent().getStringExtra("GameMode")));
    }

    /*
     * Interfaz
     * Nombre: throwStatListActivity
     * Comentario: Este método nos permite lanzar la actividad StatListActivity.
     * Cabecera: public void throwStatListActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad StatListActivity.
     * */
    public void throwStatListActivity(View v){
        startActivity(new Intent(this, StatListActivity.class).putExtra("GameMode", getIntent().getStringExtra("GameMode")));
    }
}
