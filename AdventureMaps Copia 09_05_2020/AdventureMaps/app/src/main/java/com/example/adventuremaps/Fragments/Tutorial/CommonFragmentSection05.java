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

public class CommonFragmentSection05 extends Fragment {//Contendrá las páginas de la sección de offline
    private Button btnLastFragment, btnNextFragment;
    private TutorialViewPagerActivityVM viewModel;

    public CommonFragmentSection05() {
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
     * Comentario: Este método nos permite movernos al siguiente fragmento de la sección. Reemplazando
     * el actual por el nuevo.
     * Cabecera: private void enterNextFragment()
     * Precondiciones:
     *  -debe existir un fragmento posterior al actual
     * Postcondiciones: El método reemplaza el fragmento actual por el siguiente a este.
     */
    private void enterNextFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        btnLastFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de back por si se encontraba invisible

        switch (viewModel.get_actualSubPageSection5()){
            case 0:
                transaction.replace(R.id.fragment_mainLayout, new OfflineMapsSection02Fragment()).commit();//Remplazamos el fragmento
                break;
            case 1:
                transaction.replace(R.id.fragment_mainLayout, new OfflineMapsSection03Fragment()).commit();//Remplazamos el fragmento
                btnNextFragment.setVisibility(View.INVISIBLE);//Como no quedan más fragmentos posteriores lo ocultamos
                break;
        }

        viewModel.set_actualSubPageSection5(viewModel.get_actualSubPageSection5()+1);//Indicamos hacia que fragmento nos hemos movido
    }

    /**
     * Interfaz
     * Nombre: backToLasFragment
     * Comentario: Este método nos permite movernos al anterior fragmento de la sección. Reemplazando
     * el actual por el anterior.
     * Cabecera: private void backToLasFragment()
     * Precondiciones:
     *  -debe existir un fragmento anterior al actual
     * Postcondiciones: El método reemplaza el fragmento actual por el anterior a este.
     */
    private void backToLasFragment(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        btnNextFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de next por si se encontraba invisible

        switch (viewModel.get_actualSubPageSection5()){
            case 1:
                transaction.replace(R.id.fragment_mainLayout, new OfflineMapsSection01Fragment()).commit();//Remplazamos el fragmento
                btnLastFragment.setVisibility(View.INVISIBLE);//Como no quedan más fragmentos anteriores lo ocultamos
                break;
            case 2:
                transaction.replace(R.id.fragment_mainLayout, new OfflineMapsSection02Fragment()).commit();//Remplazamos el fragmento
                break;
        }

        viewModel.set_actualSubPageSection5(viewModel.get_actualSubPageSection5()-1);//Indicamos hacia que fragmento nos hemos movido
    }

    /**
     * Interfaz
     * Nombre: loadActualPage
     * Comentario: Este método nos permite cargar una página de la sección,
     * según el valor que tenga actualmente el atributo _actualSubPageSection5 del VM.
     * Cabecera: private void loadActualPage()
     * Postcondiciones: El método carga una página de la sección.
     */
    private void loadActualPage(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        switch (viewModel.get_actualSubPageSection5()){
            case 0:
                transaction.replace(R.id.fragment_mainLayout, new OfflineMapsSection01Fragment()).commit();
                break;
            case 1:
                transaction.replace(R.id.fragment_mainLayout, new OfflineMapsSection02Fragment()).commit();
                btnLastFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de retorno
                break;
            case 2:
                transaction.replace(R.id.fragment_mainLayout, new OfflineMapsSection03Fragment()).commit();
                btnLastFragment.setVisibility(View.VISIBLE);//Volvemos visible el botón de retorno
                btnNextFragment.setVisibility(View.INVISIBLE);//Volvemos invisible el botón next
                break;
        }
    }
}
