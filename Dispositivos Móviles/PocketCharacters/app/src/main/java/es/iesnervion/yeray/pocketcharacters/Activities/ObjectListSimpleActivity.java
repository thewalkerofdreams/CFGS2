package es.iesnervion.yeray.pocketcharacters.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterCharacterList;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterObjectListSimple;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterListActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.ObjectListSimpleActivityVM;

public class ObjectListSimpleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    AdapterObjectListSimple adapter;
    ListView listView;
    ObjectListSimpleActivityVM viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_list_simple);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.ListViewSimpleObjects);
        viewModel = ViewModelProviders.of(this).get(ObjectListSimpleActivityVM.class);//Instanciamos el ViewModel
        viewModel.loadList(getIntent().getStringExtra("GameMode"));//Cargamos la lista

        //ArrayList<ClsCharacter> items = new ArrayList<>(AppDataBase.getDataBase(getApplication()).characterDao().getAllCharacters());
        adapter = new AdapterObjectListSimple(this, R.layout.item_object_list_simple, viewModel.get_objectList());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throwNewObjectActivity();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsObject item = (ClsObject) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Delete");// Setting Alert Dialog Title
        alertDialogBuilder.setMessage("Do you really want delete this Object?");// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), "Object deleted", Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getApplication()).objectDao().deleteObject(item);
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
    * Nombre: throwObjectTypeListActivity
    * Comentario: Este método nos permite lanzar la actividad ObjectTypeListActivity.
    * Cabecera: public void throwObjectTypeListActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad ObjectTypeListActivity.
    * */
    public void throwObjectTypeListActivity(View v){
        startActivity(new Intent(this, ObjectTypeListActivity.class).putExtra("GameMode", getIntent().getStringExtra("GameMode")));
    }

    /*
    * Interfaz
    * Nombre: throwNewObjectActivity
    * Comentario: Este método nos mostrará un mensaje por pantalla si aún no existe como mínimo
    * un tipo de objecto en la base de datos, si existe alguno lanza la actividad NewObjectActivity.
    * Cabecera: public void throwNewObjectActivity()
    * Postcondiciones: Si existe un tipo de objeto en la base de datos, el método lanza la actividad
    * NewObjectActivity y en caso contrario muestra un mensaje de error por pantalla.
    * */
    public void throwNewObjectActivity(){
        if(new MethodsDDBB().existAnyObjectType(this)){
            Intent intent = new Intent(this, NewObjectActivity.class);
            intent.putExtra("GameMode", getIntent().getStringExtra("GameMode"));
            startActivityForResult(intent, 1);
        }else{
            Toast.makeText(getApplication(), getApplication().getString(R.string.no_exist_object_type), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            reloadList();
        }
    }

    /*
    * Interfaz
    * Nombre: reloadList
    * Comentario: Este método nos permite recargar la lista de objetos.
    * Cabecera: public void reloadList()
    * Postcondiciones: El método recarga la lista de objetos.
    * */
    public void reloadList(){
        viewModel.loadList(getIntent().getStringExtra("GameMode"));
        adapter = new AdapterObjectListSimple(this, R.layout.item_object_list_simple, viewModel.get_objectList());
        listView.setAdapter(adapter);
    }
}
