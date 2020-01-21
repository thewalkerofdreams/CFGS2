package es.iesnervion.yeray.pocketcharacters.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.MainActivityVM;

public class MainActivity extends AppCompatActivity {

    MainActivityVM viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_idUser(1);//TODO Aún no tenemos el lógin como prueba siempre entrará el jugador con id 1
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
        startActivity(new Intent(this, CharacterListActivity.class).putExtra("idUser", viewModel.get_idUser()));
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
        startActivity(new Intent(this, GameModeListActivity.class).putExtra("idUser", viewModel.get_idUser()));
    }

    /*
    * Interfaz
    * Nombre: trySincroniceDatasWithServer
    * Comentario: Este método pregunta al usuario si de verdad desea sincronizar su
    * cuenta con su último punto de guardado realizado en el servidor.
    * Cabecera: public void trySincroniceDatasWithServer(View v)
    * Entrada:
    *   -View v
    * Postcondiciones:
    *   -Si el usuario acepta y el servidor no contiene ningún punto de guardado
    *   de ese usuario, no se realiza la sincronización e informa al usuario de lo ocurrido.
    *   -Si el usuario acepta y en el servidor existe un punto de guardado de ese usuario,
    *   se realizará la sincornización de datos.
    *   -Si el usuario cancela, no ocurre nada.
    * */
    public void trySincroniceDatasWithServer(View v){
        //TODO Realizar cuando la Api este creada
        //Si todo es correcto realizaríamos una llamada al método "sincroniceDatasWithServer" del viewmodel
    }
}
