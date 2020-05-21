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

public class CommonFragmentSection04 extends Fragment {//Contendrá las páginas de la sección de rutas
    private Button btnLastFragment, btnNextFragment;
    private TutorialViewPagerActivityVM viewModel;

    public CommonFragmentSection04() {
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
     * Comentario: Este método nos permite movernos a la siguiente página de la sección. Reemplazando
     * la actual por la nueva.
     * Cabecera: private void enterNextFragment()
     * Precondiciones:
     *  -debe existir una página (fragmento) posterior la actual
     * Postcondiciones: El método reemplaza la página actual por la siguiente en la sección.
     */
    private void enterNextFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        btnLastFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de back por si se encontraba invisible

        transaction.replace(R.id.fragment_mainLayout, new RouteSection02Fragment()).commit();//Remplazamos el fragmento
        btnNextFragment.setVisibility(View.INVISIBLE);//Como no quedan más fragmentos posteriores lo ocultamos

        viewModel.set_actualSubPageSection4(viewModel.get_actualSubPageSection4()+1);//Indicamos hacia que fragmento nos hemos movido
    }

    /**
     * Interfaz
     * Nombre: backToLasFragment
     * Comentario: Este método nos permite movernos a la página anterior de la sección. Reemplazando
     * la actual por la anterior.
     * Cabecera: private void backToLasFragment()
     * Precondiciones:
     *  -debe existir una página (fragmento) anterior a la actual
     * Postcondiciones: El método reemplaza la página actual por la anterior a esta en la sección.
     */
    private void backToLasFragment(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        btnNextFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de next por si se encontraba invisible

        transaction.replace(R.id.fragment_mainLayout, new RouteSection01Fragment()).commit();//Remplazamos el fragmento
        btnLastFragment.setVisibility(View.INVISIBLE);//Como no quedan más fragmentos anteriores lo ocultamos

        viewModel.set_actualSubPageSection4(viewModel.get_actualSubPageSection4()-1);//Indicamos hacia que fragmento nos hemos movido
    }

    /**
     * Interfaz
     * Nombre: loadActualPage
     * Comentario: Este método nos permite cargar una página de la sección,
     * según el valor que tenga actualmente el atributo _actualSubPageSection4 del VM.
     * Cabecera: private void loadActualPage()
     * Postcondiciones: El método carga una página de la sección.
     */
    private void loadActualPage(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        switch (viewModel.get_actualSubPageSection4()){
            case 0:
                transaction.replace(R.id.fragment_mainLayout, new RouteSection01Fragment()).commit();
                break;
            case 1:
                transaction.replace(R.id.fragment_mainLayout, new RouteSection02Fragment()).commit();
                btnLastFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de retorno
                btnNextFragment.setVisibility(View.INVISIBLE);//Volvemos invisible el botón next
                break;
        }
    }
}
