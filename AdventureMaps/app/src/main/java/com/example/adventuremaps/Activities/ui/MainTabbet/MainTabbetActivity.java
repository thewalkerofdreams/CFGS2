package com.example.adventuremaps.Activities.ui.MainTabbet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.adventuremaps.Activities.ChangePasswordActivity;
import com.example.adventuremaps.Activities.CreateRouteActivity;
import com.example.adventuremaps.Activities.MainActivity;
import com.example.adventuremaps.Activities.Tutorial.TutorialViewPagerActivity;
import com.example.adventuremaps.Fragments.FragmentLocalizations;
import com.example.adventuremaps.Fragments.FragmentMaps;
import com.example.adventuremaps.Fragments.FragmentRoutes;
import com.example.adventuremaps.Fragments.FragmentStart;
import com.example.adventuremaps.Fragments.FragmentStartLocalizationPointClick;
import com.example.adventuremaps.Fragments.FragmentUser;
import com.example.adventuremaps.Fragments.GoogleMapsStartFragment;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
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
    private ViewPager viewPager;
    private FragmentStartLocalizationPointClick fragment = new FragmentStartLocalizationPointClick();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbet);

        //Instanciamos el objeto SharedPreference
        sharedpreferences = this.getSharedPreferences("UserActualEmail", MODE_PRIVATE);

        //Instanciamos el ViewModel
        viewModel = ViewModelProviders.of(this).get(MainTabbetActivityVM.class);

        //Instanciamos el VM
        viewPager = findViewById(R.id.view_pager);//Instanciamos el ViewPager
        loadViewPager();//Lo cargamos con los datos
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //Cargamos los filtros chequeados, al principio todos los filtros se encuentran check
        final String[] filterItems = {getResources().getString(R.string.potable_water),
                getResources().getString(R.string.food), getResources().getString(R.string.rest_area), getResources().getString(R.string.hunting),
                getResources().getString(R.string.hotel), getResources().getString(R.string.natural_site), getResources().getString(R.string.fishing),
                getResources().getString(R.string.vivac)};
        viewModel.set_checkedFilters(new ArrayList<>(Arrays.asList(filterItems)));

        String email = getIntent().getStringExtra("LoginEmail");//Si la cuenta ya estaba abierta, email se encontrará vacío.
        if(!email.isEmpty()){//Si no hay ninguna cuenta iniciada
            viewModel.set_actualEmailUser(email);//Almacenamos en el VM el email del usuario actual
            SharedPreferences.Editor editor = sharedpreferences.edit();//Además guardamos este email con el objeto SharedPreferences
            editor.putString("UserActualEmail", email);
            editor.commit();
        }else{//Si la cuenta ya estaba abierta, almacenamos en el VM el email del usuario actual
            viewModel.set_actualEmailUser(sharedpreferences.getString("UserActualEmail", ""));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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
     * Comentario: Este método lanza la actividad CreateRouteActivity
     * Cabecera: public void throwCreateRouteActivity(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método lanza la actividad CreateRouteActivity.
     */
    public void throwCreateRouteActivity(View v){
        startActivity(new Intent(this, CreateRouteActivity.class).putExtra("ActualEmail", viewModel.get_actualEmailUser()));
    }

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
            public void onPageSelected(int position) {
                if(position == 0 && viewModel.get_localizationPointClicked() != null){//Si se pasa a la sección de inicio y había un un punto de localización seleccionado
                    viewModel.set_localizationPointClicked(null);//Deshabilitamos la posición clicada
                    reloadInitialFragment();//Recargamos el fragmento
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.title_logout);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_logout);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));//Lanzamos la actividad MainActivity
                finish();//Cerramos la actividad actual
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //Fragment Routes
    /**
     * Interfaz
     * Nombre: onClickRouteFav
     * Comentario: Este método nos permite modificar el estado de favorito de una ruta del usuario actual.
     * Cabecera: public void onClickRouteFav(View v)
     * @param v
     * Postcondiciones: El método modifica el estado del atributo favorito de una ruta, realizando el cambio
     * en la base de datos de la plataforma de FireBase, además cambia el icono del item clicado a una estrella
     * vacía si se encontraba en favoritos cuando se pulso y viceversa.
     */
    public void onClickRouteFav(View v){
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        ImageButton btnChild = (ImageButton)vwParentRow.getChildAt(0);//Obtenemos el botón de favoritos
        String routeId = (String) btnChild.getTag();//Obtenemos el id de la ruta a modificar

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Map<String, Object> hopperUpdates = new HashMap<>();

        if(btnChild.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si la ruta esta marcada como favorita
            btnChild.setBackgroundResource(R.drawable.empty_star);
            hopperUpdates.put("favourite", false);
        }else{
            btnChild.setBackgroundResource(R.drawable.fill_star);
            hopperUpdates.put("favourite", true);
        }
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(routeId).updateChildren(hopperUpdates);
    }

    //Fragment Localizations

    /**
     * Interfaz
     * Nombre: onClickLocalizationFav
     * Comentario: Este método nos permite modificar el estado de favorito de una localización del usuario actual.
     * Cabecera: public void onClickLocalizationFav(View v)
     * @param v
     * Postcondiciones: El método guarda la localización como favorita para el usuario actual, realizando el cambio
     * en la base de datos de la plataforma de FireBase, además cambia el icono del item clicado a una estrella
     * vacía si se encontraba en favoritos cuando se pulso y viceversa.
     */
    public void onClickLocalizationFav(View v){
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        ImageButton btnChild = (ImageButton)vwParentRow.getChildAt(0);//Obtenemos el botón de favoritos
        String localizationId = (String) btnChild.getTag();//Obtenemos el id de la ruta a modificar

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        if(btnChild.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si la ruta esta marcada como favorita
            btnChild.setBackgroundResource(R.drawable.empty_star);
            databaseReference.child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).child("localizationsId").child(localizationId).removeValue();
        }else{
            btnChild.setBackgroundResource(R.drawable.fill_star);
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("localizationsId").child(localizationId)
                    .setValue(localizationId);
        }
    }

    @Override//Controlamos la respuesta a los permisos
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case ApplicationConstants.REQUEST_CODE_PERMISSIONS_MAIN_TABBET_ACTIVITY_WITH_START_MAP:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//Si se concedieron los permisos
                    PlaceholderFragment.newInstance(1);//Recargamos el fragment
                }
                break;
            case ApplicationConstants.REQUEST_CODE_PERMISSIONS_MAIN_TABBET_ACTIVITY_WITH_OFFLINE_MAPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//Si se concedieron los permisos
                    PlaceholderFragment.newInstance(4);//Recargamos el fragment
                }
                break;
        }
    }

    //Fragment Star

    /**
     * Interfaz
     * Nombre: replaceFragment
     * Comentario: Este método nos permite crear un fragmento y remplazar el contenido de nuestro
     * FrameLayout por ese mismo fragmento.
     * Cabecera: public void replaceFragment()
     * Postcondiciones: El método reemplaza el contenido del FrameLayout por el nuevo fragmento.
     * */
    public void replaceFragment(){
        fragment = new FragmentStartLocalizationPointClick();
        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
        transation.replace(R.id.FrameLayout02, fragment);
        transation.commit();
    }

    /**
     * Interfaz
     * Nombre: removeYourFragment
     * Comentario: Este método nos permite eliminar el fragmento de la actividad actual.
     * Cabecera: public void removeYourFragment()
     * Postcondiciones: El método elimina el fragmento de la actividad actual.
     * */
    public void removeYourFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.remove(fragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragment = null;
        }
    }

    /**
     * Interfaz
     * Nombre: showFilterDialog
     * Comentario: Este método muestra por pantalla un dialogo con los diferentes filtros, aplicados
     * sobre el mapa, si el usuario confirma el dialogo, se aplicará la configuración actual dependiendo
     * de los filtros que haya seleccionado.
     * Cabecera: public void showFilterDialog(View view)
     * Entrada:
     *  -View view
     * Postcondiciones: El método abre un dialogo de filtros, si el usuario confirma el dialogo se
     * realizara un filtrado sobre el mapa actual.
     */
    public void showFilterDialog(View view){
        final String[] filterItems = {getResources().getString(R.string.potable_water),
                getResources().getString(R.string.food), getResources().getString(R.string.rest_area), getResources().getString(R.string.hunting),
                getResources().getString(R.string.hotel), getResources().getString(R.string.natural_site), getResources().getString(R.string.fishing),
                getResources().getString(R.string.vivac)};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
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
                //Volvemos a cargar el fragmento del mapa con el nuevo filtrado
                GoogleMapsStartFragment fragment = new GoogleMapsStartFragment();
                FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
                transation.replace(R.id.fragmentGoogleMapsStart, fragment);
                transation.commit();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Interfaz
     * Nombre: navigateToLocalization
     * Comentario: Este método nos permite navegar a una localización seleccionada.
     * Cabecera: public void navigateToLocalization(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método carga en el mapa principal la localización seleccionada.
     */
    public void navigateToLocalization(View v){
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        ImageButton btnChild = (ImageButton)vwParentRow.getChildAt(0);//Obtenemos el botón de favoritos
        String id = (String) btnChild.getTag();//Obtenemos el id de la ruta a modificar

        DatabaseReference localizationference = FirebaseDatabase.getInstance().getReference("Localizations");

        localizationference.orderByChild("localizationPointId").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double latitude = 0, longitude = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    latitude = data.child("latitude").getValue(Double.class);
                    longitude = data.child("longitude").getValue(Double.class);
                }
                viewModel.set_latLngToNavigate(new LatLng(latitude, longitude));
                reloadInitialFragment();//Recargamos el fragmento inicial
                viewPager.setCurrentItem(0);//Lanzamos el fragmento principal
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * Interfaz
     * Nombre: reloadInitialFragment
     * Comentario: Este método nos permite recargar el fragmento inicial de la aplicación.
     * Cabecera: public void reloadInitialFragment()
     * Postcondiciones: El método recarga el fragmento inicial de la aplicación.
     */
    public void reloadInitialFragment(){
        viewPager.getAdapter().notifyDataSetChanged();//Recargamos el fragmento inicial
    }
}