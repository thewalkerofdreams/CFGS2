package es.iesnervion.yeray.pocketcharacters.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.EditObjectActivityVM;

public class EditObjectActivity extends AppCompatActivity {

    TextView objectType;
    EditText objectName, objectDescription;
    EditObjectActivityVM viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_datas_details);
        viewModel = ViewModelProviders.of(this).get(EditObjectActivityVM.class);//Instanciamos el ViewModel
        viewModel.set_inObject(getIntent().getExtras().getParcelable("Object"));
        viewModel.set_outObject(getIntent().getExtras().getParcelable("Object"));

        objectType = findViewById(R.id.TextViewModObjectType);
        objectName = findViewById(R.id.EditTextModObjectName);
        objectDescription = findViewById(R.id.EditTextModObjectDescription);

        objectType.setText(viewModel.get_inObject().get_type());
        objectName.setText(viewModel.get_inObject().get_name());
        objectDescription.setText(viewModel.get_inObject().get_description());
    }

    /*
     * Interfaz
     * Nombre: updateObject
     * Comentario: Este método guardará los cambios un objeto en la base de datos si todos los datos introducidos
     * son válidos, en caso contrario el método muestra un mensaje de error por pantalla.
     * Cabecera: public void saveObject(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: Si los datos son válidos el método almacena el nuevo objeto en la base de datos y
     * finaliza al actividad actual.
     * */
    public void updateObject(View v){
        viewModel.get_outObject().set_name(objectName.getText().toString());
        viewModel.get_outObject().set_description(objectDescription.getText().toString());

        if(viewModel.get_outObject().get_name().length() > 0){
            if(viewModel.get_outObject().get_description().length() > 0){
                if(!viewModel.get_outObject().get_name().equals(viewModel.get_inObject().get_name()) &&
                        new MethodsDDBB().existObject(this, getIntent().getStringExtra("GameMode"), viewModel.get_outObject().get_name())){
                    Toast.makeText(getApplication(), getApplication().getString(R.string.already_exist_object), Toast.LENGTH_SHORT).show();
                }else{
                    AppDataBase.getDataBase(getApplication()).objectDao().updateObject(viewModel.get_outObject());
                    setResult(2);
                    finish();
                }
            }else{
                Toast.makeText(getApplication(), getApplication().getString(R.string.object_description_empty), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplication(), getApplication().getString(R.string.object_name_empty), Toast.LENGTH_SHORT).show();
        }
    }
}
