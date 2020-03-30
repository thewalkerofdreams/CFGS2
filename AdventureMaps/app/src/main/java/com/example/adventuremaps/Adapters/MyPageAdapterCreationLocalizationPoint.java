package com.example.adventuremaps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.Models.ClsImageWithId;
import com.example.adventuremaps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPageAdapterCreationLocalizationPoint extends PagerAdapter
{
    private Context context;
    private ArrayList<ClsImageWithId> images;
    private LinearLayout itemImage;

    public MyPageAdapterCreationLocalizationPoint(Context context, ArrayList<ClsImageWithId> images){
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount()//Devuelve el número de imágenes que contiene nuestro ViewPage
    {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position)//cargará el archivo de la página XML a mostrar
    {
        loadImage(position);

        ViewPager vp=(ViewPager) collection;//Generamos un ViewPager con el ViewGroup collection
        vp.addView(itemImage, 0);//Le agregamos la página actual con indice 0.
        return itemImage;
    }

    /*
     * Interfaz
     * Nombre: loadImage
     * Comentario: Este método nos permite inflar un layout con su imagen.
     * Cebecera: private void loadImage(int position)
     * */
    private void loadImage(int position){
        itemImage = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_image_view_pager_simple, null);
        ImageView image = itemImage.findViewById(R.id.itemImage);
        Picasso.with(context).load(images.get(position).get_uri()).fit().centerCrop().into(image);
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public void destroyItem(View collection, int position, Object view)
    {
        ((ViewPager) collection).removeView((View) view);
    }
}