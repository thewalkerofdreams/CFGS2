package com.example.adventuremaps.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.FireBaseEntities.ClsRoutePoint;
import com.example.adventuremaps.ViewModels.CreateRouteActivityVM;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

public class GoogleMapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap map;
    private CreateRouteActivityVM viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(CreateRouteActivityVM.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);//Inicializamos el mapa
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        viewModel.eliminarMarcador(marker);//Eliminamos el marcador
        marker.remove();//Lo eliminamos de la UI
        pintarLineas(viewModel.get_localizationPoints());//Volvemos a pintar las líneas de la ruta
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //Mientras se esta moviendo el marcador
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Al finalizar el moviento del marcador
    }

    /*
     * Interfaz
     * Nombre: funcionActualizarMapa
     * Comanterio: Esta función nos permite actualizar el mapa.
     * Cabecera: public void funcionEngancheMapa()
     * Postcondiciones: El método modifica la localización del mapa en el fragmento.
     * */
    public void funcionEngancheMapa() {
        getMapAsync(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //Mostramos las coordenadas con un Toast
        String format = String.format(Locale.getDefault(), "Lat/Lng = (%f,%f)", latLng.latitude, latLng.longitude);
        Toast.makeText(getContext(), format, Toast.LENGTH_LONG).show();

        //Almacenamos la última localización clicada
        viewModel.set_lastLocalizationClicked(new ClsRoutePoint(latLng.latitude, latLng.longitude));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng latLng;
        //Posicionamos el mapa en una localización y con un nivel de zoom
        float zoom;
        if(viewModel.get_actualLocation() == null){
            latLng = new LatLng(40.4636688, -3.7492199);//Le daremos un valor por defecto
            zoom = 13;
        }else{
            latLng = new LatLng(viewModel.get_actualLocation().getLatitude(), viewModel.get_actualLocation().getLongitude());
            zoom = viewModel.get_zoom();
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//Movemos la camara según los valores definidos

        //Declaramos los eventos
        map.setOnMapClickListener(this);
        map.setOnMarkerDragListener(this);
    }

    /*
     * Interfaz
     * Nombre: colocarMarcador
     * Comentario: Este método nos permite colocar un marcador en el mapa.
     * Cabecera: public void colocarMarcador(LatLng latLng)
     * Entrada:
     *   -LatLng latLng
     * Postcondiciones: El método coloca un marcador en el mapa.
     * */
    public void colocarMarcador(LatLng latLng, ArrayList<ClsRoutePoint> localizaciones){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);//Indicamos la posición del marcador
        markerOptions.draggable(true);//Permite que podamos mover elmarcador por el mapa, en este caso, lo utilizamos para hacer un marcado largo
        map.addMarker(markerOptions);//Agregamos el marcador a la UI

        viewModel.almacenarUltimaLocalizacion();//Almacenamos la última localización en el  VM
        pintarLineas(localizaciones);//Volvemos a pintar las líneas de la ruta
    }

    /*
     * Interfaz
     * Nombre: pintarLineas
     * Comentario: Este método nos permite pintar las lineas entre las
     * diferentes localizaciones almacenadas para la ruta.
     * Cabecera: public void pintarLineas(ArrayList<LocalizationPoint> localizaciones)
     * Entrada:
     *   -ArrayList<LocalizationPoint> localizaciones
     * Postcondiciones: El método pinta lineas entre los diferentes puntos de localización.
     * */
    public void pintarLineas(ArrayList<ClsRoutePoint> localizaciones) {
        PolylineOptions opcionesPoliLinea = new PolylineOptions();
        for(int i = 0; i < localizaciones.size(); i++){
            opcionesPoliLinea.add(new LatLng(localizaciones.get(i).getLatitude(), localizaciones.get(i).getLongitude()));
        }

        opcionesPoliLinea.color(Color.BLUE);
        Polyline polyline = map.addPolyline(opcionesPoliLinea);
        polyline.setColor(Color.RED);//Indicamos el color de la línea
    }

}
