package es.iesnervion.yeray.pocketcharacters.Activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import es.iesnervion.yeray.pocketcharacters.R;

public class CharacterStatsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_stats_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /*
    * Interfaz
    * Nombre: reloadList
    * Comentario: Este método nos permite recargar la lista de stats.
    * Cabecera: public void reloadList()
    * Postcondiciones: El método recarga la lista de stats.
    * */
    public void reloadList(){
        //TODO Hacer...
    }
}
