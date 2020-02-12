package es.iesnervion.avazquez.practicaroomfragments.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.GregorianCalendar;

import es.iesnervion.avazquez.practicaroomfragments.room.converters.CalendarConverter;

@Entity
public class Mascota {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;

    @TypeConverters(CalendarConverter.class)
    private GregorianCalendar fechaAdopcion;

    @Ignore
    public Mascota() {
        this.id = 0;
        this.nombre = "";
        this.fechaAdopcion = new GregorianCalendar();
    }


    public Mascota( String nombre, GregorianCalendar fechaAdopcion) {

        this.nombre = nombre;
        this.fechaAdopcion = fechaAdopcion;
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

    public GregorianCalendar getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(GregorianCalendar fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }
}
