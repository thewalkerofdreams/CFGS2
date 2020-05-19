package com.example.adventuremaps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.Activities.ImageGalleryViewPagerActivity;
import com.example.adventuremaps.Models.ClsImageWithId;
import com.example.adventuremaps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPageAdapter extends PagerAdapter
{
    private Context context;
    private ArrayList<ClsImageWithId> images;
    private LinearLayout itemImage;

    public MyPageAdapter(Context context, ArrayList<ClsImageWithId> images){
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount()
    {
        return images.size();
    }

    @Override @NonNull
    public Object instantiateItem(@NonNull ViewGroup collection, int position)//cargará el archivo de la página XML a mostrar
    {
        loadImage(position);

        ViewPager vp=(ViewPager) collection;//Generamos un ViewPager con el ViewGroup collection
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((ImageGalleryViewPagerActivity)context).changeRatingBarToGone();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.addView(itemImage, 0);//Le agregamos la página actual con indice 0.
        return itemImage;
    }

    /**
     * Interfaz
     * Nombre: loadImage
     * Comentario: Este método nos permite inflar el layout del item del PagerAdapter con la imagen de una posición específica.
     * Cebecera: private void loadImage(int position)
     * Entrada:
     *  -int position
     * Precondiciones:
     *  -position debe hacer referencia a una imagen existente de la lista obtenida por parámetros.
     * Postcondiciones: El método infla el layout del item con una imagen de la lista, dada su posición.
     * */
    private void loadImage(int position){
        itemImage = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_image_view_pager, null);
        ImageView image = itemImage.findViewById(R.id.itemImage);
        Picasso.with(context).load(images.get(position).get_uri()).fit().centerCrop().into(image);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull View collection, int position, @NonNull Object view)
    {
        ((ViewPager) collection).removeView((View) view);
    }
}