package com.example.adventuremaps.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.CreateLocalizationPointActivity;
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
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos eferencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
    private BitmapDrawable bitmapdraw;
    private Bitmap b, smallMarker;

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
        if(viewModel.get_localizationPointClicked() != null) {//Si ya existe un marcador seleccionado
            restoreIcomMarker(viewModel.get_localizationPointClicked());//Restablecemos el icono por defecto del marcador seleccionado
        }
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));//Cambiamos el color del nuevo marcador seleccionado
        viewModel.set_localizationPointClicked(marker);//Almacenamos el marcador seleccionado

        if(getActivity() != null)//Por si no le diera tiempo a la actividad
            (getActivity().findViewById(R.id.FrameLayout02)).setVisibility(View.VISIBLE);//Volvemos invisible el fragmento inferior
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //Mostramos las coordenadas con un Toast
        String format = String.format(Locale.getDefault(), "Lat/Lng = (%f,%f)", latLng.latitude, latLng.longitude);
        Toast.makeText(getContext(), format, Toast.LENGTH_LONG).show();
        ocultarFragmentoInferior();//Ocultamos el fragmento inferior, si este no lo estuviera

        if(viewModel.get_localizationPointClicked() != null) {//Si existe un marcador seleccionado, cambiamos su icono
            //setIconToMarker(viewModel.get_localizationPointClicked(), String.valueOf(R.drawable.simple_marker));
            restoreIcomMarker(viewModel.get_localizationPointClicked());//Restauramos el icono del marcador seleccionado
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng latLng;
        float zoom = 13;//Posicionamos el mapa en una localización y con un nivel de zoom

        if(viewModel.get_latLngToNavigate() == null){//Si no se ha especificado una localización a la que navegar
            if(viewModel.get_actualLocation() == null){//Si no podemos obtener la localización actusal del usuario
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
     * Nombre: restoreIcomMarker
     * Comentario: Este método nos permite restablecer el icono por defecto de un marcador específico.
     * Cabecera: private void restoreIcomMarker(Marker marker)
     * Entrada:
     *  -Marker marker
     * Postcondiciones: El método restablece el icono por defecto de un macador.
     */
    private void restoreIcomMarker(Marker marker){
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

    /**
     * Interfaz
     * Nombre: insertarMarcador
     * Comentario: Este método nos permite insertar un marcador en el mapa.
     * Además guarda ese marcador en el VM MainTabbetActicityVM.
     * Cabecera: public void insertarMarcador(LatLng latLng)
     * Entrada:
     *   -LatLng latLng
     * Postcondiciones: El método inserta un marcador en el mapa. Además se guarda
     * ese marcador en el VM MainTabbetActicityVM.
     * */
    public void insertarMarcador(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);//Indicamos la posición del marcador
        markerOptions.draggable(false);//Evitamos que se puedan mover los marcadores por el mapa
        Marker marker = map.addMarker(markerOptions);//Agregamos el marcador a la UI
        setIconToMarker(marker, String.valueOf(R.drawable.simple_marker));//Le colocamos el icono al marcador
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
        //Almacenamos el punto de localización en la plataforma
        FirebaseDatabase.getInstance().getReference("Localizations").
                child(viewModel.get_localizationToSave().getLocalizationPointId())
                .setValue(viewModel.get_localizationToSave());
        viewModel.get_localizationPointsWithMarker().add(new ClsMarkerWithLocalization(marker, viewModel.get_localizationToSave()));//Almacenamos el marcador en una clase modelo

        //Almacenamos los tipos del punto de localización
        String typeId;
        for(int i = 0; i < viewModel.get_localizationTypesToSave().size(); i++){
            typeId = localizationReference.push().getKey();
            FirebaseDatabase.getInstance().getReference("Localizations").
                    child(viewModel.get_localizationToSave().getLocalizationPointId()).child("types")
                    .child(typeId).setValue(viewModel.get_localizationTypesToSave().get(i));
        }
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
                viewModel.get_localizationPointsWithMarker().clear();//Limpiamos la lista de puntos de localización que contienen los marcadores
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);

                    ArrayList<String> actualTypes = new ArrayList<>();
                    for(DataSnapshot types : datas.child("types").getChildren()){
                        actualTypes.add(String.valueOf(types.getValue()));//Almacenamos los tipos de la localización actual
                    }

                    if((localizationPoint.isShared() || localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser()))
                    && UtilStrings.arraysWithSameData(actualTypes, viewModel.get_checkedFilters()))//Si la localización esta compartida o es del usuario actual y si cumple los filtros
                        viewModel.get_localizationPoints().add(localizationPoint);
                }
                loadLocalizationPoints();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        userReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.set_localizationsIdFavourites(new ArrayList<String>());//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("localizationsId").getChildren()){//Almacenamos las id de la puntos de localización favoritos
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdFavourites().add(localizationId);
                    }
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
                Intent intent = new Intent(getActivity(), CreateLocalizationPointActivity.class);
                intent.putExtra("ActualEmailUser", viewModel.get_actualEmailUser());
                intent.putExtra("ActualLatitude", viewModel.get_longClickPosition().latitude);
                intent.putExtra("ActualLongitude", viewModel.get_longClickPosition().longitude);
                startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_CREATE_LOCALIZATION_POINT);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ApplicationConstants.REQUEST_CODE_CREATE_LOCALIZATION_POINT) {
            if (resultCode == Activity.RESULT_OK) {//Si se confirmó el guardado del nuevo punto de localización
                viewModel.set_localizationToSave((ClsLocalizationPoint)data.getExtras().getSerializable("LocalizationToSave"));//Guardamos la localización en el VM
                viewModel.set_localizationTypesToSave((ArrayList<String>)data.getSerializableExtra("LocalizationTypesToSave"));//Obtenemos los tipos de la localización

                insertarMarcador(viewModel.get_longClickPosition());//Insertamos el marcador en el mapa actual
                saveLocalizationPoint(viewModel.get_markerToCreate());//Almacenamos el punto de localización en FireBase
                Toast.makeText(getActivity(), R.string.localization_point_created, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Interfaz
     * Nombre: ocultarFragmentoInferior
     * Comentario: Este método nos permite ocultar el fragmento inferior del fragmento actual.
     * Cabecera: public void ocultarFragmentoInferior()
     * Postcondiciones: El método oculta el fragmento inferior del fragmento actual.
     */
    public void ocultarFragmentoInferior(){
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
    private void setIconToMarker(Marker marker, String addressIcon){
        //Ajustamos el tamaño que tendrá el marcador
        int height = 120;
        int width = 120;
        bitmapdraw = (BitmapDrawable) this.getResources().getDrawable(Integer.valueOf(addressIcon));
        b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
    }
}
