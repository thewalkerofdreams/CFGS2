package es.iesnervion.yeray.pocketcharacters.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import es.iesnervion.yeray.pocketcharacters.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    * Interfaz
    * Nombre: throwCharacterListActivity
    * Comentario: Este método nos permite lanzar la actividad CharacterListActivity.
    * Cabecera: public void throwCharacterListActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad CharacterListActivity.
    * */
    public void throwCharacterListActivity(View v){
        startActivity(new Intent(this, CharacterListActivity.class));
    }

    /*
     * Interfaz
     * Nombre: throwGameModeListActivity
     * Comentario: Este método nos permite lanzar la actividad GameModeListActivity.
     * Cabecera: public void throwGameModeListActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad GameModeListActivity.
     * */
    public void throwGameModeListActivity(View v){
        startActivity(new Intent(this, GameModeListActivity.class));
    }
}
