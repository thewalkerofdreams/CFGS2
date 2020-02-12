package es.iesnervion.yeray.fragments_jugadores.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import es.iesnervion.yeray.fragments_jugadores.DDBB.AppDataBase;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayer;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayerPosition;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPosition;
import es.iesnervion.yeray.fragments_jugadores.POJO.ClsPlayerWithPositions;
import es.iesnervion.yeray.fragments_jugadores.R;
import es.iesnervion.yeray.fragments_jugadores.ViewModels.ClsMainActivityVM;

public class AddPlayerFragment extends Fragment implements View.OnClickListener {

    EditText nombre, apellidos;
    CheckBox portero, defensa, centrocampista, delantero;
    ClsPlayerWithPositions player = new ClsPlayerWithPositions();
    Button addButton;
    ClsMainActivityVM viewModel;
    public AddPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_player, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(ClsMainActivityVM.class);

        nombre = view.findViewById(R.id.EditPlayerFistNameAddFragment);
        apellidos = view.findViewById(R.id.EditPlayerLastNameAddFragment);
        portero = view.findViewById(R.id.checkboxPortero);
        defensa = view.findViewById(R.id.checkboxDefensa);
        centrocampista = view.findViewById(R.id.checkboxCentrocampista);
        delantero = view.findViewById(R.id.checkboxDelantero);

        addButton = view.findViewById(R.id.btnInsertPlayerFragment);
        addButton.setOnClickListener(this);

        player.positions = new ArrayList<ClsPosition>();

        return view;
    }

    @Override
    public void onClick(View view) {

        viewModel.get_createdPlayer().player.set_nombre(nombre.getText().toString());
        viewModel.get_createdPlayer().player.set_apellidos(apellidos.getText().toString());

        if(!portero.isChecked() && !defensa.isChecked() && !centrocampista.isChecked() && !delantero.isChecked()){
            Toast.makeText(getContext(), R.string.no_checkbox_selected, Toast.LENGTH_SHORT).show();
        }else{
            if(!viewModel.get_createdPlayer().getPlayer().get_nombre().isEmpty() && !viewModel.get_createdPlayer().getPlayer().get_apellidos().isEmpty()){
                //Insertamos al jugador en la base de datos
                AppDataBase.getDataBase(getActivity()).clsPlayerDao().insertPlayer(new ClsPlayer(viewModel.get_createdPlayer().getPlayer().get_nombre(),
                        viewModel.get_createdPlayer().getPlayer().get_apellidos()));
                //Obtenemos el id del nombre del jugador
                ClsPlayer idPlayer = AppDataBase.getDataBase(getActivity()).clsPlayerDao().getPlayerByFirstName(viewModel.get_createdPlayer().getPlayer().get_nombre());
                //Insertaremos los datos restantes en la tabla PlayerPosition
                if(portero.isChecked()){
                    ClsPosition position = AppDataBase.getDataBase(getActivity()).clsPositionDao().getPositionByName("portero");
                    AppDataBase.getDataBase(getActivity()).clsPlayerPositionDao().insertPlayerPosition(new ClsPlayerPosition(idPlayer.get_id(), position.get_id()));
                }

                if(defensa.isChecked()){
                    ClsPosition position = AppDataBase.getDataBase(getActivity()).clsPositionDao().getPositionByName("defensa");
                    AppDataBase.getDataBase(getActivity()).clsPlayerPositionDao().insertPlayerPosition(new ClsPlayerPosition(idPlayer.get_id(), position.get_id()));
                }

                if(centrocampista.isChecked()){
                    ClsPosition position = AppDataBase.getDataBase(getActivity()).clsPositionDao().getPositionByName("centrocampista");
                    AppDataBase.getDataBase(getActivity()).clsPlayerPositionDao().insertPlayerPosition(new ClsPlayerPosition(idPlayer.get_id(), position.get_id()));
                }

                if(delantero.isChecked()){
                    ClsPosition position = AppDataBase.getDataBase(getActivity()).clsPositionDao().getPositionByName("delantero");
                    AppDataBase.getDataBase(getActivity()).clsPlayerPositionDao().insertPlayerPosition(new ClsPlayerPosition(idPlayer.get_id(), position.get_id()));
                }

                //actualizamos la lista de jugadores del viewModel
                viewModel.cargarListadoJugadores();
                Toast.makeText(getContext(), R.string.player_inserted, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), R.string.firts_and_last_name_required, Toast.LENGTH_SHORT).show();
            }
        }
    }
}