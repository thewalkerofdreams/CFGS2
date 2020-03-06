package com.example.adventuremaps.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.example.adventuremaps.Activities.Models.ClsMarkerWithPriority;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.FireBaseEntities.ClsRoutePoint;
import com.example.adventuremaps.Fragments.GoogleMapsFragment;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.CreateRouteActivityVM;
import com.example.adventuremaps.ViewModels.SeeAndEditRouteActivityVM;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.List;

public class SeeAndEditRouteActivity extends AppCompatActivity {

    private CreateRouteActivityVM viewModel;
    private DatabaseReference routeReference;
    private DatabaseReference myDataBaseReference = FirebaseDatabase.getInstance().getReference("Users");
    private ArrayList<ClsRoutePoint> routePoints = new ArrayList<>();
    private ArrayList<ClsRoutePoint> initialRoutePoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        //Si la aplicación no tiene los permisos necesarios, muestra por pantalla un dialogo para obtenerlos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        //Si la aplicación tiene los permisos de localización se instancia el VM
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel = ViewModelProviders.of(this).get(CreateRouteActivityVM.class);
            viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmail"));
            viewModel.set_actualIdRoute(getIntent().getStringExtra("ActualIdRoute"));
            viewModel.set_actualRouteName(getIntent().getStringExtra("ActualRouteName"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//Resultado de repuesta de un permiso
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String mensaje = "";

        if(requestCode == 1){
            mensaje = "Coarse Location and Fine Location";
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, mensaje+" Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, mensaje+" Permission Denied", Toast.LENGTH_SHORT).show();
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
     * Comentario: Este método nos permite guardar la ruta actual rellenando un dialogo pudiendo editar
     * el nombre actual de la ruta, si la ruta actual tiene menos de dos puntos de localizaciones, no
     * se lanzará el dialogo, sino que se lanzará un mensaje de error informando al usuario que debe
     * marcar como mínimo dos puntos en el mapa.
     * Cabecera: public void trySaveRouteDialog(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método modifica la ruta actual en la plataforma de FireBase o devuelve un mensaje
     * de error por pantalla si la ruta contiene menos de dos puntos de localización marcados en el mapa.
     */
    public void trySaveRouteDialog(View v){
        if(viewModel.get_localizationPoints().size() >= 2){//Si la ruta actual tiene como mínimo dos puntos de localización
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText nameEdit = new EditText(this);
            nameEdit.setText(viewModel.get_actualRouteName());
            nameEdit.setHint(getString(R.string.hint_route_name));//Le insertamos una pista
            builder.setTitle(getString(R.string.save_route))
                    .setView(nameEdit)
                    .setMessage(getString(R.string.hint_route_name))
                    .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String routeName = nameEdit.getText().toString();
                            if (routeName.length() == 0) {
                                Toast.makeText(getApplication(), getApplication().getString(R.string.route_name_empty), Toast.LENGTH_SHORT).show();
                            } else {
                                viewModel.set_mostrarRuta(false);//Con esto indicamos que vamor a guardar datos en FireBase en el VM

                                routeReference = FirebaseDatabase.getInstance().getReference("ClsRoute");
                                String routeId = routePoints.get(0).getRouteId();

                                deleteOldRoutePoints();//Eliminamos los anteriores puntos de la ruta

                                //Almacenamos los puntos de la ruta
                                for(int i = 0; i < viewModel.get_localizationPoints().size(); i++){
                                    String routePointId = routeReference.push().getKey();//Obtenemos una id para la ruta
                                    ClsRoutePoint newRoutePoint = new ClsRoutePoint(routePointId,(long) viewModel.get_localizationPoints().get(i).getPriority(),
                                            routeId, viewModel.get_localizationPoints().get(i).getMarker().getPosition().latitude, viewModel.get_localizationPoints().get(i).getMarker().getPosition().longitude);

                                    FirebaseDatabase.getInstance().getReference("Users").
                                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(viewModel.get_actualIdRoute())
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

    /**
     * Interfaz
     * Nombre: deleteOldRoutePoints
     * Comentario: Este método nos permite eliminar todos los puntos de localización de la ruta actual.
     * Cabecera: public void deleteOldRoutePoints()
     * Postcondiciones: El método elimina todos los puntos de localización de la ruta actual.
     */
    public void deleteOldRoutePoints(){
        DatabaseReference drRoutePoint;
        drRoutePoint = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(initialRoutePoints.get(0).getRouteId()).child("routePoints");
        drRoutePoint.removeValue();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.set_mostrarRuta(true);

        // Read from the database
        myDataBaseReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                routePoints.clear();//Limpiamos la lista de rutas
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot routes : datas.child("routes").getChildren()){
                        //loop 2 to go through all the child nodes of routes node
                        ClsRoute route = routes.getValue(ClsRoute.class);
                        if(route.getRouteId().equals(viewModel.get_actualIdRoute())){//Si es la ruta que queremos mostrar

                            routePoints = new ArrayList<>();
                            initialRoutePoints = new ArrayList<>();
                            for(DataSnapshot points : routes.child("routePoints").getChildren()){
                                ClsRoutePoint routePoint = points.getValue(ClsRoutePoint.class);
                                routePoints.add(routePoint);
                                initialRoutePoints.add(routePoint);
                            }

                            break;//TODO No me puedo creer que lo este solucionando así
                        }
                    }
                }
                if(viewModel.is_mostrarRuta()) {
                    cargarRuta();//Cargamos la ruta
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
     * Nombre: cargarRuta
     * Comentario: Este método nos permite cargar la ruta a visualizar.
     * Cabecera: public void cargarRuta()
     * Postcondiciones: El método carga la ruta por defecto creada con anterioridad.
     */
    public void cargarRuta(){
        GoogleMapsFragment fragment = (GoogleMapsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentGoogleMapsCreateRouteActivity);
        LatLng latLng;
        viewModel.set_localizationPoints(new ArrayList<ClsMarkerWithPriority>());

        if(fragment != null){//TODO Porque se mete a veces en esta función???
            for(int i = 0; i < routePoints.size(); i++){
                latLng = new LatLng(routePoints.get(i).getLatitude(), routePoints.get(i).getLongitude());
                fragment.colocarMarcador(latLng); //Comenzamos a marcar los puntos de la ruta almacenada
            }
        }
    }
}
