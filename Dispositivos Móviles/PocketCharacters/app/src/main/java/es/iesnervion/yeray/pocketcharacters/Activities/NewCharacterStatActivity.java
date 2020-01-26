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
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewCharacterStatActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewObjectActivityVM;

public class NewCharacterStatActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> items;
    NewCharacterStatActivityVM viewModel;
    Spinner spinner;
    EditText value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_character_stat);

        value = findViewById(R.id.EditTextValueNewStat);
        spinner = findViewById(R.id.SpinnerStats);

        viewModel = ViewModelProviders.of(this).get(NewCharacterStatActivityVM.class);//Instanciamos el ViewModel
        viewModel.loadList(((ClsCharacter)getIntent().getSerializableExtra("Character")).get_gameMode());

        value = findViewById(R.id.EditTextValueNewStat);
        spinner.setOnItemSelectedListener(this);

        items = new ArrayList<>();
        ArrayList<ClsStat> stats = viewModel.get_stats();
        for(int i = 0; i < stats.size(); i++){
            items.add(stats.get(i).get_name());
        }

        viewModel.set_statName(items.get(0));//Asignamos el tipo de objeto por defecto al nuevo objeto

        //Creamos un adaptador ArrayAdapter
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        //Especificamos el layout que aparecerá al desplegarse la lista
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Agregamos el adaptador al tipo spinner
        spinner.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        viewModel.set_statName(items.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /*
     * Interfaz
     * Nombre: saveStat
     * Comentario: Este método guardará un stat con su valor en la base de datos si todos los datos introducidos
     * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
     * Cabecera: public void saveStat(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: Si los datos son válidos el método almacena el nuevo stat con su valor en la base de datos y
     * finaliza al actividad actual.
     * */
    public void saveStat(View v){
        if(value.getText().length() > 0){
            //Obtenemos el stat (su id)
            ClsStat clsStat = AppDataBase.getDataBase(this).statDao().getStatByGameModeAndName(((ClsCharacter)getIntent().getSerializableExtra("Character")).get_gameMode(), viewModel.get_statName());
            if(new MethodsDDBB().existStatWithValueByCharacter(this, (ClsCharacter)getIntent().getSerializableExtra("Character")
                    , clsStat)){
                Toast.makeText(getApplication(), "Already the character have this Stat!", Toast.LENGTH_SHORT).show();
            }else{
                ClsCharacterAndStat characterAndStat = new ClsCharacterAndStat(((ClsCharacter)getIntent().getSerializableExtra("Character")).get_id(),
                        clsStat.get_id(), value.getText().toString());
                AppDataBase.getDataBase(getApplication()).characterAndStatDao().insertCharacterAndStat(characterAndStat);
                //Intent intent=new Intent();
                setResult(1);
                finish();
            }
        }else{
            Toast.makeText(getApplication(), "The value is empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
