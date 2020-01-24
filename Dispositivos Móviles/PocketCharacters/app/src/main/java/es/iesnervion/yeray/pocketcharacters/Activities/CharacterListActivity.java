package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.renderscript.Sampler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.Lists.AdapterCharacterList;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterListActivityVM;

public class CharacterListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    AdapterCharacterList adapter;
    ListView listView;
    CharacterListActivityVM viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.ListViewCharacters);
        viewModel = ViewModelProviders.of(this).get(CharacterListActivityVM.class);//Instanciamos el ViewModel

        ArrayList<ClsCharacter> items = new ArrayList<>(AppDataBase.getDataBase(getApplication()).characterDao().getAllCharacters());
        adapter = new AdapterCharacterList(this, R.layout.item_character_list, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);//Declaramos el botón flotante
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throwNewCharacterActivity();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /*
    * Interfaz
    * Nombre: throwNewCharacterActivity
    * Comentario: Este método intentará lanzar la actividad NewCharacterActivity para la creación de
    * un nuevo personaje.
    * -Si aún no exite ningún GameMode la función mostrará un mensaje de error, y no se lanzará la actividad.
    * Cabecera: public void throwNewCharacterActivity()
    * Postcondiciones: El método lanzará la actividad NewCharacterActivity o mandará un mensaje de error si
    * aún no hay ningún GameMode almacenado en la base de datos.
    * */
    public void throwNewCharacterActivity(){
        //final ArrayList<ClsGameMode> gameModes;
        ExecutorService myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            final ArrayList<ClsGameMode> gameModes = new ArrayList<ClsGameMode>(AppDataBase.getDataBase(this).gameModeDao().getAllGameModes());

            if(gameModes.size() > 0){//Si existe más de un modo de juego
                startActivity(new Intent(this, NewCharacterActivity.class));
            }else{
                Toast.makeText(this, "@string/no_exist_gamemode", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
