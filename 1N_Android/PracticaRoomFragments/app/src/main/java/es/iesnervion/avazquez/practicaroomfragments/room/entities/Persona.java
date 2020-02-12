package es.iesnervion.avazquez.practicaroomfragments.room.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.GregorianCalendar;

import es.iesnervion.avazquez.practicaroomfragments.room.converters.CalendarConverter;

@Entity(indices = {@Index("idPareja")}
        ,foreignKeys = @ForeignKey(entity = Persona.class,
        parentColumns = "id",
        childColumns = "idPareja"))
public class Persona {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //La ID de la pareja (Persona tiene relacion reflexiva )

    private Integer idPareja;
    private String nombre;
    @TypeConverters(CalendarConverter.class)
    private GregorianCalendar fechaNacimiento;

//    public Persona() {
//        this.id = 0;
//        this.idPareja = 0;
//        this.nombre = "";
//        this.fechaNacimiento = new GregorianCalendar(1900,0,1);
//    }


    public Persona(String nombre, GregorianCalendar fechaNacimiento) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdPareja() {
        return idPareja;
    }

    public void setIdPareja(Integer idPareja) {
        this.idPareja = idPareja;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public GregorianCalendar getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(GregorianCalendar fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
