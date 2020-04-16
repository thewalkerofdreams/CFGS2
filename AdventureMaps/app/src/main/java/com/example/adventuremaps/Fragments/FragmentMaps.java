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
import com.example.adventuremaps.Models.ClsMarkerWithLocalizationMapbox;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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
    private Button downloadButton;
    private Button listButton;
    private LinearLayout linearLayoutInferior;
    private FrameLayout frameLayoutInferior;
    //Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;
    //Insert Markers
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos referencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
    private SymbolManager symbolManager;

    private OnFragmentInteractionListener mListener;

    public FragmentMaps() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        //Insertamos el fragmento "FragmentOfflineLocalizationPointClick" en el FrameLayout inferior
        FragmentOfflineLocalizationPointClick fragment = new FragmentOfflineLocalizationPointClick();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.FrameLayoutLocalizationClicked, fragment);
        transaction.commit();

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

            //Instanciamos el progressBar
            progressBar = getActivity().findViewById(R.id.progress_bar);

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
                //Limpiamos los posibles símbolos insertados en el mapa
                if(symbolManager != null){//Si el administrador ya fue instanciado
                    symbolManager.delete(viewModel.get_markersInserted());//Elimminamos todos los símbolos que inserto anteriormente
                }
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        CameraPosition position = new CameraPosition.Builder()//Movemos la camara del mapa a la posición del usuario actual
                                .target(new LatLng(viewModel.get_actualLocation().getLatitude(),
                                        viewModel.get_actualLocation().getLongitude()))
                                .zoom(10)
                                .tilt(20)
                                .build();
                        map.setCameraPosition(position);
                        //Declaramos el evento de clicado del mapa
                        map.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                Toast.makeText(getActivity(), "Click al mapa", Toast.LENGTH_SHORT).show();
                                mostrarAccionesSobreElMapa();
                                return false;
                            }
                        });

                        //Creamos un objeto symbol manager
                        symbolManager = new SymbolManager(mapView, mapboxMap, style);

                        //Declaramos los eventos de los marcadores
                        symbolManager.addClickListener(new OnSymbolClickListener() {
                            @Override
                            public void onAnnotationClick(Symbol symbol) {
                                Toast.makeText(getActivity(), "Click corto", Toast.LENGTH_SHORT).show();
                                viewModel.set_localizationPointClickedMapbox(symbol.getLatLng());
                                mostrarAccionesSobreUnMarcador();
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

        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        HttpRequestUtil.setLogEnabled(false);//Nos permite deshabilitar los logs cuando la actuvidad se pause
        clearAmbientCache();//Limpiamos la caché del mapa
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        mapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        userReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_localizationPointsMapbox().clear();//Limpiamos la lista de puntos de localización del VM
                cleanAllLocalizations();
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
                    //cleanAllLocalizations();
                    viewModel.get_localizationPointsWithMarkerMapbox().clear();//Limpiamos la lista de puntos de localización que contienen los marcadores
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);

                        if(localizationPoint != null && ((viewModel.get_localizationsIdActualUser().contains(localizationPoint.getLocalizationPointId())//Si la localización es del usuario actual o la tiene marcada como favorita
                                || (localizationPoint.getEmailCreator() != null && localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser()))))){
                            viewModel.get_localizationPointsMapbox().add(localizationPoint);
                        }
                    }

                    loadOnMapReadyCallback();//Cargamos los elementos instanciados en onMapReady
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Métodos de marcado

    /**
     * Interfaz
     * Nombre: loadLocalizationPoints
     * Comentario: Este método nos permite cargar en el mapa los puntos de localización almacenados en el VM.
     * Cabecera: private void loadLocalizationPoints()
     * Postcondiciones: El método carga los puntos de localización en el mapa actual.
     */
    private void loadLocalizationPoints(){
        for(int i = 0; i < viewModel.get_localizationPointsMapbox().size(); i++){
            colocarMarcador(viewModel.get_localizationPointsMapbox().get(i)); //Comenzamos a marcar las localizaciones almacenadaa
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
        if(map != null){//Si se ha cargado la referencia al mapa de inicio
            Marker marker = map.addMarker(markerOptions);//Agregamos el marcador a la UI

            adjustMarkerType(marker, localizationPoint);//Ajustamos el marcador actual
            viewModel.get_localizationPointsWithMarkerMapbox().add(new ClsMarkerWithLocalizationMapbox(marker, localizationPoint));//Almacenamos el Marcador en un modelo

            //Si el marcador colocado es igual al marcador seleccionado almacenado en el VM (Nos permite conservar el color del marker seleccionado cuando cambiamos de pantalla)
            /*if(viewModel.get_localizationPointClickedMapbox() != null && viewModel.get_localizationPointClickedMapbox().getPosition().getLatitude() == marker.getPosition().getLatitude() &&
                    viewModel.get_localizationPointClickedMapbox().getPosition().getLongitude() == marker.getPosition().getLongitude()){
                viewModel.set_localizationPointClickedMapbox(marker);//Almacenamos la referencia al nuevo marcador
                //onMarkerClick(viewModel.get_localizationPointClickedMapbox());//Volvemos a seleccionarlo
            }*/
        }
    }

    /**
     * Interfaz
     * Nombre: adjustMarkerType
     * Comentario: Este método nos permite ajustar un marcador en específico, insertandole un icono
     * y un id, que lo permita distinguir del resto de marcadores que no son del mismo tipo. Este
     * método es llamado desde dentro del método "colocarMarcador".
     * Cabecera: public void private(Marker marker, ClsLocalizationPoint localizationPoint)
     * Entrada:
     *  -Marker marker
     *  -ClsLocalizationPoint localizationPoint
     * Postcondciones: El método inserta un icono y un id al marcador pasado por parámetros.
     */
    private void adjustMarkerType(Marker marker, ClsLocalizationPoint localizationPoint){
        if(viewModel.get_localizationsIdActualUser() != null && viewModel.get_localizationsIdActualUser().contains(localizationPoint.getLocalizationPointId())){//Si el usuario marcó como favorita la ruta
            setIconToMarker(marker, String.valueOf(R.drawable.marker_fav));//Le colocamos el icono al marcador
            marker.setId(0);//1 indica que el marcador esta marcado como favorito
        }else{
            if(localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización es del usuario actual
                setIconToMarker(marker, String.valueOf(R.drawable.own_location));//Le colocamos el icono al marcador
                marker.setId(1);//1 indica que el marcador es del usuario
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
        if(marker != null){
            switch ((int) marker.getId()){
                case 0://Si esta marcado como favorito
                    setIconToMarker(marker, String.valueOf(R.drawable.marker_fav));//Le colocamos el icono al marcador
                    break;
                case 1://Si es del usuario actual
                    setIconToMarker(marker, String.valueOf(R.drawable.own_location));//Le colocamos el icono al marcador
                    break;
            }
        }
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
        for(int i = 0; i < viewModel.get_localizationPointsWithMarkerMapbox().size(); i++){
            viewModel.get_localizationPointsWithMarkerMapbox().get(i).getMarker().remove();
        }
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
                //Ajustamos la imagen del icono
                BitmapDrawable bitmapdraw = (BitmapDrawable) getContext().getResources().getDrawable(Integer.valueOf(addressIcon));
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), ApplicationConstants.MARKER_WITH_SIZE, ApplicationConstants.MARKER_HEIGHT_SIZE, false);
                //Creamos un objeto icono para la imagen del marcador
                IconFactory iconFactory = IconFactory.getInstance(getActivity());
                Icon icon = iconFactory.fromBitmap(smallMarker);
                //Añadimos el icono al marcador
                marker.setIcon(icon);
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
}
