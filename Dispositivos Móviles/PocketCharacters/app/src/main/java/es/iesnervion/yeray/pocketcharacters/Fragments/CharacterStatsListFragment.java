package es.iesnervion.yeray.pocketcharacters.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import es.iesnervion.yeray.pocketcharacters.Activities.CharacterStatsListActivity;
import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterStatsListVM;

public class CharacterStatsListFragment extends Fragment {

    EditText statName, value;
    CharacterStatsListVM viewModel;
    Button btnUpdate;

    //Constructor por defecto.
    public CharacterStatsListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_stat_fragment, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(CharacterStatsListVM.class);

        statName = view.findViewById(R.id.TextViewStatNameFrag);
        value = view.findViewById(R.id.EditTextStatValueFrag);
        btnUpdate = view.findViewById(R.id.btnModHen);

        if(viewModel.get_statSelected().getValue() != null){
            statName.setText(viewModel.get_statSelected().getValue().get_name());
            value.setText(viewModel.get_statSelected().getValue().get_value());
        }

        //Los observers
        final Observer<ClsStatModel> contactObserver = new Observer<ClsStatModel>() {
            @Override
            public void onChanged(ClsStatModel clsStatModel) {
                if(viewModel.get_statSelected().getValue() != null){
                    statName.setText(viewModel.get_statSelected().getValue().get_value());
                    value.setText(viewModel.get_statSelected().getValue().get_value());
                }
            }
        };

        //Observamos los LiveData
        viewModel.get_statSelected().observe(this, contactObserver);

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(value.getText().length() > 0){
                    viewModel.get_statSelected().getValue().set_name(statName.getText().toString());//Modificamos la gallina seleccionada
                    viewModel.get_statSelected().getValue().set_value(value.getText().toString());

                    //Aquí obtenemos el id del stat a modificar
                    ClsStat stat = AppDataBase.getDataBase(getContext()).statDao().getStatByGameModeAndName(viewModel.get_character().get_gameMode(), viewModel.get_statSelected().getValue().get_name());
                    ClsCharacterAndStat clsCharacterAndStat = new ClsCharacterAndStat(viewModel.get_character().get_id(),
                    stat.get_id(), viewModel.get_statSelected().getValue().get_value());
                    //Insertamos los datos en la tabla CharacterAndStat
                    AppDataBase.getDataBase(getContext()).characterAndStatDao().insertCharacterAndStat(clsCharacterAndStat);

                    ((CharacterStatsListActivity) getActivity()).reloadList();//Recargamos la lista del mainActivity.
                    Toast.makeText(getContext(), "Stat modified!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "The value is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
