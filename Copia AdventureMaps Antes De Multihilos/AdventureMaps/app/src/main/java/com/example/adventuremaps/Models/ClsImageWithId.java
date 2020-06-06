package com.example.adventuremaps.Models;

import java.io.Serializable;

public class ClsImageWithId implements Serializable {

    private String _uri;
    private String _imageId;
    private String _userEmailCreator;

    public ClsImageWithId(){
        _uri = "";
        _userEmailCreator = "";
        _imageId = "";
    }

    public ClsImageWithId(String uri, String userEmailCreator, String imageId){
        _uri = uri;
        _userEmailCreator = userEmailCreator;
        this._imageId = imageId;
    }

    //Get y Set
    public String get_uri() {
        return _uri;
    }

    public void set_uri(String _uri) {
        this._uri = _uri;
    }

    public String get_userEmailCreator() {
        return _userEmailCreator;
    }

    public void set_userEmailCreator(String _userEmailCreator) {
        this._userEmailCreator = _userEmailCreator;
    }

    public String get_imageId() {
        return _imageId;
    }

    public void set_imageId(String _imageId) {
        this._imageId = _imageId;
    }

    //Funciones sobreescritas

    /**
     * Interfaz
     * Nombre: equals
     * Comentario: Este método nos permite comprobar si dos objetos ClsImageWithId son iguales.
     * Para que sean iguales deben tener la misma id y el mismo gmail del creador.
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
            if(obj instanceof ClsImageWithId){
                ClsImageWithId aux = (ClsImageWithId) obj;
                if(aux.get_imageId().equals(this.get_imageId()) &&
                        aux.get_userEmailCreator().equals(this.get_userEmailCreator())){
                    ret = true;
                }
            }
        }

        return ret;
    }
}
