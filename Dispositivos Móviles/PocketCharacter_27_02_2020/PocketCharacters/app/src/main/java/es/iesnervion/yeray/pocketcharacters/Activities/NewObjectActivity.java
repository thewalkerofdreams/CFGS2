package es.iesnervion.yeray.pocketcharacters.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewObjectActivityVM;

public class NewObjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> items;
    NewObjectActivityVM viewModel;
    Spinner spinner;
    EditText objectName, objectDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_datas_create);
        viewModel = ViewModelProviders.of(this).get(NewObjectActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_actualGameMode(getIntent().getStringExtra("GameMode"));

        objectName = findViewById(R.id.EditTextObjectName);
        objectDescription = findViewById(R.id.EditTextObjectDescription);
        spinner = findViewById(R.id.SpinnerObjectTypeCreate);
        spinner.setOnItemSelectedListener(this);

        items = new ArrayList<>();
        ArrayList<ClsObjectType> types = viewModel.get_objectTypes();
        for(int i = 0; i < types.size(); i++){//Cargamos los elementos a mostrar por el spinner
            items.add(types.get(i).get_name());
        }

        if(items.size() > 0)
            viewModel.set_objectType(items.get(0));//Asignamos el tipo de objeto por defecto al nuevo objeto

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        viewModel.set_objectType(items.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
    * Interfaz
    * Nombre: saveObject
    * Comentario: Este método almacenará un objeto en la base de datos si todos los datos introducidos
    * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
    * Cabecera: public void saveObject(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: Si los datos son válidos el método almacena el nuevo objeto en la base de datos y
    * finaliza al actividad actual.
    * */
    public void saveObject(View v){
        viewModel.set_objectName(objectName.getText().toString());
        viewModel.set_objectDescription(objectDescription.getText().toString());

        if(viewModel.get_objectName().length() > 0){
            if(viewModel.get_objectDescription().length() > 0){
                if(new MethodsDDBB().existObject(this, viewModel.get_actualGameMode(), viewModel.get_objectName())){
                    Toast.makeText(getApplication(), getApplication().getString(R.string.already_exist_object), Toast.LENGTH_SHORT).show();
                }else{
                    AppDataBase.getDataBase(getApplication()).objectDao().insertObject(new ClsObject(viewModel.get_objectType(), viewModel.get_objectName(),
                            viewModel.get_objectDescription(), viewModel.get_actualGameMode()));
                    setResult(1);
                    finish();
                }
            }else{
                Toast.makeText(getApplication(), getApplication().getString(R.string.object_description_empty), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplication(), getApplication().getString(R.string.object_name_empty), Toast.LENGTH_SHORT).show();
        }
    }
}
