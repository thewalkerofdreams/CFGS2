package com.example.adventuremaps.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.MainTabbetActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class GoogleMapsStartFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private MainTabbetActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos eferencia de las Localizaciones

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        //Cargamos el fragmento inferior
        ((MainTabbetActivity)getActivity()).replaceFragment();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);//Inicializamos el mapa
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        viewModel.set_localizationPointClicked(marker);//Almacenamos el marcador seleccionado
        //((MainTabbetActivity)getActivity()).replaceFragment();//Hacemos que aparezca el fragmento en la actividad
        (getActivity().findViewById(R.id.FrameLayout02)).setVisibility(View.VISIBLE);//Volvemos visible el fragmento inferior
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //Mostramos las coordenadas con un Toast
        String format = String.format(Locale.getDefault(), "Lat/Lng = (%f,%f)", latLng.latitude, latLng.longitude);
        Toast.makeText(getContext(), format, Toast.LENGTH_LONG).show();
        //((MainTabbetActivity)getActivity()).removeYourFragment();//Eliminamos el fragment actual, si existe.
        (getActivity().findViewById(R.id.FrameLayout02)).setVisibility(View.GONE);//Volvemos invisible el fragmento inferior
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
        map.setOnMapLongClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        viewModel.set_longClickPosition(latLng);//Almacenamos la posición seleccionada en el mapa en el VM
        insertLocalizationDialog();//Comenzamos un dialogo de inserción
    }

    /**
     * Interfaz
     * Nombre: loadLocalizationPoints
     * Comentario: Este método nos permite cargar en el mapa los puntos de localización almacenados en la plataforma.
     * Cabecera: public void loadLocalizationPoints()
     * Postcondiciones: El método carga los puntos de localización en el mapa actual.
     */
    public void loadLocalizationPoints(){
        for(int i = 0; i < viewModel.get_localizationPoints().size(); i++){
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
                cleanAllLocalizations();
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
     * Nombre: cleanAllLocalizations
     * Comentario: Este método nos permite limpiar todos los marcadores sobre el mapa.
     * Cabecera: public void cleanAllLocalizations()
     * Postcondiciones: El método elimina todos los marcadores que se encuentran sobre
     * el mapa actual.
     */
    public void cleanAllLocalizations(){
        for(int i = 0; i < viewModel.get_localizationPointsWithMarker().size(); i++){
            viewModel.get_localizationPointsWithMarker().get(i).getMarker().remove();
        }
    }

    /**
     * Interfaz
     * Nombre: insertLocalizationDialog
     * Comentario: Este método muestra un dialogo por pantalla para insertar un punto de localización en el mapa.
     * Si el usuario confirma la creación, se cargará un formulario para la creación del nuevo punto de localización.
     * Cabecera: public void insertLocalizationDialog()
     * Postcondiciones: Si el usuario confirma el dialogo, se cargará una nueva actividad formulario para la
     * creación del nuevo punto de localización.
     */
    public void insertLocalizationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_insert);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_create_localization_point);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.localization_point_created, Toast.LENGTH_SHORT).show();
                //Insertamos el nuevo punto de localización
                insertarMarcador(viewModel.get_longClickPosition());//TODO Por ahora lo insertamos
                saveLocalizationPoint(viewModel.get_markerToCreate());//TODO Por ahora lo guardamos
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialogDeleteRoute = alertDialogBuilder.create();
        alertDialogDeleteRoute.show();
    }
}
