package com.iesnervion.usuario.examensegundaevaluacionpablojarana.Fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Futbolista;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.FutbolistaPosicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Posicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.R;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.VMyRepositorios.AddJugadorRepository;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.VMyRepositorios.AddJugadorViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *  interface
 * to handle interaction events.
 * Use the {@link AddJugadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddJugadorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AddJugadorViewModel viewModel;
    private EditText txtNombreJugador;
    private Button btnAceptar;
    private ArrayList<CheckBox> checkBoxes;
    private CheckBox checkBoxPortero;
    private  CheckBox checkBoxDelantero;
    private CheckBox checkBoxDefensa;
    private CheckBox checkBoxCentrocampista;
    private EditText txtApellidosJugador;
    private Futbolista futbolista;
    private AddJugadorRepository repository;
    private ArrayList<Integer> idsPosiciones;
    //private OnFragmentInteractionListener mListener;

    public AddJugadorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddJugadorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddJugadorFragment newInstance(String param1, String param2) {
        AddJugadorFragment fragment = new AddJugadorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel= ViewModelProviders.of((FragmentActivity) getActivity()).get(AddJugadorViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        repository=new AddJugadorRepository(getActivity().getApplication());
        checkBoxes=new ArrayList<CheckBox>();
        idsPosiciones=new ArrayList<Integer>();
        final View v= inflater.inflate(R.layout.fragment_add_jugador, container, false);
        futbolista=new Futbolista();
        txtNombreJugador=(EditText)v.findViewById(R.id.txtNombreJugador);
        txtApellidosJugador=(EditText)v.findViewById(R.id.txtApellidoJugador);
        btnAceptar=(Button)v.findViewById(R.id.btnAceptar);
        checkBoxPortero=(CheckBox)v.findViewById(R.id.checkboxPortero);
        checkBoxDefensa=(CheckBox)v.findViewById(R.id.checkboxDefensa);
        checkBoxCentrocampista=(CheckBox)v.findViewById(R.id.checkboxCentrocampista);
        checkBoxDelantero=(CheckBox)v.findViewById(R.id.checkboxDelantero);
        checkBoxes.add(checkBoxPortero);
        checkBoxes.add(checkBoxCentrocampista);
        checkBoxes.add(checkBoxDefensa);
        checkBoxes.add(checkBoxDelantero);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hayAlgunCheckBoxActivado())
                {
                    getIDdeCheckboxesCheckeados();
                    futbolista.setNombre(txtNombreJugador.getText().toString());
                    futbolista.setApellidos(txtApellidosJugador.getText().toString());
                    quitarChecks();
                    txtNombreJugador.setText("");
                    txtApellidosJugador.setText("");
                    viewModel.insertFutbolista(futbolista);
                    for(int i=0;i<idsPosiciones.size();i++)
                    {
                        FutbolistaPosicion futbolistaPosicion= new FutbolistaPosicion();
                        futbolistaPosicion.setIdFutbolista(futbolista.getId());
                        futbolistaPosicion.setIdPosicion(idsPosiciones.get(i));
                        viewModel.insertFutbolistaPosicion(futbolistaPosicion);
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Por favor, elija alguna posicion",Toast.LENGTH_LONG).show();
                }
            }
        });
        repository.getIdPosicion().observe((LifecycleOwner)getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer!=null) {
                    int idPosicion = integer;
                    idsPosiciones.add(idPosicion);
                }
            }
        });
        repository.getIdFutbolista().observe((LifecycleOwner) getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer!=null)
                {
                    futbolista.setId(integer);
                }
            }
        });
        /*viewModel.getLiveDataPosiciones().observe((LifecycleOwner) getActivity(), new Observer<List<Posicion>>() {
            @Override
            public void onChanged(@Nullable List<Posicion> posicions) {

                for(int i=0;i<posicions.size();i++) {
                    CheckBox checkBox = new CheckBox(getActivity().getApplicationContext());
                    checkBox.setText(posicions.get(i).getDemarcarcion());
                    checkBox.setLayoutParams();
                }

            }
        });*/
        return v;
    }

    private void getIDdeCheckboxesCheckeados()
    {
        for(int i =0;i<checkBoxes.size();i++)
        {
            if(checkBoxes.get(i).isChecked()) {
                repository.getIDdePosicion(checkBoxes.get(i).getText().toString());
            }
        }
    }
    private boolean hayAlgunCheckBoxActivado()
    {
        boolean hayActivado=false;
        for(int i=0;i<checkBoxes.size()&&!hayActivado;i++)
        {
            if(checkBoxes.get(i).isChecked())
            {
                hayActivado=true;
            }
        }
        return hayActivado;
    }
    private void quitarChecks()
    {
        for(int i=0;i<checkBoxes.size();i++)
        {
            checkBoxes.get(i).setChecked(false);
        }
    }
    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
