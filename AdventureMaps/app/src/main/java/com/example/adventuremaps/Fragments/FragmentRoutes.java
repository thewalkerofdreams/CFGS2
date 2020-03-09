package com.example.adventuremaps.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.SeeAndEditRouteActivity;
import com.example.adventuremaps.Adapters.RouteListAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.Management.OrderLists;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.auth.FirebaseAuth;
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
    private AlertDialog alertDialogDeleteRoute;
    SharedPreferences sharedpreferences;

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
                intent.putExtra("ActualIdRoute", item.getRouteId());
                intent.putExtra("ActualEmail", viewModel.get_actualEmailUser());
                intent.putExtra("ActualRouteName", item.getName());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClsRoute item = (ClsRoute) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                viewModel.set_routeSelected(item);
                showDeleteRouteDialog();
                return true;
            }
        });

        if(savedInstanceState != null && viewModel.is_dialogDeleteRouteShowing()) {//Si el dialogo de eliminación estaba abierto lo recargamos
            showDeleteRouteDialog();
        }

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
     * Nombre: deleteRouteDialog
     * Comentario: Este método muestra un dialogo por pantalla para eliminar una ruta seleccionada.
     * Si el usuario confirma la eliminación, se eliminará la ruta de la plataforma FireBase, en caso
     * contrario no sucederá nada.
     * Cabecera: public void deleteRouteDialog()
     * Postcondiciones: El método muestra un dialogo por pantalla, si el usuario lo confirma eliminará
     * la tuta seleccionada, en caso contrario no sucederá nada.
     */
    public void showDeleteRouteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_route);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.route_deleted, Toast.LENGTH_SHORT).show();
                //Eliminamos la ruta seleccionada
                DatabaseReference drRoutePoint;
                drRoutePoint = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(viewModel.get_routeSelected().getRouteId());
                drRoutePoint.removeValue();

                loadList();
                viewModel.set_dialogDeleteRouteShowing(false);
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogDeleteRouteShowing(false);
            }
        });

        alertDialogDeleteRoute = alertDialogBuilder.create();
        alertDialogDeleteRoute.show();
    }

    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar las rutas del usuario en la lista actual.
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista rutas del usuario actual.
     */
    public void loadList(){
        sharedpreferences = getActivity().getSharedPreferences("OrderRouteListField", Context.MODE_PRIVATE);//Instanciamos el objeto SharedPreference
        int field = sharedpreferences.getInt("OrderRouteListField", 1);
        sharedpreferences = getActivity().getSharedPreferences("OrderRouteListFav", Context.MODE_PRIVATE);//Instanciamos el objeto SharedPreference
        boolean favourite = sharedpreferences.getBoolean("OrderRouteListFav", false);
        orderList(field, favourite);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteRoute != null && alertDialogDeleteRoute.isShowing()) {//Si se encuentra abierto el dialogo de deleteGameMode
            alertDialogDeleteRoute.dismiss();// close dialog to prevent leaked window
            viewModel.set_dialogDeleteRouteShowing(true);
        }
    }

    /**
     * Interfaz
     * Nombre: orderList
     * Comentario: Este método nos permite ordenar la lista de rutas según un criterio
     * en específico. La lista se ordenará según los parámetros de entrada.
     * Field:
     *  -1 (RouteName)
     *  -2 (DateOfCreation)
     * Favourite:
     *  -true (Order by favourites routes)
     *  -false (Does not take into account favorite routes)
     * Cabecera: public void orderList(int field, boolean favourite)
     * Entrada:
     *  -int field
     *  -boolean favourite
     * Precondiciones:
     *  -fields debe ser igual a 1 o 2.
     * Postcondiciones: El método ordena la lista de rutas según los criterios introducidos
     * por parámetros.
     */
    public void orderList(int field, boolean favourite){
        if(favourite){//Si la lista se encuentra ordenada por favoritos
            if(field == 1){
                //Order list by name and fav
            }else{
                //Order list by date of creation and fav
            }
        }else{
            if(field == 1){
                //Order list by name
                new OrderLists().orderRouteListByName(itemsList);
            }else{
                //Order list by date of creation
            }
        }
    }
}
