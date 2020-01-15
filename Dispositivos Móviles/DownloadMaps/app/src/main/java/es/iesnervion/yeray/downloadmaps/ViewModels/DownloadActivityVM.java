package es.iesnervion.yeray.downloadmaps.ViewModels;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.json.JSONObject;

import java.util.List;

import es.iesnervion.yeray.downloadmaps.DownloadActivity;
import es.iesnervion.yeray.downloadmaps.R;
import timber.log.Timber;

public class DownloadActivityVM extends AndroidViewModel {

    //JSON encoding/decoding
    private final String JSON_CHARSET = "UTF-8";
    private final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";
    private LocationManager _locManager;
    private Location _actualLocation;
    private Context _context;
    private int _regionSelected;

    public DownloadActivityVM(Application application){
        super(application);
        _context = application.getBaseContext();
        _locManager = (LocationManager)_context.getApplicationContext().getSystemService(_context.LOCATION_SERVICE);
        _actualLocation = getLastKnownLocation();
        _regionSelected = 0;
    }

    //Get Y Set

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

    //Funciones añadidas
    /*
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

    /*
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
}
