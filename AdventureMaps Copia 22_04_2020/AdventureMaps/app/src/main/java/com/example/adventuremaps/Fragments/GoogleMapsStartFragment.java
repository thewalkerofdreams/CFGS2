package com.example.adventuremaps.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.ui.MainTabbet.MainTabbetActivity;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.Models.ClsMarkerWithLocalization;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Management.UtilStrings;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class GoogleMapsStartFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private MainTabbetActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos referencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
    private BitmapDrawable bitmapdraw;
    private Bitmap smallMarker;
    private ValueEventListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(getActivity() != null){//Si la actividad no es nula
            //Instanciamos el VM
            viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

            //Cargamos el fragmento inferior
            ((MainTabbetActivity)getActivity()).replaceFragment();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);//Inicializamos el mapa
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        tryChangeMarkerToDefaultImage();//Si ya existe un marcador seleccionado, restauramos su icono por defecto

        setIconToMarker(marker, String.valueOf(R.drawable.blue_marker));//Cambiamos el color del nuevo marcador seleccionado
        viewModel.set_localizationPointClicked(marker);//Almacenamos el marcador seleccionado

        if(getActivity() != null)//Si la referencia a la actividad no es nula
            (getActivity().findViewById(R.id.FrameLayout02)).setVisibility(View.VISIBLE);//Volvemos visible el fragmento inferior
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //Mostramos las coordenadas con un Toast
        String format = String.format(Locale.getDefault(), "Lat/Lng = (%f,%f)", latLng.latitude, latLng.longitude);
        Toast.makeText(getContext(), format, Toast.LENGTH_LONG).show();
        ocultarFragmentoInferior();//Ocultamos el fragmento inferior, si este no lo estuviera

        tryChangeMarkerToDefaultImage();//Si existe un marcador seleccionado, restauramos su icono por defecto
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng latLng;
        float zoom = 13;//Posicionamos el mapa en una localización y con un nivel de zoom

        if(viewModel.get_latLngToNavigate() == null){//Si no se ha especificado una localización a la que navegar
            if(viewModel.get_actualLocation() == null){//Si no podemos obtener la localización actual del usuario
                latLng = new LatLng(40.4636688, -3.7492199);//Le daremos un valor por defecto
            }else{
                latLng = new LatLng(viewModel.get_actualLocation().getLatitude(), viewModel.get_actualLocation().getLongitude());
            }
        }else{
            latLng = viewModel.get_latLngToNavigate();
            viewModel.set_latLngToNavigate(null);//Indicamos que ya se ha desplazado hacia el punto de navegación
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//Movemos la camara según los valores definidos

        //Declaramos los eventos
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Si la aplicación tiene los permisos necesarios, mostramos un dialogo de inserción
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            viewModel.set_longClickPosition(latLng);//Almacenamos la posición seleccionada en el mapa en el VM
            viewModel.insertLocalizationDialog(getActivity(), 1);//Comenzamos un dialogo de inserción
        }else{
            Toast.makeText(getActivity(), R.string.create_localization_permission_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interfaz
     * Nombre: tryChangeMarkerToDefaultImage
     * Comentario: El método cambia el icono del marcador almacenado en el atributo
     * "get_localizationPointClicked" del VM, al icono por defecto si este tiene un valor diferente de nulo.
     * Cabecera: private void tryChangeMarkerToDefaultImage()
     * Postcondiciones: Si el simbolo almacenado en el VM es diferente de null, se cambia su icono
     * por el de por defecto.
     */
    private void tryChangeMarkerToDefaultImage(){
        if(viewModel.get_localizationPointClicked() != null) {//Si existe un marcador seleccionado
            restoreIconMarker(viewModel.get_localizationPointClicked());//Restauramos el icono del marcador seleccionado
        }
    }

    /**
     * Interfaz
     * Nombre: loadLocalizationPoints
     * Comentario: Este método nos permite cargar en el mapa los puntos de localización almacenados en la plataforma.
     * Cabecera: private void loadLocalizationPoints()
     * Postcondiciones: El método carga los puntos de localización en el mapa actual.
     */
    private void loadLocalizationPoints(){
        for(int i = 0; i < viewModel.get_localizationPoints().size(); i++){
            colocarMarcador(viewModel.get_localizationPoints().get(i)); //Comenzamos a marcar las localizaciones almacenadaa
        }
    }

    /**
     * Interfaz
     * Nombre: colocarMarcador
     * Comentario: Este métdodo coloca un marcador en el mapa.
     * Cabecera: private void colocarMarcador(ClsLocalizationPoint localizationPoint)
     * Entrada:
     *  -ClsLocalizationPoint localizationPoint
     * Postcondiciones: El método coloca un marcador en el mapa.
     */
    private void colocarMarcador(ClsLocalizationPoint localizationPoint){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(localizationPoint.getLatitude(), localizationPoint.getLongitude()));//Indicamos la posición del marcador
        markerOptions.draggable(false);//Evitamos que se puedan mover los marcadores por el mapa
        if(map != null){//Si se ha cargado la referencia al mapa de inicio
            Marker marker = map.addMarker(markerOptions);//Agregamos el marcador a la UI
            adjustMarkerType(marker, localizationPoint);//Ajustamos el marcador actual
            viewModel.get_localizationPointsWithMarker().add(new ClsMarkerWithLocalization(marker, localizationPoint));//Almacenamos el Marcador en un modelo

            //Si el marcador colocado es igual al marcador seleccionado almacenado en el VM (Nos permite conservar el color del marker seleccionado cuando cambiamos de pantalla)
            if(viewModel.get_localizationPointClicked() != null && viewModel.get_localizationPointClicked().getPosition().latitude == marker.getPosition().latitude &&
                    viewModel.get_localizationPointClicked().getPosition().longitude == marker.getPosition().longitude){
                viewModel.set_localizationPointClicked(marker);//Almacenamos la referencia al nuevo marcador
                onMarkerClick(viewModel.get_localizationPointClicked());//Volvemos a seleccionarlo
            }
        }
    }

    /**
     * Interfaz
     * Nombre: adjustMarkerType
     * Comentario: Este método nos permite ajustar un marcador en específico, insertandole un icono
     * y un tag, que lo permita distindguir del resto de marcadores que no son del mismo tipo. Este
     * método es llamado desde dentro del método "colocarMarcador".
     * Cabecera: public void private(Marker marker, ClsLocalizationPoint localizationPoint)
     * Entrada:
     *  -Marker marker
     *  -ClsLocalizationPoint localizationPoint
     * Postcondciones: El método inserta un icono y un tag al marcador pasado por parámetros.
     */
    private void adjustMarkerType(Marker marker, ClsLocalizationPoint localizationPoint){
        if(viewModel.get_localizationsIdActualUser() != null && viewModel.get_localizationsIdActualUser().contains(localizationPoint.getLocalizationPointId())){//Si el usuario marcó como favorita la ruta
            setIconToMarker(marker, String.valueOf(R.drawable.marker_fav));//Le colocamos el icono al marcador
            marker.setTag("Fav");
        }else{
            if(localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización es del usuario actual
                setIconToMarker(marker, String.valueOf(R.drawable.own_location));//Le colocamos el icono al marcador
                marker.setTag("Owner");
            }else{//Si la loclización no es del usuario actual
                setIconToMarker(marker, String.valueOf(R.drawable.simple_marker));//Le colocamos el icono al marcador
                marker.setTag("NoOwner");
            }
        }
    }

    /**
     * Interfaz
     * Nombre: restoreIconMarker
     * Comentario: Este método nos permite restablecer el icono por defecto de un marcador específico.
     * Cabecera: private void restoreIconMarker(Marker marker)
     * Entrada:
     *  -Marker marker
     * Postcondiciones: El método restablece el icono por defecto de un macador.
     */
    private void restoreIconMarker(Marker marker){
        if(marker != null && marker.getTag() != null){
            switch (marker.getTag().toString()){
                case "Fav":
                    setIconToMarker(marker, String.valueOf(R.drawable.marker_fav));//Le colocamos el icono al marcador
                    break;
                case "Owner":
                    setIconToMarker(marker, String.valueOf(R.drawable.own_location));//Le colocamos el icono al marcador
                    break;
                case "NoOwner":
                    setIconToMarker(marker, String.valueOf(R.drawable.simple_marker));//Le colocamos el icono al marcador
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Read from the database
        listener = localizationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getContext() != null){//Si se encuentra en el contexto actual
                    viewModel.get_localizationPoints().clear();//Limpiamos la lista de rutas
                    cleanAllLocalizations();
                    viewModel.get_localizationPointsWithMarker().clear();//Limpiamos la lista de puntos de localización que contienen los marcadores
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);

                        if(localizationPoint != null && ((localizationPoint.isShared() || (localizationPoint.getEmailCreator() != null && localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser()))))){ //Si la localización esta compartida o es del usuario actual

                            ArrayList<String> actualTypes = new ArrayList<>();
                            for(DataSnapshot types : datas.child("types").getChildren()){
                                actualTypes.add(String.valueOf(types.getValue()));//Almacenamos los tipos de la localización actual
                            }

                            if(UtilStrings.arraysWithSameData(actualTypes, viewModel.get_checkedFilters())){//Si cumple los filtros
                                viewModel.get_localizationPoints().add(localizationPoint);
                            }
                        }
                    }
                    storeFavouriteLocalizationsId();//Almacenamos las localizaciones favoritas del usuario, para luego mostrarlas
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Interfaz
     * Nombre: storeFavouriteLocalizationsId
     * Comentario: El método almacena las id's de las localizaciones favoritas del usuario actual en el VM.
     * Cabecera: private void storeFavouriteLocalizationsId()
     * Postcondiciones: El método almacena las id's de las localizaciones favoritas del usuario actual en el VM.
     */
    private void storeFavouriteLocalizationsId(){
        userReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cleanAllLocalizations();
                viewModel.set_localizationsIdActualUser(new ArrayList<String>());//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("localizationsId").getChildren()){//Almacenamos las id's de las localizaciones favoritas del usuario
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdActualUser().add(localizationId);
                    }
                }
                loadLocalizationPoints();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        localizationReference.removeEventListener(listener);//Eliminamos el evento unido a la referencia de las localizaciones
    }

    /**
     * Interfaz
     * Nombre: cleanAllLocalizations
     * Comentario: Este método nos permite limpiar todos los marcadores sobre el mapa.
     * Cabecera: private void cleanAllLocalizations()
     * Postcondiciones: El método elimina todos los marcadores que se encuentran sobre
     * el mapa actual.
     */
    private void cleanAllLocalizations(){
        for(int i = 0; i < viewModel.get_localizationPointsWithMarker().size(); i++){
            viewModel.get_localizationPointsWithMarker().get(i).getMarker().remove();
        }
    }

    /**
     * Interfaz
     * Nombre: ocultarFragmentoInferior
     * Comentario: Este método nos permite ocultar el fragmento inferior del fragmento actual.
     * Cabecera: private void ocultarFragmentoInferior()
     * Postcondiciones: El método oculta el fragmento inferior del fragmento actual.
     */
    private void ocultarFragmentoInferior(){
        if(getActivity() != null)
        (getActivity().findViewById(R.id.FrameLayout02)).setVisibility(View.GONE);//Volvemos invisible el fragmento inferior
    }

    /**
     * Interfaz
     * Nombre: setIconToMarker
     * Comentario: Este método nos permite insertar un icono en un marcador.
     * Cabecera: private void setIconToMarker(String addressIcon)
     * Entrada:
     *  -String addressIcon
     * Precondiciones:
     *  -addressIcon debe apuntar a una imagen existente.
     * Postcondiciones: El método agrega un icono a un marcador.
     */
    private void setIconToMarker(final Marker marker, final String addressIcon){
        getActivity().runOnUiThread(new Runnable() {//TODO el icono de inserta en un hilo secundario
            public void run() {
                bitmapdraw = (BitmapDrawable) getContext().getResources().getDrawable(Integer.valueOf(addressIcon));
                smallMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), ApplicationConstants.MARKER_WITH_SIZE, ApplicationConstants.MARKER_HEIGHT_SIZE, false);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
        });
    }
}