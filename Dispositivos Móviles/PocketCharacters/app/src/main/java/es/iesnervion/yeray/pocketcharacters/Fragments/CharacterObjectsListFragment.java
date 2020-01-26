package es.iesnervion.yeray.pocketcharacters.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import es.iesnervion.yeray.pocketcharacters.Activities.CharacterStatsListActivity;
import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsObjectAndQuantity;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;
import es.iesnervion.yeray.pocketcharacters.R;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterObjectListActivityVM;
import es.iesnervion.yeray.pocketcharacters.ViewModels.CharacterStatsListActivityVM;

public class CharacterObjectsListFragment extends Fragment {
    EditText quantity;
    TextView objectName, objectType;
    CharacterObjectListActivityVM viewModel;
    Button btnUpdate;

    //Constructor por defecto.
    public CharacterObjectsListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_object_fragment, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(CharacterObjectListActivityVM.class);

        quantity = view.findViewById(R.id.EditTextObjectQuantityFrag);
        objectName = view.findViewById(R.id.TextViewObjectNameFrag);
        objectType = view.findViewById(R.id.TextViewObjectTypeFrag);
        btnUpdate = view.findViewById(R.id.btnModObject);

        if(viewModel.get_objectSelected().getValue() != null){
            objectName.setText(viewModel.get_objectSelected().getValue().get_object().get_name());
            objectType.setText(viewModel.get_objectSelected().getValue().get_object().get_type());
            quantity.setText(viewModel.get_objectSelected().getValue().get_quantity());
        }

        //Los observers
        final Observer<ClsObjectAndQuantity> contactObserver = new Observer<ClsObjectAndQuantity>() {
            @Override
            public void onChanged(ClsObjectAndQuantity objectAndQuantity) {
                if(viewModel.get_objectSelected().getValue() != null){
                    objectName.setText(viewModel.get_objectSelected().getValue().get_object().get_name());
                    objectType.setText(viewModel.get_objectSelected().getValue().get_object().get_type());
                    quantity.setText(viewModel.get_objectSelected().getValue().get_quantity());
                }
            }
        };

        //Observamos los LiveData
        viewModel.get_objectSelected().observe(this, contactObserver);

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Integer.valueOf(quantity.getText().toString()) >= 0){
                    viewModel.get_objectSelected().getValue().get_object().set_type(objectType.getText().toString());
                    viewModel.get_objectSelected().getValue().get_object().set_name(objectName.getText().toString());
                    viewModel.get_objectSelected().getValue().set_quantity(Integer.valueOf(quantity.getText().toString()));

                    //Aquí obtenemos el id del objeto a modificar
                    ClsObject object = AppDataBase.getDataBase(getContext()).objectDao().getObjectByGameModeObjectNameAndType(viewModel.get_character().get_gameMode(), viewModel.get_objectSelected().getValue().get_object().get_type(),
                            viewModel.get_objectSelected().getValue().get_object().get_name());
                    ClsObjectAndCharacter clsObjectAndCharacter = new ClsObjectAndCharacter(viewModel.get_character().get_id(),
                            object.get_id(), viewModel.get_objectSelected().getValue().get_quantity());
                    //Insertamos los datos en la tabla CharacterAndStat
                    AppDataBase.getDataBase(getContext()).objectAndCharacterDao().insertObjectAndCharacter(clsObjectAndCharacter);

                    Toast.makeText(getContext(), "Object modified!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "La cantidad debe ser mayor o igual que 0!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
