package com.example.adventuremaps.Management;

import android.content.Context;
import android.content.res.Configuration;

public class UtilDispositive {

    /**
     * Interfaz
     * Nombre: isTablet
     * Comentario: El método verifica si el dispositivo actual esta siendo manejado
     * en una pantalla grande.
     * Cabecera:   public static boolean isTablet(Context context)
     * Entrada:
     *  -Context context
     * Salida:
     *  -isTablet
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si el
     * dispositivo tiene una pantalla grande como una tablet o false en caso contrario.
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
