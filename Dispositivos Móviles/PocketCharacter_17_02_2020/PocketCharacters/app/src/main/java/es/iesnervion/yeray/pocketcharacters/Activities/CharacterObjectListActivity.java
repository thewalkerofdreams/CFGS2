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
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsObjectAndQuantity;
import es.iesnervion.yeray.pocketcharacters.Fragments.CharacterObjectsListFragment;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterObjectList;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterObjectListActivityVM;

public class CharacterObjectListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    ListView listView;
    ArrayList<ClsObjectAndQuantity> objectList = new ArrayList<ClsObjectAndQuantity>();
    AdapterObjectList adapter;
    CharacterObjectListActivityVM viewModel;
    CharacterObjectsListFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_object_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(CharacterObjectListActivityVM.class);
        viewModel.set_character((ClsCharacter) getIntent().getExtras().getSerializable("Character01"));

        objectList = viewModel.get_objectList().getValue();//Obtenemos el listado de stats
        listView = findViewById(R.id.ListViewObjectsCharacter);
        adapter = new AdapterObjectList(this, R.layout.item_object_list, objectList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throwNewCharacterObjectActivity();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(viewModel.get_objectSelected() == null || fragment == null){//Si no hay ningún objeto seleccionado o el fragmento aún no ha sido instanciado
            replaceFragment();
        }
        viewModel.set_objectSelected((ClsObjectAndQuantity) listView.getAdapter().getItem(position));
    }

    @Override
    public boolean onItemLongClick (AdapterView<?> adapterView, View view,int i, long l){
        final ClsObjectAndQuantity item = (ClsObjectAndQuantity) adapterView.getItemAtPosition(i);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_object);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.object_deleted, Toast.LENGTH_SHORT).show();
                ClsObjectAndCharacter objectAndCharacter = new ClsObjectAndCharacter(viewModel.get_character().get_id(),
                        item.get_object().get_id(), item.get_quantity());
                //Insertamos los datos en la tabla CharacterAndStat
                AppDataBase.getDataBase(getApplication()).objectAndCharacterDao().deleteObjectAndCharacter(objectAndCharacter);
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
     * FrameLayout por ese mismo fragmento.
     * Cabecera: public void replaceFragment()
     * Postcondiciones: El método reemplaza el contenido del FrameLayout por el nuevo fragmento.
     * */
    public void replaceFragment(){
        fragment = new CharacterObjectsListFragment();
        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
        transation.replace(R.id.FrameLayout02, fragment);
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
     * Nombre: throwNewCharacterObjectActivity
     * Comentario: Este método nos permite lanzar la actividad NewCharacterObjectActivity.
     * Cabecera: public void throwNewCharacterObjectActivity()
     * Postcondiciones: El método lanza la actividad NewCharacterObjectActivity.
     * */
    public void throwNewCharacterObjectActivity(){
        Intent i = new Intent(this, NewCharacterObjectActivity.class);
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
     * Comentario: Este método nos permite recargar la lista de objetos.
     * Cabecera: public void reloadList()
     * Postcondiciones: El método recarga la lista de objetos.
     * */
    public void reloadList(){
        viewModel.loadObjectList();
        objectList = viewModel.get_objectList().getValue();//Obtenemos el listado de objetos
        adapter = new AdapterObjectList(this, R.layout.item_object_list, objectList);
        listView.setAdapter(adapter);
    }
}
