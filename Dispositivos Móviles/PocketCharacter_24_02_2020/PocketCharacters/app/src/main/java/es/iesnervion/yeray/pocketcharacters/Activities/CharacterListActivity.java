package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
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
        viewModel = ViewModelProviders.of(this).get(CharacterListActivityVM.class);//Instanciamos el ViewModel

        listView = findViewById(R.id.ListViewCharacters);
        adapter = new AdapterCharacterList(this, R.layout.item_character_list, viewModel.get_characterList());
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
        ClsCharacter item = (ClsCharacter) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        Intent intent = new Intent(this, CharacterDetailsActivity.class);
        intent.putExtra("Character", item);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsCharacter item = (ClsCharacter) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_character);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getBaseContext(), R.string.character_deleted, Toast.LENGTH_SHORT).show();
                viewModel.deleteCharacter(item);
                reloadList();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode >= 1 && requestCode <= 2){
            reloadList();
        }
    }

    /**
    * Interfaz
    * Nombre: throwNewCharacterActivity
    * Comentario: Este método intentará lanzar la actividad NewCharacterActivity para la creación de
    * un nuevo personaje.
    * Si aún no exite ningún GameMode la función mostrará un mensaje de error, y no se lanzará la actividad.
    * Cabecera: public void throwNewCharacterActivity()
    * Postcondiciones: El método lanzará la actividad NewCharacterActivity o mandará un mensaje de error si
    * aún no hay ningún GameMode almacenado en la base de datos.
    * */
    public void throwNewCharacterActivity(){
        if(new MethodsDDBB().existAnyGameMode(getApplication())){//Si existe más de un modo de juego
            startActivityForResult(new Intent(this, NewCharacterActivity.class).putExtra("GameMode", getIntent().getStringExtra("GameMode")), 2);
        }else{
            Toast.makeText(getApplication(), R.string.no_exist_gamemode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
    * Interfaz
    * Nombre: reloadList
    * Comentario: Este método nos permite recargar la lista de personajes.
    * Cabecera: public void reloadList()
    * Postcondiciones: El método recarga la lista de personajes.
    * */
    public void reloadList(){
        viewModel.loadList();
        adapter = new AdapterCharacterList(this, R.layout.item_character_list, viewModel.get_characterList());
        listView.setAdapter(adapter);
    }
}
