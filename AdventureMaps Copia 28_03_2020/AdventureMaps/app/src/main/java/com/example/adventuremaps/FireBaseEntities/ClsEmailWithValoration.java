package com.example.adventuremaps.FireBaseEntities;

public class ClsEmailWithValoration {

    private String email;
    private double valoration;

    public ClsEmailWithValoration(){
        email = "";
        valoration = 0;
    }

    public ClsEmailWithValoration(String email, double valoration){
        this.email = email;
        this.valoration = valoration;
    }

    //Get y Set
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getValoration() {
        return valoration;
    }

    public void setValoration(double valoration) {
        this.valoration = valoration;
    }
}
