package com.example.listadopersonas_va.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.Configuration;
import android.os.Bundle;

import com.example.listadopersonas_va.DDBB_Entities.ClsPersona;
import com.example.listadopersonas_va.Fragments.AddPersonFragment;
import com.example.listadopersonas_va.Fragments.DetailsPersonFragment;
import com.example.listadopersonas_va.Fragments.MasterFragment;
import com.example.listadopersonas_va.R;
import com.example.listadopersonas_va.ViewModels.MainPageVM;

public class MainActivity extends AppCompatActivity {


    Fragment master, details, add;
    MainPageVM viewModel;
    int orientacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainPageVM.class);

        master = new MasterFragment();
        details = new DetailsPersonFragment();
        add = new AddPersonFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        orientacion = getResources().getConfiguration().orientation;

        if(orientacion == Configuration.ORIENTATION_PORTRAIT){//Modo Portrait
            fragmentTransaction.replace(R.id.fragment, master);

            /*El observer*/
            final Observer<ClsPersona> personObserver = new Observer<ClsPersona>() {
                @Override
                public void onChanged(ClsPersona person) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, details).addToBackStack(null).commit();
                }
            };

            //Observo el LiveData con ese observer que acabo de crear
            viewModel.get_personSelected().observe(this, personObserver);

            /*El observer para el fragment de añadir*/
            final Observer<Boolean> isBtnAddPressedObserver = new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isPressed) {
                    if(viewModel.isAddBtnPressed().getValue()) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment, add).addToBackStack(null).commit();
                        viewModel.setIsAddBtnPressed(false);
                    }
                }
            };

            //Observo el LiveData con ese observer que acabo de crear
            viewModel.isAddBtnPressed().observe(this, isBtnAddPressedObserver);

        }else {
            if(orientacion == Configuration.ORIENTATION_LANDSCAPE){   //Modo Landscape
                fragmentTransaction.replace(R.id.masterFragment, master);
                fragmentTransaction.replace(R.id.detailsFragment, details);

                /*El observer*/
                final Observer<ClsPersona> personObserver = new Observer<ClsPersona>() {
                    @Override
                    public void onChanged(ClsPersona person) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.detailsFragment, details).addToBackStack(null).commit();
                    }
                };

                //Observo el LiveData con ese observer que acabo de crear
                viewModel.get_personSelected().observe(this, personObserver);

                /*El observer para el fragment de añadir*/
                final Observer<Boolean> isBtnAddPressedObserver = new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isPressed) {
                        if(viewModel.isAddBtnPressed().getValue()) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.detailsFragment, add).addToBackStack(null).commit();
                            viewModel.setIsAddBtnPressed(false);
                        }
                    }
                };

                //Observo el LiveData con ese observer que acabo de crear
                viewModel.isAddBtnPressed().observe(this, isBtnAddPressedObserver);
            }
        }

        fragmentTransaction.commit();
    }
}
