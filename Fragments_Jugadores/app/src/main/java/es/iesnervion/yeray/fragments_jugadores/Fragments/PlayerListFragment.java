package es.iesnervion.yeray.fragments_jugadores.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import es.iesnervion.yeray.fragments_jugadores.Adapters.AdapterPlayerList;
import es.iesnervion.yeray.fragments_jugadores.DDBB.AppDataBase;
import es.iesnervion.yeray.fragments_jugadores.POJO.ClsPlayerWithPositions;
import es.iesnervion.yeray.fragments_jugadores.R;
import es.iesnervion.yeray.fragments_jugadores.ViewModels.ClsMainActivityVM;

public class PlayerListFragment extends Fragment implements AdapterView.OnItemLongClickListener
{
    ClsMainActivityVM viewModel;
    ListView listView;
    ImageButton btnAddContact, searchButton;
    public PlayerListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_list, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(ClsMainActivityVM.class);

        listView = view.findViewById(R.id.PlayerListFragment);
        //Adapter
        final AdapterPlayerList adapter = new AdapterPlayerList(getActivity().getBaseContext(), R.layout.item_player_list, new ArrayList<ClsPlayerWithPositions>(viewModel.get_playerList().getValue()));
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        /*El observer*/
        final Observer<List<ClsPlayerWithPositions>> listObserver = new Observer<List<ClsPlayerWithPositions>>() {
            @Override
            public void onChanged(List<ClsPlayerWithPositions> players) {
                listView.invalidate();
                ArrayList<ClsPlayerWithPositions> playe = new ArrayList<ClsPlayerWithPositions>(viewModel.get_playerList().getValue());
                AdapterPlayerList adapter02 = new AdapterPlayerList(getActivity().getBaseContext(), R.layout.item_player_list, playe);
                listView.setAdapter(adapter02);
            }
        };

        //Observo el LiveData con ese observer que acabo de crear
        viewModel.get_playerList().observe(getActivity(), listObserver);

        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ClsPlayerWithPositions item = (ClsPlayerWithPositions) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_person);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.person_deleted, Toast.LENGTH_SHORT).show();
                AppDataBase.getDataBase(getActivity()).clsPlayerDao().deletePlayer(item.player);//Eliminamos al jugador
                AppDataBase.getDataBase(getActivity()).clsPlayerPositionDao().deleteRelationPlayer(item.getPlayer().get_id());
                reloadList();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    public void reloadList(){
        viewModel.cargarListadoJugadores();
        final AdapterPlayerList adapter = new AdapterPlayerList(getActivity().getBaseContext(), R.layout.item_player_list, new ArrayList<ClsPlayerWithPositions>(viewModel.get_playerList().getValue()));
        listView.setAdapter(adapter);
    }
}
