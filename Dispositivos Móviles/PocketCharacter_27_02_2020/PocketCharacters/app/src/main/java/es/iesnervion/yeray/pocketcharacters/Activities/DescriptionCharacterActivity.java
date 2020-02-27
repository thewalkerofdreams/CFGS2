package es.iesnervion.yeray.pocketcharacters.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterDetailsActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.DescriptionCharacterActivityVM;

public class DescriptionCharacterActivity extends AppCompatActivity {

    DescriptionCharacterActivityVM viewModel;
    EditText editDescription;
    Button btnSaveDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_character);
        viewModel = ViewModelProviders.of(this).get(DescriptionCharacterActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_actualCharacter((ClsCharacter) getIntent().getExtras().getSerializable("Character"));//Obtenemos el personaje

        editDescription = findViewById(R.id.EditTextDescriptionCharacter);
        editDescription.setText(viewModel.get_actualCharacter().get_character().get_story());

        btnSaveDescription = findViewById(R.id.btnSaveDescriptionCharacter);
        btnSaveDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.get_actualCharacter().get_character().set_story(editDescription.getText().toString());
                if(!viewModel.get_actualCharacter().get_character().get_story().isEmpty()){
                    AppDataBase.getDataBase(getApplication()).characterDao().updateCharacter(viewModel.get_actualCharacter().get_character());
                    Toast.makeText(getApplication(), R.string.description_saved, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra("Description", viewModel.get_actualCharacter().get_character().get_story());
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(getApplication(), R.string.description_required, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
