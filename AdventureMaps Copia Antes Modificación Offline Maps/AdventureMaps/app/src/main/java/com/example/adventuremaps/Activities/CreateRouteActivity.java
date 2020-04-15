package com.example.adventuremaps.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.RouteActivitiesVM;
import com.google.android.gms.maps.model.LatLng;
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

        //Si la aplicación no tiene los permisos necesarios, muestra por pantalla un dialogo para obtenerlos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ApplicationConstants.REQUEST_CODE_PERMISSIONS_FINE_AND_COARSE_LOCATION);
        }

        //Si la aplicación tiene los permisos de localización se instancia el VM
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel = ViewModelProviders.of(this).get(RouteActivitiesVM.class);
            viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmail"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//Resultado de repuesta de un permiso
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String mensaje = "";

        if(requestCode == ApplicationConstants.REQUEST_CODE_PERMISSIONS_FINE_AND_COARSE_LOCATION){
            mensaje = "Coarse Location and Fine Location";
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(CreateRouteActivity.this, mensaje+" Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(CreateRouteActivity.this, mensaje+" Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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

                            if (routeName.isEmpty()) {//Si esta vacío
                                Toast.makeText(getApplication(), getApplication().getString(R.string.route_name_empty), Toast.LENGTH_SHORT).show();
                            } else {
                                String routeId = routeReference.push().getKey();//Obtenemos una id para la ruta
                                //Almacenamos la ruta
                                ClsRoute newRoute = new ClsRoute(routeId, routeName, false, System.currentTimeMillis(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                FirebaseDatabase.getInstance().getReference("Users").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(newRoute.getRouteId())
                                        .setValue(newRoute);

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

                                Toast.makeText(getApplication(), getApplication().getString(R.string.route_saved), Toast.LENGTH_SHORT).show();
                                finish();
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

}
