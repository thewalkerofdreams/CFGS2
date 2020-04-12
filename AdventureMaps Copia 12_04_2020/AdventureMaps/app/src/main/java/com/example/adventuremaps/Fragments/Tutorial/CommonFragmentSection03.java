package com.example.adventuremaps.Fragments.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.TutorialViewPagerActivityVM;

public class CommonFragmentSection03 extends Fragment {//Contendrá las páginas de la sección de localizaciones

    private Button btnLastFragment, btnNextFragment;
    private TutorialViewPagerActivityVM viewModel;

    public CommonFragmentSection03() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos el layout del fragment
        View rootView = inflater.inflate(R.layout.fragment_common, container, false);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(TutorialViewPagerActivityVM.class);

        //Instanciamos los elementos de la UI
        btnLastFragment = rootView.findViewById(R.id.last_button);
        btnLastFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLasFragment();
            }
        });

        btnNextFragment = rootView.findViewById(R.id.next_button);
        btnNextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });

        //Instanciamos el fragmento de inicio de la sección
        loadActualPage();

        return rootView;
    }


    /**
     * Interfaz
     * Nombre: enterNextFragment
     * Comentario: Este método nos permite movernos al siguiente fragmento de la sección, si este existe. Reemplazando
     * el actual por el nuevo.
     * Cabecera: private void enterNextFragment()
     * Postcondiciones: El método reemplaza el fragmento actual por el siguiente a este, si existe algún
     * fragmento posterior.
     */
    private void enterNextFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        btnLastFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de back
        if(viewModel.get_actualSubPageSection3() == 0){//Si estamos la primera página de la sección
            transaction.replace(R.id.fragment_mainLayout, new LocalizationSection02Fragment()).commit();
            btnNextFragment.setVisibility(View.INVISIBLE);//Como no quedan más fragmentos posteriores lo ocultamos
            viewModel.set_actualSubPageSection3(viewModel.get_actualSubPageSection3()+1);//Indicamos hacia que fragmento nos hemos movido
        }
    }

    /**
     * Interfaz
     * Nombre: backToLasFragment
     * Comentario: Este método nos permite movernos al anterior fragmento de la sección, si este existe. Reemplazando
     * el actual por el anterior.
     * Cabecera: private void backToLasFragment()
     * Postcondiciones: El método reemplaza el fragmento actual por el anterior a este, si existe algún
     * fragmento anterior.
     */
    private void backToLasFragment(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        btnNextFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de next por si se encontraba invisible
        if(viewModel.get_actualSubPageSection3() == 1){//Si nos encontrabamos en la última página de la sección
            transaction.replace(R.id.fragment_mainLayout, new LocalizationSection01Fragment()).commit();
            btnLastFragment.setVisibility(View.INVISIBLE);//Como no quedan más fragmentos anteriores lo ocultamos
            viewModel.set_actualSubPageSection3(viewModel.get_actualSubPageSection3()-1);//Indicamos hacia que fragmento nos hemos movido
        }
    }

    /**
     * Interfaz
     * Nombre: loadActualPage
     * Comentario: Este método nos permite cargar la primera página por defecto de la sección,
     * según el valor que tenga actualmente el atributo _actualSubPageSection3 del VM.
     * Cabecera: private void loadActualPage()
     * Postcondiciones: El método carga la página actual de la sección.
     */
    private void loadActualPage(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        switch (viewModel.get_actualSubPageSection3()){
            case 0:
                transaction.replace(R.id.fragment_mainLayout, new LocalizationSection01Fragment()).commit();
                break;
            case 1:
                transaction.replace(R.id.fragment_mainLayout, new LocalizationSection02Fragment()).commit();
                btnLastFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de retorno
                btnNextFragment.setVisibility(View.INVISIBLE);//Volvemos invisible el botón next
                break;
        }
    }
}