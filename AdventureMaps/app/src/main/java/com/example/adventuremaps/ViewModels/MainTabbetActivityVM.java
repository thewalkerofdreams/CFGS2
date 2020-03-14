package com.example.adventuremaps.ViewModels;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;

import com.example.adventuremaps.Activities.MainTabbetActivity;
import com.example.adventuremaps.Activities.Models.ClsMarkerWithLocalization;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainTabbetActivityVM extends AndroidViewModel {

    private String _actualEmailUser;

    //Fragment Offline Maps Part
    //JSON encoding/decoding
    private final String JSON_CHARSET = "UTF-8";
    private final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";
    private LocationManager _locManager; //This class provides access to the system location services
    private Location _actualLocation;
    private Context _context;
    private int _regionSelected;

    //Fragment Routes
    private boolean _dialogDeleteRouteShowing;
    private ArrayList<ClsRoute> _itemsRouteList;
    private ArrayList<ClsRoute> _selectedRoutes;

    //Fragment Start
    private LatLng _longClickPosition;//Para crear un nuevo punto de localización
    private Marker _markerToCreate;
    private ArrayList<ClsLocalizationPoint> _localizationPoints;//Los puntos de localización que obtendremos de la plataforma FireBase
    private ArrayList<ClsMarkerWithLocalization> _localizationPointsWithMarker;//A cada punto de localización le asignaremos un Marker para evitar errores de posicionamiento
    private Marker _localizationPointClicked;//Obtendremos el marcador de un punto de localización clicado

    public MainTabbetActivityVM(Application application){
        super(application);
        _actualEmailUser = "";

        //Fragment Offline Maps Part
        _context = application.getBaseContext();
        _locManager = (LocationManager)_context.getApplicationContext().getSystemService(_context.LOCATION_SERVICE);
        _actualLocation = getLastKnownLocation();
        _regionSelected = 0;

        //Fragment Routes
        _dialogDeleteRouteShowing = false;
        _itemsRouteList = new ArrayList<>();
        _selectedRoutes = new ArrayList<>();

        //Fragment Start
        _longClickPosition = null;
        _markerToCreate = null;
        _localizationPointsWithMarker = new ArrayList<>();
        _localizationPoints = new ArrayList<>();
        _localizationPointClicked = null;
    }

    //Get y Set
    public String get_actualEmailUser() {
        return _actualEmailUser;
    }

    public void set_actualEmailUser(String _actualEmailUser) {
        this._actualEmailUser = _actualEmailUser;
    }

    //Gets y Sets Fragment Offline Maps Part
    public Location get_actualLocation() {
        return _actualLocation;
    }

    public void set_actualLocation(Location _actualLocation) {
        this._actualLocation = _actualLocation;
    }

    public int get_regionSelected() {
        return _regionSelected;
    }

    public void set_regionSelected(int _regionSelected) {
        this._regionSelected = _regionSelected;
    }

    public String getJsonCharset() {
        return JSON_CHARSET;
    }

    public String getJsonFieldRegionName() {
        return JSON_FIELD_REGION_NAME;
    }

    //Gets y Sets Fragment Routes
    public boolean is_dialogDeleteRouteShowing() {
        return _dialogDeleteRouteShowing;
    }

    public void set_dialogDeleteRouteShowing(boolean _dialogDeleteRouteShowing) {
        this._dialogDeleteRouteShowing = _dialogDeleteRouteShowing;
    }

    public ArrayList<ClsRoute> get_itemsRouteList() {
        return _itemsRouteList;
    }

    public void set_itemsRouteList(ArrayList<ClsRoute> _itemsRouteList) {
        this._itemsRouteList = _itemsRouteList;
    }

    public ArrayList<ClsRoute> get_selectedRoutes() {
        return _selectedRoutes;
    }

    public void set_selectedRoutes(ArrayList<ClsRoute> _selectedRoutes) {
        this._selectedRoutes = _selectedRoutes;
    }

    //Gets y Sets Fragment Start
    public LatLng get_longClickPosition() {
        return _longClickPosition;
    }

    public void set_longClickPosition(LatLng _longClickPosition) {
        this._longClickPosition = _longClickPosition;
    }

    public Marker get_markerToCreate() {
        return _markerToCreate;
    }

    public void set_markerToCreate(Marker _markerToCreate) {
        this._markerToCreate = _markerToCreate;
    }

    public ArrayList<ClsLocalizationPoint> get_localizationPoints() {
        return _localizationPoints;
    }

    public void set_localizationPoints(ArrayList<ClsLocalizationPoint> _localizationPoints) {
        this._localizationPoints = _localizationPoints;
    }

    public ArrayList<ClsMarkerWithLocalization> get_localizationPointsWithMarker() {
        return _localizationPointsWithMarker;
    }

    public void set_localizationPointsWithMarker(ArrayList<ClsMarkerWithLocalization> _localizationPointsWithMarker) {
        this._localizationPointsWithMarker = _localizationPointsWithMarker;
    }

    public Marker get_localizationPointClicked() {
        return _localizationPointClicked;
    }

    public void set_localizationPointClicked(Marker _localizationPointClicked) {
        this._localizationPointClicked = _localizationPointClicked;
    }

    //Functions Fragment Offline Maps Part
    /**
     * Interfaz
     * Nombre: getLastKnownLocation
     * Comentario: Este método nos permite obtener la última localización
     * conocida del dispositivo.
     * Cabecera: private Location getLastKnownLocation()
     * Postcondiciones: El método modifica el estado del atributo _actualLocalization del viewModel.
     * */
    private Location getLastKnownLocation() {
        List<String> providers = _locManager.getProviders(true); //Returns a list of the names of location providers.
        Location bestLocation = null;
        //Si el usuario aceptó los permisos de localización
        if(ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            for (String provider : providers) {
                Location l = _locManager.getLastKnownLocation(provider); //Gets the last known location from the given provider, or null if there is no last known location.
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {//getAccuracy: Get the estimated horizontal accuracy of this location, radial, in meters.
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    /**
     * Interfaz
     * Nombre: getRegionName
     * Comentario: Con este método obtenemos el nombre de la región de un metadato offlineRegion.
     * Cabecera: public String getRegionName(OfflineRegion offlineRegion)
     * Entrada:
     *   -OfflineRegion offlineRegion
     * Salida:
     *   -String regionName
     * Postcondiciones: El método nos devuelve el nombre de la región.
     * */
    public String getRegionName(OfflineRegion offlineRegion) {
        String regionName;
        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            Timber.e("Failed to decode metadata: %s", exception.getMessage());
            regionName = "DEFAULT";
        }
        return regionName;
    }

    //Funciones Fragment Start

    /**
     * Interfaz
     * Nombre: eliminarPuntoDeLocalizacionSeleccionado
     * Comentario: Este método nos permite eliminar el punto de localización clicado actualmente.
     * Cabecera: public void eliminarPuntoDeLocalizacionSeleccionado()
     * Postcondiciones:
     */
    public void eliminarPuntoDeLocalizacionSeleccionado(){
        Marker marker = get_localizationPointClicked();
        //TODO Intentar eliminar por Query en un futuro
        DatabaseReference drLocalization = FirebaseDatabase.getInstance().getReference("Localizations");
        ClsLocalizationPoint localizationPoint = getLocalizationPoint(marker);
        if(localizationPoint != null){
            drLocalization.child(localizationPoint.getLocalizationPointId()).removeValue();
            //marker.remove();//TODO Aquí no funciona
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
     * Postcondiciones: El método devuelve un punto de localización asociado al nombre o null si
     * no exite ninguno con la misma posición que el marcador introducido por parámetros.
     */
    public ClsLocalizationPoint getLocalizationPoint(Marker marcador){
        ClsLocalizationPoint localization = null;
        boolean found = false;

        for(int i = 0; i < get_localizationPointsWithMarker().size() && !found; i++){
            ClsMarkerWithLocalization aux = get_localizationPointsWithMarker().get(i);
            if(aux.getMarker().getPosition().latitude == marcador.getPosition().latitude &&
                    aux.getMarker().getPosition().longitude == marcador.getPosition().longitude){
                localization = aux.getLocalizationPoint();
                found = true;
            }
        }

        return localization;
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
    public void deleteLocalizationDialog(final Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_localization_point);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(context, R.string.localization_point_deleted, Toast.LENGTH_SHORT).show();
                //Eliminamos el punto de localización
                eliminarPuntoDeLocalizacionSeleccionado();
                ((MainTabbetActivity)context).removeYourFragment();//Removemos el fragmento actual
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
