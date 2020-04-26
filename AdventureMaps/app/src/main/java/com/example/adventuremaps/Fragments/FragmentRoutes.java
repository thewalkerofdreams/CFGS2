package com.example.adventuremaps.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
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

public class FragmentRoutes extends Fragment {

    private OnFragmentInteractionListener mListener;
    private DatabaseReference myDataBaseReference = FirebaseDatabase.getInstance().getReference("Users");
    private ListView listView;
    private MainTabbetActivityVM viewModel;
    private AlertDialog alertDialogDeleteRoute, alertDialogShortList;
    private Button btnFav, btnDelete, btnOrderRoutes;
    private SharedPreferences sharedpreferencesField;
    private SharedPreferences sharedPreferencesFav;

    public FragmentRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routes, container, false);

        //Instanciamos los SharedPreference
        sharedpreferencesField = getActivity().getSharedPreferences("OrderRouteListField", Context.MODE_PRIVATE);
        sharedPreferencesFav = getActivity().getSharedPreferences("OrderRouteListFav", Context.MODE_PRIVATE);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        //Instanciamos los elementos de la UI
        btnFav = view.findViewById(R.id.btnFavFragmentRoutes);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor02 = sharedPreferencesFav.edit();
                if(btnFav.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si el filtro estaba activado
                    btnFav.setBackgroundResource(R.drawable.empty_star);
                    editor02.putBoolean("OrderRouteListFav", false);
                }else{
                    btnFav.setBackgroundResource(R.drawable.fill_star);
                    editor02.putBoolean("OrderRouteListFav", true);
                }
                editor02.commit();
                loadList();//Recargamos la lista
            }
        });

        btnDelete = view.findViewById(R.id.btnTrashFragmentRoutes);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewModel.get_selectedRoutes().isEmpty()){
                    showDeleteRouteDialog();
                }else{
                    Toast.makeText(getActivity(), R.string.no_exist_selected_route, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOrderRoutes = view.findViewById(R.id.btnOrderFragmentRoutes);
        btnOrderRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderRoutesListDialog();
            }
        });

        listView = view.findViewById(R.id.RouteList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsRoute item = (ClsRoute) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_selectedRoutes().isEmpty()){//Si no hay ninguna ruta seleccionada
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){//Si el usuario concedió los permisos de localización
                        throwSeeAndEditRouteActivity(item);//Lanzamos la actividad SeeAndEditRouteActivity, mostrando la ruta clicada
                    }else{
                        Toast.makeText(getActivity(), R.string.error_location_permission, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(viewModel.get_selectedRoutes().contains(item)){//Si la ruta ya estaba seleccionada, la deselecciona
                        viewModel.get_selectedRoutes().remove(item);//Eliminamos esa ruta de la lista de seleccionadas
                        view.setBackgroundColor(getResources().getColor(R.color.WhiteItem));//Cambiamos el color de la ruta deseleccionada
                    }else{
                        viewModel.get_selectedRoutes().add(item);//Añadimos la ruta a la lista de seleccionadas
                        view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la ruta seleccionada
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(viewModel.get_selectedRoutes().isEmpty()){//Si aún no se ha seleccionado una ruta
                    ClsRoute item = (ClsRoute) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                    viewModel.get_selectedRoutes().add(item);//Añadimos la ruta a la lista de seleccionadas
                    view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la ruta seleccionada
                }

                return true;
            }
        });

        if(sharedPreferencesFav.getBoolean("OrderRouteListFav", false))//Si el filtro de favoritos se encuentra activo
            btnFav.setBackgroundResource(R.drawable.fill_star);

        if(savedInstanceState != null){//Si la actividad tiene datos almacenados
            if(viewModel.is_dialogDeleteRouteShowing()) {//Si el dialogo de eliminación estaba abierto lo recargamos
                showDeleteRouteDialog();
            }else{
                if(viewModel.is_dialogShortRouteListShowing())//Si el dialogo de ordenación estaba abierto
                    showOrderRoutesListDialog();
            }
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla
            LinearLayout linearLayout = view.findViewById(R.id.LinearLayoutTabRoutes);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            param.weight = 40;
            linearLayout.setLayoutParams(param);
        }

        return view;
    }

    /**
     * Interfaz
     * Nombre: throwSeeAndEditRouteActivity
     * Comentario: El método lanza la actividad SeeAndEditRouteActivity, que mostrará una ruta en
     * específico.
     * Cabecera: private void throwSeeAndEditRouteActivity(ClsRoute route)
     *  -ClsRoute route
     * Postcondiciones: El método lanza la actividad SeeAndEditRouteActivity, mostrando una ruta.
     */
    private void throwSeeAndEditRouteActivity(ClsRoute route){
        Intent intent = new Intent(getActivity(), SeeAndEditRouteActivity.class);
        intent.putExtra("ActualIdRoute", route.getRouteId());
        intent.putExtra("ActualEmail", viewModel.get_actualEmailUser());
        intent.putExtra("ActualRouteName", route.getName());
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        storeAndLoadRoutesFromActualUser();//Obtenemos las rutas del usuario actual y cargamos la lista con ellas
    }

    /**
     * Interfaz
     * Nombre: storeAndLoadRoutesFromActualUser
     * Comentario: El método guarda en el VM las rutas almacenadas en la plataforma Firebase del usuario actual,
     * para luego mostrarlas en la lista de rutas.
     * Cabecera: private void storeAndLoadRoutesFromActualUser()
     * Postcondiciones: El método almacena las rutas del usuario actual en el VM y las carga en
     * una lista.
     */
    private void storeAndLoadRoutesFromActualUser(){
        // Read from the database
        myDataBaseReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_itemsRouteList().clear();//Limpiamos la lista de rutas
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("routes").getChildren()){
                        ClsRoute route = booksSnapshot.getValue(ClsRoute.class);
                        viewModel.get_itemsRouteList().add(route);
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
     * Cabecera: private void deleteRouteDialog()
     * Postcondiciones: El método muestra un dialogo por pantalla, si el usuario lo confirma eliminará
     * la tuta seleccionada, en caso contrario no sucederá nada.
     */
    private void showDeleteRouteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_route);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.routes_deleted, Toast.LENGTH_SHORT).show();
                deleteSelectedRoutes();//Eliminamos las rutas seleccionadas

                viewModel.set_dialogDeleteRouteShowing(false);//Indicamos que el dialogo ha finalizado
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
     * Nombre: deleteSelectedRoutes
     * Comentario: El método elimina las rutas seleccionadas en la lista.
     * Cabecera: private void deleteSelectedRoutes()
     * Postcondiciones: El método elimina las rutas seleccionadas en la lista.
     */
    private void deleteSelectedRoutes(){
        DatabaseReference drRoutePoint;

        for(int i = 0; i < viewModel.get_selectedRoutes().size(); i++){
            drRoutePoint = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routes").child(viewModel.get_selectedRoutes().get(i).getRouteId());
            drRoutePoint.removeValue();
        }

        viewModel.get_selectedRoutes().clear();//Vaciamos la lista de selecionadas
    }

    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar las rutas del usuario en la lista actual.
     * Cabecera: private void loadList()
     * Postcondiciones: El método carga la lista rutas del usuario actual.
     */
    private void loadList(){
        Parcelable state = listView.onSaveInstanceState();//Guardamos el estado actual del listview (Nos interesa la posición actual del scroll)

        //Instanciamos los SharedPreference (Este método es llamado desde onStart, por lo que también debemos instanciarlos aquí también)
        if(getActivity() != null){
            sharedpreferencesField = getActivity().getSharedPreferences("OrderRouteListField", Context.MODE_PRIVATE);
            sharedPreferencesFav = getActivity().getSharedPreferences("OrderRouteListFav", Context.MODE_PRIVATE);
        }

        int field = sharedpreferencesField.getInt("OrderRouteListField", 1);
        boolean favourite = sharedPreferencesFav.getBoolean("OrderRouteListFav", false);
        orderList(field, favourite);//Ordenamos la lista

        RouteListAdapter adapter = new RouteListAdapter(getActivity(), R.layout.route_list_item, viewModel.get_itemsRouteList()){
            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                View view = super.getView(position, convertView, viewGroup);

                if(viewModel.get_selectedRoutes().contains(viewModel.get_itemsRouteList().get(position))){//Si la ruta se encuentra en la lista de seleccionadas
                    view.setBackgroundResource(R.color.BlueItem);
                }else{
                    view.setBackgroundResource(R.color.WhiteItem);
                }

                return view;
            }
        };
        listView.setAdapter(adapter);

        listView.onRestoreInstanceState(state);//Le asignamos el estado que almacenamos
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
        }else{
            if(alertDialogShortList != null && alertDialogShortList.isShowing()){//Si se encuentra abierto el dialogo de ordenación
                alertDialogShortList.dismiss();
                viewModel.set_dialogShortRouteListShowing(true);
            }
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
     * Cabecera: private void orderList(int field, boolean favourite)
     * Entrada:
     *  -int field
     *  -boolean favourite
     * Precondiciones:
     *  -fields debe ser igual a 1 o 2.
     * Postcondiciones: El método ordena la lista de rutas según los criterios introducidos
     * por parámetros.
     */
    private void orderList(int field, boolean favourite){
        if(favourite){//Si la lista se encuentra ordenada por favoritos
            if(field == 1){
                new OrderLists().orderRouteListAscByNameAndFavourite(viewModel.get_itemsRouteList());//Order list by name and fav
            }else{
                new OrderLists().orderRouteListAscByDateAndFavourite(viewModel.get_itemsRouteList());//Order list by date of creation and fav
            }
        }else{
            if(field == 1){
                new OrderLists().orderRouteListByName(viewModel.get_itemsRouteList());//Order list by name
            }else{
                new OrderLists().orderRouteListAscByDate(viewModel.get_itemsRouteList());//Order list by date of creation
            }
        }
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

    /**
     * Interfaz
     * Nombre: showOrderRoutesListDialog
     * Comentario: Este método muestra por pantalla un dialogo con los diferentes tipos de ordenación, que se puede aplicar
     * sobre la lista de rutas, si el usuario confirma el dialogo, se ordenará la lista actual dependiendo
     * del tipo seleccionado.
     * Cabecera: private void showOrderRoutesListDialog()
     * Postcondiciones: El método abre un dialogo de ordenación, si el usuario confirma el dialogo se
     * ordena la lista de rutas por el criterio seleccionado.
     */
    private void showOrderRoutesListDialog() {
        final CharSequence [] orderTypes = {getResources().getString(R.string.name),
                getResources().getString(R.string.date_of_creation)};

        viewModel.set_positionSelectedOrderTypesRoutes(sharedpreferencesField.getInt("OrderRouteListField", 1) -1);//Obtenemos el último tipo de selección, si lo hubiera

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.order_types);
        builder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        builder.setSingleChoiceItems(orderTypes, viewModel.get_positionSelectedOrderTypesRoutes(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_positionSelectedOrderTypesRoutes(which);
            }
        });
        builder.setPositiveButton(R.string.apply_changes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor01 = sharedpreferencesField.edit();//Guardamos los filtros
                editor01.putInt("OrderRouteListField", viewModel.get_positionSelectedOrderTypesRoutes()+1);
                editor01.commit();
                loadList();//Recargamos la lista

                viewModel.set_dialogShortRouteListShowing(false);//Indicamos que el dialogo finalizó
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogShortRouteListShowing(false);//Indicamos que el dialogo finalizó
            }
        });
        alertDialogShortList = builder.create();
        alertDialogShortList.show();
    }
}
