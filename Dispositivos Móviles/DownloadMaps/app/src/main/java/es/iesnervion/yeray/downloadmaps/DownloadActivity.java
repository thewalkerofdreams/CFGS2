package es.iesnervion.yeray.downloadmaps;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;

import java.util.ArrayList;

import es.iesnervion.yeray.downloadmaps.ViewModels.DownloadActivityVM;
import timber.log.Timber;

/**
 * Download, view, navigate to, and delete an offline region.
 */
public class DownloadActivity extends AppCompatActivity {
    //ViewModel
    DownloadActivityVM downloadActivityVM;
    //UI elements
    private MapView mapView;
    private MapboxMap map;
    private ProgressBar progressBar;
    private Button downloadButton;
    private Button listButton;
    //Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Necesitamos instanciar Mapbox con una de sus claves, la obtenemos a través de una cuenta.
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_download_map);

        //Pedimos los permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        downloadActivityVM = ViewModelProviders.of(this).get(DownloadActivityVM.class);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {//Se invocará cuando el mapa este listo para ser usado
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {//Called when the map is ready to be used.
                map = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(downloadActivityVM.get_actualLocation().getLatitude(),
                                        downloadActivityVM.get_actualLocation().getLongitude()))
                                .zoom(10)
                                .tilt(20)
                                .build();
                        map.setCameraPosition(position);
                        //Asignamos el progressBar
                        progressBar = findViewById(R.id.progress_bar);
                        //Instanciamos la variable offlineManager
                        offlineManager = OfflineManager.getInstance(DownloadActivity.this);
                        //Asignamos los botones de la actividad
                        downloadButton = findViewById(R.id.download_button);
                        downloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadRegionDialog();
                            }
                        });
                        listButton =  findViewById(R.id.list_button);
                        listButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadedRegionList();
                            }
                        });
                    }
                });
            }
        });
       }
    }

    @Override//Controlamos la respuesta a los permisos
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    //Métodos sobrecargados del ciclo de vida de la actividad
    @Override
    public void onResume() {
        super.onResume();
        //mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }

    /*
    * Interfaz
    * Nombre: downloadRegionDialog
    * Comentario: Este método muestra un dialogo por pantalla para descargar la región actual.
    * Cabecera: private void downloadRegionDialog()
    * Postcondiciones: Si el usuario ha introducido un nombre para la región y ha confirmado guardarlo,
    * se almacenará esa nueva región. En caso de falta de memoria o conexión el método informa de ello
    * al usuario.
    * */
    private void downloadRegionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
        //Declaramos un editText temporal
        final EditText regionNameEdit = new EditText(DownloadActivity.this);
        regionNameEdit.setHint(getString(R.string.set_region_name_hint));//Le insertamos una pista
        // Build the dialog box
        builder.setTitle(getString(R.string.dialog_title))
                .setView(regionNameEdit)
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regionName = regionNameEdit.getText().toString();
                        //Es necesario el nombre de la región, si esta vacío se mostrará un mensaje de error
                        //por pantalla y no se realizará la descarga.
                        if (regionName.length() == 0) {
                            Toast.makeText(DownloadActivity.this, getString(R.string.dialog_toast), Toast.LENGTH_SHORT).show();
                        } else {
                            downloadRegion(regionName);//Descargamos la región
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();//Lanzamos el dialogo
    }

    private void downloadRegion(final String regionName) {
        //Definimos los parámetros de la región offline, indicando los límites, el mínimo y máximo zoom y los metadatos
        startProgress();//Start the progressBar
        // Creamos la definición offline usando el estilo actual y los límites visibles del area del mapa
        map.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                String styleUrl = style.getUri();
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;//Esto me ahorra demasiado trabajo, aprende google maps!!!
                double minZoom = map.getCameraPosition().zoom;
                double maxZoom = map.getMaxZoomLevel();
                //Con esto obtenemos la densidad lógica de la pantalla, nos permitirá ajustar el estilo a la pantalla del dispositivo.
                float pixelRatio = DownloadActivity.this.getResources().getDisplayMetrics().density;
                OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                        styleUrl, bounds, minZoom, maxZoom, pixelRatio);//Aquí instanciamos la definición offline

                //Creamos un JSONObject usando el título de la región,
                //convertiendolo a una cadena y usandolo para crear una variable de metadato.
                //Esta variable luego será pasada al método createOfflineRegion()
                byte[] metadata;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(downloadActivityVM.getJsonFieldRegionName(), regionName);
                    String json = jsonObject.toString();//Pasamos el objeto a un String
                    metadata = json.getBytes(downloadActivityVM.getJsonCharset());//Pasamos esa cadena a un array de bytes
                } catch (Exception exception) {
                    Timber.e("Failed to encode metadata: %s", exception.getMessage());
                    metadata = null;
                }

                //Creamos la región offline y lanzamos la descarga
                offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        Timber.d( "Offline region created: %s" , regionName);
                        DownloadActivity.this.offlineRegion = offlineRegion;
                        launchDownload();//Comenzamos la descarga de la región
                    }

                    @Override
                    public void onError(String error) {
                        Timber.e( "Error: %s" , error);
                    }
                });
            }
        });
    }

    private void launchDownload() {
        //Colocamos un observador a la barra de descarga y avisaremos al usuario cuando esta finalice.
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {//Se llamará cuando el estado de la descarga cambie
                //Calculamos un porcentaje (getCompletedResourceCount nos indica el número de recursos descargados)
                double percentage = status.getRequiredResourceCount() >= 0
                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) : 0.0;

                if (status.isComplete()) {//Se ha completado la descarga
                    endProgress(getString(R.string.end_progress_success));
                } else if (status.isRequiredResourceCountPrecise()) {
                    setPercentage((int) Math.round(percentage));//Modificamos el porcentaje de descarga en la barra
                    //Indicamos al usuario como va la descarga
                    Timber.d("%s/%s resources; %s bytes downloaded.",//Estos mensajes me aparecerán con el debug
                            String.valueOf(status.getCompletedResourceCount()),
                            String.valueOf(status.getRequiredResourceCount()),
                            String.valueOf(status.getCompletedResourceSize()));
                }
            }

            @Override
            public void onError(OfflineRegionError error) {
                Timber.e("onError reason: %s", error.getReason());
                Timber.e("onError message: %s", error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {//Si no se puede descargar por falta de memoria
                Timber.e("Mapbox tile count limit exceeded: %s", limit);
            }
        });

        //Cambiamos el estado de la región offline
        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }

    private void downloadedRegionList() {
        //Creamos una lista de regiones descargadas cuando el usuario pulse el botón del listado
        downloadActivityVM.set_regionSelected(0);//Reseteamos el indicador de la región seleccionada

        //Consulta la base de datos asincronamente
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(final OfflineRegion[] offlineRegions) {
                //Si aún no existen mapas descargados, se informa de ello al usuario
                if (offlineRegions == null || offlineRegions.length == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_no_regions_yet), Toast.LENGTH_SHORT).show();
                }else{
                    //Añadimos todos los nombres de las regiones descargadas a la lista
                    ArrayList<String> offlineRegionsNames = new ArrayList<>();
                    for (OfflineRegion offlineRegion : offlineRegions) {
                        offlineRegionsNames.add(downloadActivityVM.getRegionName(offlineRegion));
                    }
                    //Para mostrar la lista de regiones en un dialogo, esta debe ser un array de CharSequence
                    final CharSequence[] items = offlineRegionsNames.toArray(new CharSequence[offlineRegionsNames.size()]);

                    //Creamos un dialogo que contiene la lista de las regiones
                    AlertDialog dialog = new AlertDialog.Builder(DownloadActivity.this)
                            .setTitle(getString(R.string.navigate_title))
                            .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadActivityVM.set_regionSelected(which);//Indicamos la región seleccionada
                                }
                            })
                            .setPositiveButton(getString(R.string.navigate_positive_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(DownloadActivity.this, items[downloadActivityVM.get_regionSelected()], Toast.LENGTH_LONG).show();
                                    //Obtenemos los límites de la región y el zoom
                                    LatLngBounds bounds = (offlineRegions[downloadActivityVM.get_regionSelected()].getDefinition()).getBounds();
                                    double regionZoom = (offlineRegions[downloadActivityVM.get_regionSelected()].getDefinition()).getMinZoom();

                                    //Modificamos la posición de la "camara" sobre el mapa
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(bounds.getCenter())
                                            .zoom(regionZoom)
                                            .build();

                                    map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));//Movemos la "camara"
                                }
                            })
                            .setNeutralButton(getString(R.string.navigate_neutral_button_title), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //Hacemos que el progressBar pase a estado indeterminado y lo hacemos visible para el proceso de eliminación
                                    progressBar.setIndeterminate(true);
                                    progressBar.setVisibility(View.VISIBLE);

                                    AlertDialog alertDialog = new AlertDialog.Builder(DownloadActivity.this)
                                    .setTitle("Confirm Delete")// Setting Alert Dialog Title
                                    //alertDialogBuilder.setIcon(R.drawable.question);// Icon Of Alert Dialog
                                    .setMessage("Do you really want delete this route?")// Setting Alert Dialog Message
                                    .setCancelable(false)//Para que no podamos quitar el dialogo sin contestarlo

                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            //Comenzamos el proceso de eliminación
                                            offlineRegions[downloadActivityVM.get_regionSelected()].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                                                @Override
                                                public void onDelete() {
                                                    //Cuando la región sea eliminada eliminamos el progressbar y le mandamos un mensaje al usuario
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    progressBar.setIndeterminate(false);
                                                    Toast.makeText(getApplicationContext(), getString(R.string.toast_region_deleted),
                                                            Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    progressBar.setIndeterminate(false);
                                                    Timber.e( "Error: %s", error);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create();

                                    alertDialog.show();
                                }
                            })
                            .setNegativeButton(getString(R.string.navigate_negative_button_title), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //Cuando el usuario cancela no pasa nada
                                }
                            }).create();
                    dialog.show();
                }
            }

            @Override
            public void onError(String error) {
                Timber.e( "Error: %s", error);
            }
        });
    }

    //Metodos sobre el progressBar
    private void startProgress() {
        //Deshabilitamos los botones de la pantalla actual
        downloadButton.setEnabled(false);
        listButton.setEnabled(false);

        //Iniciamos y mostramos el progress bar
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setPercentage(final int percentage) {//Modificamos el estado del porcentaje del progressBar
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }

    private void endProgress(final String message) {
        //Habilitamos los botones
        downloadButton.setEnabled(true);
        listButton.setEnabled(true);

        //Paramos y ocultamos la barra de progreso
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);

        //Mostramos un mensaje
        Toast.makeText(DownloadActivity.this, message, Toast.LENGTH_LONG).show();
    }
}