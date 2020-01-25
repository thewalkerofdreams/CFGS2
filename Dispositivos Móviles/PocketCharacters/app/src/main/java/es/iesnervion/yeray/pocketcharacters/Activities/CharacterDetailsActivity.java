package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterCharacterStats;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterDetailsActivityVM;

public class CharacterDetailsActivity extends AppCompatActivity {

    AdapterCharacterStats adapter;
    ListView listView;
    EditText characterName, chapterName;
    CharacterDetailsActivityVM viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_datas);
        viewModel = ViewModelProviders.of(this).get(CharacterDetailsActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_inCharacter(getIntent().getExtras().getParcelable("Character"));//Obtenemos el personaje
        viewModel.set_outCharacter(getIntent().getExtras().getParcelable("Character"));
        listView = findViewById(R.id.ListViewStats);
        characterName = findViewById(R.id.CharacterName);
        characterName.setText(viewModel.get_inCharacter().get_characterName());
        chapterName = findViewById(R.id.PhaseName);
        chapterName.setText(viewModel.get_inCharacter().get_chapterName());

        adapter = new AdapterCharacterStats(this, R.layout.item_object_list_simple, viewModel.get_outCharacter().get_stats());
        listView.setAdapter(adapter);
    }

    /*
    * Interfaz
    * Nombre: updateNameAndChapter
    * Comentario: Este método nos permite actualizar el nombre y el capitulod el personaje en
    * la base de datos. Si se intenta cambiar el nombre por otro igual al de otro personaje
    * en el mismo GameMode, el método mostrará un error por pantalla.
    * Cabecera: public void updateNameAndChapter(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método modifica el nombre y el capitulo del personaje o se muestra un mensaje
    * de error.
    * */
    public void updateNameAndChapter(View v){
        viewModel.get_outCharacter().set_characterName(characterName.getText().toString());
        viewModel.get_outCharacter().set_chapterName(chapterName.getText().toString());

        if(viewModel.get_outCharacter().get_characterName().length() > 0 && viewModel.get_outCharacter().get_chapterName().length() > 0){
            if(viewModel.get_inCharacter().get_characterName().equals(viewModel.get_outCharacter().get_characterName())){
                Toast.makeText(getApplication(), "Already exist a character with this name!", Toast.LENGTH_SHORT).show();
            }else{
                AppDataBase.getDataBase(getApplication()).characterDao().updateCharacter(viewModel.get_outCharacter());
                Toast.makeText(getApplication(), "Character saved!", Toast.LENGTH_SHORT).show();
                viewModel.set_inCharacter(viewModel.get_outCharacter());
            }
        }else{
            Toast.makeText(getApplication(), "The character name and the chapter are required!", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * Interfaz
    * Nombre: throwCharacterStatsListActivity
    * Comentario: Este método nos permite lanzar la actividad CharacterStatsListActivity.
    * Cabecera: public void throwCharacterStatsListActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad CharacterStatsListActivity.
    * */
    public void throwCharacterStatsListActivity(View v){
        startActivityForResult(new Intent(this, CharacterStatsListActivity.class).putExtra("Character", viewModel.get_inCharacter()), 1);
    }
}
