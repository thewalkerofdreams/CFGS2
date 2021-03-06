package com.example.adventuremaps.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Adapters.TypeLocalizationPointsAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.DetailsLocalizationPointActivityVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsLocalizationPointActivity extends AppCompatActivity {

    private TextView nameLocalizationPoint, descriptionLocalizationPoint;
    private ListView localizationTypesList;
    private Button btnEditLocalizationPoint, btnFavourite, btnImageGallery;
    private ImageButton goodValoration, badValoration;
    private AlertDialog goodValorationDialog, badValorationDialog;
    private DetailsLocalizationPointActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);//Tomamos la referencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_USERS_ADDRESS);
    private FirebaseUser firebaseCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization_point_details);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(DetailsLocalizationPointActivityVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra(ApplicationConstants.INTENT_ACTUAL_USER_EMAIL));
        viewModel.set_actualLocalizationPoint((ClsLocalizationPoint) getIntent().getSerializableExtra(ApplicationConstants.INTENT_ACTUAL_LOCALIZATION));

        //Obtenemos la referencia del usuario actual
        firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Instanciamos los elementos de la UI
        nameLocalizationPoint = findViewById(R.id.TextViewNameLocalizationPointDetailsActivity);
        nameLocalizationPoint.setText(viewModel.get_actualLocalizationPoint().getName());

        descriptionLocalizationPoint = findViewById(R.id.TextViewDescriptionLocalizationPointDetailsActivity);
        descriptionLocalizationPoint.setText(viewModel.get_actualLocalizationPoint().getDescription());
        localizationTypesList = findViewById(R.id.ListViewLocalizationTypes);

        btnEditLocalizationPoint = findViewById(R.id.btnEditLocalizationPoint);
        btnEditLocalizationPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.get_actualLocalizationPoint().getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización pertenece al usuario actual
                    throwEditLocalizationPointActivity();//Lanzamos la actividad de edición
                }else{
                    Toast.makeText(getApplication(), R.string.error_edit_non_own_localization, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnFavourite = findViewById(R.id.btnFavouriteLocalizationPointDetails);
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFavourite.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si el punto de localización estaba marcado como favorito
                    btnFavourite.setBackgroundResource(R.drawable.empty_star);//Cambiamos el icono y almacenamos el nuevo resultado en la plataforma
                    userReference.child(firebaseCurrentUser.getUid()).child(ApplicationConstants.FB_LOCALIZATIONS_ID).child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).removeValue();

                }else{
                    btnFavourite.setBackgroundResource(R.drawable.fill_star);//Cambiamos el icono y almacenamos el nuevo resultado en la plataforma
                    userReference.child(firebaseCurrentUser.getUid()).child(ApplicationConstants.FB_LOCALIZATIONS_ID).child(
                            viewModel.get_actualLocalizationPoint().getLocalizationPointId())
                            .setValue(viewModel.get_actualLocalizationPoint().getLocalizationPointId());
                }
            }
        });

        btnImageGallery = findViewById(R.id.btnImageGalleryLocalizationPointDetails);
        btnImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throwImageGalleryActivity();//Lanzamos la galería de imágenes
            }
        });

        goodValoration = findViewById(R.id.ImageButtonGoodDescription);
        goodValoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoodDescriptionDialog();
            }
        });

        badValoration = findViewById(R.id.ImageButtonBadDescription);
        badValoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBadDescriptionDialog();
            }
        });

        if(savedInstanceState != null && viewModel.is_goodValorationDialogShowing()) {//Si algún dialogo de valoración se encuentra abierto, lo recarga
            showGoodDescriptionDialog();
        }else{
            if(savedInstanceState != null && viewModel.is_badValorationDialogShowing())
                showBadDescriptionDialog();
        }


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla en landscape
            //Modificamos el tamaño de los botones
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(200, 200, (float) 0.0);
            btnFavourite.setLayoutParams(param);
            btnImageGallery.setLayoutParams(param);

            param = new LinearLayout.LayoutParams(100, 100, (float) 0.0);
            param.setMarginStart(20);
            param.setMarginEnd(20);
            goodValoration.setLayoutParams(param);
            badValoration.setLayoutParams(param);

            //Ocultamos los textViews del nombre y la descripcción.
            TextView textViewName = findViewById(R.id.TextViewNameDetailsLocalizationPointActivity);
            textViewName.setVisibility(View.GONE);
            String aux = getString(R.string.name_form) + " " + viewModel.get_actualLocalizationPoint().getName();
            nameLocalizationPoint.setText(aux);

            TextView textViewDescription = findViewById(R.id.TextViewDescriptionDetailsLocalizationPointActivity);
            textViewDescription.setVisibility(View.GONE);
            aux = getString(R.string.description_form) + " " + viewModel.get_actualLocalizationPoint().getDescription();
            descriptionLocalizationPoint.setText(aux);
        }
    }

    /**
     * Interfaz
     * Nombre: throwEditLocalizationPointActivity
     * Comentario: Este método lanza la actividad EditLocalizationPointActivity con los
     * datos necesarios parar editar el punto de localización actual.
     * Cabecera: private void throwEditLocalizationPointActivity()
     * Postcondiciones: El método lanza la actividad EditLocalizationPointActivity, con los datos
     * necesarios para actualizar el punto de localización actual.
     */
    private void throwEditLocalizationPointActivity(){
        Intent intent = new Intent(getApplication(), EditLocalizationPointActivity.class);
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_USER_EMAIL, viewModel.get_actualEmailUser());
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_LOCALIZATION, viewModel.get_actualLocalizationPoint());
        intent.putStringArrayListExtra(ApplicationConstants.DATA_LOCALIZATION_TYPES, viewModel.get_localizationKeyTypes());
        intent.putStringArrayListExtra(ApplicationConstants.DATA_LOCALIZATIONS_ID_ACTUAL_USER, viewModel.get_localizationsIdActualUser());
        startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_EDIT_LOCALIZATION_POINT);
    }

    /**
     * Interfaz
     * Nombre: throwImageGalleryActivity
     * Comentario: Este método lanza la actividad ImageGalleryActivity con los
     * datos necesarios parar mostrar las imagenes del punto de localización actual.
     * Cabecera: private void throwImageGalleryActivity()
     * Postcondiciones: El método lanza la actividad ImageGalleryActivity, con los datos
     * necesarios para cargar las imagenes del punto de localización actual.
     */
    private void throwImageGalleryActivity(){
        Intent intent = new Intent(getApplication(), ImageGalleryActivity.class);
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_USER_EMAIL, viewModel.get_actualEmailUser());
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_LOCALIZATION_POINT, viewModel.get_actualLocalizationPoint());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Read from the database
        localizationReference.orderByChild(ApplicationConstants.FB_LOCALIZATION_POINT_ID).equalTo(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewModel.get_localizationKeyTypes().clear();
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot types : datas.child(ApplicationConstants.FB_LOCALIZATION_TYPES_CHILD).getChildren()){
                        viewModel.get_localizationKeyTypes().add(String.valueOf(types.getValue()));//Almacenamos los tipos de la localización
                    }
                }
                getTranslateNameTypes();
                loadList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        userReference.orderByChild(ApplicationConstants.FB_USER_EMAIL_CHILD).equalTo(viewModel.get_actualEmailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewModel.get_localizationsIdActualUser().clear();//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child(ApplicationConstants.FB_LOCALIZATIONS_ID).getChildren()){
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdActualUser().add(localizationId);
                    }
                }
                if(viewModel.get_localizationsIdActualUser().contains(viewModel.get_actualLocalizationPoint().getLocalizationPointId())){//Si el punto de localización estaba en favoritos
                    btnFavourite.setBackgroundResource(R.drawable.fill_star);//Modificamos el icono
                }else{
                    btnFavourite.setBackgroundResource(R.drawable.empty_star);//Modificamos el icono
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Interfaz
     * Nombre: getTranslateNameTypes
     * Comentario: El método almacena en el VM una lista con los nombres de los tipos de la localización
     * traducidos al idioma del dispositivo actual.
     * Cabecera: private void getTranslateNameTypes()
     * Postcondiciones: El método almacena en el VM los nombres traducidos de los tipos de la localización.
     */
    private void getTranslateNameTypes(){
        viewModel.get_localizationNameTypes().clear();//Vaciamos la lista de tipos
        for(int i = 0; i < viewModel.get_localizationKeyTypes().size(); i++){
            switch (viewModel.get_localizationKeyTypes().get(i)){
                case "Camping":
                    viewModel.get_localizationNameTypes().add(getString(R.string.camping));
                    break;
                case "Vivac":
                    viewModel.get_localizationNameTypes().add(getString(R.string.vivac));
                    break;
                case "Fishing":
                    viewModel.get_localizationNameTypes().add(getString(R.string.fishing));
                    break;
                case "Natural Site":
                    viewModel.get_localizationNameTypes().add(getString(R.string.natural_site));
                    break;
                case "Lodging house/Hotel":
                    viewModel.get_localizationNameTypes().add(getString(R.string.hotel));
                    break;
                case "Culture":
                    viewModel.get_localizationNameTypes().add(getString(R.string.culture));
                    break;
                case "Hunting":
                    viewModel.get_localizationNameTypes().add(getString(R.string.hunting));
                    break;
                case "Rest Area":
                    viewModel.get_localizationNameTypes().add(getString(R.string.rest_area));
                    break;
                case "Food":
                    viewModel.get_localizationNameTypes().add(getString(R.string.food));
                    break;
                case "Potable Water":
                    viewModel.get_localizationNameTypes().add(getString(R.string.potable_water));
                    break;
            }
        }
    }

    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de tipos del punto de localización actual.
     * Cabecera: public void loadList()
     * Postcondiciones: El método nos permite cargar los tipos del punto de localización actual en una lista.
     */
    public void loadList(){
        TypeLocalizationPointsAdapter adapter = new TypeLocalizationPointsAdapter(this, R.layout.localization_type_item_list, viewModel.get_localizationNameTypes());
        localizationTypesList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ApplicationConstants.REQUEST_CODE_EDIT_LOCALIZATION_POINT){
            if(resultCode == Activity.RESULT_OK && data != null){//Si se finalizó la edición y se obtuvieron datos de respuesta
                nameLocalizationPoint.setText(data.getStringExtra(ApplicationConstants.INTENT_NAME_UPDATED));
                descriptionLocalizationPoint.setText(data.getStringExtra(ApplicationConstants.INTENT_DESCRIPTION_UPDATED));
            }
        }
    }

    /**
     * Interfaz
     * Nombre: showGoodDescriptionDialog
     * Comentario: Este método muestra por pantalla un dialogo para indicar que un punto de localización
     * es válido, es decir, describe adecuadamente lo que es.
     * Cabecera: public void showGoodDescriptionDialog()
     * Postconidiciones: El método muestra un dialogo por pantalla, si el usuario confirma el dialogo
     * se valorará positivamente el punto de localización.
     */
    public void showGoodDescriptionDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_valoration);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_good_valoration);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplication(), R.string.valoration_saved, Toast.LENGTH_SHORT).show();
                sendValorationToFirebase(true);//Enviamos una valoración positiva
                viewModel.set_goodValorationDialogShowing(false);
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_goodValorationDialogShowing(false);
            }
        });

        goodValorationDialog = alertDialogBuilder.create();
        goodValorationDialog.show();
    }

    /**
     * Interfaz
     * Nombre: showBadDescriptionDialog
     * Comentario: Este método muestra por pantalla un dialogo para indicar que un punto de localización
     * no es válido, es decir, no describe adecuadamente lo que es.
     * Cabecera: public void showBadDescriptionDialog()
     * Postconidiciones: El método muestra un dialogo por pantalla, si el usuario confirma el dialogo
     * se valorará negativamente el punto de localización.
     */
    public void showBadDescriptionDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_valoration);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_bad_valoration);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplication(), R.string.valoration_saved, Toast.LENGTH_SHORT).show();
                sendValorationToFirebase(false);//Enviamos una valoración negativa
                viewModel.set_badValorationDialogShowing(false);
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_badValorationDialogShowing(false);
            }
        });

        badValorationDialog = alertDialogBuilder.create();
        badValorationDialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {//Lo utilizamos para permitir que el dialogo sobreviva a los cambios de la pantalla
        super.onSaveInstanceState(outState);

        if(goodValorationDialog != null && goodValorationDialog.isShowing()) {//Si se encuentra abierto el dialogo de valoración positiva
            goodValorationDialog.dismiss();// close dialog to prevent leaked window
            viewModel.set_goodValorationDialogShowing(true);
        }else{
            if(badValorationDialog != null && badValorationDialog.isShowing()){//Si se encuentra abierto el dialogo de valoración negativa
                badValorationDialog.dismiss();// close dialog to prevent leaked window
                viewModel.set_badValorationDialogShowing(true);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: sendValorationToFirebase
     * Comentario: Este método nos permite enviar la valoración a la plataforma Firebase.
     * El método recibe un parámetro de entrada booleano, true si la valoración es positiva y
     * false en caso contrario. Al enviar la valoración, se llamará al método tryToDeleteLocalizationPoint
     * por si se debe eliminar la localización actual, debido a que se ha superado el 80% de las valoraciones
     * negativas.
     * Cabecera: public void sendValorationToFirebase(boolean valoracion)
     * Entrada:
     *  -boolean valoracion
     * Postcondiciones: El método envia la valoración a la plataforma Firebase.
     */
    public void sendValorationToFirebase(boolean valoracion){
        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_LOCALIZATION_VALORATIONS_CHILD)
                .child(viewModel.get_actualEmailUser().replaceAll("[.]", " ")).child(ApplicationConstants.FB_LOCALIZATION_VALORATION_CHILD).setValue(valoracion);
        if(!valoracion && viewModel.get_actualLocalizationPoint().isShared())//Si la valoración es negativa y el punto de localización esta compartido
            tryToStopSharingLocalizationPoint();
    }

    /**
     * Interfaz
     * Nombre: tryToStopSharingLocalizationPoint
     * Comentario: Este método deja de compartir el punto de localización si el 80 por ciento de sus valoraciones
     * son negativas, además debe tiener como mínimo 30 valoraciones.
     * Cabecera: public void tryToStopSharingLocalizationPoint()
     * Postcondiciones: El método deja de compartir el punto de localización actual si tiene más de 30 valoraciones y el
     * 80 porciento de estas son negativas.
     */
    public void tryToStopSharingLocalizationPoint(){
        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_LOCALIZATION_VALORATIONS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int goodValorationCounter = 0;
                int badValorationCounter = 0;
                for(DataSnapshot emails: dataSnapshot.getChildren()){//Obtenemos la valoración de todos los usuarios
                    if(emails.child(ApplicationConstants.FB_LOCALIZATION_VALORATION_CHILD).getValue(Boolean.class)){
                        goodValorationCounter++;
                    }else{
                        badValorationCounter++;
                    }
                }

                if(badValorationCounter > (80 * (goodValorationCounter + badValorationCounter) / 100)){//Si se ha superado el 80 por ciento de valoraciones negativas
                    localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_LOCATION_SHARED_CHILD).setValue(false);//Dejamos de compartir el punto de localización actual
                    desmarcarLocalizacionDeFavoritos();

                    if(!viewModel.get_actualLocalizationPoint().getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización no pertenece al usuario actual
                        setResult(Activity.RESULT_OK);
                        finish();//Finalizamos la actividad actual
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Interfaz
     * Nombre: desmarcarLocalizacionDeFavoritos
     * Comentario: Este método nos permite descamar los enlaces de favoritos a una localización
     * en específico, es decir, destruye el enlace entre los usuarios y la localización. A excepción
     * del usuario que creó la localización.
     * Cabecera: public void desmarcarLocalizacionDeFavoritos(String localizationPointId)
     * Entrada:
     *  -String localizationPointId
     * Postcondiciones: El método desvincula a los usuarios no propietarios del punto de localización.
     */
    public void desmarcarLocalizacionDeFavoritos(){
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot users: dataSnapshot.getChildren()){//Obtenemos los diferentes usuarios
                    String userId = users.getKey();
                    String email = (String)users.child(ApplicationConstants.FB_USER_EMAIL_CHILD).getValue();
                    for(DataSnapshot booksSnapshot : users.child(ApplicationConstants.FB_LOCALIZATIONS_ID).getChildren()){
                        if(viewModel.get_actualLocalizationPoint().getLocalizationPointId().equals(booksSnapshot.getValue(String.class)) &&
                            !viewModel.get_actualLocalizationPoint().getEmailCreator().equals(email))
                            userReference.child(userId).child(ApplicationConstants.FB_LOCALIZATIONS_ID).child(booksSnapshot.getValue(String.class)).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
