package com.example.listado_empleados.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.listado_empleados.Adapters.AdapterEmployeeList;
import com.example.listado_empleados.DDBB.DDBBMethods;
import com.example.listado_empleados.DDBB_Entities.ClsDepartamento;
import com.example.listado_empleados.Fragments.AddDepartamentFragment;
import com.example.listado_empleados.Fragments.AddPersonFragment;
import com.example.listado_empleados.Fragments.EditPersonFragment;
import com.example.listado_empleados.R;
import com.example.listado_empleados.Tuple.ClsPersonaConDepartamentoTuple;
import com.example.listado_empleados.ViewModels.MainActivityVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.text.Layout;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    ListView listView;
    AdapterEmployeeList adapter;
    MainActivityVM viewModel;
    AddDepartamentFragment addDepartamentFragment = null;
    AddPersonFragment addPersonFragment = null;
    EditPersonFragment editPersonFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(MainActivityVM.class);//Instanciamos el ViewModel

        listView = findViewById(R.id.ListViewMainActivity);
        reloadList();

        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new DDBBMethods().existenDepartamentos(getApplication())){//Si ya existe algún departamento
                    addPersonFragment = new AddPersonFragment();
                    replaceFragment(addPersonFragment);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClsPersonaConDepartamentoTuple item = (ClsPersonaConDepartamentoTuple) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        viewModel.set_selectedEmployee(item);
        editPersonFragment = new EditPersonFragment();
        replaceFragment(editPersonFragment);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsPersonaConDepartamentoTuple item = (ClsPersonaConDepartamentoTuple) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_employee);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.employee_deleted, Toast.LENGTH_SHORT).show();
                viewModel.deleteEmployee(item, getApplication());
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
        return true;//De esta forma evitamos que se lance el evento onClickItem después de este
    }

    /**
     * Interfaz
     * Nombre: replaceFragment
     * Comentario: Este método nos permite crear un fragmento y remplazar el contenido de nuestro
     * FrameLayout por ese mismo fragmento.
     * Cabecera: public void replaceFragment()
     * Postcondiciones: El método reemplaza el contenido del FrameLayout por el nuevo fragmento.
     * */
    public void replaceFragment(Fragment fragment){
        removeList();//Limpiamos la lista para dejar espacio a los fragments
        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
        transation.replace(R.id.FrameLayoutMainActivity, fragment);
        transation.commit();
    }

    /**
     * Interfaz
     * Nombre: removeYourFragment
     * Comentario: Este método nos permite eliminar el fragmento de la actividad actual.
     * Cabecera: public void removeYourFragment()
     * Postcondiciones: El método elimina el fragmento de la actividad actual.
     * */
    public void removeYourFragments(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addDepartamentFragment != null) {
            transaction.remove(addDepartamentFragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            addDepartamentFragment = null;
        }

        if (addPersonFragment != null) {
            transaction.remove(addPersonFragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            addPersonFragment = null;
        }

        if(editPersonFragment != null){
            transaction.remove(editPersonFragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            editPersonFragment = null;
        }

        reloadList();//Recargamos la lista de empleados
    }

    /*
     * Interfaz
     * Nombre: addDepartament
     * Comentario: Este método nos permite abrir el formulario para insertar un departamento a la base de datos.
     * Cabecera: public void addDepartament(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método abre un formulario para insertar un nuevo departamento.
     */
    public void addDepartament(View v){
        removeYourFragments();//Removemos los posibles fragmentos
        addDepartamentFragment = new AddDepartamentFragment();
        replaceFragment(addDepartamentFragment);
    }

    /*
     * Interfaz
     * Nombre: reloadList
     * Comentario: Este método nos permite recargar la lista de empleados.
     * Cabecera: public void reloadList()
     * Postcondiciones: El método recarga la lista.
     */
    public void reloadList(){
        viewModel.loadEmployeeList();//Volvemos a cargar los datos de la base de datos en el viewModel
        if(viewModel.get_employeeList().getValue() != null){
            adapter = new AdapterEmployeeList(this, R.layout.item_person_list, viewModel.get_employeeList().getValue());
        }else{
            adapter = new AdapterEmployeeList(this, R.layout.item_person_list, new ArrayList<ClsPersonaConDepartamentoTuple>());
        }
        listView.setAdapter(adapter);
    }

    /*
     * Interfaz
     * Nombre: removeList
     * Comentario: Este método nos permite limpiar la lista de empleados. Lo utilizaremos
     * daca vez que llamemos a algún fragmento en la actividad MainActivity.
     * Cabecera: public void removeList()
     * Postcondiciones: El método limpia la lista de empleados.
     */
    public void removeList(){
        adapter = new AdapterEmployeeList(this, R.layout.item_person_list, new ArrayList<ClsPersonaConDepartamentoTuple>());
        listView.setAdapter(adapter);
    }
}
