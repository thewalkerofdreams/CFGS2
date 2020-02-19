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
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewCharacterStatActivityVM;

public class NewCharacterStatActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> items;
    NewCharacterStatActivityVM viewModel;
    Spinner spinner;
    EditText value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_character_stat);
        viewModel = ViewModelProviders.of(this).get(NewCharacterStatActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_actualCharacter((ClsCharacter)getIntent().getSerializableExtra("Character"));
        viewModel.loadList();

        value = findViewById(R.id.EditTextValueNewStat);
        spinner = findViewById(R.id.SpinnerStats);
        spinner.setOnItemSelectedListener(this);

        items = new ArrayList<>();
        ArrayList<ClsStat> stats = viewModel.get_stats();
        for(int i = 0; i < stats.size(); i++){
            items.add(stats.get(i).get_name());
        }

        if(items.size() > 0)
            viewModel.set_statName(items.get(0));//Asignamos el primer stat por defecto al personaje

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        viewModel.set_statName(items.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Interfaz
     * Nombre: saveStat
     * Comentario: Este método almacenará un stat con su valor en la base de datos si todos los datos introducidos
     * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
     * Cabecera: public void saveStat(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: Si los datos son válidos el método almacena el nuevo stat con su valor en la base de datos y
     * finaliza al actividad actual.
     * */
    public void saveStat(View v){
        if(value.getText().length() > 0){
            ClsStat clsStat = AppDataBase.getDataBase(this).statDao().getStatByGameModeAndName(viewModel.get_actualCharacter().get_gameMode(), viewModel.get_statName());
            if(new MethodsDDBB().existStatWithValueByCharacter(this, viewModel.get_actualCharacter()
                    , clsStat)){
                Toast.makeText(getApplication(), R.string.already_exist_stat_character, Toast.LENGTH_SHORT).show();
            }else{
                ClsCharacterAndStat characterAndStat = new ClsCharacterAndStat(viewModel.get_actualCharacter().get_id(),
                        clsStat.get_id(), value.getText().toString());
                AppDataBase.getDataBase(getApplication()).characterAndStatDao().insertCharacterAndStat(characterAndStat);
                setResult(1);
                finish();
            }
        }else{
            Toast.makeText(getApplication(), R.string.value_empty, Toast.LENGTH_SHORT).show();
        }
    }
}
