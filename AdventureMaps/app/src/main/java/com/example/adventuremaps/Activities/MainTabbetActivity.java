package com.example.adventuremaps.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventuremaps.Activities.ui.main.PlaceholderFragment;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.Fragments.FragmentLocalizations;
import com.example.adventuremaps.Fragments.FragmentMaps;
import com.example.adventuremaps.Fragments.FragmentRoutes;
import com.example.adventuremaps.Fragments.FragmentStart;
import com.example.adventuremaps.Fragments.FragmentStartLocalizationPointClick;
import com.example.adventuremaps.Fragments.FragmentUser;
import com.example.adventuremaps.Fragments.GoogleMapsStartFragment;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainActivityVM;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adventuremaps.Activities.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainTabbetActivity extends AppCompatActivity implements FragmentStart.OnFragmentInteractionListener, FragmentLocalizations.OnFragmentInteractionListener,
        FragmentRoutes.OnFragmentInteractionListener, FragmentMaps.OnFragmentInteractionListener, FragmentUser.OnFragmentInteractionListener {

    private MainTabbetActivityVM viewModel;
    private FragmentStartLocalizationPointClick fragment = new FragmentStartLocalizationPointClick();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbet);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        sharedpreferences = this.getSharedPreferences("UserActualEmail", MODE_PRIVATE);//Instanciamos el objeto SharedPreference

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(MainTabbetActivityVM.class);

        String email = getIntent().getStringExtra("LoginEmail");//Si la cuenta ya estaba abierta, email se encontrará vacío.
        if(!email.isEmpty()){
            viewModel.set_actualEmailUser(email);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("UserActualEmail", email);
            editor.commit();
        }else{
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
     * Nombre: logoutDialog
     * Comentario: Este método nos carga un dialogo por pantalla para cerrar la sesión del usuario actual.
     * Cabecera: public void logoutDialog(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método cierra la sesión del usuario actual, si el usuario confirma el dialogo o
     * no hace nada si el usuario cancela el dialogo.
     */
    public void logoutDialog(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.title_logout);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_logout);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();//Cerramos la actividad actual
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

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

    @Override//Controlamos la respuesta a los permisos
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PlaceholderFragment.newInstance(4);//Recargamos el fragment
            }
        }
    }

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
}