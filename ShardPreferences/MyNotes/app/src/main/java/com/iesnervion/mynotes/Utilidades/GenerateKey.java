package com.iesnervion.mynotes.Utilidades;

import java.util.Random;

public class GenerateKey {

    private static final String Characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   /**
    * Genera una clave de 10 digitos aleatorios. Puede contener mayusculas, minusculas y numeros.
    */
    public String randomKey(){
        StringBuilder key = new StringBuilder();
        Random rd = new Random();

        for(int i = 0; i < 10; i++){
            key.append(Characters.charAt(rd.nextInt(Characters.length())));
        }

        return key.toString();
    }
}
