package com.example.adventuremaps.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.DetailsLocalizationPointActivity;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentOfflineLocalizationPointClick extends Fragment {

    private MainTabbetActivityVM viewModel;
    private Button btnDelete, btnDetails;
    //Constructor por defecto.
    public FragmentOfflineLocalizationPointClick(){
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_click_localization_point, container, false);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        btnDelete = view.findViewById(R.id.btnDeleteLocalizationPointFragmentStart);
        btnDetails = view.findViewById(R.id.btnDetailsLocalizationPointFragmentStart);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference drLocalization = FirebaseDatabase.getInstance().getReference("Localizations");
                drLocalization.orderByChild("index").equalTo(viewModel.get_localizationPointClickedMapbox().getLatitude()+"~"+viewModel.get_localizationPointClickedMapbox().getLongitude()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ClsLocalizationPoint localizationPointToDelete = null;
                        for(DataSnapshot datas: dataSnapshot.getChildren()){//Solo habrá como máximo uno
                            localizationPointToDelete = datas.getValue(ClsLocalizationPoint.class);
                        }

                        viewModel.set_selectedLocalizationPoint(localizationPointToDelete);
                        tryShowDeleteLocalizationPointDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference drLocalization = FirebaseDatabase.getInstance().getReference("Localizations");
                drLocalization.orderByChild("index").equalTo(viewModel.get_localizationPointClickedMapbox().getLatitude()+"~"+viewModel.get_localizationPointClickedMapbox().getLongitude()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ClsLocalizationPoint localizationPointToShow = null;
                        for(DataSnapshot datas: dataSnapshot.getChildren()){//Solo habrá como máximo uno
                            localizationPointToShow = datas.getValue(ClsLocalizationPoint.class);
                        }

                        viewModel.set_selectedLocalizationPoint(localizationPointToShow);
                        throwDetailsLocalizationPointActivity();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        return view;
    }

    /**
     * Interfaz
     * Nombre: tryShowDeleteLocalizationPointDialog
     * Comentario: Este método muestra por pantalla un dialogo para eliminar un punto de localización
     * seleccionado o muestra un mensaje de error por pantalla si el punto de localización seleccionado
     * no le perteenece al usuario actual.
     * Cabecera: private void tryShowDeleteLocalizationPointDialog()
     * Postcondiciones: El método muestra por pantalla un dialogo para eliminar un punto de localización o
     * un mensaje de error si el punto de localización seleccionado no le pertenece al usuario actual.
     */
    private void tryShowDeleteLocalizationPointDialog(){
        if(viewModel.get_selectedLocalizationPoint() != null && viewModel.get_selectedLocalizationPoint().getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización existe y es del usuario actual
            viewModel.deleteLocalizationDialog(getActivity(), 2);
        }else{
            Toast.makeText(getContext(), R.string.cant_delete_localization_owner, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Interfaz
     * Nombre: throwDetailsLocalizationPointActivity
     * Comentario: Este método lanza la actividad DetailsLocalizationPointActivity.
     * Cabecera: private void throwDetailsLocalizationPointActivity()
     * Postcondiciones: El método lanza la actividad DetailsLocalizationPointActivity o
     * muestra un mensaje de error por pantalla si la ruta seleccionada ya no existe.
     */
    private void throwDetailsLocalizationPointActivity(){
        if(viewModel.get_selectedLocalizationPoint() == null){
            Toast.makeText(getContext(), R.string.error_localization_no_exist, Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(getActivity(), DetailsLocalizationPointActivity.class);
            intent.putExtra("ActualLocalization", viewModel.get_selectedLocalizationPoint());
            intent.putExtra("ActualEmailUser", viewModel.get_actualEmailUser());
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_DETAILS_LOCALIZATION_POINT);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ApplicationConstants.REQUEST_CODE_DETAILS_LOCALIZATION_POINT){
            if(resultCode == Activity.RESULT_OK){//Si el punto de localización se ha dejado de compartir
                viewModel.set_localizationPointClicked(null);//Indicamos que la localización seleccionada pasa a null
                //((MainTabbetActivity)getActivity()).reloadInitialFragment();//Recargamos el mapa de inicio
            }
        }
    }
}
