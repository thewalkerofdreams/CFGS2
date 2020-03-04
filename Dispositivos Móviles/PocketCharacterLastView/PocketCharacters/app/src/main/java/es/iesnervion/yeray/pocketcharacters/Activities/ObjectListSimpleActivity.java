package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.Adapters.AdapterObjectListSimple;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.ObjectListSimpleActivityVM;

public class ObjectListSimpleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    AdapterObjectListSimple adapter;
    ListView listView;
    ObjectListSimpleActivityVM viewModel;
    AlertDialog alertDialogDeleteObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_list_simple);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(ObjectListSimpleActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_actualGameMode(getIntent().getStringExtra("GameMode"));//Con esto también cargamos la lista

        listView = findViewById(R.id.ListViewSimpleObjects);
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

        if(savedInstanceState != null && viewModel.is_openDialogDeleteObject()) {//Si se encuentra abierto el dialogo de deleteObject
            showDialogToDeleteObject();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClsObject item = (ClsObject) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        Intent intent = new Intent(this, EditObjectActivity.class);
        intent.putExtra("GameMode", viewModel.get_actualGameMode());
        intent.putExtra("Object", item);
        startActivityForResult(intent, 2);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsObject item = (ClsObject) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        if(new MethodsDDBB().objectEquipToACharacter(this, item.get_id())){
            Toast.makeText(getBaseContext(), R.string.object_equiped, Toast.LENGTH_SHORT).show();
        }else{
            viewModel.set_objectToDelete(item);
            showDialogToDeleteObject();
            viewModel.set_openDialogDeleteObject(true);
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteObject != null && alertDialogDeleteObject.isShowing()) {//Si se encuentra abierto el dialogo de deleteGameMode
            alertDialogDeleteObject.dismiss();// close dialog to prevent leaked window
            viewModel.set_openDialogDeleteObject(true);
        }
    }

    /**
    * Interfaz
    * Nombre: showDialogToDeleteObject
    * Comentario: Este método muestra un dialogo por pantalla para eliminar un objeto, si el usuario confirma
    * la acción, se eliminará ese objeto de la base de datos.
    * Cabecera: public void showDialogToDeleteObject()
    * Postcondiciones: El método elimina un objeto de la base de datos o no hace nada si el usuario cancela
    * el dialogo.
    * */
    public void showDialogToDeleteObject(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_object);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.object_deleted, Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getApplication()).objectDao().deleteObject(viewModel.get_objectToDelete());
                reloadList();
                viewModel.set_openDialogDeleteObject(false);
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_openDialogDeleteObject(false);
            }
        });

        alertDialogDeleteObject = alertDialogBuilder.create();
        alertDialogDeleteObject.show();
    }

    /**
    * Interfaz
    * Nombre: throwObjectTypeListActivity
    * Comentario: Este método nos permite lanzar la actividad ObjectTypeListActivity.
    * Cabecera: public void throwObjectTypeListActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad ObjectTypeListActivity.
    * */
    public void throwObjectTypeListActivity(View v){
        startActivity(new Intent(this, ObjectTypeListActivity.class).putExtra("GameMode", viewModel.get_actualGameMode()));
    }

    /**
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
            intent.putExtra("GameMode", viewModel.get_actualGameMode());
            startActivityForResult(intent, 1);
        }else{
            Toast.makeText(getApplication(), getApplication().getString(R.string.no_exist_object_type), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode >= 1 && requestCode <= 2 ) {
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
        viewModel.loadList();
        adapter = new AdapterObjectListSimple(this, R.layout.item_object_list_simple, viewModel.get_objectList());
        listView.setAdapter(adapter);
    }
}
