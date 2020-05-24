package com.example.adventuremaps.Activities.ui.MainTabbet;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adventuremaps.Activities.ChangePasswordActivity;
import com.example.adventuremaps.Activities.CreateLocalizationPointActivity;
import com.example.adventuremaps.Activities.CreateRouteActivity;
import com.example.adventuremaps.Activities.MainActivity;
import com.example.adventuremaps.Activities.Tutorial.TutorialViewPagerActivity;
import com.example.adventuremaps.Adapters.MapViewPager;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Fragments.FragmentLocalizations;
import com.example.adventuremaps.Fragments.FragmentMaps;
import com.example.adventuremaps.Fragments.FragmentRoutes;
import com.example.adventuremaps.Fragments.FragmentStart;
import com.example.adventuremaps.Fragments.FragmentUser;
import com.example.adventuremaps.Fragments.GoogleMapsStartFragment;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainTabbetActivity extends AppCompatActivity implements FragmentStart.OnFragmentInteractionListener, FragmentLocalizations.OnFragmentInteractionListener,
        FragmentRoutes.OnFragmentInteractionListener, FragmentMaps.OnFragmentInteractionListener, FragmentUser.OnFragmentInteractionListener {

    private MainTabbetActivityVM viewModel;
    private MapViewPager viewPager;//ViewPager adaptado para las secciones que contienen mapas
    private FirebaseUser firebaseCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbet);

        //Instanciamos el objeto SharedPreference
        SharedPreferences sharedpreferences = this.getSharedPreferences(ApplicationConstants.SP_ACTUAL_USER_EMAIL, MODE_PRIVATE);

        //Instanciamos el ViewModel
        viewModel = ViewModelProviders.of(this).get(MainTabbetActivityVM.class);

        //Instanciamos los elementos de la UI
        viewPager = findViewById(R.id.view_pager);//Instanciamos el ViewPager
        loadViewPager();//Lo cargamos con los datos
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //Cargamos los filtros chequeados, al principio todos los filtros se encuentran check
        final String[] filterItems = {getResources().getString(R.string.key_potable_water), getResources().getString(R.string.key_food), getResources().getString(R.string.key_rest_area), getResources().getString(R.string.key_hunting),
                getResources().getString(R.string.key_hotel), getResources().getString(R.string.key_natural_site), getResources().getString(R.string.key_fishing), getResources().getString(R.string.key_vivac), getResources().getString(R.string.key_culture),  getResources().getString(R.string.key_camping)};
        viewModel.set_checkedFilters(new ArrayList<>(Arrays.asList(filterItems)));

        String email = getIntent().getStringExtra(ApplicationConstants.INTENT_LOGIN_EMAIL);//Si la cuenta ya estaba abierta, email se encontrará vacío.
        if(email != null && !email.isEmpty()){//Si no hay ninguna cuenta iniciada
            viewModel.set_actualEmailUser(email);//Almacenamos en el VM el email del usuario actual
            SharedPreferences.Editor editor = sharedpreferences.edit();//Además guardamos este email con el objeto SharedPreferences
            editor.putString(ApplicationConstants.SP_ACTUAL_USER_EMAIL, email);
            editor.apply();
        }else{//Si la cuenta ya estaba abierta, almacenamos en el VM el email del usuario actual
            viewModel.set_actualEmailUser(sharedpreferences.getString(ApplicationConstants.SP_ACTUAL_USER_EMAIL, ""));
        }

        //Instanciamos la referencia del usuario actual
        firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //ThrowActivity Methods

    /**
     * Interfaz
     * Nombre: throwChangePasswordActivity
     * Comentario: Este método lanza la actividad ChangePasswordActivity
     * Cabecera: public void throwChangePasswordActivity(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método lanza la actividad ChangePasswordActivity.
     */
    public void throwChangePasswordActivity(View v){
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    /**
     * Interfaz
     * Nombre: throwTutorialViewPagerActivity
     * Comentario: Este método lanza la actividad TutorialViewPagerActivity.
     * Cabecera: public void throwTutorialViewPagerActivity(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método lanza la actividad TutorialViewPagerActivity.
     */
    public void throwTutorialViewPagerActivity(View v){
        startActivity(new Intent(this, TutorialViewPagerActivity.class));
    }

    /**
     * Interfaz
     * Nombre: throwCreateRouteActivity
     * Comentario: Este método lanza la actividad CreateRouteActivity si el usuario aceptó todos
     * los permisos de localización. El método muestra un mensaje por pantalla si el usuario no los
     * aceptó.
     * Cabecera: public void throwCreateRouteActivity(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método lanza la actividad CreateRouteActivity si el usuario aceptó los permisos
     * de localización, en caso contrario el método solo muestra un mensaje de error por pantalla.
     */
    public void throwCreateRouteActivity(View v){
        //Si el usuario aceptó los permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(this, CreateRouteActivity.class).putExtra(ApplicationConstants.INTENT_ACTUAL_EMAIL, viewModel.get_actualEmailUser()));
        }else{
            Toast.makeText(this, R.string.create_route_permission_error, Toast.LENGTH_SHORT).show();
        }
    }

    //UI Methods

    /**
     * Interfaz
     * Nombre: loadViewPager
     * Comentario: Este método nos permite cargar el viewPager de la actividad actual.
     * Cabecera: public void loadViewPager()
     * Postcondiciones: El método carga el viewPager de la actividad actual.
     */
    public void loadViewPager(){
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {//Cuando el usuario se desplace a una sección del viewPager

                if(viewModel.get_localizationPointClicked() != null && position > 1){//Si existía una localización seleccionada y el fragmento de inicio se cerró
                    viewModel.set_localizationPointClicked(null);//Limpiamos la localización seleccionada
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //Configuramos el adapter del viewPager
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    //Methods Fragment Information

    /**
     * Interfaz
     * Nombre: showLogoutDialog
     * Comentario: Este método carga un dialogo por pantalla para cerrar la sesión del usuario actual.
     * Cabecera: public void showLogoutDialog(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método cierra la sesión del usuario actual, si el usuario confirma el dialogo o
     * no hace nada si el usuario cancela el dialogo.
     */
    public void showLogoutDialog(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.title_logout);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_logout);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();//Le indicamos a la plataforma el logout del usuario
                startActivity(new Intent(getApplicationContext(), MainActivity.class));//Lanzamos la actividad MainActivity
                finish();//Cerramos la actividad actual
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//No hacemos nada
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();//Mostramos el dialogo
    }

    //Methods Fragment Routes
    /**
     * Interfaz
     * Nombre: onClickRouteFav
     * Comentario: Este método nos permite modificar el estado de favorito de una ruta del usuario actual.
     * En el caso de que no haya ningún ítem selecciondo en la lista de rutas, en caso contrario el método
     * muestra un mensaje de error por pantalla.
     * Cabecera: public void onClickRouteFav(View v)
     * @param v
     * Postcondiciones: Si no hay ningún ítem seleccionado en la lista de rutas, el método modifica el estado del
     * atributo favorito de una ruta, realizando el cambio en la base de datos de la plataforma FireBase, además
     * cambia el icono del item clicado a una estrella vacía si se encontraba en favoritos cuando se pulso y viceversa.
     */
    public void onClickRouteFav(View v){
        if(viewModel.get_selectedRoutes().isEmpty()){//Si no hay ningúna ruta seleccionada en la lista
            //get the row the clicked button is in
            LinearLayout vwParentRow = (LinearLayout)v.getParent();
            ImageButton btnChild = (ImageButton)vwParentRow.getChildAt(0);//Obtenemos el botón de favoritos
            String routeId = (String) btnChild.getTag();//Obtenemos el id de la ruta a modificar

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_USERS_ADDRESS);
            Map<String, Object> hopperUpdates = new HashMap<>();

            if(btnChild.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si la ruta esta marcada como favorita
                btnChild.setBackgroundResource(R.drawable.empty_star);//Cambiamos el icono
                hopperUpdates.put(ApplicationConstants.FB_ROUTE_FAVOURITE_CHILD, false);
            }else{
                btnChild.setBackgroundResource(R.drawable.fill_star);
                hopperUpdates.put(ApplicationConstants.FB_ROUTE_FAVOURITE_CHILD, true);
            }
            databaseReference.child(firebaseCurrentUser.getUid()).child(ApplicationConstants.FB_ROUTES_ADDRESS).child(routeId).updateChildren(hopperUpdates);//Realizamos la actualización en la plataforma
        }else{
            Toast.makeText(this, R.string.error_item_selected, Toast.LENGTH_SHORT).show();
        }
    }

    //Permisos

    @Override//Controlamos la respuesta a los permisos
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case ApplicationConstants.REQUEST_CODE_PERMISSIONS_MAIN_TABBET_ACTIVITY_WITH_START_MAP:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//Si se concedieron los permisos de localización
                    viewModel.reloadActualLocalization();//Recargamos la localización del usuario
                    reloadInitialFragment();//Recargamos el fragment inicial
                }
                break;
            case ApplicationConstants.REQUEST_CODE_PERMISSIONS_MAIN_TABBET_ACTIVITY_WITH_OFFLINE_MAPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//Si se concedieron los permisos para la descarga de mapas offline
                    viewModel.reloadActualLocalization();//Recargamos la localización del usuario
                    viewPager.setCurrentItem(0);//Nos movemos a la primera sección
                    Toast.makeText(this, R.string.permission_acepted, Toast.LENGTH_SHORT).show();//Indicamos que se aceptaron los permisos
                }
                break;
        }
    }

    //Methods Fragment Star
    /*/**
     * Interfaz
     * Nombre: removeYourFragment
     * Comentario: Este método nos permite eliminar el fragmento FragmentStartLocalizationPointClick de la actividad actual.
     * Cabecera: public void removeYourFragment()
     * Postcondiciones: El método elimina el fragmento FragmentStartLocalizationPointClick de la actividad actual.
     * */
    /*public void removeYourFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.remove(fragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragment = null;
        }
    }*()

    /**
     * Interfaz
     * Nombre: showFilterDialog
     * Comentario: Este método muestra por pantalla un dialogo con los diferentes filtros aplicados
     * sobre el mapa. Si el usuario confirma el dialogo, se recargará el mapa actual mostrando las
     * localizaciones que cumplan los filtros seleccionados.
     * Cabecera: public void showFilterDialog(View view)
     * Entrada:
     *  -View view
     * Postcondiciones: El método abre un dialogo de filtros, si el usuario confirma el dialogo se
     * realizará un filtrado sobre el mapa actual.
     */
    public void showFilterDialog(View view){
        final String[] filterItems = {getResources().getString(R.string.potable_water),
                getResources().getString(R.string.food), getResources().getString(R.string.rest_area), getResources().getString(R.string.hunting),
                getResources().getString(R.string.hotel), getResources().getString(R.string.natural_site), getResources().getString(R.string.fishing),
                getResources().getString(R.string.vivac), getResources().getString(R.string.culture), getResources().getString(R.string.camping)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.filter_title);
        builder.setMultiChoiceItems(filterItems, viewModel.get_dialogPostisionsChecked(), new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){//Si se ha chequeado un filtro
                    viewModel.get_checkedFilters().add(filterItems[which]);//Añadimos el filtro en la lista del VM
                }else{
                    viewModel.get_checkedFilters().remove(filterItems[which]);//Eliminamos el filtro en la lista del VM
                }
            }
        });
        builder.setPositiveButton(R.string.apply_changes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reloadStartFragmentMaps(); //Volvemos a cargar el fragmento del mapa con el nuevo filtrado
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();//Mostramos el dialogo por pantalla
    }

    /**
     * Interfaz
     * Nombre: reloadStartFragmentMaps
     * Comentario: El método vuelve a cargar el fragmento GoogleMapsStartFragment en el contenedor fragmentGoogleMapsStart.
     * Cabecera: private void reloadStartFragmentMaps()
     * Postcondiciones: El método recarga el fragmento GoogleMapsStartFragment en el contenedor fragmentGoogleMapsStart.
     */
    private void reloadStartFragmentMaps(){
        GoogleMapsStartFragment fragment = new GoogleMapsStartFragment();
        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
        transation.replace(R.id.fragmentGoogleMapsStart, fragment);
        transation.commit();
    }

    //Métodos FragmentLocalizations

    /**
     * Interfaz
     * Nombre: onClickLocalizationFav
     * Comentario: Este método nos permite modificar el estado de favorito de una localización del usuario actual.
     * En el caso de que no haya ningún ítem selecciondo en la lista de localizaciones, en caso contrario el método
     * muestra un mensaje de error por pantalla.
     * Cabecera: public void onClickLocalizationFav(View v)
     * @param v
     * Postcondiciones: Si no hay ningún ítem seleccionado en la lista de localizaciones, el método guarda la modificación
     * sobre el estado de favorito de la localización, almacenando el cambio en la base de datos de la plataforma FireBase,
     * además cambia el icono del item clicado a una estrella vacía si se encontraba en favoritos cuando se pulso y viceversa.
     */
    public void onClickLocalizationFav(View v){
        if(viewModel.get_selectedLocalizations().isEmpty()){//Si no hay ningún punto de localización seleccionado
            //get the row the clicked button is in
            LinearLayout vwParentRow = (LinearLayout)v.getParent();
            ImageButton btnChild = (ImageButton)vwParentRow.getChildAt(0);//Obtenemos el botón de favoritos
            String localizationId = (String) btnChild.getTag();//Obtenemos el id de la ruta a modificar

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_USERS_ADDRESS);

            if(btnChild.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si la ruta esta marcada como favorita
                btnChild.setBackgroundResource(R.drawable.empty_star);
                databaseReference.child(firebaseCurrentUser.getUid()).child(ApplicationConstants.FB_LOCALIZATIONS_ID).child(localizationId).removeValue();//Desmarcamos la localización como favorita
            }else{
                btnChild.setBackgroundResource(R.drawable.fill_star);
                databaseReference.child(firebaseCurrentUser.getUid()).child(ApplicationConstants.FB_LOCALIZATIONS_ID).child(localizationId).setValue(localizationId);//Almacenamos el id de la localización en una lista de favoritas
            }

            reloadInitialFragment();//Recargamos el mapa principal
        }else{
            Toast.makeText(this, R.string.error_item_selected, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interfaz
     * Nombre: navigateToLocalization
     * Comentario: Este método nos permite navegar a una localización específica en el mapa de inicio.
     * Si el usuario no aceptó los permisos de localización, el método solo muestra un mensaje por pantalla
     * explicando el error.
     * Cabecera: public void navigateToLocalization(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: Si el usuario aceptó los permisos de localización, el método nos mueve a la sección
     * del mapa principal, posicionando este sobre el punto de localización almacenado en el VM.
     * En caso contrario el método muestra un mensaje de error por pantalla.
     */
    public void navigateToLocalization(View v){
        //Si la aplicación tiene los permisos necesarios
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //get the row the clicked button is in
            LinearLayout vwParentRow = (LinearLayout)v.getParent();
            ImageButton btnChild = (ImageButton)vwParentRow.getChildAt(0);//Obtenemos el botón de favoritos
            String id = (String) btnChild.getTag();//Obtenemos el id de la ruta a modificar

            DatabaseReference localizationference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);

            localizationference.orderByChild(ApplicationConstants.FB_LOCALIZATION_POINT_ID).equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double latitude = 0, longitude = 0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {//Obtenemos la posición del punto de localización
                        latitude = data.child(ApplicationConstants.FB_LOCATION_LATITUDE).getValue(Double.class);
                        longitude = data.child(ApplicationConstants.FB_LOCATION_LONGITUDE).getValue(Double.class);
                    }
                    viewModel.set_latLngToNavigate(new LatLng(latitude, longitude));//Almacenamos lo posición del punto de localización en el VM
                    reloadInitialFragment();//Recargamos el fragmento inicial
                    viewPager.setCurrentItem(0);//Lanzamos el fragmento principal
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else{
            Toast.makeText(this, R.string.navigate_localization_permission_error, Toast.LENGTH_SHORT).show();
        }
    }

    //Métodos de recarga de la interfaz

    /**
     * Interfaz
     * Nombre: reloadInitialFragment
     * Comentario: Este método nos permite recargar el fragmento inicial de la aplicación.
     * Cabecera: public void reloadInitialFragment()
     * Precondiciones:
     *  -el viewpager que contiene los fragmentos debe contar con un adaptador
     * Postcondiciones: El método recarga el fragmento inicial de la aplicación.
     */
    public void reloadInitialFragment(){
        if(viewPager.getAdapter() != null)
            viewPager.getAdapter().notifyDataSetChanged();//Recargamos el fragmento inicial
    }

    /**
     * Interfaz
     * Nombre: reloadOfflineFragment
     * Comentario: Este método nos permite recargar el fragmento offline de la aplicación.
     * Cabecera: public void reloadOfflineFragment()
     * Precondiciones:
     *  -el viewpager que contiene los fragmentos debe contar con un adaptador
     * Postcondiciones: El método recarga el fragmento offline de la aplicación.
     */
    public void reloadOfflineFragment(){
        if(viewPager.getAdapter() != null){
            viewPager.getAdapter().notifyDataSetChanged();//Recargamos el fragmento offline
            viewPager.setCurrentItem(3);
        }
    }

    //GoogleMapsStartFragment and FragmentMaps (Métodos para la creación de un punto de localización)
    /**
     * Interfaz
     * Nombre: throwCreateLocalizationPointActivity
     * Comentario: Este método lanza la actividad CreateLocalizationPointActivity, para crear una localización
     * en uno de los mapas de la aplicación. Se le pasarán los datos necesarios para poder crear la nueva localización en el
     * lugar seleccionado, para ello se debe indicar la sección donde se realizó la llamada:
     *  1 --> En el mapa de incio
     *  2 --> En el mapa offline
     * Cabecera: public void throwCreateLocalizationPointActivity(int callSection)
     * Entrada:
     *  int callSection
     * Precondiciones:
     *  -callSection debe ser igual a 1 o 2.
     * Postcondiciones: El método lanza la actividad CreateLocalizationPointActivity.
     */
    public void throwCreateLocalizationPointActivity(int callSection){
        double latitude, longitude;
        if(callSection == 1){//Si se llamó desde la sección de inicio
            latitude = viewModel.get_longClickPosition().latitude;//Obtenemos los datos de un ojeto LatLng propio de Google almacenado en el VM
            longitude = viewModel.get_longClickPosition().longitude;
        }else{//Si se llamó desde la sección offline
            latitude = viewModel.get_longClickPositionMapbox().getLatitude();//Obtenemos los datos de un ojeto LatLng propio de Mapbox almacenado en el VM
            longitude = viewModel.get_longClickPositionMapbox().getLongitude();
        }

        Intent intent = new Intent(this, CreateLocalizationPointActivity.class);
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_USER_EMAIL, viewModel.get_actualEmailUser());
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_LATITUDE, latitude);
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_LONGITUDE, longitude);
        startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_CREATE_LOCALIZATION_POINT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ApplicationConstants.REQUEST_CODE_CREATE_LOCALIZATION_POINT) {//Al salir del formulario de creación de una ruta
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {//Si se confirmó el guardado del nuevo punto de localización y se obtuvieron datos de respuesta
                viewModel.set_localizationToSave((ClsLocalizationPoint) data.getExtras().getSerializable(ApplicationConstants.DATA_LOCALIZATION_TO_SAVE));//Guardamos la localización en el VM
                viewModel.set_localizationTypesToSave((ArrayList<String>)data.getSerializableExtra(ApplicationConstants.DATA_LOCALIZATION_TYPES_TO_SAVE));//Obtenemos los tipos de la localización

                viewModel.saveLocalizationPoint();//Almacenamos el punto de localización en FireBase
                Toast.makeText(this, R.string.localization_point_created, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Back Button

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(this.findViewById(R.id.FrameLayout02) != null){//Si la actividad de inicio se encuentra abierta
            this.findViewById(R.id.FrameLayout02).setVisibility(View.GONE);//Volvemos invisible el FrameLayout inferior
        }
        finish();//Cerramos la actividad
    }
}