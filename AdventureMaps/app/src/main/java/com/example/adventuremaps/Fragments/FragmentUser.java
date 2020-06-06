package com.example.adventuremaps.Fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentUser extends Fragment {

    private OnFragmentInteractionListener mListener;
    private DatabaseReference myDataBaseReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_USERS_ADDRESS);
    private DatabaseReference localiationReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);
    private MainTabbetActivityVM viewModel;
    private TextView txtEmail, txtNickName, numberOfRoutes, numberOfLocalizations;
    //Manejador de hilos
    private Handler mainHandler;

    public FragmentUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        //Instanciamos el VM
        if(getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        //Instanciamos los elementos de la vista
        txtEmail = view.findViewById(R.id.TextViewEmailInfoActivity);
        txtNickName = view.findViewById(R.id.TextViewNickNameInfoActivity);
        numberOfRoutes = view.findViewById(R.id.TextViewNumberRoutesInfoActivity);
        numberOfLocalizations = view.findViewById(R.id.TextViewNumberLocalizationsInfoActivity);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla
            LinearLayout linearLayout = view.findViewById(R.id.LinearLayoutInfo01);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            param.weight = 50;
            linearLayout.setLayoutParams(param);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getContext() != null){
            //Instanciamos un manejador para el hilo secundario, esta parte del código se ejecutará una vez el código main haya finalizado
            mainHandler = new Handler(getContext().getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    getCurrentUserInfo();//Obtenemos información del usuario actual y la colocamos en la actividad actual
                }
            };
            mainHandler.post(myRunnable);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mainHandler != null)
            mainHandler.removeCallbacksAndMessages(null);//Removemos los mensajes y callbacks del controlador
    }

    /**
     * Interfaz
     * Nombre: getCurrentUserInfo
     * Comentario: El método nos permite obtener información del usuario actual (email, nickname y número
     * de rutas y localizaciones creadas) a través de la plataforma Firebase, colocando esta información
     * en TextViews de la actividad actual.
     * Cabecera: private void getCurrentUserInfo()
     * Postcondiciones: El método obtiene y coloca información del usuario en TextViews de la actividad actual.
     */
    private void getCurrentUserInfo(){
        myDataBaseReference.orderByChild(ApplicationConstants.FB_USER_EMAIL_CHILD).equalTo(viewModel.get_actualEmailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = "", nickName = "";
                int routesCreated = 0;
                for(DataSnapshot datas: dataSnapshot.getChildren()){//Solo se repetirá una vez
                    email=datas.child(ApplicationConstants.FB_USER_EMAIL_CHILD).getValue().toString();
                    nickName = datas.child(ApplicationConstants.FB_USER_NICKNAME_CHILD).getValue().toString();

                    for(DataSnapshot routes: datas.child(ApplicationConstants.FB_ROUTES_ADDRESS).getChildren()){//Se repetirá por cada ruta que haya creado el usuario
                        routesCreated++;
                    }
                }
                txtEmail.setText(email);//Introducimos el email del usuario actual
                txtNickName.setText(nickName);//Introducimos el nickName
                numberOfRoutes.setText(String.valueOf(routesCreated));//Introducimos el número de rutas creadas
                //Ahora obtendremos el número de localizaciones creadas por el usuario
                getNumberOfLocalizationPointsCreated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Interfaz
     * Nombre: getNumberOfLocalizationPointsCreated
     * Comentario: Este método nos permite almacenar en el VM el número de puntos de localización creados por
     * el usuario actual.
     * Cabecera: private void getNumberOfLocalizationPointsCreated()
     * Postcondiciones: El método almacena en el VM el número de puntos de localización creados por el
     * usuario actual.
     */
    private void getNumberOfLocalizationPointsCreated(){
        localiationReference.orderByChild(ApplicationConstants.FB_LOCATION_EMAIL_CREATOR_CHILD).equalTo(viewModel.get_actualEmailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int localizationsCreated = 0;
                for(DataSnapshot datas: dataSnapshot.getChildren()){//Solo se repetirá una vez
                    localizationsCreated++;
                }
                numberOfLocalizations.setText(String.valueOf(localizationsCreated));//Introducimos el número de puntos de localización creados
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
