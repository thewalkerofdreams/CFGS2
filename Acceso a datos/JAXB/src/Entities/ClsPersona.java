package Entities;

import java.util.ArrayList;
import java.util.Date;

public class ClsPersona {
    private int _id;
    private String _nombre;
    private char _sexo;
    private char _sexoBuscado;
    private Date _fechaNacimiento;
    private double _ingresos;
    private ArrayList<ClsGusto> _preferencias;

    //Constructores
    public ClsPersona(){
        _id = 0;
        _nombre = "DEFAULT";
        _sexo = '0';
        _sexoBuscado = '0';
        _fechaNacimiento = new Date();
        _ingresos = 0.0;
        _preferencias = new ArrayList<>();
    }

    public ClsPersona(int id, String nombre, char sexo, char sexoBuscado, Date fechaNacimiento, double ingresos, ArrayList<ClsGusto> preferencias){
        _id = id;
        _nombre = nombre;
        _sexo = sexo;
        _sexoBuscado = sexoBuscado;
        _fechaNacimiento = fechaNacimiento;
        _ingresos = ingresos;
        _preferencias = preferencias;
    }

    //Get Y Set
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

    public char get_sexo() {
        return _sexo;
    }

    public void set_sexo(char _sexo) {
        this._sexo = _sexo;
    }

    public char get_sexoBuscado() {
        return _sexoBuscado;
    }

    public void set_sexoBuscado(char _sexoBuscado) {
        this._sexoBuscado = _sexoBuscado;
    }

    public Date get_fechaNacimiento() {
        return _fechaNacimiento;
    }

    public void set_fechaNacimiento(Date _fechaNacimiento) {
        this._fechaNacimiento = _fechaNacimiento;
    }

    public double get_ingresos() {
        return _ingresos;
    }

    public void set_ingresos(double _ingresos) {
        this._ingresos = _ingresos;
    }

    public ArrayList<ClsGusto> get_preferencias() {
        return _preferencias;
    }

    public void set_preferencias(ArrayList<ClsGusto> _preferencias) {
        this._preferencias = _preferencias;
    }
}
