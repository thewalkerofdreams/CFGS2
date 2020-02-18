package es.iesnervion.yeray.pocketcharacters.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterStatList;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.StatListActivityVM;

public class StatListActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    AdapterStatList adapter;
    StatListActivityVM viewModel;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.ListViewStats02);
        viewModel = ViewModelProviders.of(this).get(StatListActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_gameMode(getIntent().getStringExtra("GameMode"));

        adapter = new AdapterStatList(this, R.layout.item_stat_list, viewModel.get_statList());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNewStat();
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsStat item = (ClsStat) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_stat);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.stat_deleted, Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getApplication()).statDao().deleteStat(item);
                reloadList();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    /**
     * Interfaz
     * Nombre: dialogNewStat
     * Comentario: Este método muestra por pantalla un dialogo para crear un nuevo stat,
     * si el usuario inderta todos los datos necesarios y son válidos, una vez pulse el botón de crear
     * se almacenará ese nuevo stat en la base de datos de la aplicación.
     * Cabecera: public void dialogNewStat()
     * Postcondiciones: El método inserta un nuevo stat en la base de datos o se cancela el dialogo.
     * */
    public void dialogNewStat(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StatListActivity.this);
        //Declaramos un editText temporal
        final EditText nameEdit = new EditText(StatListActivity.this);
        nameEdit.setHint(R.string.stat_name);//Le insertamos una pista
        // Build the dialog box
        builder.setTitle(R.string.new_stat)
                .setView(nameEdit)
                .setMessage(R.string.create_a_new_stat)
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String statName = nameEdit.getText().toString();
                        if (statName.length() == 0) {
                            Toast.makeText(getApplication(), R.string.name_stat_empty, Toast.LENGTH_SHORT).show();
                        } else {
                            if(new MethodsDDBB().existStat(getApplication(), viewModel.get_gameMode(), statName)){//Si ya existe un stat con ese nombre en el mismo gameMode
                                Toast.makeText(getApplication(), R.string.already_exist_stat_game_mode, Toast.LENGTH_SHORT).show();
                            }else{
                                ClsStat stat = new ClsStat(statName, viewModel.get_gameMode());
                                AppDataBase.getDataBase(getApplication()).statDao().insertStat(stat);
                                reloadList();
                                Toast.makeText(getApplication(), R.string.stat_saved, Toast.LENGTH_SHORT).show();
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

    /**
     * Interfaz
     * Nombre: reloadList
     * Comentario: Este método nos permite recargar la lista de stats.
     * Cabecera: public void reloadList()
     * Postcondiciones: El método recarga la lista.
     * */
    public void reloadList(){
        viewModel.set_statList(new ArrayList<ClsStat>(AppDataBase.getDataBase(getApplication()).statDao().getStatsByGameMode(viewModel.get_gameMode())));
        adapter = new AdapterStatList(this, R.layout.item_stat_list, viewModel.get_statList());
        listView.setAdapter(adapter);
    }
}
