package com.example.adventuremaps.Fragments.Tutorial;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.Activities.Tutorial.OnBackPressListener;
import com.example.adventuremaps.Adapters.ViewPagerAdapter;
import com.example.adventuremaps.R;

import com.viewpagerindicator.TabPageIndicator;

public class CarouselFragment extends Fragment {

    private TabPageIndicator indicator;
    private ViewPager pager;
    private ViewPagerAdapter adapter;


    public CarouselFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragment
        View rootView = inflater.inflate(R.layout.fragment_carousel, container, false);

        //Instanciamos los elementos de la UI
        indicator = rootView.findViewById(R.id.tpi_header);
        pager = rootView.findViewById(R.id.vp_pages);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Instanciamos el adapter con los fragmentos hijos
        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
    }

    /**
     * Interfaz
     * Nombre: onBackPressed
     * Comentario: Recuperamos el fragmento anterior de la pestaña actualmente visible y propagamos la
     * devolución de llamada onBackPressed
     * Cabecera: public boolean onBackPressed()
     * @return true = Si este fragmento y/o uno de sus fragmentos asociados puede soportar backPress
     */
    public boolean onBackPressed() {//TODO Revisar en la última revisión del proyecto
        //El fragment tab visible actual
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());

        if (currentFragment != null) {
            //Veremos si el actual fragmento o alguno de sus descendientes puede soportar onBackPressed
            return currentFragment.onBackPressed();
        }

        //Este fragmento nos puede soportar la llamada a onBackPressed
        return false;
    }

}
