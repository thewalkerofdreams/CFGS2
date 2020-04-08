package com.example.adventuremaps.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos eferencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization_point_details);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(DetailsLocalizationPointActivityVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmailUser"));
        viewModel.set_actualLocalizationPoint((ClsLocalizationPoint) getIntent().getSerializableExtra("ActualLocalization"));

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
                throwEditLocalizationPointActivity();//Lanzamos la actividad de edición
            }
        });

        btnFavourite = findViewById(R.id.btnFavouriteLocalizationPointDetails);
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFavourite.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si el punto de localización estaba marcado como favorito
                    btnFavourite.setBackgroundResource(R.drawable.empty_star);//Cambiamos el icono y almacenamos el nuevo resultado en la plataforma
                    userReference.child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("localizationsId").child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).removeValue();
                }else{
                    btnFavourite.setBackgroundResource(R.drawable.fill_star);//Cambiamos el icono y almacenamos el nuevo resultado en la plataforma
                    userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("localizationsId").child(
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
        intent.putExtra("ActualEmailUser", viewModel.get_actualEmailUser());
        intent.putExtra("ActualLocalization", viewModel.get_actualLocalizationPoint());
        intent.putStringArrayListExtra("LocalizationTypes", viewModel.get_localizationTypes());
        intent.putStringArrayListExtra("LocationsIdActualUser", viewModel.get_localizationsIdActualUser());
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
        intent.putExtra("ActualEmailUser", viewModel.get_actualEmailUser());
        intent.putExtra("ActualLocalizationPoint", viewModel.get_actualLocalizationPoint());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Read from the database
        localizationReference.orderByChild("localizationPointId").equalTo(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_localizationTypes().clear();
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot types : datas.child("types").getChildren()){
                        viewModel.get_localizationTypes().add(String.valueOf(types.getValue()));//Almacenamos los tipos de la localización
                    }
                }
                loadList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        userReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_localizationsIdActualUser().clear();//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("localizationsId").getChildren()){
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
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de tipos del punto de localización actual.
     * Cabecera: public void loadList()
     * Postcondiciones: El método nos permite cargar los tipos del punto de localización actual en una lista.
     */
    public void loadList(){
        TypeLocalizationPointsAdapter adapter = new TypeLocalizationPointsAdapter(this, R.layout.localization_type_item_list, viewModel.get_localizationTypes());
        localizationTypesList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ApplicationConstants.REQUEST_CODE_EDIT_LOCALIZATION_POINT){
            if(resultCode == Activity.RESULT_OK){
                nameLocalizationPoint.setText(data.getStringExtra("NameUpdated"));
                descriptionLocalizationPoint.setText(data.getStringExtra("DescriptionUpdated"));
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
    public void onSaveInstanceState(Bundle outState) {//Lo utilizamos para permitir que el dialogo sobreviva a los cambios de la pantalla
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
        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child("valorations").child(viewModel.get_actualEmailUser().replaceAll("[.]", " ")).child("Valoration").setValue(valoracion);
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
        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child("valorations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int goodValorationCounter = 0;
                int badValorationCounter = 0;
                for(DataSnapshot emails: dataSnapshot.getChildren()){//Obtenemos la valoración de todos los usuarios
                    if(emails.child("Valoration").getValue(Boolean.class)){
                        goodValorationCounter++;
                    }else{
                        badValorationCounter++;
                    }
                }

                if(badValorationCounter > (80 * (goodValorationCounter + badValorationCounter) / 100)){//Si se ha superado el 80 por ciento de valoraciones negativas //TODO Falta tener en cuenta el numero de votos pero lo dejo para las pruebas
                    localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child("shared").setValue(false);//Dejamos de compartir el punto de localización actual
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
                    String email = (String)users.child("email").getValue();
                    for(DataSnapshot booksSnapshot : users.child("localizationsId").getChildren()){
                        if(viewModel.get_actualLocalizationPoint().getLocalizationPointId().equals(booksSnapshot.getValue(String.class)) &&
                            !viewModel.get_actualLocalizationPoint().getEmailCreator().equals(email))
                            userReference.child(userId).child("localizationsId").child(booksSnapshot.getValue(String.class)).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
