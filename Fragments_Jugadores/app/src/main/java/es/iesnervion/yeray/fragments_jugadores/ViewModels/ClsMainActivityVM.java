package es.iesnervion.yeray.fragments_jugadores.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import es.iesnervion.yeray.fragments_jugadores.DDBB.AppDataBase;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayer;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayerPosition;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPosition;
import es.iesnervion.yeray.fragments_jugadores.POJO.ClsPlayerWithPositions;

public class ClsMainActivityVM extends AndroidViewModel {
    private MutableLiveData<List<ClsPlayerWithPositions>> _playerList;
    private MutableLiveData<ClsPlayerWithPositions> _selectedPlayer;
    private ClsPlayerWithPositions _createdPlayer;
    private MutableLiveData<Integer> _fragmentoACargar;

    //Constructor
    public ClsMainActivityVM(Application application){
        super(application);
        _playerList = new MutableLiveData<List<ClsPlayerWithPositions>>();
        cargarListadoJugadores();
        _selectedPlayer = new MutableLiveData<ClsPlayerWithPositions>();
        _createdPlayer = new ClsPlayerWithPositions();
        _createdPlayer.setPlayer(new ClsPlayer());
        _fragmentoACargar = new MutableLiveData<Integer>();
        //_fragmentoACargar.setValue(1);//Por defecto dejamos el fragmento de la lista
    }

    //Get y Set

    public LiveData<List<ClsPlayerWithPositions>> get_playerList(){
        return _playerList;
    }
    public void set_playerList(ArrayList<ClsPlayerWithPositions> playerWithPositions){
        this._playerList.setValue(playerWithPositions);
    }

    public LiveData<ClsPlayerWithPositions> get_selectedPlayer(){
        return _selectedPlayer;
    }
    public void set_selectedPlayer(ClsPlayerWithPositions selectedPlayer){
        this._selectedPlayer.setValue(selectedPlayer);
    }

    public ClsPlayerWithPositions get_createdPlayer() {
        return _createdPlayer;
    }

    public void set_createdPlayer(ClsPlayerWithPositions _createdPlayer) {
        this._createdPlayer = _createdPlayer;
    }

    public LiveData<Integer> get_fragmentoACargar(){
        return _fragmentoACargar;
    }
    public void set_fragmentoACargar(int fragmentoACargar){
        this._fragmentoACargar.setValue(fragmentoACargar);
    }

    //Funciones Extra
    public void cargarListadoJugadores(){
        ArrayList<ClsPlayerWithPositions> jugadores = new ArrayList<>(AppDataBase.getDataBase(getApplication()).clsPlayerDao().loadPlayersWithPositions());
        _playerList.setValue(jugadores);
        //ArrayList<ClsPlayerWithPositions> jugadores = new ArrayList<>();
        //ArrayList<ClsPosition> positions = new ArrayList<>(AppDataBase.getDataBase(getApplication()).clsPositionDao().getAllPositions());
        //ArrayList<ClsPlayer> players = new ArrayList<>(AppDataBase.getDataBase(getApplication()).clsPlayerDao().getAllPlayers());
        //for(int i = 0; i < players.size(); i++){
            //ArrayList<ClsPlayerPosition> playerPosition = new ArrayList<ClsPlayerPosition>(AppDataBase.getDataBase(getApplication()).clsPlayerPositionDao().getPlayerPositionByPlayerId(players.get(i).get_id()));
            //for(int j = 0; j < playerPosition.size(); i++){
                //ClsPosition position = AppDataBase.getDataBase(getApplication()).clsPositionDao().getPositionById(playerPosition.get(j).get_idPosition());

            //}
            //jugadores.add(playerPosition.get(i));
        //}

    }
}
