package com.example.adventuremaps.ViewModels;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import com.example.adventuremaps.Activities.MainActivity;
import com.example.adventuremaps.Activities.ui.MainTabbet.MainTabbetActivity;
import com.example.adventuremaps.Fragments.GoogleMapsStartFragment;
import com.example.adventuremaps.Models.ClsLocalizationPointWithFav;
import com.example.adventuremaps.Models.ClsMarkerWithLocalization;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.FireBaseEntities.ClsUser;
import com.example.adventuremaps.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MainTabbetActivityVM extends AndroidViewModel {

    private String _actualEmailUser;

    //Fragment Offline Maps Part
    private final String JSON_CHARSET = "UTF-8";
    private final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";
    private LocationManager _locManager; //This class provides access to the system location services
    private Location _actualLocation;
    private Context _context;
    private int _regionSelected;

    //Fragment Localizations
    private boolean _dialogDeleteLocalizationShowing;
    private boolean _dialogShareLocalizationShowing;
    private ArrayList<String> _localizationsIdActualUser;
    private ArrayList<ClsLocalizationPoint> _localizationsActualUser;
    private ArrayList<ClsLocalizationPointWithFav> _itemsLocalizationList;
    private ArrayList<ClsLocalizationPointWithFav> _selectedLocalizations;
    private LatLng _latLngToNavigate;

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
    private ClsLocalizationPoint _localizationToSave;
    private ArrayList<String> _localizationTypesToSave;
    private ArrayList<String> _checkedFilters;
    private boolean[] _dialogPostisionsChecked;
    private ClsLocalizationPoint _selectedLocalizationPoint;

    public MainTabbetActivityVM(Application application){
        super(application);
        _actualEmailUser = "";

        //Fragment Offline Maps Part
        _context = application.getBaseContext();
        _locManager = (LocationManager)_context.getApplicationContext().getSystemService(_context.LOCATION_SERVICE);
        _actualLocation = getLastKnownLocation();
        _regionSelected = 0;

        //Fragment Localizations
        _dialogDeleteLocalizationShowing = false;
        _dialogShareLocalizationShowing = false;
        _localizationsActualUser = new ArrayList<>();
        _localizationsIdActualUser = new ArrayList<>();
        _itemsLocalizationList = new ArrayList<>();
        _selectedLocalizations = new ArrayList<>();
        _latLngToNavigate = null;

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
        _localizationToSave = null;
        _localizationTypesToSave = new ArrayList<>();
        _checkedFilters = new ArrayList<>();
        _dialogPostisionsChecked = new boolean[8];
        for(int i = 0; i < _dialogPostisionsChecked.length; i++){//Inicializamos el filtrado sobr el mapa
            _dialogPostisionsChecked[i] = true;
        }
        _selectedLocalizationPoint = null;
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

    //Gets y Sets Fragment Localizations
    public ArrayList<ClsLocalizationPoint> get_localizationsActualUser() {
        return _localizationsActualUser;
    }

    public void set_localizationsActualUser(ArrayList<ClsLocalizationPoint> _localizationsActualUser) {
        this._localizationsActualUser = _localizationsActualUser;
    }

    public boolean is_dialogShareLocalizationShowing() {
        return _dialogShareLocalizationShowing;
    }

    public void set_dialogShareLocalizationShowing(boolean _dialogShareLocalizationShowing) {
        this._dialogShareLocalizationShowing = _dialogShareLocalizationShowing;
    }

    public boolean is_dialogDeleteLocalizationShowing() {
        return _dialogDeleteLocalizationShowing;
    }

    public void set_dialogDeleteLocalizationShowing(boolean _dialogDeleteLocalizationShowing) {
        this._dialogDeleteLocalizationShowing = _dialogDeleteLocalizationShowing;
    }

    public ArrayList<String> get_localizationsIdActualUser() {
        return _localizationsIdActualUser;
    }

    public void set_localizationsIdActualUser(ArrayList<String> _localizationsIdActualUser) {
        this._localizationsIdActualUser = _localizationsIdActualUser;
    }

    public ArrayList<ClsLocalizationPointWithFav> get_itemsLocalizationList() {
        return _itemsLocalizationList;
    }

    public void set_itemsLocalizationList(ArrayList<ClsLocalizationPointWithFav> _itemsLocalizationList) {
        this._itemsLocalizationList = _itemsLocalizationList;
    }

    public ArrayList<ClsLocalizationPointWithFav> get_selectedLocalizations() {
        return _selectedLocalizations;
    }

    public void set_selectedLocalizations(ArrayList<ClsLocalizationPointWithFav> _selectedLocalizations) {
        this._selectedLocalizations = _selectedLocalizations;
    }

    public LatLng get_latLngToNavigate() {
        return _latLngToNavigate;
    }

    public void set_latLngToNavigate(LatLng _latLngToNavigate) {
        this._latLngToNavigate = _latLngToNavigate;
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

    public ClsLocalizationPoint get_localizationToSave() {
        return _localizationToSave;
    }

    public void set_localizationToSave(ClsLocalizationPoint _localizationToSave) {
        this._localizationToSave = _localizationToSave;
    }

    public ArrayList<String> get_localizationTypesToSave() {
        return _localizationTypesToSave;
    }

    public void set_localizationTypesToSave(ArrayList<String> _localizationTypesToSave) {
        this._localizationTypesToSave = _localizationTypesToSave;
    }

    public ArrayList<String> get_checkedFilters() {
        return _checkedFilters;
    }

    public void set_checkedFilters(ArrayList<String> _checkedFilters) {
        this._checkedFilters = _checkedFilters;
    }

    public boolean[] get_dialogPostisionsChecked() {
        return _dialogPostisionsChecked;
    }

    public void set_dialogPostisionsChecked(boolean[] _dialogPostisionsChecked) {
        this._dialogPostisionsChecked = _dialogPostisionsChecked;
    }

    public ClsLocalizationPoint get_selectedLocalizationPoint() {
        return _selectedLocalizationPoint;
    }

    public void set_selectedLocalizationPoint(ClsLocalizationPoint _selectedLocalizationPoint) {
        this._selectedLocalizationPoint = _selectedLocalizationPoint;
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
     * Postcondiciones: El método elimina el punto de localización seleccionado.
     */
    public void eliminarPuntoDeLocalizacionSeleccionado(){
        final DatabaseReference drLocalization = FirebaseDatabase.getInstance().getReference("Localizations");
        final DatabaseReference drUser = FirebaseDatabase.getInstance().getReference("Users");

        if(get_selectedLocalizationPoint() != null){
            drUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ClsUser user = null;
                    for(DataSnapshot datas: dataSnapshot.getChildren()){//Solo habrá como máximo uno
                        user = datas.getValue(ClsUser.class);
                        //Eliminamos el id del punto de localización asignado a la lista de favoritos de los usuarios que lo tengan asignado como favorito
                        drUser.child(user.getUserId()).child("localizationsId").child(get_selectedLocalizationPoint().getLocalizationPointId()).removeValue();
                    }
                    //Eliminamos el punto de localización
                    drLocalization.child(get_selectedLocalizationPoint().getLocalizationPointId()).removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
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
                set_localizationPointClicked(null);//Indicamos que el marcador seleccionado pasa a null
                ((MainTabbetActivity) context).findViewById(R.id.FrameLayout02).setVisibility(View.GONE);//Volvemos invisible el fragmento FragmentStartLocalizationPointClick
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

    //Fragment Localizations

    /**
     * Interfaz
     * Nombre: shareLocalizationPoint
     * Comentario: Este método nos permite compartir un punto de localización con la plataforma.
     * Cabecera: public void shareLocalizationPoint()
     * Precondiciones:
     *  -El atributo _selectedLocalizations del VM debe contener una sola localización (Por razones de seguridad solo le dejamos compartir de una en una)
     * Postcondiciones: El método hace visible para todos los usuarios un punto de localización
     * seleccionado, la localización seleccionada se almacena en el atributo _selectedLocalizations
     * del VM.
     */
    public void shareLocalizationPoint(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Localizations");
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("shared", true);
        databaseReference.child(get_selectedLocalizations().get(0).get_localizationPoint().getLocalizationPointId()).updateChildren(hopperUpdates);
    }
}