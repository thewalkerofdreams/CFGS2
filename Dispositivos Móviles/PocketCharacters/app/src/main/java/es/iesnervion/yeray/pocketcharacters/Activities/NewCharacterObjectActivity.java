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
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsObjectAndQuantity;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewCharacterObjectActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewCharacterStatActivityVM;

public class NewCharacterObjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> items;
    NewCharacterObjectActivityVM viewModel;
    Spinner spinner;
    EditText quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_character_object);

        quantity = findViewById(R.id.EditTextQuantityNewObject);
        spinner = findViewById(R.id.SpinnerObjects);

        viewModel = ViewModelProviders.of(this).get(NewCharacterObjectActivityVM.class);//Instanciamos el ViewModel
        viewModel.loadList(((ClsCharacter)getIntent().getSerializableExtra("Character")).get_gameMode());

        spinner.setOnItemSelectedListener(this);

        items = new ArrayList<>();
        ArrayList<ClsObject> objects = viewModel.get_objects();
        for(int i = 0; i < objects.size(); i++){
            items.add(objects.get(i).get_name());
        }

        viewModel.set_objectName(items.get(0));//Asignamos el tipo de objeto por defecto al nuevo objeto

        //Creamos un adaptador ArrayAdapter
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        //Especificamos el layout que aparecerá al desplegarse la lista
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Agregamos el adaptador al tipo spinner
        spinner.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        viewModel.set_objectName(items.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /*
     * Interfaz
     * Nombre: saveObject
     * Comentario: Este método guardará un objeto con su cantidad en la base de datos si todos los datos introducidos
     * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
     * Cabecera: public void saveObject(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: Si los datos son válidos el método almacena el nuevo objeto con su cantidad en la base de datos y
     * finaliza al actividad actual.
     * */
    public void saveObject(View v){
        if(quantity.getText().length() > 0){
            //Obtenemos el stat (su id)
            ClsObject clsObject = AppDataBase.getDataBase(this).objectDao().getObjectByGameModeAndName(((ClsCharacter)getIntent().getSerializableExtra("Character")).get_gameMode(), viewModel.get_objectName());
            if(new MethodsDDBB().existObjectWithCharacterAndObject(this, (ClsCharacter)getIntent().getSerializableExtra("Character")
                    , clsObject)){
                Toast.makeText(getApplication(), "Already the character have this Object!", Toast.LENGTH_SHORT).show();
            }else{
                ClsObjectAndCharacter objectAndCharacter = new ClsObjectAndCharacter(((ClsCharacter)getIntent().getSerializableExtra("Character")).get_id(),
                        clsObject.get_id(), Integer.valueOf(quantity.getText().toString()));
                AppDataBase.getDataBase(getApplication()).objectAndCharacterDao().insertObjectAndCharacter(objectAndCharacter);
                //Intent intent=new Intent();
                setResult(1);
                finish();
            }
        }else{
            Toast.makeText(getApplication(), "La cantidad debe ser mayor o igual a 0!", Toast.LENGTH_SHORT).show();
        }
    }
}

