package com.example.adventuremaps.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.SeeAndEditRouteActivity;
import com.example.adventuremaps.Adapters.RouteListAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentRoutes extends Fragment {

    private OnFragmentInteractionListener mListener;
    private DatabaseReference myDataBaseReference = FirebaseDatabase.getInstance().getReference("Users");
    private ListView listView;
    private RouteListAdapter adapter;
    private ArrayList<ClsRoute> itemsList = new ArrayList<>();
    private MainTabbetActivityVM viewModel;

    public FragmentRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routes, container, false);

        //Inflamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        listView = view.findViewById(R.id.RouteList);

        loadList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsRoute item = (ClsRoute) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                Intent intent = new Intent(getActivity(), SeeAndEditRouteActivity.class);
                intent.putExtra("IdRoute", item.getRouteId());
                startActivity(intent);
            }
        });
        //listView.setOnItemLongClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Read from the database
        myDataBaseReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                itemsList.clear();//Limpiamos la lista de rutas
                List<String> keys = new ArrayList<>();
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("routes").getChildren()){
                        //loop 2 to go through all the child nodes of routes node
                        ClsRoute route = booksSnapshot.getValue(ClsRoute.class);
                        itemsList.add(route);
                    }
                }
                loadList();//Recargamos la lista de rutas
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar las rutas del usuario en la lista actual.
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista rutas del usuario actual.
     */
    public void loadList(){
        adapter = new RouteListAdapter(getActivity(), R.layout.route_list_item, itemsList);
        listView.setAdapter(adapter);
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
}
