package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
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
        viewModel.set_inCharacter((ClsCharacter) getIntent().getExtras().getSerializable("Character"));//Obtenemos el personaje
        viewModel.set_outCharacter((ClsCharacter) getIntent().getExtras().getSerializable("Character"));

        characterName = findViewById(R.id.CharacterName);
        characterName.setText(viewModel.get_inCharacter().get_character().get_characterName());
        chapterName = findViewById(R.id.PhaseName);
        chapterName.setText(viewModel.get_inCharacter().get_character().get_chapterName());

        listView = findViewById(R.id.ListViewStats);
        reloadList();//Cargamos la lista de stats del personaje

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//Deshabilitamos el foco por defecto
    }

    /**
    * Interfaz
    * Nombre: updateNameAndChapter
    * Comentario: Este método nos permite actualizar el nombre y el capitulo del personaje en
    * la base de datos. Si se intenta cambiar el nombre por otro igual al de otro personaje
    * en el mismo GameMode, el método mostrará un error por pantalla. Lo mismo ocurre si se
    * deja alguno de los dos valores en blanco.
    * Cabecera: public void updateNameAndChapter(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método modifica el nombre y el capitulo del personaje o se muestra un mensaje
    * de error.
    * */
    public void updateNameAndChapter(View v){
        viewModel.get_outCharacter().get_character().set_characterName(characterName.getText().toString());
        viewModel.get_outCharacter().get_character().set_chapterName(chapterName.getText().toString());

        if(viewModel.get_outCharacter().get_character().get_characterName().length() > 0 && viewModel.get_outCharacter().get_character().get_chapterName().length() > 0){
            if(!viewModel.get_inCharacter().get_character().get_characterName().equals(viewModel.get_outCharacter().get_character().get_characterName()) ||
                    !viewModel.get_inCharacter().get_character().get_chapterName().equals(viewModel.get_outCharacter().get_character().get_chapterName())){
                AppDataBase.getDataBase(getApplication()).characterDao().updateCharacter(viewModel.get_outCharacter().get_character());
                Toast.makeText(getApplication(), R.string.character_saved, Toast.LENGTH_SHORT).show();
                viewModel.set_inCharacter(viewModel.get_outCharacter().get_character());
            }else{
                Toast.makeText(getApplication(), R.string.already_exist_character_name, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplication(), R.string.character_required_values, Toast.LENGTH_SHORT).show();
        }
    }

    /**
    * Interfaz
    * Nombre: throwCharacterStatsListActivity
    * Comentario: Este método nos permite lanzar la actividad CharacterStatsListActivity.
    * Cabecera: public void throwCharacterStatsListActivity(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método lanza la actividad CharacterStatsListActivity.
    * */
    public void throwCharacterStatsListActivity(View v){
        Intent intent = new Intent(this, CharacterStatsListActivity.class);
        intent.putExtra("Character01", viewModel.get_inCharacter().get_character());
        startActivity(intent);
    }

    /**
     * Interfaz
     * Nombre: throwCharacterObjectListActivity
     * Comentario: Este método nos permite lanzar la actividad CharacterObjectListActivity.
     * Cabecera: public void throwCharacterObjectListActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad CharacterObjectListActivity.
     * */
    public void throwCharacterObjectListActivity(View v){
        Intent intent = new Intent(this, CharacterObjectListActivity.class);
        intent.putExtra("Character01", viewModel.get_inCharacter().get_character());
        startActivity(intent);
    }

    /**
     * Interfaz
     * Nombre: throwDescriptionCharacterActivity
     * Comentario: Este método nos permite lanzar la actividad DescriptionCharacterActivity.
     * Cabecera: public void throwDescriptionCharacterActivity(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método lanza la actividad DescriptionCharacterActivity.
     * */
    public void throwDescriptionCharacterActivity(View v){
        Intent intent = new Intent(this, DescriptionCharacterActivity.class);
        intent.putExtra("Character", viewModel.get_inCharacter().get_character());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            viewModel.get_inCharacter().get_character().set_story(data.getStringExtra("Description"));
            viewModel.get_outCharacter().get_character().set_story(data.getStringExtra("Description"));
        }
    }

    /**
    * Interfaz
    * Nombre: reloadList
    * Comentario: Este método nos permite recargar la lista de stats del personaje.
    * Cabecera: public void reloadList()
    * Postcondiciones: El método recarga la lista de stats del personaje.
    * */
    public void reloadList(){
        viewModel.get_inCharacter().loadStats();
        adapter = new AdapterCharacterStats(this, R.layout.item_character_stats, viewModel.get_inCharacter().get_stats());
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }
}
