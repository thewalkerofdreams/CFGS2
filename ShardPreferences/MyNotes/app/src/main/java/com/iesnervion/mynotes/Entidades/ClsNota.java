package com.iesnervion.mynotes.Entidades;

import com.iesnervion.mynotes.Utilidades.GenerateKey;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClsNota implements Serializable {
    //Propiedades privadas
    private String ID;
    private String titulo;
    private String fecha;
    private String contenido;

    //Constructores
    //Por defecto

    public ClsNota(){
        GenerateKey key = new GenerateKey();
        ID = key.randomKey();
        titulo = "Nueva nota";
        //Obtenemos la fecha actual.
        obtenerFecha();
        contenido = "";
    }

    //Con parametros
    public ClsNota( String ID ,String titulo, String fecha, String contenido) {
        this.ID = ID;
        this.titulo = titulo;
        this.fecha = fecha;
        this.contenido = contenido;
    }

    //Getter and setters
    public String getID() {
        return ID;
    }
    public void setID(String id) {
        this.ID = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    //Metodos añadidos

    /**
     * Obtiene la fecha actual en forma de String.
     */
    private void obtenerFecha(){
        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        fecha = dateFormat.format(date);
    }
}
