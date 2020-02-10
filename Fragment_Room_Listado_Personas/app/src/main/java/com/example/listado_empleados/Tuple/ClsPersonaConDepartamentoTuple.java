package com.example.listado_empleados.Tuple;

import androidx.room.Ignore;

import com.example.listado_empleados.DDBB_Entities.ClsPersona;

public class ClsPersonaConDepartamentoTuple extends ClsPersona {

    private String _departamento;

    //Constructores
    public ClsPersonaConDepartamentoTuple(){
        super();
    }

    @Ignore
    public ClsPersonaConDepartamentoTuple(int id, String nombre, String apellidos, String telefono, int idDepartamento, String departamento){
        super(id, nombre, apellidos, telefono, idDepartamento);
        _departamento = departamento;
    }

    //Get y Set

    public String get_departamento() {
        return _departamento;
    }

    public void set_departamento(String _departamento) {
        this._departamento = _departamento;
    }
}
