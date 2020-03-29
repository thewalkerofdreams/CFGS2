package com.example.adventuremaps.Activities.Tutorial;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class BackPressImpl implements OnBackPressListener {

    private Fragment parentFragment;

    public BackPressImpl(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public boolean onBackPressed() {
        boolean canHandle = false;
        int childCount = 0;

        if (parentFragment != null){//Si tiene algún fragmento padre
            childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();//Obtenemos el número de fragmentos hijo

            if (childCount != 0) {  //Si no tiene fragmentos hijos no puede soportar la función onBackPressed por si mismo
                //Obtenemos el fragmento hijo
                FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
                OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);

                //Propagamos el método onBackPressed al fragmento hijo
                if (!childFragment.onBackPressed()) {//Si el hijo es incapaz de soportarlo, suele ocurrir cuando el hijo es el último de la cadena
                    childFragmentManager.popBackStackImmediate();//Eliminamos el hijo del stack
                }

                //Si el fragmento o el hijo soporta la tarea
                canHandle = true;
            }
        }

        return canHandle;
    }
}