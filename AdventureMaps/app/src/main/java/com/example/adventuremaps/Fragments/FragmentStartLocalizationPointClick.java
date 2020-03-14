package com.example.adventuremaps.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.MainTabbetActivity;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;

public class FragmentStartLocalizationPointClick extends Fragment {

    private MainTabbetActivityVM viewModel;
    private Button btnDelete, btnDetails;
    //Constructor por defecto.
    public FragmentStartLocalizationPointClick(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_click_localization_point, container, false);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        btnDelete = view.findViewById(R.id.btnDeleteLocalizationPointFrgamentStart);
        btnDetails = view.findViewById(R.id.btnDetailsLocalizationPointFrgamentStart);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.getLocalizationPoint().getEmailCreator().equals(viewModel.get_actualEmailUser())){
                    viewModel.deleteLocalizationDialog(getActivity());
                }else{
                    Toast.makeText(getContext(), R.string.cant_delete_localization_owner, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
