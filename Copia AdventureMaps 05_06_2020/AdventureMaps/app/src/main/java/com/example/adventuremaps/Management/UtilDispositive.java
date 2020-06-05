package com.example.adventuremaps.Management;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilDispositive {

    /**
     * Interfaz
     * Nombre: isOnline
     * Comentario: Este método nos permite verificar si el dispositivo actual tiene conexión a Internet.
     * Cabecera: public static boolean isOnline(Context context)
     * Entrada:
     *  -Context context
     * Salida:
     *  -boolean connection
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si el dispositivo
     * actual tiene conexión a Internet o false en caso contrario.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

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
