package com.example.adventuremaps.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.Models.ClsImageWithId;
import com.example.adventuremaps.Adapters.MyPageAdapterCreationLocalizationPoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.CreateLocalizationPointViewPagerActivityVM;

import java.util.ArrayList;

public class CreateLocalizationPointImageGalleryViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CreateLocalizationPointViewPagerActivityVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_image_gallery_view_pager_simple);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(CreateLocalizationPointViewPagerActivityVM.class);
        viewModel.set_positionSelectedImage(getIntent().getIntExtra("PositionImageSelected",0));
        viewModel.set_imagesToLoad((ArrayList<ClsImageWithId>) getIntent().getSerializableExtra("ImagesToLoad"));

        //Instanciamos los elementos de la UI
        viewPager = findViewById(R.id.viewPagerCreateLocalizationPoint);
        loadViewPager();
    }


    /**
     * Interfaz
     * Nombre: loadViewPager
     * Comentario: Este método nos permite cargar el viewPager que contiene la galería de imagenes.
     * Cabecera: public void loadViewPager()
     * Postcondiciones: El método carga el viewPager que contiene la galería de imagenes.
     */
    public void loadViewPager(){
        MyPageAdapterCreationLocalizationPoint myAdminPageAdapter = new MyPageAdapterCreationLocalizationPoint(this, viewModel.get_imagesToLoad());
        viewPager.setAdapter(myAdminPageAdapter);
        viewPager.setCurrentItem(viewModel.get_positionSelectedImage());//Mostramos la imagen que se seleccionó en la actividad anterior
    }
}
