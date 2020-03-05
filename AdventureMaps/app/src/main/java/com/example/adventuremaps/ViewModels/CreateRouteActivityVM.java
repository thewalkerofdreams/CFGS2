package com.example.adventuremaps.ViewModels;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;

import com.example.adventuremaps.Activities.Models.ClsMarkerWithPriority;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.FireBaseEntities.ClsRoutePoint;
import com.google.android.gms.maps.model.Marker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CreateRouteActivityVM extends AndroidViewModel {

    private String _actualEmailUser;
    private String _actualIdRoute;
    private String _actualRouteName;
    private boolean _mostrarRuta;
    private LocationManager _locManager;
    private Location _actualLocation;
    private float _zoom;
    private Context _context;
    private ArrayList<ClsMarkerWithPriority> _localizationPoints;
    private ClsRoute _route;
    private ClsRoutePoint _lastLocalizationClicked;

    public CreateRouteActivityVM(Application application){
        super(application);
        _context = application.getBaseContext();
        _locManager = (LocationManager)_context.getApplicationContext().getSystemService(_context.LOCATION_SERVICE);
        _actualLocation = getLastKnownLocation();
        _localizationPoints = new ArrayList<ClsMarkerWithPriority>();
        _route = new ClsRoute();
        _lastLocalizationClicked = null;
        _zoom = 13;
        _actualEmailUser = "";
        _actualIdRoute = "";
        _actualRouteName = "";
        _mostrarRuta = true;
    }

    public LocationManager get_locManager() {
        return _locManager;
    }

    public void set_locManager(LocationManager _locManager) {
        this._locManager = _locManager;
    }

    public Location get_actualLocation() {
        return _actualLocation;
    }

    public void set_actualLocation(Location _actualLocation) {
        this._actualLocation = _actualLocation;
    }

    public Context get_context() {
        return _context;
    }

    public void set_context(Context _context) {
        this._context = _context;
    }

    public ArrayList<ClsMarkerWithPriority> get_localizationPoints() {
        return _localizationPoints;
    }

    public void set_localizationPoints(ArrayList<ClsMarkerWithPriority> _localizationPoints) {
        this._localizationPoints = _localizationPoints;
    }

    public ClsRoute get_route() {
        return _route;
    }

    public void set_route(ClsRoute _route) {
        this._route = _route;
    }

    public ClsRoutePoint get_lastLocalizationClicked() {
        return _lastLocalizationClicked;
    }

    public void set_lastLocalizationClicked(ClsRoutePoint _lastLocalizationClicked) {
        this._lastLocalizationClicked = _lastLocalizationClicked;
    }

    public float get_zoom() {
        return _zoom;
    }

    public void set_zoom(float _zoom) {
        this._zoom = _zoom;
    }

    public String get_actualEmailUser() {
        return _actualEmailUser;
    }

    public void set_actualEmailUser(String _actualEmailUser) {
        this._actualEmailUser = _actualEmailUser;
    }

    public String get_actualIdRoute() {
        return _actualIdRoute;
    }

    public void set_actualIdRoute(String _actualIdRoute) {
        this._actualIdRoute = _actualIdRoute;
    }

    public String get_actualRouteName() {
        return _actualRouteName;
    }

    public void set_actualRouteName(String _actualRouteName) {
        this._actualRouteName = _actualRouteName;
    }

    public boolean is_mostrarRuta() {
        return _mostrarRuta;
    }

    public void set_mostrarRuta(boolean _mostrarRuta) {
        this._mostrarRuta = _mostrarRuta;
    }

    /**
     * Interfaz
     * Nombre: getLastKnownLocation
     * Comentario: Este método nos permite obtener la última localización
     * conocida del dispositivo.
     * Cabecera: private Location getLastKnownLocation()
     * Postcondiciones: El método modifica el estado del atributo _actualLocalization.
     * */
    private Location getLastKnownLocation() {
        List<String> providers = _locManager.getProviders(true);
        Location bestLocation = null;
        if(ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            for (String provider : providers) {
                Location l = _locManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    /**
     * Interfaz
     * Nombre: getLastPositionNumber
     * Comentario: Este método nos permite obtener el número(prioridad) asignado al último
     * punto de localización añadido a la ruta. Si la función devuelve 0 significa que
     * aún no hay puntos de localización en la ruta.
     * Cabecera: public int getLastPositionNumber()
     * Salida:
     *   -int positionNumber
     * Postcondiciones: La función devuelve el número de la ultima posición añadida a la
     * ruta.
     * */
    public int getLastPositionNumber(){
        int positionNumber = 0;
        int numeroDeLocalizaciones = _localizationPoints.size();
        if(numeroDeLocalizaciones > 0){
            positionNumber = _localizationPoints.get(_localizationPoints.size() - 1).getPriority();
        }
        return positionNumber;
    }

    /**
     * Interfaz
     * Nombre: eliminarMarcador
     * Comentario: Este método nos permite eliminar un punto de localización
     * según un marcador dado por parámetro, es decir, si pasamos un marcador que tiene
     * la misma localización que uno de los puntos de localización almacenamos en este
     * ViewModel, se eliminará ese punto de localización.
     * Cabecera:public boolean eliminarMarcador(Marker marcador)
     * Entrada:
     *   -Marker marcador
     * Salida:
     *   -boolean eliminado
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si
     * se ha conseguido eliminar el punto de localización o false en caso contrario.
     * */
    public boolean eliminarMarcador(Marker marcador){
        boolean eliminado = false;
        int pointNumber = getPointNumber(marcador);

        if(pointNumber >= 0){//Si el punto de localización existe
            _localizationPoints.remove(pointNumber);
            eliminado = true;
        }

        return eliminado;
    }

    /**
     * Interfaz
     * Nombre: getPointNumber
     * Comentario: Este método nos permite obtener el atributo pointNumber(prioridad) de un punto de
     * localización según un marcador.
     * Cabecera: public int getPointNumber(Marker marcador)
     * Entrada:
     *   -Marker marcador
     * Salida:
     *   -int pointNumber
     * Postcondiciones: El método devuelve un número entero asociado al nombre, que será el
     * pointNumber de una localización si el marcador coincide con ella o -1 si no se ha encontrado
     * ningún punto de localización que coincida con el marcador. En el caso de existir más de un
     * punto de localización que coincida con el marcador solo se devolverá la posición del
     * primero encontrado.
     * */
    private int getPointNumber(Marker marcador){
        boolean encontrado = false;
        int pointNumber = -1;

        for(int i = 0; i < _localizationPoints.size() && !encontrado; i++){
            double latitude = marcador.getPosition().latitude;
            double longitude = marcador.getPosition().longitude;
            if( _localizationPoints.get(i).getMarker().getPosition().latitude == latitude &&
                    _localizationPoints.get(i).getMarker().getPosition().longitude == longitude){
                pointNumber = i;
                encontrado = true;
            }
        }

        return pointNumber;
    }
}
