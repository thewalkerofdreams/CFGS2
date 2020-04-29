package com.example.adventuremaps.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.FireBaseEntities.ClsRoutePoint;
import com.example.adventuremaps.Fragments.GoogleMapsFragment;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.RouteActivitiesVM;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRouteActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private RouteActivitiesVM viewModel;
    private DatabaseReference routeReference = FirebaseDatabase.getInstance().getReference("ClsRoute");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(RouteActivitiesVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmail"));
    }

    /**
     * Interfaz
     * Nombre: intentarMarcarLocalizacion
     * Comentario: Este método nos permite marcar la última lozalización pulsada en el mapa.
     * Si no existe una localización marcada anteriormente, no se creará ningún marcador.
     * Cabecera: public boolean intentarMarcarLocalizacion(View view)
     * Entrada:
     *   -View view
     * Postcondiciones: El método marca la última localización pulsada en el mapa, si existe
     * alguna.
     * */
    public void intentarMarcarLocalizacion(View view){

        if(viewModel.get_lastLocalizationClicked() != null){//Si se ha marcado una localización
            FragmentManager fm = getSupportFragmentManager();
            GoogleMapsFragment fragment = (GoogleMapsFragment)fm.findFragmentById(R.id.fragmentGoogleMapsCreateRouteActivity);
            LatLng latLng = new LatLng(viewModel.get_lastLocalizationClicked().getLatitude(), viewModel.get_lastLocalizationClicked().getLongitude());
            //Marcamos esa posición
            fragment.colocarMarcador(latLng);
        }
    }

    /**
     * Interfaz
     * Nombre: trySaveRouteDialog
     * Comentario: Este método nos permite guardar la ruta actual rellenando un dialogo con los
     * datos finales de la ruta, si la ruta actual tiene menos de dos puntos de localizaciones, no
     * se lanzará el dialogo, sino que se lanzará un mensaje de error informando al usuario que debe
     * marcar como mínimo dos puntos en el mapa.
     * Cabecera: public void trySaveRouteDialog(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método almacena la ruta actual en la plataforma de FireBase o devuelve un mensaje
     * de error por pantalla si la ruta contiene menos de dos puntos de localización marcados en el mapa.
     */
    public void trySaveRouteDialog(View v){
        if(viewModel.get_localizationPoints().size() >= 2){//Si la ruta actual tiene como mínimo dos puntos de localización
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText nameEdit = new EditText(this);
            nameEdit.setHint(getString(R.string.hint_route_name));//Le insertamos una pista
            builder.setTitle(getString(R.string.save_route))
                    .setView(nameEdit)
                    .setMessage(getString(R.string.hint_route_name))
                    .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String routeName = nameEdit.getText().toString();//Nombre de la ruta a crear

                            if (routeName.isEmpty()) {//Si el nombre está vacío
                                Toast.makeText(getApplication(), getApplication().getString(R.string.route_name_empty), Toast.LENGTH_SHORT).show();
                            } else {
                                saveRoute(routeName);//Almacenamos la nueva ruta en la plataforma Firebase
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialogCreateGameMode = builder.create();
            alertDialogCreateGameMode.show();//Lanzamos el dialogo
        }else{
            Toast.makeText(getApplication(), getApplication().getString(R.string.insufficient_markers), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interfaz
     * Nombre: saveRoute
     * Comentario: El método almacena la ruta actual junto con sus puntos en la plataforma Firebase.
     * Cabecera: private void saveRoute(String routeName)
     * Entrada:
     *  -String routeName
     * Postcondiciones: El método almacena la ruta actual con un nombre determinado en la plataforma de Firebase.
     */
    private void saveRoute(String routeName){
        final String routeId = routeReference.push().getKey();//Obtenemos una id para la ruta

        //Almacenamos la ruta
        final ClsRoute newRoute = new ClsRoute(routeId, routeName, false, System.currentTimeMillis(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
        FirebaseDatabase.getInstance().getReference("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(newRoute.getRouteId())
                .setValue(newRoute).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {//Una vez almacenada la ruta, almacenamos los puntos que la forman
                //Almacenamos los puntos de la ruta
                for(int i = 0; i < viewModel.get_localizationPoints().size(); i++){
                    String routePointId = routeReference.push().getKey();//Obtenemos una id para el punto de la ruta
                    ClsRoutePoint newRoutePoint = new ClsRoutePoint(routePointId,(long) viewModel.get_localizationPoints().get(i).getPriority(),
                            routeId, viewModel.get_localizationPoints().get(i).getMarker().getPosition().latitude, viewModel.get_localizationPoints().get(i).getMarker().getPosition().longitude);

                    FirebaseDatabase.getInstance().getReference("Users").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(newRoute.getRouteId())
                            .child("routePoints").child(newRoutePoint.getRoutePointId())
                            .setValue(newRoutePoint);
                }

                Toast.makeText(getApplication(), getApplication().getString(R.string.route_saved), Toast.LENGTH_SHORT).show();//Indicamos que se ha guardado la ruta
                finish();//Finalizamos la actividad actual
            }
        });
    }
}
