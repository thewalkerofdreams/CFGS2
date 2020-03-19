package com.example.adventuremaps.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewParent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.Activities.Models.ClsImageWithId;
import com.example.adventuremaps.Activities.Models.ImageRecycle;
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

        ArrayList<ImageRecycle> imageRecycles = new ArrayList<>();
        for(int i = 0; i < viewModel.get_imagesToLoad().size(); i++){
            imageRecycles.add(new ImageRecycle(Uri.parse(viewModel.get_imagesToLoad().get(i).get_uri())));
        }

        MyPageAdapter myAdminPageAdapter = new MyPageAdapter(this, imageRecycles);
        viewPager.setAdapter(myAdminPageAdapter);
        viewPager.setCurrentItem(viewModel.get_positionSelectedImage());
    }
}
