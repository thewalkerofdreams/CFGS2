package com.example.adventuremaps.Fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.Models.ClsMarkerWithPriority;
import com.example.adventuremaps.FireBaseEntities.ClsRoutePoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.RouteActivitiesVM;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

public class GoogleMapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap map;
    private RouteActivitiesVM viewModel;
    private Polyline polyline;//No la convertimos en local para que podamos limpiar el mapa en caso de eliminación de un marcador

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(RouteActivitiesVM.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);//Inicializamos el mapa
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        viewModel.eliminarMarcador(marker);//Eliminamos el marcador del VM
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


        if(viewModel.get_actualLocation() == null){//Si no podemos obtener la localización actusal del usuario
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

    /**
     * Interfaz
     * Nombre: moveCamera
     * Comentario: Este método nos permite modificar la posición de la camara sobre el mapa.
     * Cabecera: public void moveCamera(LatLng latlng)
     * Entrada:
     *  -LatLng latlng
     * Postcondiciones: El método modifica la posición de la camara sobre el mapa del fragmento.
     */
    public void moveCamera(LatLng latlng, float zoom){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    /**
     * Interfaz
     * Nombre: colocarMarcador
     * Comentario: Este método nos permite colocar un marcador en el mapa.
     * Además el método almacena ese marcador en la lista de localizaciones
     * del VM. Si la latitud y longitud coincide con la última marca registrada,
     * no se realizará el marcado.
     * Cabecera: public void colocarMarcador(LatLng latLng)
     * Entrada:
     *   -LatLng latLng
     * Postcondiciones: El método coloca un marcador en el mapa, excepto si
     * la latitud y longitud coincide con la última marca registrada.
     * */
    public void colocarMarcador(LatLng latLng){
        if(!sameLastMarker(latLng)){//De esta forma evitamos crear marcadores innecesarios
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);//Indicamos la posición del marcador
            markerOptions.draggable(true);//Permite que podamos mover elmarcador por el mapa, en este caso, lo utilizamos para hacer un marcado largo
            Marker marker = map.addMarker(markerOptions);//Agregamos el marcador a la UI
            setIconToMarker(marker);//Le agregamos el icono de punto de ruta

            //Almacenaremos un ClsMarkerWithPriority en la lista de puntos de localización del VM
            ClsMarkerWithPriority markerWithPriority = new ClsMarkerWithPriority(marker, viewModel.getLastPositionNumber()+1);
            viewModel.get_localizationPoints().add(markerWithPriority);

            pintarLineas(viewModel.get_localizationPoints());//Volvemos a pintar las líneas de la ruta
        }
    }

    /**
     * Interfaz
     * Nombre: sameLastMarker
     * Comentario: Este método nos permite verificar su una posición (Latitud y longitud) coinciden
     * con el último marcador insertado en el mapa del fragmento.
     * Cabecera: private boolean sameLastMarker(LatLng latLng)
     * Entrada:
     *  -LatLng latLng
     * Salida:
     *  -boolean samePosition
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si la posición
     * coincide con el último marcador insertado en el mapa o false en caso contrario. Si el mapa
     * no contiene ningún marcador también devolverá false.
     */
    private boolean sameLastMarker(LatLng latLng) {
        boolean samePosition = false;
        LatLng lastPositionMarked;

        if(viewModel.get_localizationPoints().size() > 0){
            lastPositionMarked = viewModel.get_localizationPoints().get(viewModel.get_localizationPoints().size() -1).getMarker().getPosition();
            if(lastPositionMarked.latitude == latLng.latitude || lastPositionMarked.longitude == latLng.longitude){
                samePosition = true;
            }
        }

        return samePosition;
    }

    /**
     * Interfaz
     * Nombre: pintarLineas
     * Comentario: Este método nos permite pintar las lineas entre las
     * diferentes localizaciones almacenadas para la ruta.
     * Cabecera: private void pintarLineas(ArrayList<ClsMarkerWithPriority> localizaciones)
     * Entrada:
     *   -ArrayList<ClsMarkerWithPriority> localizaciones
     * Postcondiciones: El método pinta lineas entre los diferentes puntos de localización.
     * */
    private void pintarLineas(ArrayList<ClsMarkerWithPriority> localizaciones) {
        PolylineOptions opcionesPoliLinea = new PolylineOptions();
        for(int i = 0; i < localizaciones.size(); i++){//Indicamos los puntos por los que va a pasar la línea
            opcionesPoliLinea.add(new LatLng(localizaciones.get(i).getMarker().getPosition().latitude,
                    localizaciones.get(i).getMarker().getPosition().longitude));
        }

        if (polyline != null)
            polyline.remove();//Removemos la polilínea del mapa

        polyline = map.addPolyline(opcionesPoliLinea);
        polyline.setColor(Color.RED);//Indicamos el color de la línea
    }

    /**
     * Interfaz
     * Nombre: setIconToMarker
     * Comentario: Este método nos permite insertar el icono de punto de ruta en un marcador.
     * Cabecera: private void setIconToMarker(final Marker marker)
     * Entrada:
     *  -final Marker marker
     * Postcondiciones: El método agrega el icono de punto de ruta a un marcador.
     */
    private void setIconToMarker(final Marker marker){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.route_point);
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), ApplicationConstants.MARKER_WITH_SIZE, ApplicationConstants.MARKER_HEIGHT_SIZE, false);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
        });
    }
}
