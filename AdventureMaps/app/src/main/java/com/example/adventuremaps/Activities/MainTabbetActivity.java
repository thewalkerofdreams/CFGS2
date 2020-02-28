package com.example.adventuremaps.Activities;

import android.net.Uri;
import android.os.Bundle;

import com.example.adventuremaps.Fragments.FragmentLocalizations;
import com.example.adventuremaps.Fragments.FragmentMaps;
import com.example.adventuremaps.Fragments.FragmentRoutes;
import com.example.adventuremaps.Fragments.FragmentStart;
import com.example.adventuremaps.Fragments.FragmentUser;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainActivityVM;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adventuremaps.Activities.ui.main.SectionsPagerAdapter;

public class MainTabbetActivity extends AppCompatActivity implements FragmentStart.OnFragmentInteractionListener, FragmentLocalizations.OnFragmentInteractionListener,
        FragmentRoutes.OnFragmentInteractionListener, FragmentMaps.OnFragmentInteractionListener, FragmentUser.OnFragmentInteractionListener {

    private MainTabbetActivityVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbet);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(MainTabbetActivityVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("LoginEmail"));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}