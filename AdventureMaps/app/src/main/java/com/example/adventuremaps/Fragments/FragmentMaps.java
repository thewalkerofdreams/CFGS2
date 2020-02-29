package com.example.adventuremaps.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.ui.main.PlaceholderFragment;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
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

import timber.log.Timber;

public class FragmentMaps extends Fragment {

    //ViewModel
    private MainTabbetActivityVM viewModel;
    //UI elements
    private MapView mapView;
    private MapboxMap map;
    private ProgressBar progressBar;
    private Button downloadButton;
    private Button listButton;
    //Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;
    private OnFragmentInteractionListener mListener;

    public FragmentMaps() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instanciamos Mapbox con una de sus claves, la obtenemos a través de una cuenta (En este caso utilizamos una de prueba).
        Mapbox.getInstance(getActivity(), getString(R.string.access_token));

        //Inflamos el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //Comprobamos que tiene los permisos, si no los tiene enviamos un dialogo
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        //Si se han aceptado los permisos continuamos
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //Instanciamos el VM
            viewModel = ViewModelProviders.of(this).get(MainTabbetActivityVM.class);

            //Instanciamos los elementos de la UI
            mapView = view.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(new OnMapReadyCallback() {//Se invocará cuando el mapa este listo para ser usado
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {//Called when the map is ready to be used.
                    map = mapboxMap;
                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(viewModel.get_actualLocation().getLatitude(),
                                            viewModel.get_actualLocation().getLongitude()))
                                    .zoom(10)
                                    .tilt(20)
                                    .build();
                            map.setCameraPosition(position);

                            //Instanciamos el progressBar
                            progressBar = getActivity().findViewById(R.id.progress_bar);

                            //Instanciamos la variable offlineManager
                            offlineManager = OfflineManager.getInstance(getActivity());

                            //Instanciamos los botones de la actividad
                            downloadButton = getActivity().findViewById(R.id.download_button);
                            downloadButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    downloadRegionDialog();
                                }
                            });

                            listButton = getActivity().findViewById(R.id.list_button);
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

        return view;
    }

    /**
     * Interfaz
     * Nombre: downloadRegionDialog
     * Comentario: Este método muestra un dialogo por pantalla para descargar la región actual.
     * Cabecera: private void downloadRegionDialog()
     * Postcondiciones: Si el usuario ha introducido un nombre para la región y ha confirmado guardarlo,
     * se almacenará esa nueva región. En caso de falta de memoria o de conexión el método informa de ello
     * al usuario.
     * */
    private void downloadRegionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//Declaramos un dialogo

        //Declaramos un editText temporal
        final EditText regionNameEdit = new EditText(getActivity());
        regionNameEdit.setHint(getString(R.string.set_region_name_hint));//Le insertamos una pista

        //Contruimos el dialogo
        builder.setTitle(getString(R.string.dialog_title))
                .setView(regionNameEdit)
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regionName = regionNameEdit.getText().toString();
                        //Es necesario el nombre de la región, si esta vacío se mostrará un mensaje de error por pantalla y no se realizará la descarga.
                        if (regionName.length() == 0) {
                            Toast.makeText(getActivity(), getString(R.string.dialog_toast), Toast.LENGTH_SHORT).show();
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
        startProgress();//Activamos e iniciamos el progressBar

        //Creamos la definición offline usando el estilo actual y los límites visibles del area del mapa
        map.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                String styleUrl = style.getUri();

                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                double minZoom = map.getCameraPosition().zoom;
                double maxZoom = map.getMaxZoomLevel();

                //Con esto obtenemos la densidad lógica de la pantalla, nos permitirá ajustar el estilo a la pantalla del dispositivo.
                float pixelRatio = getActivity().getResources().getDisplayMetrics().density;
                OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                        styleUrl, bounds, minZoom, maxZoom, pixelRatio);

                //Creamos un JSONObject usando el título de la región,
                //convertiendolo a una cadena y usandolo para crear una variable de metadato.
                //Esta variable luego será pasada al método createOfflineRegion()
                byte[] metadata;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(viewModel.getJsonFieldRegionName(), regionName);
                    String json = jsonObject.toString();//Pasamos el objeto a un String
                    metadata = json.getBytes(viewModel.getJsonCharset());//Pasamos esa cadena a un array de bytes
                } catch (Exception exception) {
                    Timber.e("Failed to encode metadata: %s", exception.getMessage());//Utilizamos Timber en vez de un Log
                    metadata = null;
                }

                //Creamos la región offline y lanzamos la descarga
                offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        //Timber.d( "Offline region created: %s" , regionName);
                        FragmentMaps.this.offlineRegion = offlineRegion;
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

    /**
     * Interfaz
     * Nombre: launchDownload
     * Comentario: Este método nos permite descargar la región actual que muestra el mapa de MapBox.
     * Cabecera: private void launchDownload()
     * Postccondiciones: El método descarga la región actual, en caso de haber superado el límite de
     * descarga por defecto de mapBox, el método informará de ello al usuario y no se almacenará la
     * región.
     */
    private void launchDownload() {
        //Observer a offlineRegion, cuando se vaya descargando el mapa iremos actualizando un progressbar
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {//Se llamará cuando el estado de la descarga cambie
                //Calculamos un porcentaje (getCompletedResourceCount nos indica el número de recursos descargados)
                double percentage = status.getRequiredResourceCount() >= 0
                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) : 0.0;

                if (status.isComplete()) {//Se ha completado la descarga
                    endProgress();//Deshabilitamos el progressbar y habilitamos los botones de la interfaz
                    Toast.makeText(getActivity(), R.string.end_progress_success, Toast.LENGTH_LONG).show();//Mensaje de descarga completada
                } else if (status.isRequiredResourceCountPrecise()) {
                    setPercentage((int) Math.round(percentage));//Modificamos el porcentaje de descarga en la barra
                }
            }

            @Override
            public void onError(OfflineRegionError error) {
                Timber.e("onError reason: %s", error.getReason());
                Timber.e("onError message: %s", error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {//Si se supera el límite de descarga
                //Timber.e("Mapbox tile count limit exceeded: %s", limit);
                Toast.makeText(getActivity(), getString(R.string.exceeded_download_limit), Toast.LENGTH_SHORT).show();
            }
        });

        //Cambiamos el estado de la región offline a activa
        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }

    /**
     * Interfaz
     * Nombre: downloadedRegionList
     * Comentario: Este método carga una lista de las regiones offline descargadas.
     * Cabecera: private void downloadedRegionList()
     * Postcondiciones: El método muestra una lista de la regiones descargadas, en una interfaz
     * donde se podrá viajar a estas regiones e incluso eliminarlas.
     */
    private void downloadedRegionList() {
        //Consultamos la base de datos de MapBox asincronamente
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(final OfflineRegion[] offlineRegions) {
                //Si aún no existen mapas descargados, se informa de ello al usuario
                if (offlineRegions == null || offlineRegions.length == 0) {
                    Toast.makeText(getActivity(), getString(R.string.toast_no_regions_yet), Toast.LENGTH_SHORT).show();
                }else{
                    viewModel.set_regionSelected(0);//Seleccionamos la primera región por defecto

                    //Añadimos todos los nombres de las regiones descargadas a la lista
                    ArrayList<String> offlineRegionsNames = new ArrayList<>();
                    for (OfflineRegion offlineRegion : offlineRegions) {
                        offlineRegionsNames.add(viewModel.getRegionName(offlineRegion));
                    }

                    //Para mostrar la lista de regiones en un dialogo, esta debe ser un array de CharSequence
                    final CharSequence[] items = offlineRegionsNames.toArray(new CharSequence[offlineRegionsNames.size()]);

                    //Creamos un dialogo que contiene la lista de las regiones
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.navigate_title))
                            .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    viewModel.set_regionSelected(which);//Indicamos la región seleccionada
                                }
                            })
                            .setPositiveButton(getString(R.string.navigate_positive_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getActivity(), items[viewModel.get_regionSelected()], Toast.LENGTH_LONG).show();

                                    //Obtenemos los límites de la región y el zoom
                                    LatLngBounds bounds = (offlineRegions[viewModel.get_regionSelected()].getDefinition()).getBounds();
                                    double regionZoom = (offlineRegions[viewModel.get_regionSelected()].getDefinition()).getMinZoom();

                                    //Modificamos la posición de la "camara" sobre el mapa, centrandola en el centro de la ragión a la que se ha navegado
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

                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                            .setTitle(R.string.confirm_delete)// Setting Alert Dialog Title
                                            .setMessage(R.string.question_delete_region)// Setting Alert Dialog Message
                                            .setCancelable(false)//De esta manera no podemos quitar el dialogo sin contestarlo
                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    //Comenzamos el proceso de eliminación
                                                    offlineRegions[viewModel.get_regionSelected()].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                                                        @Override
                                                        public void onDelete() {
                                                            //Cuando la región es eliminada inhabilitamos el progressbar e informamos al usuario
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            progressBar.setIndeterminate(false);
                                                            Toast.makeText(getActivity(), getString(R.string.toast_region_deleted), Toast.LENGTH_LONG).show();
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
                                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    //Metodos sobre el funcionamiento de los elementos de la UI

    /**
     * Interfaz
     * Nombre: startProgress
     * Comentario: Este método nos permite inhabilitar los botones de descarga de regiones y de
     * la lista de estas regiones, además de habilitar el progressbar.
     * Cabecera: private void startProgress()
     */
    private void startProgress() {
        //Deshabilitamos los botones de la pantalla actual
        downloadButton.setEnabled(false);
        listButton.setEnabled(false);

        //Habilitamos y mostramos el progress bar
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Interfaz
     * Nombre: setPercentage
     * Comentario: Este método nos permite modificar el estado del porcentaje del progressBar.
     * Cabecera: private void setPercentage(int percentage)
     * Entrada:
     *  @param percentage
     * Postcondiciones: El método modifica el estado del progressbar.
     */
    private void setPercentage(int percentage) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }

    /**
     * Interfaz
     * Nombre: endProgress
     * Comentario: Este método nos permite deshabilitar el progressbar y habilitar los botones de descarga
     * y del listado de las regiones de la interfaz.
     * Cabecera: private void endProgress()
     * Postcondiciones: El método habilita los botones de descarga y listado, además de deshabilitar el progressbar.
     */
    private void endProgress() {
        //Habilitamos los botones
        downloadButton.setEnabled(true);
        listButton.setEnabled(true);

        //Deshabilitamos y ocultamos la barra de progreso
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
