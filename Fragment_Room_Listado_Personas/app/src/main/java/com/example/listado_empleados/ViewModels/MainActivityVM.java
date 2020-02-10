package com.example.listado_empleados.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listado_empleados.DDBB.AppDataBase;
import com.example.listado_empleados.DDBB_Entities.ClsDepartamento;
import com.example.listado_empleados.DDBB_Entities.ClsPersona;
import com.example.listado_empleados.Tuple.ClsPersonaConDepartamentoTuple;

import java.util.ArrayList;

public class MainActivityVM extends AndroidViewModel {

    private MutableLiveData<ArrayList<ClsPersonaConDepartamentoTuple>> _employeeList;
    private MutableLiveData<ArrayList<ClsDepartamento>> _departamentList;
    private String newPersonFirstName;
    private String newPersonLastName;
    private String newPersonPhone;
    private int newPersonDepartament;
    private String newDepartamentName;

    public MainActivityVM(Application application){
        super(application);
        _employeeList = new MutableLiveData<ArrayList<ClsPersonaConDepartamentoTuple>>();
        _departamentList = new MutableLiveData<ArrayList<ClsDepartamento>>();
        loadEmployeeList();
        loadDepartamentList();
    }

    //Get y Set
    public LiveData<ArrayList<ClsPersonaConDepartamentoTuple>> get_employeeList() {
        return _employeeList;
    }

    public void set_employeeList(ArrayList<ClsPersonaConDepartamentoTuple> employeeList) {
        this._employeeList.setValue(employeeList);
    }

    public LiveData<ArrayList<ClsDepartamento>> get_departamentList() {
        return _departamentList;
    }

    public void set_departamentList(ArrayList<ClsDepartamento> departamentList) {
        this._departamentList.setValue(departamentList);
    }

    public String getNewPersonFirstName() {
        return newPersonFirstName;
    }

    public void setNewPersonFirstName(String newPersonFirstName) {
        this.newPersonFirstName = newPersonFirstName;
    }

    public String getNewPersonLastName() {
        return newPersonLastName;
    }

    public void setNewPersonLastName(String newPersonLastName) {
        this.newPersonLastName = newPersonLastName;
    }

    public String getNewPersonPhone() {
        return newPersonPhone;
    }

    public void setNewPersonPhone(String newPersonPhone) {
        this.newPersonPhone = newPersonPhone;
    }

    public int getNewPersonDepartament() {
        return newPersonDepartament;
    }

    public void setNewPersonDepartament(int newPersonDepartament) {
        this.newPersonDepartament = newPersonDepartament;
    }

    public String getNewDepartamentName() {
        return newDepartamentName;
    }

    public void setNewDepartamentName(String newDepartamentName) {
        this.newDepartamentName = newDepartamentName;
    }

    //Funciones sobre la base de datos

    /**
     * Interfaz
     * Nombre: loadEmployeeList
     * Comentario: Este método nos permite cargar el listado de empleados junto con
     * el nombre de su departamento de la base de datos.
     * Cabecera: private void loadEmployeeList()
     * Postcondiciones: El método carga la lista de empleados.
     * */
    public void loadEmployeeList(){
        /*ArrayList<ClsPersonaConDepartamentoTuple> listado = new ArrayList<ClsPersonaConDepartamentoTuple>(AppDataBase.getDataBase(getApplication()).clsPersonaDao().getPersonsWithDepartament());
        if(listado.size() > 0)
            _employeeList.setValue(listado);*/
        ArrayList<ClsPersona> listado = new ArrayList<ClsPersona>(AppDataBase.getDataBase(getApplication()).clsPersonaDao().getAllPersons());
        ArrayList<ClsPersonaConDepartamentoTuple> listado2 = new ArrayList<>();
        if(listado.size() > 0){
            for(int i = 0; i < listado.size(); i++){
                ClsDepartamento departamento = AppDataBase.getDataBase(getApplication()).clsDepartamentoDao().getAllDepartamentById(listado.get(i).get_idDepartamento());
                ClsPersonaConDepartamentoTuple persona = new ClsPersonaConDepartamentoTuple(listado.get(i).get_id(),
                        listado.get(i).get_nombre(), listado.get(i).get_apellidos(), listado.get(i).get_telefono(),
                        listado.get(i).get_idDepartamento(), departamento.get_nombre());
                listado2.add(persona);
            }
            _employeeList.setValue(listado2);
        }
    }

    /**
     * Interfaz
     * Nombre: loadDepartamentList
     * Comentario: Este método nos permite cargar el listado de departamentos
     * de la base de datos.
     * Cabecera: private void loadDepartamentList()
     * Postcondiciones: El método carga la lista de departamentos.
     * */
    public void loadDepartamentList(){
        ArrayList<ClsDepartamento> listado = new ArrayList<ClsDepartamento>(AppDataBase.getDataBase(getApplication()).clsDepartamentoDao().getAllDepartaments());
        if(listado.size() > 0){
            _departamentList.setValue(listado);
        }
    }

    /*
     * Interfaz
     * Nombre: deleteEmployee
     * Comentario: Este método nos permite eliminar a un empleado de la base de datos.
     * Cabecera: public void deleteEmployee(ClsPersonaConDepartamentoTuple persona, Context context)
     * Entrada:
     *  -ClsPersonaConDepartamentoTuple persona
     * Postcondiciones: El método elimina esa persona de la base de datos si existe en ella.
     */
    public void deleteEmployee(ClsPersonaConDepartamentoTuple persona, Context context){
        //ClsPersona personaAEliminar = AppDataBase.getDataBase(context).clsPersonaDao().getPersonById(persona.get_id());
        ClsPersona personaAEliminar = new ClsPersona(persona.get_id(), persona.get_nombre(), persona.get_apellidos(), persona.get_telefono(),
                persona.get_idDepartamento());
        AppDataBase.getDataBase(context).clsPersonaDao().deletePerson(personaAEliminar);
    }
}
