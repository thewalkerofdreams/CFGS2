package com.example.listado_empleados.DDBB;

import android.content.Context;

import com.example.listado_empleados.DDBB_Entities.ClsDepartamento;

import java.util.ArrayList;

public class DDBBMethods {
    /*
    Interfaz
    Nombre: existenDepartamentos
    Comentario: Este método nos permite verificar si existe como mínimo algún departamento
    en las base de datos.
    Cabecera: public boolean existenDepartamentos(Context contexto)
    Entrada:
        -Context contexto
    Salida:
        -boolean exist
    Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si existe algun departamento
    en la base de datos o false en caso contrario.
    */
    public boolean existenDepartamentos(Context contexto){
        boolean exist = false;

        ArrayList<ClsDepartamento> departamentos = new ArrayList<ClsDepartamento>(AppDataBase.getDataBase(contexto).clsDepartamentoDao().getAllDepartaments());

        if (departamentos.size() > 0){
            exist = true;
        }

        return exist;
    }
}
