package com.iesnervion.mynotes.ui.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.iesnervion.mynotes.R;

public class ToolsFragment extends Fragment {

    private Button esp, en;
    private SharedPreferences sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        esp = root.findViewById(R.id.buttonEsp);
        en = root.findViewById(R.id.buttonEn);

        esp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarIdioma(view);
            }
        });

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarIdioma(view);
            }
        });

        sharedPref = getActivity().getSharedPreferences("idiomas", Context.MODE_PRIVATE);

        return root;
    }

    /**
     * Cambia el idioma de la aplicacion entera.
     * @param view Vista sobre la que se ha pulsado, en este caso sobre alguno de los botones para cambiar el idioma.
     */
    public void cambiarIdioma(View view) {
        String lang = "";
        SharedPreferences.Editor editor = sharedPref.edit();
        //Vemos que boton ha sido pulsado.
        if(view.getId() == esp.getId()){
            lang = "es";
        }else if(view.getId() == en.getId()){
            lang = "en";
        }
        //Establecemos el idioma segun el boton pulsado.
        editor.putString("idioma", lang);
        editor.apply();
        //Mensaje explicativo
        Toast.makeText(getContext(), R.string.cambiarIdioma, Toast.LENGTH_SHORT).show();
    }
}