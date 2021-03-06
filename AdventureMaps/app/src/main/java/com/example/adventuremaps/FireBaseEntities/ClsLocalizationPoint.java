package com.example.adventuremaps.FireBaseEntities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties //Ignoramos el resto de atributos que se encuentren almacenados en el fragmento de Firebase que no se presenten en esta clase
public class ClsLocalizationPoint implements Serializable {

    private String localizationPointId;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Long dateOfCreation;//En FireBase no podemos almacenar el dato Int
    private String index;//Tengo que crear un index sintético debido a que FireBase DataBaseRealTime no permite ordenar una referencia por más de un hijo
    private String emailCreator;
    private boolean shared;

    public ClsLocalizationPoint(){

    }

    public ClsLocalizationPoint(String name, String description, Double latitude, Double longitude, Long dateOfCreation, String emailCreator){
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateOfCreation = dateOfCreation;
        index = latitude+"~"+longitude;
        this.emailCreator = emailCreator;
        shared = false;
    }

    public ClsLocalizationPoint(String localizationPointId, String name, String description, Double latitude, Double longitude, Long dateOfCreation, String emailCreator){
        this.localizationPointId = localizationPointId;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateOfCreation = dateOfCreation;
        index = latitude+"~"+longitude;
        this.emailCreator = emailCreator;
        shared = false;
    }

    //Gets y Sets
    public String getLocalizationPointId() {
        return localizationPointId;
    }

    public void setLocalizationPointId(String localizationPointId) {
        this.localizationPointId = localizationPointId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getEmailCreator() {
        return emailCreator;
    }

    public void setEmailCreator(String emailCreator) {
        this.emailCreator = emailCreator;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
