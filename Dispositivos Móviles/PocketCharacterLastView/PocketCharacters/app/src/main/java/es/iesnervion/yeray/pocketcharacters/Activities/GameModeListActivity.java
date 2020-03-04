package es.iesnervion.yeray.pocketcharacters.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.Adapters.AdapterGameModeList;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.GameModeListActivityVM;

public class GameModeListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    AdapterGameModeList adapter;
    GameModeListActivityVM viewModel;
    ListView listView;
    AlertDialog alertDialogDeleteGameMode, alertDialogCreateGameMode;
    EditText nameEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(GameModeListActivityVM.class);//Instanciamos el ViewModel

        listView = findViewById(R.id.ListViewGameModes);
        adapter = new AdapterGameModeList(this, R.layout.item_game_mode_list, viewModel.get_gameModeList());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNewGameMode();
            }
        });

        if(savedInstanceState != null && viewModel.is_dialogDeleteShowing()) {//Si el dialogo de eliminación estaba abierto lo recargamos
            showDialogDeleteGameMode(getApplication());
        }else{
            if(savedInstanceState != null && viewModel.is_dialogCreateShowing()){//Si el dialogo de creación estaba abierto lo recargamos
                dialogNewGameMode();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClsGameMode item = (ClsGameMode) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        startActivity(new Intent(this, GameModeDatasActivity.class).putExtra("GameMode", item.get_name()));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsGameMode item = (ClsGameMode) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        if(!new MethodsDDBB().gameModeDependency(getApplication(), item.get_name())){
            viewModel.set_gameModeToDelete(item);//Almacenamos el item seleccionado en el VM
            showDialogDeleteGameMode(this);//Abrimos el dialogo de eliminación
        }else{
            Toast.makeText(getApplication(), getApplication().getString(R.string.you_cant_delete_this_game_mode), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
    * Interfaz
    * Nombre: reloadList
    * Comentario: Este método nos permite recargar la lista de modos de juego.
    * Cabecera: public void reloadList()
    * Postcondiciones: El método recarga la lista.
    * */
    public void reloadList(){
        viewModel.set_gameModeList(new ArrayList<>(AppDataBase.getDataBase(getApplication()).gameModeDao().getAllGameModes()));
        adapter = new AdapterGameModeList(this, R.layout.item_game_mode_list, viewModel.get_gameModeList());
        listView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteGameMode != null && alertDialogDeleteGameMode.isShowing()) {//Si se encuentra abierto el dialogo de deleteGameMode
            alertDialogDeleteGameMode.dismiss();// close dialog to prevent leaked window
            viewModel.set_dialogDeleteShowing(true);
        }else{
            if(alertDialogCreateGameMode != null && alertDialogCreateGameMode.isShowing()){//Si se encuentra abierto el dialogo de createGameMode
                alertDialogCreateGameMode.dismiss();
                viewModel.set_dialogCreateShowing(true);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: dialogNewGameMode
     * Comentario: Este método muestra por pantalla un dialogo para crear un nuevo modo de juego,
     * si el usuario inserta todos los datos necesarios y son válidos, una vez pulse el botón de crear
     * se almacenará ese nuevo GameMode en la base de datos de la aplicación.
     * Cabecera: public void dialogNewGameMode()
     * Postcondiciones: El método inserta un nuevo GameMode en la base de datos o se cancela el dialogo.
     * */
    public void dialogNewGameMode(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameModeListActivity.this);
        nameEdit = new EditText(GameModeListActivity.this);
        nameEdit.setText(viewModel.get_newGameModeName());
        nameEdit.setHint(getString(R.string.set_game_mode_name_hint));//Le insertamos una pista
        builder.setTitle(getString(R.string.dialog_title))
                .setView(nameEdit)
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.set_newGameModeName(nameEdit.getText().toString().trim());
                        if (viewModel.get_newGameModeName().length() == 0) {
                            Toast.makeText(getApplication(), getApplication().getString(R.string.dialog_toast1), Toast.LENGTH_SHORT).show();
                        } else {
                            if(new MethodsDDBB().existGameMode(getApplication(), viewModel.get_newGameModeName())){//Si ya existe un GameMode con ese nombre
                                Toast.makeText(getApplication(), getApplication().getString(R.string.dialog_toast2), Toast.LENGTH_SHORT).show();
                            }else{
                                AppDataBase.getDataBase(getApplication()).gameModeDao().insertGameMode(new ClsGameMode(viewModel.get_newGameModeName()));
                                reloadList();
                                Toast.makeText(getApplication(), getApplication().getString(R.string.dialog_toast3), Toast.LENGTH_SHORT).show();
                                viewModel.set_dialogCreateShowing(false);
                                viewModel.set_newGameModeName("");
                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.set_dialogCreateShowing(false);
                        viewModel.set_newGameModeName("");
                        dialog.cancel();
                    }
                });
        alertDialogCreateGameMode = builder.create();
        alertDialogCreateGameMode.show();//Lanzamos el dialogo
    }

    /**
     * Interfaz
     * Nombre: showDialogDeleteGameMode
     * Comentario: Este método muestra por pantalla un dialogo para eliminar un modo de juego, si el usuario
     * confirma la eliminación, se elimina ese modo de juego de la base de datos.
     * Cabecera: public void showDialogDeleteGameMode()
     * Postcondiciones: El método elimina un GameMode en la base de datos o se cancela el dialogo.
     * */
    protected void showDialogDeleteGameMode(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_game_mode);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.game_mode_deleted, Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getApplication()).gameModeDao().deleteGameMode(viewModel.get_gameModeToDelete());
                reloadList();
                viewModel.set_dialogDeleteShowing(false);
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogDeleteShowing(false);
            }
        });

        alertDialogDeleteGameMode = alertDialogBuilder.create();
        alertDialogDeleteGameMode.show();
    }

    public void onStop() {

        super.onStop();

        if(alertDialogCreateGameMode != null){//Si cerramos la actividad con un dialogo de creación en pantalla
            viewModel.set_newGameModeName(nameEdit.getText().toString());//Guardamos sus datos
        }
    }
}
