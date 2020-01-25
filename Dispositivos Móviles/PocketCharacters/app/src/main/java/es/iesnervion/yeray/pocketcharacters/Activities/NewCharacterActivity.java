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
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewCharacterActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.NewObjectActivityVM;

public class NewCharacterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> items;
    NewCharacterActivityVM viewModel;
    Spinner spinner;
    EditText characterName, chapterName, story;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_character);

        viewModel = ViewModelProviders.of(this).get(NewCharacterActivityVM.class);//Instanciamos el ViewModel

        characterName = findViewById(R.id.EditTextCharacterNameCreate);
        chapterName = findViewById(R.id.EditTextChapterCreate);
        story = findViewById(R.id.EditTextStoryCreate);
        spinner = findViewById(R.id.SpinnerGameModes);
        spinner.setOnItemSelectedListener(this);

        items = new ArrayList<>();
        ArrayList<ClsGameMode> gameModes = viewModel.get_gameModes();
        for(int i = 0; i < gameModes.size(); i++){
            items.add(gameModes.get(i).get_name());
        }

        viewModel.set_gameMode(items.get(0));//Asignamos el tipo de objeto por defecto al nuevo objeto

        //Creamos un adaptador ArrayAdapter
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        //Especificamos el layout que aparecerá al desplegarse la lista
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Agregamos el adaptador al tipo spinner
        spinner.setAdapter(aa);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        viewModel.set_gameMode(items.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /*
     * Interfaz
     * Nombre: saveCharacter
     * Comentario: Este método guardará un personaje en la base de datos si todos los datos introducidos
     * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
     * Cabecera: public void saveCharacter(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: Si los datos son válidos el método almacena el nuevo personaje en la base de datos y
     * finaliza al actividad actual.
     * */
    public void saveCharacter(View v){
        if(characterName.getText().length() > 0 && chapterName.getText().length() > 0){
                if(new MethodsDDBB().existCharacter(this, viewModel.get_gameMode(), chapterName.getText().toString())){
                    Toast.makeText(getApplication(), "Already exist a character with that name in this GameMode!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication(), "Character saved!", Toast.LENGTH_SHORT).show();
                    AppDataBase.getDataBase(getApplication()).characterDao().insertCharacter(new ClsCharacter(viewModel.get_gameMode(), characterName.getText().toString(),
                            chapterName.getText().toString(), story.getText().toString()));

                    setResult(2);
                    finish();
                }
        }else{
            Toast.makeText(getApplication(), "The character name and the chapter are required!", Toast.LENGTH_SHORT).show();
        }
    }
}
