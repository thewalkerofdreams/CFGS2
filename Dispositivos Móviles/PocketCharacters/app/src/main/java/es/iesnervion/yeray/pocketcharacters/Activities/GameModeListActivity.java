package es.iesnervion.yeray.pocketcharacters.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterGameModeList;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterListActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.GameModeListActivityVM;

public class GameModeListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    AdapterGameModeList adapter;
    ArrayList<ClsGameMode> items = new ArrayList<>();
    GameModeListActivityVM viewModel;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.ListViewGameModes);
        viewModel = ViewModelProviders.of(this).get(GameModeListActivityVM.class);//Instanciamos el ViewModel

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
    }

    /*
     * Interfaz
     * Nombre: dialogNewGameMode
     * Comentario: Este método muestra por pantalla un dialogo para crear un nuevo modo de juego,
     * si el usuario inderta todos los datos necesarios y son válidos, una vez pulse el botón de crear
     * se almacenará ese nuevo GameMode en la base de datos de la aplicación.
     * Cabecera: public void dialogNewGameMode()
     * Postcondiciones: El método inserta un nuevo GameMode en la base de datos o se cancela el dialogo.
     * */
    public void dialogNewGameMode(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameModeListActivity.this);
        //Declaramos un editText temporal
        final EditText nameEdit = new EditText(GameModeListActivity.this);
        nameEdit.setHint(getString(R.string.set_game_mode_name_hint));//Le insertamos una pista
        // Build the dialog box
        builder.setTitle(getString(R.string.dialog_title))
                .setView(nameEdit)
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gameModeName = nameEdit.getText().toString();
                        if (gameModeName.length() == 0) {
                            Toast.makeText(getApplication(), getApplication().getString(R.string.dialog_toast1), Toast.LENGTH_SHORT).show();
                        } else {
                            if(new MethodsDDBB().existGameMode(getApplication(), gameModeName)){//Si ya existe un GameMode con ese nombre
                                Toast.makeText(getApplication(), getApplication().getString(R.string.dialog_toast2), Toast.LENGTH_SHORT).show();
                            }else{
                                AppDataBase.getDataBase(getApplication()).gameModeDao().insertGameMode(new ClsGameMode(gameModeName));
                                reloadList();
                                Toast.makeText(getApplication(), getApplication().getString(R.string.dialog_toast3), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();//Lanzamos el dialogo
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClsGameMode item = (ClsGameMode) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        startActivity(new Intent(this, GameModeDatasActivity.class).putExtra("GameMode", item.get_name()));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsGameMode item = (ClsGameMode) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Delete");// Setting Alert Dialog Title
        alertDialogBuilder.setMessage("Do you really want delete this GameMode?");// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), "GameMode deleted", Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getApplication()).gameModeDao().deleteGameMode(item);
                reloadList();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;//Nos permite no realizar la acción de clicado rápido cuando dejamos pulsado un item.
    }

    /*
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
}
