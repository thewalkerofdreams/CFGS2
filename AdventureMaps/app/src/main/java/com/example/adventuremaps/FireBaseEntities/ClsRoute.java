package com.example.adventuremaps.FireBaseEntities;

public class ClsRoute {

    private String routeId;
    private String name;
    private Boolean favourite;
    private Long dateOfCreation;//En FireBase no podemos almacenar el dato Int
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
}
