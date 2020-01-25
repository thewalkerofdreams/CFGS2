package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.Intent;
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

        objectName = findViewById(R.id.EditTextObjectName);
        objectDescription = findViewById(R.id.EditTextObjectDescription);
        spinner = findViewById(R.id.SpinnerObjectTypeCreate);
        spinner.setOnItemSelectedListener(this);

        items = new ArrayList<>();
        ArrayList<ClsObjectType> types = viewModel.get_objectTypes();
        for(int i = 0; i < types.size(); i++){
            items.add(types.get(i).get_name());
        }

        viewModel.set_objectType(items.get(0));//Asignamos el tipo de objeto por defecto al nuevo objeto

        //Creamos un adaptador ArrayAdapter
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        //Especificamos el layout que aparecerá al desplegarse la lista
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Agregamos el adaptador al tipo spinner
        spinner.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        viewModel.set_objectType(items.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /*
    * Interfaz
    * Nombre: saveObject
    * Comentario: Este método guardará un objeto en la base de datos si todos los datos introducidos
    * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
    * Cabecera: public void saveObject(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: Si los datos son válidos el método almacena el nuevo objeto en la base de datos y
    * finaliza al actividad actual.
    * */
    public void saveObject(View v){
        if(objectName.getText().length() > 0){
            if(objectDescription.getText().length() > 0){
                if(new MethodsDDBB().existObject(this, getIntent().getStringExtra("GameMode"), objectName.getText().toString())){
                    Toast.makeText(getApplication(), getApplication().getString(R.string.already_exist_object), Toast.LENGTH_SHORT).show();
                }else{
                    AppDataBase.getDataBase(getApplication()).objectDao().insertObject(new ClsObject(viewModel.get_objectType(), objectName.getText().toString(),
                            objectDescription.getText().toString(), getIntent().getStringExtra("GameMode")));
                    //Intent intent=new Intent();
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
