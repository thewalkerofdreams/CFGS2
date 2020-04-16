package com.example.adventuremaps.Management;

import java.util.ArrayList;

public class UtilStrings {

    /**
     * Interfaz
     * Nombre: arraysWithSameData
     * Comentario: Este método nos permite verificar si dos arraylist de cadenas
     * contienen al menos algún dato idéntico, es decir, si comparten alguna cadena que sea igual.
     * Cabecera: public static boolean arraysWithSameData(ArrayList<String> array01, ArrayList<String> array02)
     * Entrada:
     *  -ArrayList<String> array01
     *  -ArrayList<String> array02
     * Salida:
     *  -boolean containSameData
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si las listas
     * compartían al menos un dato o false en caso contrario.
     */
    public static boolean arraysWithSameData(ArrayList<String> array01, ArrayList<String> array02){
        boolean containSameData = false;

        if(!array01.isEmpty() && !array02.isEmpty()){//Si ninguna de las listas se encuentra vacía
            for(int i = 0; i < array01.size() && !containSameData; i++){
                if(array02.contains(array01.get(i))){
                    containSameData = true;
                }
            }
        }

        return containSameData;
    }
}
