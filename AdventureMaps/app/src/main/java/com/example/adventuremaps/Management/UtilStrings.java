package com.example.adventuremaps.Management;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Interfaz
     * Nombre: correctFormatEmail
     * Comentario: El método verifica si una cadena concuerda con el formato correcto de un email.
     * Cabecera: public static boolean correctFormatEmail(String email)
     * Entrada:
     *  -String email
     * Salida:
     *  -boolean correct
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si la cadena tiene
     * un formato correcto para ser un email o false en caso contrario.
     */
    public static boolean correctFormatEmail(String email){
        boolean correct = false;

        //Patrón para validar el email
        Pattern pattern = Pattern
                .compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");

        Matcher mather = pattern.matcher(email);

        if (mather.find()) {//Si el email coincide con los requisitos de la expresión regex, es un email válido
            correct = true;
        }

        return correct;
    }
}
