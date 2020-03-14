package com.example.adventuremaps.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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

        /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                (float) 1.0
        );
        param.weight = 30;
        view.setLayoutParams(param);*/

        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        btnDelete = view.findViewById(R.id.btnDeleteLocalizationPointFrgamentStart);
        btnDetails = view.findViewById(R.id.btnDetailsLocalizationPointFrgamentStart);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteLocalizationDialog(getActivity());
            }
        });

        return view;
    }
}
