package com.example.adventuremaps.FireBaseEntities;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ClsRoute {

    private String routeId;
    private String name;
    private Boolean favourite;
    private Long dateOfCreation;//En FireBase no podemos almacenar el dato Date o Int
    private String userEmail;

    public ClsRoute(){

    }

    public ClsRoute(String routeId, String name, Boolean favourite, Long dateOfCreation, String userEmail){
        this.routeId = routeId;
        this.name = name;
        this.favourite = favourite;
        this.dateOfCreation = dateOfCreation;
        this.userEmail = userEmail;
    }

    //Get y Set
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Long getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    //Funciones sobreescritas

    /**
     * Interfaz
     * Nombre: equals
     * Comentario: Este método nos permite comprobar si dos objetos ClsRoute son iguales.
     * Para que sean iguales deben tener un mismo id de ruta, nombre, email de usuario y
     * el mismo valor favorito.
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
            if(obj != null && obj instanceof ClsRoute){
                ClsRoute aux = (ClsRoute) obj;
                if(aux.getRouteId().equals(this.getRouteId()) &&
                        aux.getName().equals(this.getName()) &&
                        aux.getFavourite() == this.getFavourite() &&
                        aux.getUserEmail().equals(this.getUserEmail())){
                    ret = true;
                }
            }
        }

        return ret;
    }
}
