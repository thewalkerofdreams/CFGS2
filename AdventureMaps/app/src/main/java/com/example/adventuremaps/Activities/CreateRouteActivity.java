package com.example.adventuremaps.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Fragments.GoogleMapsFragment;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.CreateRouteActivityVM;
import com.google.android.gms.maps.model.LatLng;

public class CreateRouteActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CreateRouteActivityVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        //Si la aplicación no tiene los permisos necesarios, muestra por pantalla un dialogo para obtenerlos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        //Si la aplicación tiene los permisos de localización se instancia el VM
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel = ViewModelProviders.of(this).get(CreateRouteActivityVM.class);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//Resultado de repuesta de un permiso
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String mensaje = "";

        switch(requestCode){
            case 1:
                mensaje = "Coarse Location and Fine Location";
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(CreateRouteActivity.this, mensaje+" Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateRouteActivity.this, mensaje+" Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*
     * Interfaz
     * Nombre: intentarMarcarLocalizacion
     * Comentario: Este método nos permite marcar la última lozalización pulsada en el mapa.
     * Si no existe una localización marcada anteriormente, no se creará ningún marcador.
     * Cabecera: public boolean intentarMarcarLocalizacion(View view)
     * Entrada:
     *   -View view
     * Postcondiciones: El método marca la última localización pulsada en el mapa, si existe
     * alguna.
     * */
    public void intentarMarcarLocalizacion(View view){

        if(viewModel.get_lastLocalizationClicked() != null){//Si se ha marcado una localización
            FragmentManager fm = getSupportFragmentManager();
            GoogleMapsFragment fragment = (GoogleMapsFragment)fm.findFragmentById(R.id.fragmentGoogleMapsCreateRouteActivity);
            LatLng latLng = new LatLng(viewModel.get_lastLocalizationClicked().getLatitude(), viewModel.get_lastLocalizationClicked().getLongitude());
            //Marcamos esa posición
            fragment.colocarMarcador(latLng, viewModel.get_localizationPoints());
        }
    }
}
