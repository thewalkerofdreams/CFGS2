package es.iesnervion.yeray.pocketcharacters.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterGameModeList;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterObjectListSimple;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterObjectTypeList;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.ObjectListSimpleActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.ObjectTypeListActivityVM;

public class ObjectTypeListActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    AdapterObjectTypeList adapter;
    ListView listView;
    ObjectTypeListActivityVM viewModel;
    AlertDialog alertDialogCreateType, alertDialogDeleteType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_type_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(ObjectTypeListActivityVM.class);//Instanciamos el ViewModel

        listView = findViewById(R.id.ListViewObjectType);
        adapter = new AdapterObjectTypeList(this, R.layout.item_object_type_list, viewModel.get_typeList());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNewObjectType();
            }
        });

        if(savedInstanceState != null && viewModel.is_openDialogDeleteTypeObject()) {//Si el dialogo de eliminación estaba abierto lo recargamos
            showDialogDeleteTypeObject();
        }else{
            if(savedInstanceState != null && viewModel.is_openDialogCreateTypeObject()){//Si el dialogo de creación estaba abierto lo recargamos
                dialogNewObjectType();
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsObjectType item = (ClsObjectType) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        if(new MethodsDDBB().existAnyObjectByType(this, item.get_name())){
            Toast.makeText(getBaseContext(), R.string.you_cant_delete_this_object_type, Toast.LENGTH_SHORT).show();
        }else{
            viewModel.set_typeObjectToDelete(item);
            showDialogDeleteTypeObject();
            viewModel.set_openDialogDeleteTypeObject(true);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteType != null && alertDialogDeleteType.isShowing()) {//Si se encuentra abierto el dialogo de deleteGameMode
            alertDialogDeleteType.dismiss();// close dialog to prevent leaked window
            viewModel.set_openDialogDeleteTypeObject(true);
        }else{
            if(alertDialogCreateType != null && alertDialogCreateType.isShowing()){//Si se encuentra abierto el dialogo de createGameMode
                alertDialogCreateType.dismiss();
                viewModel.set_openDialogCreateTypeObject(true);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: dialogNewObjectType
     * Comentario: Este método muestra por pantalla un dialogo para crear un nuevo tipo de objeto,
     * si el usuario inderta todos los datos necesarios y son válidos, una vez pulse el botón de crear
     * se almacenará ese nuevo tipo en la base de datos de la aplicación.
     * Cabecera: public void dialogNewObjectType()
     * Postcondiciones: El método inserta un nuevo tipo de objeto en la base de datos o se cancela el dialogo.
     * */
    public void dialogNewObjectType(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ObjectTypeListActivity.this);
        //Declaramos un editText temporal
        final EditText nameEdit = new EditText(ObjectTypeListActivity.this);
        nameEdit.setHint(R.string.new_type);//Le insertamos una pista
        // Build the dialog box
        builder.setTitle(R.string.new_type)
                .setView(nameEdit)
                .setMessage(R.string.create_a_new_type)
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String typeName = nameEdit.getText().toString();
                        if (typeName.length() == 0) {
                            Toast.makeText(getApplication(), R.string.type_name_empty, Toast.LENGTH_SHORT).show();
                        } else {
                            if(new MethodsDDBB().existTypeObject(getApplication(), typeName)){//Si ya existe un tipo con ese nombre
                                Toast.makeText(getApplication(), R.string.already_exist_type_name, Toast.LENGTH_SHORT).show();
                            }else{
                                AppDataBase.getDataBase(getApplication()).objectTypeDao().insertObjectType(new ClsObjectType(typeName));
                                reloadList();
                                Toast.makeText(getApplication(), R.string.type_saved, Toast.LENGTH_SHORT).show();
                                viewModel.set_openDialogCreateTypeObject(false);
                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.set_openDialogCreateTypeObject(false);
                        dialog.cancel();
                    }
                });

        alertDialogCreateType = builder.create();
        alertDialogCreateType.show();//Lanzamos el dialogo
    }

    /**
     * Interfaz
     * Nombre: showDialogDeleteTypeObject
     * Comentario: Este método muestra por pantalla un dialogo para eliminar un tipo de objeto,
     * si el usuario confirma la acción, se elimina ese tipo de la base de datos.
     * Cabecera: public void showDialogDeleteTypeObject()
     * Postcondiciones: El método elimina un tipo de objeto de la base de datos o se cancela el dialogo.
     * */
    public void showDialogDeleteTypeObject(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_type);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.type_deleted, Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getApplication()).objectTypeDao().deleteObjectType(viewModel.get_typeObjectToDelete());
                reloadList();
                viewModel.set_openDialogDeleteTypeObject(false);
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_openDialogDeleteTypeObject(false);
            }
        });

        alertDialogDeleteType = alertDialogBuilder.create();
        alertDialogDeleteType.show();
    }

    /**
     * Interfaz
     * Nombre: reloadList
     * Comentario: Este método nos permite recargar la lista de tipos de objeto.
     * Cabecera: public void reloadList()
     * Postcondiciones: El método recarga la lista.
     * */
    public void reloadList(){
        viewModel.loadList();
        adapter = new AdapterObjectTypeList(this, R.layout.item_object_type_list, viewModel.get_typeList());
        listView.setAdapter(adapter);
    }
}
