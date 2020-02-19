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
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewCharacterObjectActivityVM;

public class NewCharacterObjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> items;
    NewCharacterObjectActivityVM viewModel;
    Spinner spinner;
    EditText quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_character_object);
        viewModel = ViewModelProviders.of(this).get(NewCharacterObjectActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_actualCharacter((ClsCharacter)getIntent().getSerializableExtra("Character"));
        viewModel.loadList();

        quantity = findViewById(R.id.EditTextQuantityNewObject);
        spinner = findViewById(R.id.SpinnerObjects);
        spinner.setOnItemSelectedListener(this);

        items = new ArrayList<>();
        ArrayList<ClsObject> objects = viewModel.get_objects();
        for(int i = 0; i < objects.size(); i++){
            items.add(objects.get(i).get_name());
        }

        if(items.size() > 0)
            viewModel.set_objectName(items.get(0));//Asignamos el tipo de objeto por defecto al nuevo objeto

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        viewModel.set_objectName(items.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Interfaz
     * Nombre: saveObject
     * Comentario: Este método almacenaŕa un objeto con su cantidad en la base de datos si todos los datos introducidos
     * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
     * Cabecera: public void saveObject(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: Si los datos son válidos el método almacena el nuevo objeto con su cantidad en la base de datos
     * para un personaje y finaliza al actividad actual.
     * */
    public void saveObject(View v){
        if(quantity.getText().length() > 0){
            //Obtenemos el objeto (su id)
            ClsObject clsObject = AppDataBase.getDataBase(this).objectDao().getObjectByGameModeAndName(viewModel.get_actualCharacter().get_gameMode(), viewModel.get_objectName());
            if(new MethodsDDBB().existObjectWithCharacterAndObject(this, viewModel.get_actualCharacter()
                    , clsObject)){
                Toast.makeText(getApplication(), R.string.already_exist_character_object, Toast.LENGTH_SHORT).show();
            }else{
                ClsObjectAndCharacter objectAndCharacter = new ClsObjectAndCharacter(viewModel.get_actualCharacter().get_id(),
                        clsObject.get_id(), Integer.valueOf(quantity.getText().toString()));
                AppDataBase.getDataBase(getApplication()).objectAndCharacterDao().insertObjectAndCharacter(objectAndCharacter);
                setResult(1);
                finish();
            }
        }else{
            Toast.makeText(getApplication(), R.string.quantity_greater_than_0, Toast.LENGTH_SHORT).show();
        }
    }
}

