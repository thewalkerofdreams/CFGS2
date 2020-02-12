package com.iesnervion.usuario.examensegundaevaluacionpablojarana.Models;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Futbolista;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Posicion;

import java.util.ArrayList;

/**
 * Created by pjarana on 21/02/18.
 *
 * ANOTACIONES: Esta clase la usaré para mostrar la lista del futbolista con sus posiciones correspondientes
 */

public class FutbolistaConPosiciones {

    private int id;
    private String nombre;
    private String apellidos;
    private ArrayList<Posicion>posiciones;

    public FutbolistaConPosiciones()
    {
        this.id=0;
        this.nombre="";
        this.apellidos="";
        this.posiciones=new ArrayList<Posicion>();
    }
    public FutbolistaConPosiciones(int id, String nombre, String apellidos, ArrayList<Posicion>posiciones)
    {
        this.id=id;
        this.nombre=nombre;
        this.apellidos=apellidos;
        this.posiciones=posiciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public ArrayList<Posicion> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(ArrayList<Posicion> posiciones) {
        this.posiciones = posiciones;
    }
}
