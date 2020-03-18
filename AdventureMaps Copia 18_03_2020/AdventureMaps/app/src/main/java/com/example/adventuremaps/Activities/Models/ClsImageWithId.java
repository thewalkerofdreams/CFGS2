package com.example.adventuremaps.Activities.Models;

import android.graphics.Bitmap;

public class ClsImageWithId {

    private int _id;
    private Bitmap _bitmap;
    private static int counter = 0;

    public ClsImageWithId(){
        _id = ++counter;
        _bitmap = null;
    }

    public ClsImageWithId(Bitmap bitmap){
        _id = ++counter;
        _bitmap = bitmap;
    }

    //Get y Set
    public int get_id() {
        return _id;
    }

    public Bitmap get_bitmap() {
        return _bitmap;
    }

    public void set_bitmap(Bitmap _bitmap) {
        this._bitmap = _bitmap;
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
