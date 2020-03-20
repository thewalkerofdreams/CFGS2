package com.example.adventuremaps.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.Activities.Models.ClsImageWithId;
import com.example.adventuremaps.Adapters.MyPageAdapter;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.ImageGalleryViewPagerActivityVM;

import java.util.ArrayList;

public class ImageGalleryViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageGalleryViewPagerActivityVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_view_pager);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(ImageGalleryViewPagerActivityVM.class);
        Bundle bundle = getIntent().getExtras();
        viewModel.set_imagesToLoad((ArrayList<ClsImageWithId>) bundle.getSerializable("ImagesToLoad"));
        viewModel.set_positionSelectedImage(getIntent().getIntExtra("PositionImageSelected",0));

        //Instanciamos los elementos de la UI
        viewPager = findViewById(R.id.viewPager);

        //Cargamos el viewPager
        MyPageAdapter myAdminPageAdapter = new MyPageAdapter(this, viewModel.get_imagesToLoad());
        viewPager.setAdapter(myAdminPageAdapter);
        viewPager.setCurrentItem(viewModel.get_positionSelectedImage());//Mostramos la imagen que se seleccionó en la actividad anterior
    }
}
