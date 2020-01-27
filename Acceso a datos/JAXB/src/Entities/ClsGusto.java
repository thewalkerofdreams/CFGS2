package Entities;

public class ClsGusto {
    private String _descripcion;
    private int _valor;

    //Constructores
    public ClsGusto(){
        _descripcion = "DEFAULT";
        _valor = 0;
    }

    public ClsGusto(String descripcion, int valor){
        _descripcion = descripcion;
        _valor = valor;
    }

    //Get Y Set
    public String get_descripcion() {
        return _descripcion;
    }

    public void set_descripcion(String _descripcion) {
        this._descripcion = _descripcion;
    }

    public int get_valor() {
        return _valor;
    }

    public void set_valor(int _valor) {
        this._valor = _valor;
    }
}
