package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;
import es.iesnervion.yeray.pocketcharacters.Fragments.CharacterStatsListFragment;
import es.iesnervion.yeray.pocketcharacters.Adapters.AdapterCharacterStats;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterStatsListActivityVM;

public class CharacterStatsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    ListView listView;
    ArrayList<ClsStatModel> statList = new ArrayList<ClsStatModel>();
    AdapterCharacterStats adapter;
    CharacterStatsListActivityVM viewModel;
    CharacterStatsListFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_stats_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(CharacterStatsListActivityVM.class);
        viewModel.set_character((ClsCharacter) getIntent().getExtras().getSerializable("Character01"));//Obtenemos el personaje

        statList = viewModel.get_statList().getValue();//Obtenemos el listado de stats
        listView = findViewById(R.id.ListViewHens);
        adapter = new AdapterCharacterStats(this, R.layout.item_character_stats, statList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new MethodsDDBB().existStatsWithoutAsignToCharacter(getApplication(), viewModel.get_character())){
                    throwNewCharacterStatActivity();
                }else{
                    Toast.makeText(getApplication(), R.string.all_stats_are_assigned, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(viewModel.get_statSelected() == null || fragment == null){//Si no hay ningún stat seleccionado o el fragmento aún no ha sido instanciado
            replaceFragment();
        }
        viewModel.set_statSelected((ClsStatModel) listView.getAdapter().getItem(position));//Obtenemos el stat seleccionado
    }

    @Override
    public boolean onItemLongClick (AdapterView<?> adapterView, View view,int i, long l){
        final ClsStatModel item = (ClsStatModel) adapterView.getItemAtPosition(i);//Obtenemos el item de la posición clicada
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_stat);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.stat_deleted, Toast.LENGTH_SHORT).show();
                //Aquí obtenemos el id del stat a modificar
                ClsStat stat = AppDataBase.getDataBase(getApplication()).statDao().getStatByGameModeAndName(viewModel.get_character().get_gameMode(), item.get_name());
                ClsCharacterAndStat clsCharacterAndStat = new ClsCharacterAndStat(viewModel.get_character().get_id(),
                        stat.get_id(), item.get_value());
                //Insertamos los datos en la tabla CharacterAndStat
                AppDataBase.getDataBase(getApplication()).characterAndStatDao().deleteCharacterAndStat(clsCharacterAndStat);
                removeYourFragment();//Eliminamos el fragmento
                reloadList();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;//Nos permite no realizar la acción de clicado rápido cuando dejamos pulsado un item.
    }

    /**
     * Interfaz
     * Nombre: replaceFragment
     * Comentario: Este método nos permite crear un fragmento y remplazar el contenido de nuestro
     * FrameLayout del layout de la actividad actual por ese mismo fragmento.
     * Cabecera: public void replaceFragment()
     * Postcondiciones: El método reemplaza el contenido del FrameLayout por el nuevo fragmento.
     * */
    public void replaceFragment(){
        fragment = new CharacterStatsListFragment();
        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
        transation.replace(R.id.FrameLayout01, fragment);
        transation.commit();
    }

    /**
     * Interfaz
     * Nombre: removeYourFragment
     * Comentario: Este método nos permite eliminar el fragmento de la actividad actual.
     * Cabecera: public void removeYourFragment()
     * Postcondiciones: El método elimina el fragmento de la actividad actual.
     * */
    public void removeYourFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.remove(fragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragment = null;
        }
    }

    /**
     * Intefaz
     * Nombre: throwNewCharacterStatActivity
     * Comentario: Este método nos permite lanzar la actividad NewCharacterStatActivity.
     * Cabecera: public void throwNewCharacterStatActivity()
     * Postcondiciones: El método lanza la actividad NewCharacterStatActivity.
     * */
    public void throwNewCharacterStatActivity(){
        Intent i = new Intent(this, NewCharacterStatActivity.class);
        i.putExtra("Character", viewModel.get_character());
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            reloadList();
        }
    }

    /**
    * Interfaz
    * Nombre: reloadList
    * Comentario: Este método nos permite recargar la lista de stats.
    * Cabecera: public void reloadList()
    * Postcondiciones: El método recarga la lista de stats.
    * */
    public void reloadList(){
        viewModel.loadStatList();
        statList = viewModel.get_statList().getValue();//Obtenemos el listado de stats
        adapter = new AdapterCharacterStats(this, R.layout.item_character_stats, statList);
        listView.setAdapter(adapter);
    }
}
