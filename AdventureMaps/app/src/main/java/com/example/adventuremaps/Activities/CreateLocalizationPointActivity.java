package com.example.adventuremaps.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.Models.ClsImageWithId;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.LocalizationPointActivitiesVM;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class CreateLocalizationPointActivity extends AppCompatActivity {

    private LocalizationPointActivitiesVM viewModel;
    private EditText name, description;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");
    private Button btnSave, btnFavourite, btnImageGallery;
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

        //Si la aplicación no tiene los permisos de lectura externa los pide
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ApplicationConstants.REQUEST_CODE_PERMISSIONS_READ_EXTERNAL_STORAGE);
        }

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

        btnImageGallery = findViewById(R.id.btnImageGalleryCreateLocalizationPoint);
        btnImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {//Si el usuario aceptó los permisos necesarios
                    startActivityForResult(new Intent(getApplication(), CreateLocalizationPointImageGalleryActivity.class).putExtra("ImagesToSave", viewModel.get_imagesToSave()), ApplicationConstants.REQUEST_CODE_START_ACTIVITY_FOR_RESULT_IMAGE_GALLERY);
                }else{
                    Toast.makeText(getApplication(), R.string.error_read_external_storage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla en landscape
            //Modificamos el tamaño de los botones
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(200, 200, (float) 0.0);
            btnFavourite.setLayoutParams(param);
            btnImageGallery.setLayoutParams(param);

            //Ocultamos los textViews del nombre y la descripcción e insertamos una pista en los editText.
            TextView textViewName = findViewById(R.id.TextViewNameCreateLocalizationPointActivity);
            textViewName.setVisibility(View.GONE);
            name.setHint(R.string.name);

            TextView textViewDescription = findViewById(R.id.TextViewDescriptionCreateLocalizationPointActivity);
            textViewDescription.setVisibility(View.GONE);
            description.setHint(R.string.description);
        }
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

        if(!viewModel.get_name().isEmpty() && !viewModel.get_description().isEmpty()){//Si el nombre y la descripción son válidos
            if(!viewModel.get_localizationTypes().isEmpty()){//Si existe al menos un tipo seleccionado para la localización
                String localizationPointId = localizationPointReference.push().getKey();//Obtenemos una id para el punto de localización
                //Creamos el punto de localización
                ClsLocalizationPoint newLocalizationPoint = new ClsLocalizationPoint(localizationPointId, viewModel.get_name(), viewModel.get_description(), viewModel.get_latitude(), viewModel.get_longitude(),
                        System.currentTimeMillis(), viewModel.get_actualEmailUser());

                if(viewModel.is_favourite()){//Si el punto de localización fue marcado como favorito
                    userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("localizationsId").child(
                            localizationPointId)
                            .setValue(localizationPointId);
                }

                //Almacenamos las imagenes del punto de localización
                insertImagesToFireBase(localizationPointId);

                //Cerramos la actividad actual
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

    /**
     * Interfaz
     * Nombre: getExtension
     * Comentario: Este método nos permite obtener la extensión de una dirección Uri.
     * Cabecera: public String getExtension(Uri uri)
     * Entrada:
     *  -Uri uri
     * Postcondiciones: El método devuelve la extensión de la dirección uri asociada al nombre.
     */
    public String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * Interfaz
     * Nombre: insertImagesToFireBase
     * Comentario: Este método nos permite insertar las imagenes asigandas al nuevo punto de localización
     * en la plataforma Firebase.
     * Cabecera: public void insertImagesToFireBase(final String localizationPointId)
     * Entrada:
     *  -String localizationPointId
     * Postcondiciones: El método inserta las imagenes asignadas al nuevo punto de localización en la
     * plataforma Firebase.
     */
    public void insertImagesToFireBase(final String localizationPointId){
        for(int i = 0; i < viewModel.get_imagesToSave().size(); i++){
            final StorageReference storageReference = mStorageRef.child("Images").child(localizationPointId).child(viewModel.get_actualEmailUser()).
                    child(System.currentTimeMillis()+""+getExtension(Uri.parse(viewModel.get_imagesToSave().get(i).get_uri())));//La imagen se colgará con la fecha de subida como nombre y su correspondiente extensión

            storageReference.putFile(Uri.parse(viewModel.get_imagesToSave().get(i).get_uri()))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {//Si los metadatos no son nulos
                                if (taskSnapshot.getMetadata().getReference() != null) {//Y podemos obtener una referencia al storage
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();//Necesitamos transformarla en un String para subirla a la plataforma
                                            String imageId = localizationReference.push().getKey();//Obtenemos una id para la imagen
                                            //Insertamos la dirección de la imagen en la base de datos
                                            localizationReference.child(localizationPointId).child("emailImages").child(viewModel.get_actualEmailUser().replaceAll("[.]", " ")).child("LocalizationImages")
                                                    .child(imageId).child("Uri").setValue(imageUrl);

                                            Toast.makeText(getApplication(), R.string.image_uploaded, Toast.LENGTH_SHORT).show();//Indicamos que la imagen se ha subido
                                        }
                                    });
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplication(), R.string.error_upload_image, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ApplicationConstants.REQUEST_CODE_START_ACTIVITY_FOR_RESULT_IMAGE_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {//Si la operación se realizó correctamente
                ArrayList<ClsImageWithId> images = (ArrayList<ClsImageWithId>) data.getSerializableExtra("ImagesToSave");
                viewModel.set_imagesToSave(images);//Almacenamos las imagenes en el VM
            }
        }
    }

    @Override//Controlamos la respuesta a los permisos
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ApplicationConstants.REQUEST_CODE_PERMISSIONS_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplication(), R.string.read_external_storage_permission_granted, Toast.LENGTH_SHORT).show();//Indicamos que se concedió el permiso
            }
        }
    }
}
