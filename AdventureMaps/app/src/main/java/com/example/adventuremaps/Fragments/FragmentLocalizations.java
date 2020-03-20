package com.example.adventuremaps.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.DetailsLocalizationPointActivity;
import com.example.adventuremaps.Activities.Models.ClsLocalizationPointWithFav;
import com.example.adventuremaps.Adapters.LocalizationListAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
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

public class FragmentLocalizations extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private MainTabbetActivityVM viewModel;
    private AlertDialog alertDialogDeleteLocalization;
    private Spinner spinner;
    private ArrayList<String> itemsSpinner = new ArrayList<>();
    private Button btnFav, btnDelete;
    private boolean selectDefaultPassed = false;
    private ArrayAdapter<String> adapter;
    private DatabaseReference drLocalization = FirebaseDatabase.getInstance().getReference("Localizations");
    private DatabaseReference drUser = FirebaseDatabase.getInstance().getReference("Users");
    SharedPreferences sharedpreferencesField;
    SharedPreferences sharedPreferencesFav;

    public FragmentLocalizations() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_localizations, container, false);

        //Instanciamos los SharedPreference
        sharedpreferencesField = getActivity().getSharedPreferences("OrderLocalizationListField", Context.MODE_PRIVATE);
        sharedPreferencesFav = getActivity().getSharedPreferences("OrderLocalizationsListFav", Context.MODE_PRIVATE);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);

        viewModel.set_localizationsActualUser(null);//Le asignamos un valor nulo para evitar fallos a la hora de cargar el listado de la plataforma
        viewModel.set_localizationsIdActualUser(null);//Le asignamos un valor nulo para evitar fallos a la hora de cargar el listado de la plataforma

        //Instanciamos los elementos de la UI
        btnFav = view.findViewById(R.id.btnFavFragmentLocaliaztions);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor02 = sharedPreferencesFav.edit();
                if(btnFav.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si el filtro estaba activado
                    btnFav.setBackgroundResource(R.drawable.empty_star);
                    editor02.putBoolean("OrderLocalizationListFav", false);
                }else{
                    btnFav.setBackgroundResource(R.drawable.fill_star);
                    editor02.putBoolean("OrderLocalizationListFav", true);
                }
                editor02.commit();
                loadList();//Recargamos la lista
            }
        });

        btnDelete = view.findViewById(R.id.btnTrashFragmentLocalizations);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewModel.get_selectedLocalizations().isEmpty()){
                    showDeleteLocalizationDialog();
                }else{
                    Toast.makeText(getActivity(), R.string.no_exist_selected_localization, Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner = view.findViewById(R.id.SpinnerFragmentLocalizations);
        itemsSpinner.add(getActivity().getResources().getString(R.string.name));
        itemsSpinner.add(getActivity().getResources().getString(R.string.date_of_creation));

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemsSpinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(sharedpreferencesField.getInt("OrderLocalizationListField", 1)-1);//Ajustamos la selección según el filtro guardado
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(selectDefaultPassed){//Evitamos la primera llamada por defecto al primer elemento del spinner
                    SharedPreferences.Editor editor01 = sharedpreferencesField.edit();//Guardamos los filtros
                    editor01.putInt("OrderLocalizationListField", position+1);
                    editor01.commit();
                    loadList();//Recargamos la lista
                }
                selectDefaultPassed = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listView = view.findViewById(R.id.LocalizationList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsLocalizationPointWithFav item = (ClsLocalizationPointWithFav) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_selectedLocalizations().isEmpty()){//Si no hay ninguna localización seleccionada
                    Intent intent = new Intent(getActivity(), DetailsLocalizationPointActivity.class);
                    intent.putExtra("ActualLocalization", item.get_localizationPoint());
                    intent.putExtra("ActualEmailUser", viewModel.get_actualEmailUser());
                    startActivity(intent);
                }else{
                    if(viewModel.get_selectedLocalizations().contains(item)){//Si la ruta ya estaba seleccionada, la deselecciona
                        viewModel.get_selectedLocalizations().remove(item);//Eliminamos esa ruta de la lista de seleccionadas
                        view.setBackgroundColor(getResources().getColor(R.color.WhiteItem));//Cambiamos el color de la ruta deseleccionada
                    }else{
                        viewModel.get_selectedLocalizations().add(item);//Añadimos la ruta a la lista de seleccionadas
                        view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la ruta seleccionada
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(viewModel.get_selectedLocalizations().isEmpty()){//Si aún no existe ninguna localización seleccionada
                    ClsLocalizationPointWithFav item = (ClsLocalizationPointWithFav) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                    viewModel.get_selectedLocalizations().add(item);//Añadimos la localización a la lista de seleccionadas
                    view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la ruta seleccionada
                }

                return true;
            }
        });

        if(sharedPreferencesFav.getBoolean("OrderLocalizationListFav", false))//Si el filtro de favoritos se encuentra activo
            btnFav.setBackgroundResource(R.drawable.fill_star);

        if(savedInstanceState != null && viewModel.is_dialogDeleteLocalizationShowing()) {//Si el dialogo de eliminación estaba abierto lo recargamos
            showDeleteLocalizationDialog();
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla
            LinearLayout linearLayout = view.findViewById(R.id.LinearLayoutTabLocalizations);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    (float) 1.0
            );
            param.weight = 40;
            linearLayout.setLayoutParams(param);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Read from the database
        drLocalization.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.set_localizationsActualUser(new ArrayList<ClsLocalizationPoint>());//Limpiamos la lista de puntos de localización favoritos
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);
                    if(localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser())){
                        viewModel.get_localizationsActualUser().add(localizationPoint);//Almacenamos el punto de localización
                    }
                }
                loadList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        drUser.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.set_localizationsIdActualUser(new ArrayList<String>());//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("localizationsId").getChildren()){
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdActualUser().add(localizationId);
                    }
                }
                loadList();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Interfaz
     * Nombre: showDeleteLocalizationDialog
     * Comentario: Este método muestra un dialogo por pantalla para eliminar una localización seleccionada.
     * Si el usuario confirma la eliminación, se eliminará la localización de la plataforma FireBase, en caso
     * contrario no sucederá nada.
     * Cabecera: public void showDeleteLocalizationDialog()
     * Postcondiciones: El método muestra un dialogo por pantalla, si el usuario lo confirma eliminará
     * la localización seleccionada, en caso contrario no sucederá nada.
     */
    public void showDeleteLocalizationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_localization_point);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.localization_point_deleted, Toast.LENGTH_SHORT).show();
                //Eliminamos las localizaciones seleccionadas
                DatabaseReference drRoutePoint;

                for(int i = 0; i < viewModel.get_selectedLocalizations().size(); i++){
                    //Eliminamos el id del punto de localización asignado a la lista de favoritos del usuario si este lo tuviera asignado como favorito
                    drUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("localizationsId").child(viewModel.get_selectedLocalizations()
                            .get(i).get_localizationPoint().getLocalizationPointId()).removeValue();
                    //Eliminamos el punto de localización
                    drLocalization.child(viewModel.get_selectedLocalizations().get(i).get_localizationPoint().getLocalizationPointId()).removeValue();
                }

                viewModel.set_dialogDeleteLocalizationShowing(false);//Indicamos que el dialogo ha finalizado
                viewModel.get_selectedLocalizations().clear();//Vaciamos la lista de selecionadas
                loadList();//Recargamos la lista de rutas
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogDeleteLocalizationShowing(false);
            }
        });

        alertDialogDeleteLocalization = alertDialogBuilder.create();
        alertDialogDeleteLocalization.show();
    }

    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar las rutas del usuario en la lista actual.
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista rutas del usuario actual.
     */
    public void loadList(){

        if(viewModel.get_localizationsActualUser() != null && viewModel.get_localizationsIdActualUser() != null){
            if(viewModel.get_selectedLocalizations().isEmpty()){//Si la lista se seleccionadas se encuentra vacía
                loadLocalizationModelList();
            }

            Parcelable state = listView.onSaveInstanceState();//Guardamos el estado actual del listview (Nos interesa la posición actual del scroll)

            //Instanciamos los SharedPreference (Este método es llamado desde onStart, por lo que también debemos instanciarlos aquí también)
            if(getActivity() != null){
                sharedpreferencesField = getActivity().getSharedPreferences("OrderLocalizationListField", Context.MODE_PRIVATE);
                sharedPreferencesFav = getActivity().getSharedPreferences("OrderLocalizationListFav", Context.MODE_PRIVATE);
            }

            int field = sharedpreferencesField.getInt("OrderLocalizationListField", 1);
            boolean favourite = sharedPreferencesFav.getBoolean("OrderLocalizationListFav", false);
            orderList(field, favourite);//Ordenamos la lista

            LocalizationListAdapter adapter = new LocalizationListAdapter(getActivity(), R.layout.localization_list_item, viewModel.get_itemsLocalizationList()){
                @Override
                public View getView(int position, View convertView, ViewGroup viewGroup) {
                    View view = super.getView(position, convertView, viewGroup);

                    if(viewModel.get_selectedLocalizations().contains(viewModel.get_itemsLocalizationList().get(position))){//Si la ruta se encuentra en la lista de seleccionadas
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
    }

    /**
     * Interfaz
     * Nombre: loadLocalizationModelList
     * Comentario: Este método nos permite cargar la lista de localizaciones, en la que los
     * items son una clase modelo de una localización. Esta clase modelo contiene la localización
     * del usuario y otro atributo booleano que indicará si el usuario tiene marcada como favorita
     * dicha localización. Para crear esta lista, el método utiliza la lista de _localizationsIdActualUser
     * y _localizationsActualUser del VM.
     * Cabecera: public void loadLocalizationModelList()
     * Postcondiciones: El método carga un listado de la clase modelo ClsLocalizationPointWithFav en el atributo _itemsLocalizationList del VM.
     */
    public void loadLocalizationModelList(){
        boolean fav;
        viewModel.get_itemsLocalizationList().clear();//Limpiamos los items de la lista
        for(int i = 0; i < viewModel.get_localizationsActualUser().size(); i++){
            if(viewModel.get_localizationsIdActualUser().contains(viewModel.get_localizationsActualUser().get(i).getLocalizationPointId())){//Si el usuario la tiene marcada como favorita
                fav = true;
            }else{
                fav = false;
            }
            viewModel.get_itemsLocalizationList().add(new ClsLocalizationPointWithFav(viewModel.get_localizationsActualUser().get(i) , fav));//Almacenamos la clase modelo en la lista de items
        }
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
                new OrderLists().orderLocalizationListAscByNameAndFavourite(viewModel.get_itemsLocalizationList());//Order list by name and fav
            }else{
                new OrderLists().orderLocalizationListAscByDateAndFavourite(viewModel.get_itemsLocalizationList());//Order list by date of creation and fav
            }
        }else{
            if(field == 1){
                new OrderLists().orderLocalizationListByName(viewModel.get_itemsLocalizationList());//Order list by name
            }else{
                new OrderLists().orderLocalizationListAscByDate(viewModel.get_itemsLocalizationList());//Order list by date of creation
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
