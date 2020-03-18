package com.example.adventuremaps.Activities.Models;

import android.graphics.Bitmap;
import android.net.Uri;

public class ClsImageWithId {

    private int _id;
    private Uri _uri;
    private static int counter = 0;

    public ClsImageWithId(){
        _id = ++counter;
        _uri = null;
    }

    public ClsImageWithId(Uri uri){
        _id = ++counter;
        _uri = uri;
    }

    //Get y Set
    public int get_id() {
        return _id;
    }

    public Uri get_uri() {
        return _uri;
    }

    public void set_uri(Uri _uri) {
        this._uri = _uri;
    }

    //Funciones sobreescritas

    /**
     * Interfaz
     * Nombre: equals
     * Comentario: Este método nos permite comprobar si dos objetos ClsImageWithId son iguales.
     * Para que sean iguales deben tener un mismo id.
     * Cabecera: public boolean equals(Object obj)
     * @param obj
     * @return ret
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si ambos
     * objetos son iguales y falso en caso contrario.
     */
    @Override
    public boolean equals(Object obj){
        boolean ret = false;

        if(this == obj){
            ret = true;
        }else{
            if(obj != null && obj instanceof ClsImageWithId){
                ClsImageWithId aux = (ClsImageWithId) obj;
                if(aux.get_id() == this.get_id()){
                    ret = true;
                }
            }
        }

        return ret;
    }
}
