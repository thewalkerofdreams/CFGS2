package com.example.adventuremaps.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

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
    private Button downloadButton, listButton;
    private LinearLayout linearLayoutInferior;
    private FrameLayout frameLayoutInferior;
    //Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;
    //Insert Markers
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos referencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
    private SymbolManager symbolManager;

    private ValueEventListener listener = null;

    private OnFragmentInteractionListener mListener;

    public FragmentMaps() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        //Instanciamos Mapbox con una de sus claves, la obtenemos a través de una cuenta (En este caso utilizamos una de prueba).
        Mapbox.getInstance(getActivity(), getString(R.string.access_token));

        HttpRequestUtil.setLogEnabled(true);//Habilitamos los logs, para poder cargar zonas online

        //Inflamos el layout para este fragmento
        final View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //Comprobamos que tiene los permisos, si no los tiene enviamos un dialogo
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ApplicationConstants.REQUEST_CODE_PERMISSIONS_MAIN_TABBET_ACTIVITY_WITH_OFFLINE_MAPS);
        }
        //Si se han aceptado los permisos continuamos
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //Instanciamos los elementos de la UI
            mapView = view.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            //Insertamos el fragmento "FragmentOfflineLocalizationPointClick" en el FrameLayout inferior
            insertInferiorFragment();

            //Instanciamos el progressBar
            progressBar = view.findViewById(R.id.progress_bar);

            //Instanciamos la variable offlineManager
            offlineManager = OfflineManager.getInstance(getActivity());

            //Instanciamos el LinearLayout inferior
            linearLayoutInferior = view.findViewById(R.id.bottom_navigation);
            //Instanciamos el FrameLayout
            frameLayoutInferior = view.findViewById(R.id.FrameLayoutLocalizationClicked);

            //Instanciamos los botones de la actividad
            downloadButton = view.findViewById(R.id.download_button);
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDownloadRegionDialog();
                }
            });

            listButton = view.findViewById(R.id.list_button);
            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadedRegionList();
                }
            });
        }

        return view;
    }

    /**
     * Interfaz
     * Nombre: loadOnMapReadyCallback
     * Comentario: El método carga la función onMapReady mediante un callback. Dentro de onMapReady
     * declaramos un nuevo estilo que ajustará la camara y agregará los marcadores sobre el mapa actual.
     * Cabecera: private void loadOnMapReadyCallback()
     * Postcondiciones: El método carga la función onMapReady, dentro se genera un estilo personalizado para
     * el mapa.
     */
    private void loadOnMapReadyCallback(){
        mapView.getMapAsync(new OnMapReadyCallback() {//Se invocará cuando el mapa este listo para ser usado
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {//Called when the map is ready to be used.
                map = mapboxMap;
                if(getActivity() != null){//Ajustamos la botonera inferior
                    getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);//Hacemos visible los botones de interación con el mapa
                    getActivity().findViewById(R.id.FrameLayoutLocalizationClicked).setVisibility(View.GONE);//Ocultamos el FrameLayout inferior
                }

                //Limpiamos los posibles símbolos insertados en el mapa
                if(symbolManager != null){//Si el administrador ya fue instanciado
                    symbolManager.delete(viewModel.get_markersInserted());//Elimminamos todos los símbolos que inserto anteriormente
                }

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        LatLng latLng = null;
                        if(viewModel.get_actualLocation() != null){//Si se pudo obtener la localización del ussuario
                            latLng = new LatLng(viewModel.get_actualLocation().getLatitude(), viewModel.get_actualLocation().getLongitude());
                        }else{//Lo mandamos a R'lyeh
                            latLng = new LatLng(ApplicationConstants.RLYEH_LATITUDE, ApplicationConstants.RLYEH_LONGITUDE);
                        }
                        //Ajustamos el zoom mínimo en el mapa
                        map.setMinZoomPreference(2);

                        CameraPosition position = new CameraPosition.Builder()//Movemos la camara del mapa a la posición del usuario actual
                                .target(latLng)
                                .zoom(10)
                                .tilt(20)
                                .build();
                        map.setCameraPosition(position);
                        //Declaramos el evento de clicado del mapa
                        map.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                tryChangeMarkerToDefaultImage();//Si ya se había clicado sobre otro marcador, se modifica el icono de este
                                mostrarAccionesSobreElMapa();//Mostramos los iconos para interactuar con el mapa
                                return false;//False para que podemos clicar en los marcadores
                            }
                        });
                        map.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                            @Override
                            public boolean onMapLongClick(@NonNull LatLng point) {
                                viewModel.set_longClickPositionMapbox(point);//Almacenamos la posición seleccionada en el mapa en el VM
                                viewModel.insertLocalizationDialog(getActivity(), 2);//Comenzamos un dialogo de inserción
                                return true;//Soluciona el error de ejecución múltiple
                            }
                        });

                        //Creamos un objeto symbol manager
                        symbolManager = new SymbolManager(mapView, mapboxMap, style);

                        //Declaramos los eventos de los marcadores
                        symbolManager.addClickListener(new OnSymbolClickListener() {
                            @Override
                            public void onAnnotationClick(Symbol symbol) {
                                tryChangeMarkerToDefaultImage();//Si ya se había clicado sobre otro marcador, se modifica el icono de este

                                viewModel.set_symbolClicked(symbol);
                                symbol.setIconImage("marker_selected");
                                symbolManager.update(symbol);
                                mostrarAccionesSobreUnMarcador();//Hacemos visible las opciones del icono
                            }
                        });

                        //Declaramos un par de propiedades
                        symbolManager.setIconAllowOverlap(true);//Permitimos la superposición del símbolo
                        symbolManager.setIconIgnorePlacement(true);//Ajustamos su colocación en el mapa

                        //Ajustamos la imagen del icono
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getContext().getResources().getDrawable(Integer.valueOf(R.drawable.simple_marker));
                        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), ApplicationConstants.MARKER_WITH_SIZE, ApplicationConstants.MARKER_HEIGHT_SIZE, false);
                        //Añadimos la imagen al estilo
                        style.addImage("my_image", smallMarker);

                        //Añadiamos la imagen para cuando se seleccione un marcador
                        bitmapdraw = (BitmapDrawable) getContext().getResources().getDrawable(Integer.valueOf(R.drawable.blue_marker));
                        smallMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), ApplicationConstants.MARKER_WITH_SIZE, ApplicationConstants.MARKER_HEIGHT_SIZE, false);
                        style.addImage("marker_selected", smallMarker);

                        //Limpiamos la lista de marcadores insertados del VM
                        viewModel.get_markersInserted().clear();
                        //Añadimos los puntos de localización almacenados en el VM
                        for(int i = 0; i < viewModel.get_localizationPointsMapbox().size(); i++){
                            Symbol symbol = symbolManager.create(new SymbolOptions()
                                    .withLatLng(new LatLng(viewModel.get_localizationPointsMapbox().get(i).getLatitude(),
                                            viewModel.get_localizationPointsMapbox().get(i).getLongitude()))
                                    .withIconImage("my_image")
                                    .withIconSize(1.0f));
                            viewModel.get_markersInserted().add(symbol);
                        }
                    }
                });
            }
        });
    }

    /**
     * Interfaz
     * Nombre: tryChangeMarkerToDefaultImage
     * Comentario: El método cambia el icono del marcador almacenado en el atributo
     * "_symbolClicked" del VM, al icono por defecto si este tiene un valor diferente de nulo.
     * Cabecera: private void tryChangeMarkerToDefaultImage()
     * Postcondiciones: Si el simbolo almacenado en el VM es diferente de null, se cambia su icono
     * por el de por defecto.
     */
    private void tryChangeMarkerToDefaultImage(){
        if(viewModel.get_symbolClicked() != null){//Si ya se había clicado sobre otro marcador
            viewModel.get_symbolClicked().setIconImage("my_image");//Le insertamos el icono por defecto
            symbolManager.update(viewModel.get_symbolClicked());
        }
    }

    /**
     * Interfaz
     * Nombre: showDownloadRegionDialog
     * Comentario: Este método muestra un dialogo por pantalla para descargar la región actual.
     * Cabecera: private void showDownloadRegionDialog()
     * Postcondiciones: Si el usuario ha introducido un nombre para la región y ha confirmado guardarlo,
     * se almacenará esa nueva región. En caso de falta de memoria o de conexión el método informa de ello
     * al usuario.
     * */
    private void showDownloadRegionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//Declaramos un dialogo

        //Declaramos un editText temporal
        final EditText nameEdit = new EditText(getActivity());
        nameEdit.setHint(getString(R.string.set_region_name_hint));//Le insertamos una pista

        //Contruimos el dialogo
        builder.setTitle(getString(R.string.dialog_title))
                .setView(nameEdit)
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regionName = nameEdit.getText().toString();
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

    /**
     * Interfaz
     * Nombre: downloadRegion
     * Comentario: Este método nos permite descargar una región del mapa online. Si
     * se supera el límite de descarga de la versión gratuita, el método informa de
     * ello y no se descarga la región.
     * Cabecera:  private void downloadRegion(final String regionName)
     * Entrada:
     * @param regionName
     * Postcondiciones: El método descarga la región que cubre la pantalla actual o muestra
     * un mensaje de error, si esta no se pudo descargar.
     */
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
                    if(getActivity() != null)
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
                endProgress();//Habilitamos los botones inferiores y deshabilitamos el progressbar
                Toast.makeText(getActivity(), getString(R.string.exceeded_download_limit), Toast.LENGTH_SHORT).show();
            }
        });

        //Cambiamos el estado de la región offline a activa
        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }

    /**
     * Interfaz
     * Nombre: downloadedRegionList
     * Comentario: Este método carga un dialogo con un listado de las regiones offline descargadas.
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

                    //Creamos el dialogo que contiene la lista de las regiones
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
                                    navigateToSpecificRegion(offlineRegions, items);//Navegamos a la región seleccionada en la lista
                                }
                            })
                            .setNeutralButton(getString(R.string.navigate_neutral_button_title), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //Hacemos que el progressBar pase a estado indeterminado y lo hacemos visible para el proceso de eliminación
                                    progressBar.setIndeterminate(true);
                                    progressBar.setVisibility(View.VISIBLE);

                                    showDeleteDownloadRegionDialog(offlineRegions);//Lanzamos el dialogo de eliminación
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

    /**
     * Interfaz
     * Nombre: showDeleteDownloadRegionDialog
     * Comentario: El método muestra por pantalla un dialogo para eliminar la región seleccionada
     * del listado de regiones descargadas que muestra la función "downloadedRegionList". Este método
     * es llamado dentra de esa misma función.
     * Cabecera: private void showDeleteDownloadRegionDialog(final OfflineRegion[] offlineRegions)
     * Entrada:
     *  -final OfflineRegion[] offlineRegions
     * Postcondiciones: El método muestra un dialogo de eliminación por pantalla, si el usuario lo confirma
     * se elimina la región seleccionada, en caso contrario no sucede nada.
     */
    private void showDeleteDownloadRegionDialog(final OfflineRegion[] offlineRegions){
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

    /**
     * Interfaz
     * Nombre: navigateToSpecificRegion
     * Comentario: Este método nos permite navegar a una región descargada de la lista de regiones
     * almacenamadas en el dispositivo actual.
     * Cabecera: private void navigateToSpecificRegion(final CharSequence[] items)
     * Entrada:
     *  -final CharSequence[] items
     * Postcondiciones: El método modifica la posición de la camara sobre el mapa actual.
     */
    private void navigateToSpecificRegion(OfflineRegion[] offlineRegions, CharSequence[] items){
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//Nos permite controlar la orientación de la página actual
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity actualActivity = getActivity();
            if(actualActivity != null)
                actualActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * Interfaz
     * Nombre: clearAmbientCache
     * Comentario: El método limpia la caché del mapa mostrado, sin eliminar las regiones descargadas.
     * Cabecera: private void clearAmbientCache()
     * Postcondiciones: El método limpia la caché de las regiones mostradas en el mapa actusal.
     */
    private void clearAmbientCache() {
        OfflineManager fileSource = OfflineManager.getInstance(getActivity());
        fileSource.clearAmbientCache(new OfflineManager.FileSourceCallback() {
            @Override
            public void onSuccess() {
                //La limpieza se realizó correctamente
            }

            @Override
            public void onError(@NonNull String message) {
                //Error al limpiar la caché
            }
        });
    }

    //Metodos sobreescritos
    @Override
    public void onStart() {
        super.onStart();
        storeFavoutireLocalizationsId();//Comenzamos a obtener los datos de la plataforma FireBase

        if(mapView != null)
            mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        HttpRequestUtil.setLogEnabled(false);//Nos permite deshabilitar los logs cuando la actuvidad se pause
        clearAmbientCache();//Limpiamos la caché del mapa

        if(mapView != null)
            mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mapView != null)
            mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        userReference.removeEventListener(listener);//Eliminamos el evento unido a la referencia de los usuarios
        if(mapView != null)
            mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mapView != null)
            mapView.onDestroy(); //after super call
    }

    //Métodos de obtención de información
    /**
     * Interfaz
     * Nombre: storeFavoutireLocalizationsId
     * Comentario: El método almacena todas las id's de las localizaciones favoritas del usuario
     * actual en el VM. Por último llama al método "storeOwnerAndFavouriteLocalizations".
     * Cabecera: private void storeFavoutireLocalizationsId()
     * Postcondiciones: El método almacena en el VM todas las id's de localizaciones favoritas del
     * usuario actual.
     */
    private void storeFavoutireLocalizationsId(){
        listener = userReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_localizationPointsMapbox().clear();//Limpiamos la lista de puntos de localización del VM
                viewModel.set_localizationsIdActualUser(new ArrayList<String>());//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("localizationsId").getChildren()){//Almacenamos las id's de las localizaciones favoritas del usuario
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdActualUser().add(localizationId);
                    }
                }
                storeOwnerAndFavouriteLocalizations();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * Interfaz
     * Nombre: storeOwnerAndFavouriteLocalizations
     * Comentario: El método almacena las localizaciones propias y/o favoritas del usuario actual en el VM.
     * Este método es llamado por la función "storeFavoutireLocalizationsId". Por último llama a la función
     * "loadLocalizationPoints" para cargar las localizaciones en el mapa actual.
     * Cabecera: private void storeOwnerAndFavouriteLocalizations()
     * Postcondiciones: El método almacena las localizaciones propias y/o favoritas del usuario actual en el VM.
     */
    private void storeOwnerAndFavouriteLocalizations(){
        localizationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getContext() != null){//Si se encuentra en el contexto actual
                    viewModel.get_localizationPointsMapbox().clear();//Limpiamos la lista de puntos de localización del VM
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);

                        if(localizationPoint != null && ((viewModel.get_localizationsIdActualUser().contains(localizationPoint.getLocalizationPointId())//Si la localización es del usuario actual o la tiene marcada como favorita
                                || (localizationPoint.getEmailCreator() != null && localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser()))))){
                            viewModel.get_localizationPointsMapbox().add(localizationPoint);
                        }
                    }

                    if(mapView != null)
                        loadOnMapReadyCallback();//Cargamos los elementos instanciados en onMapReady
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Funciones sobre la lista de botones inferior

    /**
     * Interfaz
     * Nombre: mostrarAccionesSobreElMapa
     * Comentario: El método muestra el LinearLayout inferior, dejando ver los botones que interactuan
     * con el mapa, ocultando el FrameLayout inferior.
     * Cabecera: private void mostrarAccionesSobreElMapa()
     * Postcondiciones: El método muestra el LinearLayout inferior, ocultando el FrameLayout inferior.
     */
    private void mostrarAccionesSobreElMapa(){
        linearLayoutInferior.setVisibility(View.VISIBLE);
        frameLayoutInferior.setVisibility(View.GONE);
    }

    /**
     * Interfaz
     * Nombre: mostrarAccionesSobreUnMarcador
     * Comentario: El método muestra el FrameLayout inferior, dejando ver los botones que interactuan
     * con un marcador, ocultando el LinearLayout inferior.
     * Cabecera: private void mostrarAccionesSobreUnMarcador()
     * Postcondiciones: El método muestra el FrameLayout inferior, ocultando el LinearLayout inferior.
     */
    private void mostrarAccionesSobreUnMarcador(){
        linearLayoutInferior.setVisibility(View.GONE);
        frameLayoutInferior.setVisibility(View.VISIBLE);
    }

    //Método para la inserción del fragmento inferior

    /**
     * Interfaz
     * Nombre: insertInferiorFragment
     * Comentario: Este método inserta el fragmento FragmentOfflineLocalizationPointClick en el
     * FrameLayout inferior del fragmento actual.
     * Cabecera: private void insertInferiorFragment()
     * Postcondiciones: El método carga el fragmento "FragmentOfflineLocalizationPointClick" en el FrameLayout
     * "FrameLayoutLocalizationClicked".
     */
    private void insertInferiorFragment(){
        FragmentOfflineLocalizationPointClick fragment = new FragmentOfflineLocalizationPointClick();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.FrameLayoutLocalizationClicked, fragment);
        transaction.commit();
    }
}
