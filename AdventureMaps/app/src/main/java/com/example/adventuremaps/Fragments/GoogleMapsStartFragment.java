package com.example.adventuremaps.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.Models.ClsMarkerWithLocalization;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class GoogleMapsStartFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap map;
    private MainTabbetActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos eferencia de las Localizaciones

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);//Inicializamos el mapa
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        viewModel.set_localizationPointToDelete(marker);
        deleteLocalizationDialog();
    }

    public void eliminarMarcador(Marker marcador){
        //TODO Intentar eliminar por Query en un futuro
        //String index = marcador.getPosition().latitude+""+marcador.getPosition().longitude;
        //Query qLocalization = FirebaseDatabase.getInstance().getReference("Localizations").orderByChild("index").equalTo(index);
        //DatabaseReference drLocalization = qLocalization.getRef();
        //drLocalization.child().removeValue();

        DatabaseReference drLocalization =FirebaseDatabase.getInstance().getReference("Localizations");
        ClsLocalizationPoint localizationPoint = getLocalizationPoint(marcador);
        if(localizationPoint != null){
            drLocalization.child(localizationPoint.getLocalizationPointId()).removeValue();
            marcador.remove();
        }

    }

    /**
     * Interfaz
     * Nombre: getLocalizationPoint
     * Comentario: Este método nos permite obtener un punto de localización por si latitud y longitud.
     * Si el punto de localización no existe en la base de datos, el método devuelve null.
     * Cabecera: public ClsLocalizationPoint getLocalizationPoint(LatLng latlng)
     * Entrada:
     *  -LatLng latlng
     * Salida:
     *  -ClsLocalizationPoint localization
     * Postcondiciones:
     */
    public ClsLocalizationPoint getLocalizationPoint(Marker marcador){
        ClsLocalizationPoint localization = null;
        boolean found = false;

        for(int i = 0; i < viewModel.get_localizationPointsWithMarker().size() && !found; i++){
            ClsMarkerWithLocalization aux = viewModel.get_localizationPointsWithMarker().get(i);
            if(aux.getMarker().getPosition().latitude == marcador.getPosition().latitude &&
                    aux.getMarker().getPosition().longitude == marcador.getPosition().longitude){
                localization = aux.getLocalizationPoint();
                found = true;
            }
        }

        return localization;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //Mientras se esta moviendo el marcador
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Al finalizar el moviento del marcador
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //Mostramos las coordenadas con un Toast
        String format = String.format(Locale.getDefault(), "Lat/Lng = (%f,%f)", latLng.latitude, latLng.longitude);
        Toast.makeText(getContext(), format, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng latLng;
        float zoom = 13;//Posicionamos el mapa en una localización y con un nivel de zoom

        if(viewModel.get_actualLocation() == null){//Si no podemos obtener la localización actusal del usuario
            latLng = new LatLng(40.4636688, -3.7492199);//Le daremos un valor por defecto
        }else{
            latLng = new LatLng(viewModel.get_actualLocation().getLatitude(), viewModel.get_actualLocation().getLongitude());
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//Movemos la camara según los valores definidos

        //Declaramos los eventos
        map.setOnMapClickListener(this);
        map.setOnMarkerDragListener(this);
        map.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        viewModel.set_longClickPosition(latLng);
        insertarMarcador(latLng);//TODO Por ahora lo insertamos
        saveLocalizationPoint(viewModel.get_markerToCreate());//TODO Por ahora lo guardamos
    }

    /**
     * Interfaz
     * Nombre: loadLocalizationPoints
     * Comentario: Este método nos permite cargar en el mapa los puntos de localización almacenados en la plataforma.
     * Cabecera: public void loadLocalizationPoints()
     * Postcondiciones: El método carga los puntos de localización en el mapa actual.
     */
    public void loadLocalizationPoints(){
        LatLng latLng;
        for(int i = 0; i < viewModel.get_localizationPoints().size(); i++){
            //latLng = new LatLng(viewModel.get_localizationPoints().get(i).getLatitude(), viewModel.get_localizationPoints().get(i).getLongitude());
            colocarMarcador(viewModel.get_localizationPoints().get(i)); //Comenzamos a marcar los puntos de la ruta almacenada
        }
    }

    /**
     * Interfaz
     * Nombre: colocarMarcador
     * Comentario: Este métdodo coloca un marcador en el mapa.
     * Cabecera: public void colocarMarcador(ClsLocalizationPoint localizationPoint)
     * Entrada:
     *  -ClsLocalizationPoint localizationPoint
     * Postcondiciones: El método coloca un marcador en el mapa.
     */
    public void colocarMarcador(ClsLocalizationPoint localizationPoint){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(localizationPoint.getLatitude(), localizationPoint.getLongitude()));//Indicamos la posición del marcador
        markerOptions.draggable(true);//Permite que podamos mover elmarcador por el mapa, en este caso, lo utilizamos para hacer un marcado largo
        Marker marker = map.addMarker(markerOptions);//Agregamos el marcador a la UI
        viewModel.get_localizationPointsWithMarker().add(new ClsMarkerWithLocalization(marker, localizationPoint));//Almacenamos el Marcador en un modelo
    }

    /**
     * Interfaz
     * Nombre: insertarMarcador
     * Comentario: Este método nos permite insertar un marcador en el mapa.
     * Además se guarda ese marcador en el VM MainTabbetActicityVM.
     * Cabecera: public void insertarMarcador(LatLng latLng)
     * Entrada:
     *   -LatLng latLng
     * Postcondiciones: El método inserta un marcador en el mapa. Además se guarda
     * ese marcador en el VM MainTabbetActicityVM.
     * */
    public void insertarMarcador(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);//Indicamos la posición del marcador
        markerOptions.draggable(true);//Permite que podamos mover elmarcador por el mapa, en este caso, lo utilizamos para hacer un marcado largo
        Marker marker = map.addMarker(markerOptions);//Agregamos el marcador a la UI
        viewModel.set_markerToCreate(marker);//Almacenamos el marcador creado en el VM
    }

    /**
     * Interfaz
     * Nombre: saveLocalizationPoint
     * Comentario: Este método nos permite guardar un punto de localización en la plataforma
     * FireBase a través de un Marcador.
     * Cabecera: public void saveLocalizationPoint(Marker marker)
     * Entrada:
     *  -Marker marker
     * Postcondiciones: El método almacena un punto de localización en la plataforma FireBase.
     */
    public void saveLocalizationPoint(Marker marker){
        String localizationId = localizationReference.push().getKey();//Obtenemos una id para la localización
        //Almacenamos la nueva localización
        ClsLocalizationPoint nuevaLocalizacion = new ClsLocalizationPoint(localizationId, "DEFAULT", "DEFAULT", marker.getPosition().latitude, marker.getPosition().longitude, System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference("Localizations").
                child(localizationId)
                .setValue(nuevaLocalizacion);
        viewModel.get_localizationPointsWithMarker().add(new ClsMarkerWithLocalization(marker, nuevaLocalizacion));//Almacenamos el marcador en una clase modelo
    }

    @Override
    public void onStart() {
        super.onStart();
        // Read from the database
        localizationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_localizationPoints().clear();//Limpiamos la lista de rutas
                viewModel.get_localizationPointsWithMarker().clear();//Limpiamos la lista de rutas que contienen los marcadores
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);
                    viewModel.get_localizationPoints().add(localizationPoint);
                }
                loadLocalizationPoints();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Interfaz
     * Nombre: deleteLocalizationDialog
     * Comentario: Este método muestra un dialogo por pantalla para eliminar un punto de localización seleccionado.
     * Si el usuario confirma la eliminación, se eliminará la localización de la plataforma FireBase, en caso
     * contrario no sucederá nada. Un usuario solo podrá eliminar las localizaciones que el haya creado, si intenta
     * eliminar una que no es suya el método mostrará un mensaje de error por pantalla.
     * Cabecera: public void deleteLocalizationDialog()
     * Postcondiciones: Si el usuario es propietario de ese punto de localización, el método muestra un dialogo por pantalla
     * , si el usuario lo confirma eliminará el punto de localización seleccionado, en caso contrario no sucederá nada.
     */
    public void deleteLocalizationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_localization_point);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.localization_point_deleted, Toast.LENGTH_SHORT).show();
                //Eliminamos el punto de localización
                eliminarMarcador(viewModel.get_localizationPointToDelete());
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogDeleteRouteShowing(false);
            }
        });

        AlertDialog alertDialogDeleteRoute = alertDialogBuilder.create();
        alertDialogDeleteRoute.show();
    }
}
