package com.example.adventuremaps.Adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

public class MapViewPager extends ViewPager {

    public MapViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Interfaz
     * Nombre: canScroll
     * Comentario: Método sobreescrito que nos permite adaptar el evento swipe, acortando el rango de
     * este mismo evento. Esto nos ayuda a interactuar mejor con los mapas de la aplicación, evitando
     * salir de la pantalla del mapa al arrastrar un dedo en horizontal sobre este.
     * Cabecera: protected boolean canScroll(View v, boolean checkV, int dx, int x, int y)
     * @param v
     * @param checkV
     * @param dx
     * @param x
     * @param y
     * @return
     */
    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return v instanceof SurfaceView || v instanceof PagerTabStrip || (super.canScroll(v, checkV, dx, x, y));
    }
}