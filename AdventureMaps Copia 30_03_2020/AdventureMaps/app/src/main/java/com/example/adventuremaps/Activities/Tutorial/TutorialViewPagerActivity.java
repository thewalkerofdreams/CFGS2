package com.example.adventuremaps.Activities.Tutorial;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.R;
import com.google.android.material.tabs.TabLayout;

public class TutorialViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbet);
        viewPager = findViewById(R.id.view_pager);
        loadViewPager();
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }


    /**
     * Interfaz
     * Nombre: loadViewPager
     * Comentario: Este método nos permite cargar el viewPager de la actividad actual.
     * Cabecera: public void loadViewPager()
     * Postcondiciones: El método carga el viewPager de la actividad actual.
     */
    public void loadViewPager(){
        SectionsPagerAdapterTutorial sectionsPagerAdapterTutorial = new SectionsPagerAdapterTutorial(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapterTutorial);
    }


    @Override
    public void onBackPressed() {
        finish();//Finalizamos la actividad actual
    }
}