package com.example.adventuremaps.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.Management.UtilDispositive;
import com.example.adventuremaps.R;

public class FragmentStart extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Handler mainHandler;

    public FragmentStart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        if(getContext() != null){
            //Instanciamos un manejador para el hilo secundario, esta parte del código se ejecutará una vez el código main haya finalizado
            mainHandler = new Handler(getContext().getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    //Si la aplicación no tiene los permisos necesarios, muestra por pantalla un dialogo para obtenerlos
                    if(getActivity() != null && (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ApplicationConstants.REQUEST_CODE_PERMISSIONS_MAIN_TABBET_ACTIVITY_WITH_START_MAP);
                    }
                }
            };
            mainHandler.post(myRunnable);
        }

        return view;
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
            if(actualActivity != null){//Si el dispositivo actual no es una tablet
                if(getContext() != null && !UtilDispositive.isTablet(getContext())){//Si el dispositivo actual no es una tablet
                    actualActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        }
    }
}
