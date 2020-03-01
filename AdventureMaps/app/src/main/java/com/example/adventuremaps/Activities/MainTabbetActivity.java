package com.example.adventuremaps.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.adventuremaps.Activities.ui.main.PlaceholderFragment;
import com.example.adventuremaps.Fragments.FragmentLocalizations;
import com.example.adventuremaps.Fragments.FragmentMaps;
import com.example.adventuremaps.Fragments.FragmentRoutes;
import com.example.adventuremaps.Fragments.FragmentStart;
import com.example.adventuremaps.Fragments.FragmentUser;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainActivityVM;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adventuremaps.Activities.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class MainTabbetActivity extends AppCompatActivity implements FragmentStart.OnFragmentInteractionListener, FragmentLocalizations.OnFragmentInteractionListener,
        FragmentRoutes.OnFragmentInteractionListener, FragmentMaps.OnFragmentInteractionListener, FragmentUser.OnFragmentInteractionListener {

    private MainTabbetActivityVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbet);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(MainTabbetActivityVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("LoginEmail"));
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

    @Override//Controlamos la respuesta a los permisos
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PlaceholderFragment.newInstance(4);//Recargamos el fragment
            }
        }
    }
}