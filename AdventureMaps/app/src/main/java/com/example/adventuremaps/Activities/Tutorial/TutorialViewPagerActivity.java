package com.example.adventuremaps.Activities.Tutorial;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.adventuremaps.Fragments.Tutorial.CarouselFragment;
import com.example.adventuremaps.R;

public class TutorialViewPagerActivity extends AppCompatActivity {

    private CarouselFragment carouselFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_view_pager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if (savedInstanceState == null) {

            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserve the fragment stack inside each tab
            initScreen();

        } else {
            // restoring the previously created fragment
            // and getting the reference
            carouselFragment = (CarouselFragment) getSupportFragmentManager().getFragments().get(0);
        }
    }

    /**
     * Interfaz
     * Nombre: initScreen
     * Comentario: Este método crea el contendor del ViewPager.
     * Cabecera: private void initScreen()
     */
    private void initScreen() {
        carouselFragment = new CarouselFragment();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, carouselFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        finish();//Finalizamos la actividad actual
    }
}
