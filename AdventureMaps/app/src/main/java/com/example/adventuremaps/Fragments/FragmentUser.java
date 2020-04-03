package com.example.adventuremaps.Fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentUser extends Fragment {

    private OnFragmentInteractionListener mListener;
    private DatabaseReference myDataBaseReference = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference localiationReference = FirebaseDatabase.getInstance().getReference("Localizations");
    private MainTabbetActivityVM viewModel;
    TextView txtEmail, txtNickName, numberOfRoutes, numberOfLocalizations;

    public FragmentUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        //Instanciamos los elementos del View
        txtEmail = view.findViewById(R.id.TextViewEmailInfoActivity);
        txtNickName = view.findViewById(R.id.TextViewNickNameInfoActivity);
        numberOfRoutes = view.findViewById(R.id.TextViewNumberRoutesInfoActivity);
        numberOfLocalizations = view.findViewById(R.id.TextViewNumberLocalizationsInfoActivity);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Read from the database
        myDataBaseReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = "", nickName = "";
                int routesCreated = 0;
                for(DataSnapshot datas: dataSnapshot.getChildren()){//Solo se repetirá una vez
                    email=datas.child("email").getValue().toString();
                    nickName = datas.child("nickName").getValue().toString();

                    for(DataSnapshot routes: datas.child("routes").getChildren()){//Se repetirá por cada ruta que haya creado el usuario
                        routesCreated++;
                    }
                }
                txtEmail.setText(email);//Introducimos el email del usuario actual
                txtNickName.setText(nickName);//Introducimos el nickName
                numberOfRoutes.setText(String.valueOf(routesCreated));//Introducimos el número de rutas creadas
                //Ahora obtendremos el número de localizaciones creadas por el usuario
                localiationReference.orderByChild("emailCreator").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int localizationsCreated = 0;
                        for(DataSnapshot datas: dataSnapshot.getChildren()){//Solo se repetirá una vez
                            localizationsCreated++;
                        }
                        numberOfLocalizations.setText(String.valueOf(localizationsCreated));//Introducimos el número de puntos de localización creados
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//Nos permite controlar la orientación permitida en cada página del ViewPager
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity actualActivity = getActivity();
            if(actualActivity != null)
                actualActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }
}
