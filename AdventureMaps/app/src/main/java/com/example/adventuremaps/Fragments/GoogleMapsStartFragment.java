package com.example.adventuremaps.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Management.UtilStrings;
import com.example.adventuremaps.Models.MyClusterItem;
import com.example.adventuremaps.Models.MyClusterRenderer;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Locale;

public class GoogleMapsStartFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private MainTabbetActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);//Tomamos referencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_USERS_ADDRESS);
    private ValueEventListener listener;
    //ClusterItem
    private ClusterManager<MyClusterItem> mClusterManager;
    private MyClusterRenderer myClusterRenderer;//Nos permite modificar características especiales de los items del cluster
    //For GPS
    private LocationManager manager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(getActivity() != null){//Si la actividad no es nula
            //Instanciamos el VM
            viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

            //Cargamos el fragmento inferior
            replaceFragment();

            //Instanciamos la variable LocationManager
            manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        tryDesmarkMarker();//Si ya existe un marcador seleccionado lo deseleccionamos

        setIconToMarker(marker, String.valueOf(R.mipmap.blue_marker));//Cambiamos el color del nuevo marcador seleccionado
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

        tryDesmarkMarker();//Si ya existe un marcador seleccionado lo deseleccionamos
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;
        LatLng latLng;

        if(viewModel.get_latLngToNavigate() == null){//Si no se ha especificado una localización a la que navegar
            if(viewModel.get_actualLocation() == null || (manager != null && !manager.isProviderEnabled( LocationManager.GPS_PROVIDER))){//Si no podemos obtener la localización actual del usuario
                latLng = new LatLng(ApplicationConstants.SEVILLE_LATITUDE, ApplicationConstants.SEVILLE_LONGITUDE);//Le daremos un valor por defecto
            }else{
                latLng = new LatLng(viewModel.get_actualLocation().getLatitude(), viewModel.get_actualLocation().getLongitude());
            }
        }else{
            latLng = viewModel.get_latLngToNavigate();
            viewModel.set_latLngToNavigate(null);//Indicamos que ya se ha desplazado hacia el punto de navegación
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ApplicationConstants.DEFAULT_LEVEL_ZOOM));//Movemos la camara según los valores definidos
        //Si la aplicación tiene los permisos necesarios de localización, añadimos el botón de centrar la cámara en la posición actual del usuario y señalamos a la persona en el mapa con un icono
        if(getActivity() != null && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);//Nos permite indicar donde se encuentra el usuario actual, además activa el botón para centrar la cámara
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {//Modificamos la acción del botón de centrar localización
                @Override
                public boolean onMyLocationButtonClick() {
                    if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {//Si el gps se encuentra activado en el dispositivo
                        viewModel.reloadActualLocalization();//Recargamos la localización actual del usuario
                        //Centramos la cámara
                        map.setPadding(0, 0, 0,0);//Deshabilitamos un momento el padding para centrar la cámara
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(viewModel.get_actualLocation().getLatitude(), viewModel.get_actualLocation().getLongitude()), ApplicationConstants.DEFAULT_LEVEL_ZOOM));
                        adjustPaddingMap(map);//Volvemos a habilitar el padding para la brújula
                    }

                    return true;//Con esto indicamos que no se mueva la cámara por defecto
                }
            });
            map.getUiSettings().setCompassEnabled(true);//Insertamos la brújula en el mapa
            adjustPaddingMap(map);//Modificamos la posición de los elementos que pertenecen al mapa
        }

        //Inicializamos el cluster manager
        if(getActivity() != null){
            mClusterManager = new ClusterManager<>(getActivity(), this.map);
            //Utilizamos un render personalizado para cambiar el icono por defecto de los marcadores
            myClusterRenderer = new MyClusterRenderer(getActivity(), this.map, mClusterManager);
            mClusterManager.setRenderer(myClusterRenderer);
        }

        //Declaramos los eventos
        final CameraPosition[] mPreviousCameraPosition = {null};
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {//Modificamos el evento de cambio de camara
            @Override
            public void onCameraIdle() {//Sobreescribimos el método de cambio de cámara sobre el mapa
                CameraPosition position = map.getCameraPosition();
                if(mPreviousCameraPosition[0] == null || mPreviousCameraPosition[0].zoom != position.zoom) {//Si el zoom de la cámara cambia
                    mPreviousCameraPosition[0] = map.getCameraPosition();
                    mClusterManager.cluster();//Volvemos a cargar el cluster
                    ocultarFragmentoInferior();//Ocultamos el fragmento inferior
                    if(!tryChangeSelectedItem()){//Desmarcamos el marker que se encontraba seleccionado
                        tryChangeMarkerToDefaultImage();//Si existe un marcador seleccionado, restauramos su icono por defecto
                    }
                }
            }
        });

        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Si la aplicación tiene los permisos necesarios, mostramos un dialogo de inserción
        if(getActivity() != null && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            viewModel.set_longClickPosition(latLng);//Almacenamos la posición seleccionada en el mapa en el VM
            viewModel.insertLocalizationDialog(getActivity(), 1);//Comenzamos un dialogo de inserción
        }else{
            Toast.makeText(getActivity(), R.string.create_localization_permission_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interfaz
     * Nombre: adjustPaddingMap
     * Comentario: El método ajusta el padding del mapa, según el tamaño del dispositivo actual.
     * Cabecera: private void adjustPaddingMap(GoogleMap map)
     * Entrada:
     *  -GoogleMap map
     * Postcondiciones: El método ajusta el padding del mapa, según el tamaño del dispositivo actual.
     */
    private void adjustPaddingMap(GoogleMap map){
        int googleMapPadding;
        TypedValue tv = new TypedValue();
        if(getActivity() != null){
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))//Si existe el tipo especificado en el tema actual
            {
                googleMapPadding = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());//Calculamos el padding según el tamaño de la pantalla
            }else{
                googleMapPadding = ApplicationConstants.DEFAULT_RIGHT_PADDING_MAP_BUT_NO_ACTIONBARSIZE_FOUND;//Le damos un padding por defecto
            }
            //Modificamos el padding del mapa
            map.setPadding(getActivity().getWindowManager().getDefaultDisplay().getWidth() - (googleMapPadding * 2 + ApplicationConstants.DEFAULT_RIGHT_MARGIN_MAP), 0, 0, 0);
        }
    }

    /**
     * Interfaz
     * Nombre: tryDesmarkMarker
     * Comentario: El método descarca el marcador seleccionado en el mapa, si existe uno.
     * Cabecera: private void tryDesmarkMarker()
     * Postcondiciones: Si existe un marcador seleccionado en el mapa, el método lo deselecciona.
     */
    private void tryDesmarkMarker(){
        if(!tryChangeSelectedItem()){//Si no había un item seleccionado del cluster (Si lo había se restaura su icono por defecto)
            tryChangeMarkerToDefaultImage();//Si ya existe un marcador seleccionado, restauramos su icono por defecto
        }
    }

    /**
     * Interfaz
     * Nombre: tryChangeSelectedItem
     * Comentario: El método modifica el marcador seleccionado sobre el mapa, cambiando su icono a por
     * el de por defecto. Este método es necesario cuando se ha vuelto de la pantalla de detalles de
     * la localización seleccionada, permitiendonos cambiar el icono del item del cluster lo que también
     * modificará el icono del marcador.
     * Cabecera: private boolean tryChangeSelectedItem(Marker marker)
     * Entrada:
     *  -Marker marker
     * Postcondiciones: El método cambia el icono del marcador al de por defecto.
     */
    private boolean tryChangeSelectedItem(){
        boolean changed = false;

        //Si existe un item seleccionado en el cluster
        if(viewModel.get_itemSelected() != null){
            viewModel.get_itemSelected().setItemSelected(false);//Indicamos que el item ya no se encuentra seleccionado
            restoreIconMarker(myClusterRenderer.getMarker(viewModel.get_itemSelected()));//Restauramos el icono del marcador

            viewModel.set_itemSelected(null);//Indicamos que ya no existe un item seleccionado
            mClusterManager.cluster();//Actualizamos el cluster

            changed = true;//Indicamos que se cambió el item del cluster por otro nuevo
        }

        return changed;
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
        mClusterManager.clearItems();
        for(int i = 0; i < viewModel.get_localizationPoints().size(); i++){
            colocarMarcador(viewModel.get_localizationPoints().get(i)); //Comenzamos a marcar las localizaciones almacenadaa
        }
        mClusterManager.cluster();
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
        String tag = getTagLocalization(localizationPoint);//Obtenemos el tag del punto de localización
        //Si el marcador ya se encontraba seleccionado
        if(viewModel.get_localizationPointClicked() != null && viewModel.get_localizationPointClicked().getPosition().latitude == localizationPoint.getLatitude() &&
                viewModel.get_localizationPointClicked().getPosition().longitude == localizationPoint.getLongitude()){

            MyClusterItem item = new MyClusterItem(localizationPoint.getLatitude(), localizationPoint.getLongitude(), tag, true);
            mClusterManager.addItem(item);

            viewModel.set_itemSelected(item);//Almacenamos el item en el VM
        }else{
            MyClusterItem item = new MyClusterItem(localizationPoint.getLatitude(), localizationPoint.getLongitude(), tag, false);
            mClusterManager.addItem(item);
        }
    }

    /**
     * Interfaz
     * Nombre: getTagLocalization
     * Comentario: El método obtiene un tag para una localización determinada.
     * Cabecera: private String getTagLocalization(ClsLocalizationPoint localizationPoint)
     * Entrada:
     *  -ClsLocalizationPoint localizationPoint
     * Salida:
     *  -String tag
     * Postcondiciones: El método devuelve un tag que será utilizado para insertar un punto de
     * localización en el mapa.
     */
    private String getTagLocalization(ClsLocalizationPoint localizationPoint){
        String tag = "";

        if(viewModel.get_localizationsIdActualUser() != null && viewModel.get_localizationsIdActualUser().contains(localizationPoint.getLocalizationPointId())){//Si el usuario marcó como favorita la localización
            tag = ApplicationConstants.TAG_LOCATION_FAV_TYPE;
        }else{
            if(localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización es del usuario actual
                tag = ApplicationConstants.TAG_LOCATION_OWNER_TYPE;
            }else{//Si la loclización no es del usuario actual
                tag = ApplicationConstants.TAG_LOCATION_NO_OWNER_TYPE;
            }
        }

        return tag;
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
        if(marker != null && marker.getTag() != null && getContext() != null){
            BitmapDrawable bitmapDrawable = null;
            switch (marker.getTag().toString()){
                case "Fav":
                    bitmapDrawable = (BitmapDrawable) getContext().getResources().getDrawable(R.mipmap.marker_fav);//Le colocamos el icono al marcador
                    //setIconToMarker(marker, String.valueOf(R.drawable.marker_fav));//Le colocamos el icono al marcador
                    break;
                case "Owner":
                    bitmapDrawable = (BitmapDrawable) getContext().getResources().getDrawable(R.mipmap.own_location);//Le colocamos el icono al marcador
                    //setIconToMarker(marker, String.valueOf(R.drawable.own_location));//Le colocamos el icono al marcador
                    break;
                case "NoOwner":
                    bitmapDrawable = (BitmapDrawable) getContext().getResources().getDrawable(R.mipmap.simple_marker);//Le colocamos el icono al marcador
                    //setIconToMarker(marker, String.valueOf(R.drawable.simple_marker));//Le colocamos el icono al marcador
                    break;
            }
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(bitmapDrawable.getBitmap())));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        storeLocalizationPointsToShow();//Almacenamos los puntos de localización a mostrar
    }

    @Override
    public void onStop() {
        super.onStop();
        localizationReference.removeEventListener(listener);//Eliminamos el evento unido a la referencia de las localizaciones
    }

    /**
     * Interfaz
     * Nombre: storeLocalizationPointsToShow
     * Comentario: El método almacena los puntos de localización que se van mostrar en el mapa
     * en el VM. Luego llama a la función "storeFavouriteLocalizationsId" para almacenar las id's de
     * las localizaciones favoritas del usuario, para luego mostrarlas con el icono de favorito.
     * Cabecera: private void storeLocalizationPointsToShow()
     * Postcondiciones: El método almacena los puntos de localización que se van a mostrar en el VM.
     */
    private void storeLocalizationPointsToShow(){
        // Read from the database
        listener = localizationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext() != null){//Si se encuentra en el contexto actual
                    viewModel.get_localizationPoints().clear();//Limpiamos la lista de puntos de localización
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);

                        if(localizationPoint != null && ((localizationPoint.isShared() || (localizationPoint.getEmailCreator() != null && localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser()))))){ //Si la localización esta compartida o es del usuario actual

                            ArrayList<String> actualTypes = new ArrayList<>();
                            for(DataSnapshot types : datas.child(ApplicationConstants.FB_LOCALIZATION_TYPES_CHILD).getChildren()){
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
        userReference.orderByChild(ApplicationConstants.FB_USER_EMAIL_CHILD).equalTo(viewModel.get_actualEmailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                viewModel.set_localizationsIdActualUser(new ArrayList<String>());//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child(ApplicationConstants.FB_LOCALIZATIONS_ID).getChildren()){//Almacenamos las id's de las localizaciones favoritas del usuario
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdActualUser().add(localizationId);
                    }
                }
                loadLocalizationPoints();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        if(getContext() != null){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) getContext().getResources().getDrawable(Integer.valueOf(addressIcon));//Le colocamos el icono al marcador
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(bitmapDrawable.getBitmap())));
        }
    }

    //Método para cargar el fragmento inferior
    /**
     * Interfaz
     * Nombre: replaceFragment
     * Comentario: Este método nos permite remplazar el contenido de nuestro
     * FrameLayout "FrameLayout02" por el fragmento "FragmentStartLocalizationPointClick".
     * Cabecera: private void replaceFragment()
     * Postcondiciones: El método reemplaza el contenido del FrameLayout por el fragmento FragmentStartLocalizationPointClick.
     * */
    private void replaceFragment(){
        FragmentTransaction transation = getFragmentManager().beginTransaction();
        transation.replace(R.id.FrameLayout02, new FragmentStartLocalizationPointClick());
        transation.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        transation.commit();
    }
}
