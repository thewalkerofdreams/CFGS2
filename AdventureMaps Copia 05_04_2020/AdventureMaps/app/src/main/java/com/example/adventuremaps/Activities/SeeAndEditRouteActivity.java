package com.example.adventuremaps.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.FireBaseEntities.ClsRoutePoint;
import com.example.adventuremaps.FireBaseEntities.ClsUser;
import com.example.adventuremaps.Fragments.GoogleMapsFragment;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.RouteActivitiesVM;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SeeAndEditRouteActivity extends AppCompatActivity {

    private RouteActivitiesVM viewModel;
    private DatabaseReference routeReference = FirebaseDatabase.getInstance().getReference("ClsRoute");
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(RouteActivitiesVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmail"));
        viewModel.set_actualIdRoute(getIntent().getStringExtra("ActualIdRoute"));
        viewModel.set_actualRouteName(getIntent().getStringExtra("ActualRouteName"));
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
                            if (routeName.isEmpty()) {//Si el nombre de la ruta se encuentra vacío
                                Toast.makeText(getApplication(), getApplication().getString(R.string.route_name_empty), Toast.LENGTH_SHORT).show();
                            } else {
                                //Cambiamos el nombre de la ruta si ha cambiado
                                if(!viewModel.get_actualRouteName().equals(nameEdit.getText().toString())){
                                    Map<String, Object> hopperUpdates = new HashMap<>();
                                    hopperUpdates.put("name", nameEdit.getText().toString());
                                    userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(viewModel.get_actualIdRoute()).updateChildren(hopperUpdates);
                                }

                                //Eliminamos los anteriores puntos de la ruta
                                deleteOldRoutePoints();

                                //Almacenamos los puntos de la ruta
                                for(int i = 0; i < viewModel.get_localizationPoints().size(); i++){
                                    String routePointId = routeReference.push().getKey();//Obtenemos una id para la ruta
                                    ClsRoutePoint newRoutePoint = new ClsRoutePoint(routePointId,(long) viewModel.get_localizationPoints().get(i).getPriority(),
                                            viewModel.get_actualIdRoute(), viewModel.get_localizationPoints().get(i).getMarker().getPosition().latitude, viewModel.get_localizationPoints().get(i).getMarker().getPosition().longitude);

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
        DatabaseReference drRoutePoint = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("routes").child(viewModel.get_actualIdRoute()).child("routePoints");
        drRoutePoint.removeValue();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Read from the database
        userReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {//Los datos del usuario actual
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_routePoints().clear();//Limpiamos la lista de rutas
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    ClsUser user = datas.getValue(ClsUser.class);
                        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("routes").child(viewModel.get_actualIdRoute()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                for(DataSnapshot points : dataSnapshot2.child("routePoints").getChildren()){
                                    ClsRoutePoint routePoint = points.getValue(ClsRoutePoint.class);
                                    viewModel.get_routePoints().add(routePoint);
                                }

                                cargarRuta();//Cargamos la ruta
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
        viewModel.get_localizationPoints().clear();

        if(fragment != null){//Si el fragmento se ha cargado correctamente
            for(int i = 0; i < viewModel.get_routePoints().size(); i++){
                latLng = new LatLng(viewModel.get_routePoints().get(i).getLatitude(), viewModel.get_routePoints().get(i).getLongitude());
                fragment.colocarMarcador(latLng); //Comenzamos a marcar los puntos de la ruta almacenada
            }
            fragment.moveCamera(new LatLng(viewModel.get_routePoints().get(0).getLatitude(), viewModel.get_routePoints().get(0).getLongitude()), 13);
        }
    }
}
