package com.example.adventuremaps.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.EditLocalizationPointActivityVM;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditLocalizationPointActivity extends AppCompatActivity {

    private Button btnEdit;
    private EditText name, description;
    private CheckBox water, food, restArea, hunting, culture, hotel, naturalSite, fishing, vivac, camping;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private EditLocalizationPointActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_localization_point);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(EditLocalizationPointActivityVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra(ApplicationConstants.INTENT_ACTUAL_USER_EMAIL));
        viewModel.set_actualLocalizationPoint((ClsLocalizationPoint) getIntent().getSerializableExtra(ApplicationConstants.INTENT_ACTUAL_LOCALIZATION));
        viewModel.set_localizationTypes(getIntent().getStringArrayListExtra(ApplicationConstants.DATA_LOCALIZATION_TYPES));
        viewModel.set_localizationsIdActualUser(getIntent().getStringArrayListExtra(ApplicationConstants.DATA_LOCALIZATIONS_ID_ACTUAL_USER));

        //Instanciamos los elementos de la UI
        name = findViewById(R.id.EditTextLocalizationName);
        name.setText(viewModel.get_actualLocalizationPoint().getName());

        description = findViewById(R.id.EditTextLocalizationDescription);
        description.setText(viewModel.get_actualLocalizationPoint().getDescription());

        btnEdit = findViewById(R.id.btnSaveLocalizationPoint);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySaveLocalizationPoint();
            }
        });

        water = findViewById(R.id.checkboxWater);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_potable_water)))
            water.setChecked(true);
        checkBoxes.add(water);

        food = findViewById(R.id.checkboxFood);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_food)))
            food.setChecked(true);
        checkBoxes.add(food);

        restArea = findViewById(R.id.checkboxRestArea);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_rest_area)))
            restArea.setChecked(true);
        checkBoxes.add(restArea);

        hunting = findViewById(R.id.checkboxHunting);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_hunting)))
            hunting.setChecked(true);
        checkBoxes.add(hunting);

        culture = findViewById(R.id.checkboxCulture);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_culture)))
            culture.setChecked(true);
        checkBoxes.add(culture);

        hotel = findViewById(R.id.checkboxHotel);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_hotel)))
            hotel.setChecked(true);
        checkBoxes.add(hotel);

        naturalSite = findViewById(R.id.checkboxNaturalSite);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_natural_site)))
            naturalSite.setChecked(true);
        checkBoxes.add(naturalSite);

        fishing = findViewById(R.id.checkboxFishing);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_fishing)))
            fishing.setChecked(true);
        checkBoxes.add(fishing);

        vivac = findViewById(R.id.checkboxVivac);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_vivac)))
            vivac.setChecked(true);
        checkBoxes.add(vivac);

        camping = findViewById(R.id.checkboxCamping);
        if(viewModel.get_localizationTypes().contains(getString(R.string.key_camping)))
            camping.setChecked(true);
        checkBoxes.add(camping);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla en landscape
            //Modificamos el tamaño de los LinearLayouts
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            param.weight = 20;
            LinearLayout linearLayoutName = findViewById(R.id.LinearLayoutNameEditLocalizationPointActivity);
            linearLayoutName.setLayoutParams(param);
            param.weight = 25;
            LinearLayout linearLayoutDescription = findViewById(R.id.LinearLayoutDescriptionEditLocalizationPointActivity);
            linearLayoutDescription.setLayoutParams(param);
        }
    }

    /**
     * Interfaz
     * Nombre: trySaveLocalizationPoint
     * Comentario: Este método intentará guardar los cambios sobre el punto de localización en la
     * plataforma FireBase. En caso de insertar algún dato erroneo o si falta rellenar algún campo,
     * el método mostrará un mensaje de error por pantalla.
     * Cabecera: public void trySaveLocalizationPoint()
     * Postcondiciones: El método guarda los cambios del punto de localización en la plataforma FireBase
     * si todos los campos se han rellenado adecuadamente y en caso contrario muestra un mensaje de error por pantalla.
     */
    public void trySaveLocalizationPoint(){
        viewModel.set_newName(name.getText().toString().trim());
        viewModel.set_newDescription(description.getText().toString().trim());

        viewModel.get_localizationTypes().clear();//Limpiamos el listado de tipos
        for(int i = 0; i < checkBoxes.size(); i++){
            if(checkBoxes.get(i).isChecked()){//Si el tipo se encuentra chequeado
                viewModel.get_localizationTypes().add(checkBoxes.get(i).getTag().toString());
            }
        }

        if(!viewModel.get_newName().isEmpty() && !viewModel.get_newDescription().isEmpty()){
            if(!viewModel.get_localizationTypes().isEmpty()){

                //Cambiamos el nombre del punto de localización si ha cambiado
                if(!viewModel.get_actualLocalizationPoint().getName().equals(viewModel.get_newName())){
                    Map<String, Object> hopperUpdates = new HashMap<>();
                    hopperUpdates.put(ApplicationConstants.FB_LOCATION_NAME_CHILD, viewModel.get_newName());
                    localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).updateChildren(hopperUpdates);
                }

                //Cambiamos la descripción del punto de localización si ha cambiado
                if(!viewModel.get_actualLocalizationPoint().getDescription().equals(viewModel.get_newDescription())){
                    Map<String, Object> hopperUpdates = new HashMap<>();
                    hopperUpdates.put(ApplicationConstants.FB_LOCATION_DESCRIPTION_CHILD, viewModel.get_newDescription());
                    localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).updateChildren(hopperUpdates);
                }

                //Eliminamos todos los tipos del punto de localización
                deleteOldLocalizationPointTypes();
                //Insertamos los nuevos tipos del punto de localización
                String typeId;
                for(int i = 0; i < viewModel.get_localizationTypes().size(); i++){
                    typeId = localizationReference.push().getKey();
                    if(typeId != null){//Si se pudo obtener el id para el nuevo tipo
                        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_LOCALIZATION_TYPES_CHILD)
                                .child(typeId).setValue(viewModel.get_localizationTypes().get(i));
                    }
                }

                //Indicamos que se ha realizado la modificación
                Toast.makeText(this, R.string.localization_point_saved, Toast.LENGTH_SHORT).show();

                //Volvemos a la actividad de detalles
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ApplicationConstants.INTENT_NAME_UPDATED, viewModel.get_newName());
                resultIntent.putExtra(ApplicationConstants.INTENT_DESCRIPTION_UPDATED, viewModel.get_newDescription());
                setResult(RESULT_OK, resultIntent);
                finish();
            }else{
                Toast.makeText(this, R.string.min_one_type_required, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.name_and_description_required, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interfaz
     * Nombre: deleteOldLocalizationPointTypes
     * Comentario: Este método nos permite eliminar todos los tipos del puntos de localización actual.
     * Cabecera: public void deleteOldLocalizationPointTypes()
     * Postcondiciones: El método elimina todos los tipos del punto de localización actual.
     */
    public void deleteOldLocalizationPointTypes(){
        DatabaseReference drLocalizationPoint = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS).child(viewModel.get_actualLocalizationPoint().
                getLocalizationPointId()).child(ApplicationConstants.FB_LOCALIZATION_TYPES_CHILD);
        drLocalizationPoint.removeValue();
    }
}
