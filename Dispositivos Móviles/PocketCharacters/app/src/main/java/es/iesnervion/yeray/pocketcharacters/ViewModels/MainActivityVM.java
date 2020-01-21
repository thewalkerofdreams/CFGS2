package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class MainActivityVM extends AndroidViewModel {
    private int _idUser;

    public MainActivityVM(Application application){
        super(application);
    }

    //Get Y Set
    public int get_idUser() {
        return _idUser;
    }

    public void set_idUser(int _idUser) {
        this._idUser = _idUser;
    }

    /*
    * Interfaz
    * Nombre: sincroniceDatasWithServer
    * Comentario: Este método nos permite sincronizar nuestros datos de la aplicación
    * con los del servidor. (Datas --> Characters, GameModes, Objects and Stats)
    * Es decir, los datos de la aplicación pasarán a ser los últimos datos de la aplicación
    * almacenados.
    * Cabecera: public void sincroniceDatasWithServer()
    * Entrada:
    *   -View v
    * Postcondiciones: La aplicación obtiene el estado de la última copia de seguridad del usuario
    * en el servidor.
    * */
    public void sincroniceDatasWithServer(){
        //TODO Realizar cuando la Api este creada
    }
}
