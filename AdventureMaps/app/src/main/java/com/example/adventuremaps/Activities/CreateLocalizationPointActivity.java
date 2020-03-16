package com.example.adventuremaps.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.LocalizationPointActivitiesVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateLocalizationPointActivity extends AppCompatActivity {

    private LocalizationPointActivitiesVM viewModel;
    private EditText name, description;
    private Button btnSave, btnFavourite;
    private CheckBox water, food, restArea, hunting, culture, hotel, naturalSite, fishing, vivac, camping;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private DatabaseReference localizationPointReference = FirebaseDatabase.getInstance().getReference("ClsLocalizationPoint");
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_localization_point);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(LocalizationPointActivitiesVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmailUser"));
        viewModel.set_latitude(getIntent().getDoubleExtra("ActualLatitude", 0));
        viewModel.set_longitude(getIntent().getDoubleExtra("ActualLongitude", 0));

        //Instanciamos los elementos de la UI
        name = findViewById(R.id.EditTextLocalizationName);
        description = findViewById(R.id.EditTextLocalizationDescription);

        btnSave = findViewById(R.id.btnCreateLocalizationPoint);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySaveLocalizationPoint();
            }
        });

        water = findViewById(R.id.checkboxWater);
        checkBoxes.add(water);

        food = findViewById(R.id.checkboxFood);
        checkBoxes.add(food);

        restArea = findViewById(R.id.checkboxRestArea);
        checkBoxes.add(restArea);

        hunting = findViewById(R.id.checkboxHunting);
        checkBoxes.add(hunting);

        culture = findViewById(R.id.checkboxCulture);
        checkBoxes.add(culture);

        hotel = findViewById(R.id.checkboxHotel);
        checkBoxes.add(hotel);

        naturalSite = findViewById(R.id.checkboxNaturalSite);
        checkBoxes.add(naturalSite);

        fishing = findViewById(R.id.checkboxFishing);
        checkBoxes.add(fishing);

        vivac = findViewById(R.id.checkboxVivac);
        checkBoxes.add(vivac);

        camping = findViewById(R.id.checkboxCamping);
        checkBoxes.add(camping);

        btnFavourite = findViewById(R.id.btnFavouriteLocalizationPointCreateAndEdit);
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFavourite.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si el punto de localización estaba marcado como favorito
                    btnFavourite.setBackgroundResource(R.drawable.empty_star);
                    viewModel.set_favourite(false);
                }else{
                    btnFavourite.setBackgroundResource(R.drawable.fill_star);
                    viewModel.set_favourite(true);
                }
            }
        });
    }

    /**
     * Interfaz
     * Nombre: trySaveLocalizationPoint
     * Comentario: Este método intentará guardar el punto de localización, si falta algún campo por rellenar
     * o si un campo tiene información erronea, el método mostrará un mensaje de error por pantalla, si
     * se rellenarón todos los campos necesarios adecuadamente, el método almacena el nuevo punto de localización
     * en la plataforma FireBase.
     * Cabecera: public void trySaveLocalizationPoint()
     * Postcondiciones: Si todos los campos necesarios se rellenarón adecuadamente, el método inserta el nuevo punto
     * de localización en la plataforma Firebase, en caso contrario el método muestra por pantalla un mensaje de error.
     */
    public void trySaveLocalizationPoint(){
        viewModel.set_name(name.getText().toString().trim());
        viewModel.set_description(description.getText().toString().trim());

        viewModel.get_localizationTypes().clear();//Limpiamos el listado de tipos
        for(int i = 0; i < checkBoxes.size(); i++){
            if(checkBoxes.get(i).isChecked()){//Si el tipo se encuentra chequeado
                viewModel.get_localizationTypes().add(checkBoxes.get(i).getText().toString());
            }
        }

        if(!viewModel.get_name().isEmpty() && !viewModel.get_description().isEmpty()){
            if(!viewModel.get_localizationTypes().isEmpty()){
                String localizationPointId = localizationPointReference.push().getKey();//Obtenemos una id para el punto de localización
                //Creamos el punto de localización
                ClsLocalizationPoint newLocalizationPoint = new ClsLocalizationPoint(localizationPointId, viewModel.get_name(), viewModel.get_description(), viewModel.get_latitude(), viewModel.get_longitude(),
                        System.currentTimeMillis(), viewModel.get_actualEmailUser());

                if(viewModel.is_favourite()){//Si el punto de localización fue marcado como favorito
                    userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("localizationsId").child(
                            localizationPointId)
                            .setValue(localizationPointId);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("LocalizationToSave", newLocalizationPoint);
                resultIntent.putExtra("LocalizationTypesToSave", viewModel.get_localizationTypes());
                setResult(RESULT_OK, resultIntent);
                finish();
            }else{
                Toast.makeText(this, R.string.min_one_type_required, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.name_and_description_required, Toast.LENGTH_SHORT).show();
        }
    }
}
