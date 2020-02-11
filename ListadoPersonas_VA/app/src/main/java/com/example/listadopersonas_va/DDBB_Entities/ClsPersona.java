package com.example.listadopersonas_va.DDBB_Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.listadopersonas_va.Converters.DateConverter;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

@Entity
public class ClsPersona {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;
    @ColumnInfo(name = "nombre")
    private String _nombre;
    @ColumnInfo(name = "apellidos")
    private String _apellidos;
    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "fechaNacimiento")
    private GregorianCalendar _fechaNacimiento;
    @ColumnInfo(name = "telefono")
    private String _telefono;

    //Constructores
    public ClsPersona(){
    }

    @Ignore
    public ClsPersona(String nombre, String apellidos, GregorianCalendar fechaNacimiento, String telefono){
        _nombre = nombre;
        _apellidos = apellidos;
        _fechaNacimiento = fechaNacimiento;
        _telefono = telefono;
    }

    @Ignore
    public ClsPersona(int id, String nombre, String apellidos, GregorianCalendar fechaNacimiento, String telefono){
        _id = id;
        _nombre = nombre;
        _apellidos = apellidos;
        _fechaNacimiento = fechaNacimiento;
        _telefono = telefono;
    }

    //Get y Set

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }

    public String get_apellidos() {
        return _apellidos;
    }

    public void set_apellidos(String _apellidos) {
        this._apellidos = _apellidos;
    }

    public GregorianCalendar get_fechaNacimiento() {
        return _fechaNacimiento;
    }

    public void set_fechaNacimiento(GregorianCalendar _fechaNacimiento) {
        this._fechaNacimiento = _fechaNacimiento;
    }

    public String get_telefono() {
        return _telefono;
    }

    public void set_telefono(String _telefono) {
        this._telefono = _telefono;
    }

    //Funciones Extra

    /**
     * Interfaz
     * Nombre: obtenerFechaNacimientoCorta
     * Comentario: Este método nos permite obtener la fecha de nacimiento de la persona
     * en formato corto (dd/MM/yyyy).
     * Cabecera: public String obtenerFechaNacimientoCorta()
     * Salida:
     *  -String fechaFormateada
     * Postcondiciones: El método devuelve una cadena asociada al nombre, que
     * es la fecha formateada.
     */
    public String obtenerFechaNacimientoCorta(){
        String dateFormatted;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setCalendar(get_fechaNacimiento());
        dateFormatted = sdf.format(get_fechaNacimiento().getTime());

        return dateFormatted;
    }
}
