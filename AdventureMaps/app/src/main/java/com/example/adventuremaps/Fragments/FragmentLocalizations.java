package com.example.adventuremaps.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.DetailsLocalizationPointActivity;
import com.example.adventuremaps.Activities.ui.MainTabbet.MainTabbetActivity;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.Models.ClsLocalizationPointWithFav;
import com.example.adventuremaps.Adapters.LocalizationListAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.Management.OrderLists;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainTabbetActivityVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private AlertDialog alertDialogDeleteLocalization, alertDialogShareLocalization, alertDialogShortList;
    private Button btnFav, btnDelete, btnShare, btnOrderLocalizations;
    private DatabaseReference drLocalization = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);
    private DatabaseReference drUser = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_USERS_ADDRESS);
    private FirebaseUser firebaseCurrentUser;
    private SharedPreferences sharedpreferencesField;
    private SharedPreferences sharedPreferencesFav;
    private ValueEventListener listener;

    public FragmentLocalizations() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_localizations, container, false);

        //Instanciamos los SharedPreference
        if(getActivity() != null){
            sharedpreferencesField = getActivity().getSharedPreferences(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FIELD, Context.MODE_PRIVATE);
            sharedPreferencesFav = getActivity().getSharedPreferences(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FAV, Context.MODE_PRIVATE);

            //Instanciamos el VM
            viewModel = ViewModelProviders.of(getActivity()).get(MainTabbetActivityVM.class);
            viewModel.set_localizationsActualUser(null);//Le asignamos un valor nulo para evitar fallos a la hora de cargar la lista de localizaciones
            viewModel.set_localizationsIdActualUser(null);//Le asignamos un valor nulo para evitar fallos a la hora de cagar la lista de localizaciones
        }

        //Obtenemos la referencia del usuario actual
        firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Instanciamos los elementos de la UI
        btnFav = view.findViewById(R.id.btnFavFragmentLocaliaztions);//Botón para ordenar la lista por favoritos
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor02 = sharedPreferencesFav.edit();
                if(btnFav.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si el filtro estaba activado
                    btnFav.setBackgroundResource(R.drawable.empty_star);
                    editor02.putBoolean(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FAV, false);
                }else{
                    btnFav.setBackgroundResource(R.drawable.fill_star);
                    editor02.putBoolean(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FAV, true);
                }
                editor02.apply();
                loadList();//Recargamos la lista
            }
        });

        btnDelete = view.findViewById(R.id.btnTrashFragmentLocalizations);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewModel.get_selectedLocalizations().isEmpty()){//Si hay alguna localización seleccionada
                    showDeleteLocalizationDialog();
                }else{
                    Toast.makeText(getActivity(), R.string.no_exist_selected_localization, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOrderLocalizations = view.findViewById(R.id.btnOrderLocalizations);//Botón para ordenar la lista por otros criterios
        btnOrderLocalizations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderLocalizationListDialog();
            }
        });
        listView = view.findViewById(R.id.LocalizationList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsLocalizationPointWithFav item = (ClsLocalizationPointWithFav) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_selectedLocalizations().isEmpty()){//Si no hay ninguna localización seleccionada, entramos en los detalles de la localización
                    throwDetailsLocalizationPointActivity(item);//Lanzamos la actividad de detalles de la localización
                }else{
                    if(!item.get_localizationPoint().getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización no le pertenece al usuario
                        Toast.makeText(getActivity(), R.string.error_selected_localizations_no_owner, Toast.LENGTH_SHORT).show();
                    }else{
                        if(!item.get_localizationPoint().isShared()){//Si la localización no se encuentra compartida con la aplicación
                            if(viewModel.get_selectedLocalizations().contains(item)){//Si la localización ya estaba seleccionada, la deselecciona
                                viewModel.get_selectedLocalizations().remove(item);//Eliminamos esa localización de la lista de seleccionadas
                                changeBackgroundColorItemView(view, position);//Cambiamos el color de la localización
                            }else{
                                viewModel.get_selectedLocalizations().add(item);//Añadimos la localización a la lista de seleccionadas
                                view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la localización seleccionada
                            }
                        }else{
                            Toast.makeText(getActivity(), R.string.error_selected_localizations_shared, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(viewModel.get_selectedLocalizations().isEmpty()){//Si aún no existe ninguna localización seleccionada
                    ClsLocalizationPointWithFav item = (ClsLocalizationPointWithFav) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                    if(!item.get_localizationPoint().getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización no le pertenece al usuario
                        Toast.makeText(getActivity(), R.string.error_selected_localizations_no_owner, Toast.LENGTH_SHORT).show();
                    }else{
                        if(!item.get_localizationPoint().isShared()) {//Si la localización no se encuentra compartida con la aplicación
                            viewModel.get_selectedLocalizations().add(item);//Añadimos la localización a la lista de seleccionadas
                            view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la ruta seleccionada
                        }else{
                            Toast.makeText(getActivity(), R.string.error_selected_localizations_shared, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            }
        });

        btnShare = view.findViewById(R.id.btnShareFragmentLocalizations);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.get_selectedLocalizations().isEmpty()){//Si no hay ninguna localización seleccionada
                    Toast.makeText(getActivity(), R.string.error_selected_localizations_empty, Toast.LENGTH_SHORT).show();
                }else{
                    if(viewModel.get_selectedLocalizations().size() > 1){//Si hay más de una localización seleccionada
                        Toast.makeText(getActivity(), R.string.error_selected_localizations_overflowed, Toast.LENGTH_SHORT).show();
                    }else{
                        openShareDialog();//Abrimos el dialogo
                    }
                }
            }
        });

        if(sharedPreferencesFav.getBoolean(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FAV, false))//Si el filtro de favoritos se encuentraba activo
            btnFav.setBackgroundResource(R.drawable.fill_star);

        if(savedInstanceState != null){//Si la actividad ya contiene datos almacenados
            if(viewModel.is_dialogDeleteLocalizationShowing()) {//Si el dialogo de eliminación estaba abierto lo recargamos
                showDeleteLocalizationDialog();
            }else{
                if(viewModel.is_dialogShareLocalizationShowing()) {//Si el dialogo para compartir una localización estaba abierto lo recargamos
                    openShareDialog();
                }else{
                    if(viewModel.is_dialogShortLocalizationListShowing())//Si el dialogo de ordenación se encontraba abierto
                        showOrderLocalizationListDialog();
                }
            }
        }

        return view;
    }

    /**
     * Interfaz
     * Nombre: throwDetailsLocalizationPointActivity
     * Comentario: Este método lanza la actividad DetailsLocalizationPointActivity, con los
     * datos necesarios para visualizar los detalles del punto de localización clicado.
     * Cabecera: private void throwDetailsLocalizationPointActivity(ClsLocalizationPointWithFav localizationPoint)
     * Entrada:
     *  -ClsLocalizationPointWithFav localizationPoint
     * Postcondiciones: El método lanza la actividad DetailsLocalizationPointActivity, con los datos
     * suficientes para mostrar los detalles del punto de localización clicado.
     */
    private void throwDetailsLocalizationPointActivity(ClsLocalizationPointWithFav localizationPoint){
        Intent intent = new Intent(getActivity(), DetailsLocalizationPointActivity.class);
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_LOCALIZATION, localizationPoint.get_localizationPoint());
        intent.putExtra(ApplicationConstants.INTENT_ACTUAL_USER_EMAIL, viewModel.get_actualEmailUser());
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        storeIdLocalizationPointsFavourites();//Cargamos las id's de las localizaaciones favoritas del usuario actual
    }

    @Override
    public void onStop() {
        super.onStop();
        drUser.removeEventListener(listener);//Eliminamos el evento unido a la referencia de los usuarios
    }

    /**
     * Interfaz
     * Nombre: storeIdLocalizationPointsFavourites
     * Comentario: El métoco almacena las id's de los puntos de localización marcados como
     * favoritos por el usuario actual. Una vez se hayan obtenido estos datos, el método lanza
     * la función "loadLocalizationsUserFromPlataform" para proceder a cargar los luntos de localización
     * que sean del usuario actual o que esten marcados como favoritos por este.
     * Cabecera: private void storeIdLocalizationPointsFavourites()
     * Postcondiciones: El método almacena las id's de las localizaciones favoritas del usuario almacenadas
     * en la plataforma Firebase, luego lanza la función "loadLocalizationsUserFromPlataform".
     */
    private void storeIdLocalizationPointsFavourites(){
        listener = drUser.orderByChild(ApplicationConstants.FB_USER_EMAIL_CHILD).equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewModel.set_localizationsIdActualUser(new ArrayList<String>());//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child(ApplicationConstants.FB_LOCALIZATIONS_ID).getChildren()){//Almacenamos las id's de las localizaciones favoritas del usuario
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdActualUser().add(localizationId);
                    }
                }

                loadLocalizationsUserFromPlataform();//Cargamos las localizaciones del usuario
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Interfaz
     * Nombre: loadLocalizationsUserFromPlataform
     * Comentario: Este método nos permite cargar las localizaciones del usuario actual desde
     * la plataforma de Firebase.
     * Cabecera: private void loadLocalizationsUserFromPlataform()
     * Postcondiciones: El método carga las localizaciones del usuario actual.
     */
    private void loadLocalizationsUserFromPlataform(){
        drLocalization.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewModel.set_localizationsActualUser(new ArrayList<ClsLocalizationPoint>());//Limpiamos la lista de puntos de localización
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);
                    if(localizationPoint != null && (localizationPoint.getEmailCreator() != null && localizationPoint.getEmailCreator().equals(viewModel.get_actualEmailUser()) ||
                            viewModel.get_localizationsIdActualUser().contains(localizationPoint.getLocalizationPointId()))){//Si la localización pertenece al usuario o la tiene en favoritos
                        viewModel.get_localizationsActualUser().add(localizationPoint);//Almacenamos el punto de localización
                    }
                }
                loadList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        drUser.removeEventListener(listener);
    }

    /**
     * Interfaz
     * Nombre: showDeleteLocalizationDialog
     * Comentario: Este método muestra un dialogo por pantalla para eliminar una localización seleccionada.
     * Si el usuario confirma la eliminación, se eliminará la localización de la plataforma FireBase, en caso
     * contrario no sucederá nada.
     * Cabecera: private void showDeleteLocalizationDialog()
     * Postcondiciones: El método muestra un dialogo por pantalla, si el usuario lo confirma eliminará
     * la localización seleccionada, en caso contrario no sucederá nada.
     */
    private void showDeleteLocalizationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_localization_point);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.localization_point_deleted, Toast.LENGTH_SHORT).show();
                //Eliminamos las localizaciones seleccionadas
                for(int i = 0; i < viewModel.get_selectedLocalizations().size(); i++){
                    //Eliminamos el id del punto de localización asignado a la lista de favoritos del usuario si este lo tuviera asignado como favorito
                    drUser.child(firebaseCurrentUser.getUid()).child(ApplicationConstants.FB_LOCALIZATIONS_ID).child(viewModel.get_selectedLocalizations()
                            .get(i).get_localizationPoint().getLocalizationPointId()).removeValue();
                    //Eliminamos el punto de localización
                    drLocalization.child(viewModel.get_selectedLocalizations().get(i).get_localizationPoint().getLocalizationPointId()).removeValue();

                    loadLocalizationsUserFromPlataform();//Cargamos las localizaciones del usuario

                    if(viewModel.get_localizationPointClicked() != null && getActivity() != null &&
                            viewModel.get_selectedLocalizations().get(i).get_localizationPoint().getLatitude() == viewModel.get_localizationPointClicked().getPosition().latitude && //Si la localizacióna eliminar es la seleccionada
                            viewModel.get_selectedLocalizations().get(i).get_localizationPoint().getLongitude() == viewModel.get_localizationPointClicked().getPosition().longitude){
                        ((MainTabbetActivity) getActivity()).reloadInitialFragment();//Recargamos el fragmento inicial
                    }
                }

                viewModel.set_dialogDeleteLocalizationShowing(false);//Indicamos que el dialogo ha finalizado
                viewModel.get_selectedLocalizations().clear();//Vaciamos la lista de selecionadas
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
     * Cabecera: private void loadList()
     * Postcondiciones: El método carga la lista rutas del usuario actual.
     */
    private void loadList(){

        if(viewModel.get_localizationsActualUser() != null && viewModel.get_localizationsIdActualUser() != null){//Si se han cargado las dos búsquedas necesarias desde FireBase
            if(viewModel.get_selectedLocalizations().isEmpty())//Si la lista de seleccionadas se encuentra vacía
                loadLocalizationModelList();//Asignamos los items de la lista

            Parcelable state = listView.onSaveInstanceState();//Guardamos el estado actual del listview (Nos interesa la posición actual del scroll)

            //Instanciamos los SharedPreference (Este método es llamado desde onStart, por lo que también debemos instanciarlos aquí también)
            if(getActivity() != null){
                sharedpreferencesField = getActivity().getSharedPreferences(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FIELD, Context.MODE_PRIVATE);
                sharedPreferencesFav = getActivity().getSharedPreferences(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FAV, Context.MODE_PRIVATE);
            }

            int field = sharedpreferencesField.getInt(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FIELD, 1);
            boolean favourite = sharedPreferencesFav.getBoolean(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FAV, false);
            orderList(field, favourite);//Ordenamos la lista

            LocalizationListAdapter adapter = new LocalizationListAdapter(getActivity(), R.layout.localization_list_item, viewModel.get_itemsLocalizationList()){
                @Override
                public View getView(int position, View convertView, ViewGroup viewGroup) {
                    View view = super.getView(position, convertView, viewGroup);

                    if(viewModel.get_selectedLocalizations().contains(viewModel.get_itemsLocalizationList().get(position))){//Si la localización se encuentra en la lista de seleccionadas
                        view.setBackgroundResource(R.color.BlueItem);
                    }else{
                        changeBackgroundColorItemView(view, position);
                    }

                    return view;
                }
            };
            listView.setAdapter(adapter);

            listView.onRestoreInstanceState(state);//Le asignamos el estado que almacenamos anteriormente
        }
    }

    /**
     * Interfaz
     * Nombre: changeBackgroundColorItemView
     * Comentario: Este método nos permite cambiar el color de fondo de un item
     * de la lista de localizaciones. El item tomará un color diferente según el siguiente
     * criterio:
     * -Azul si la localización no pertenece al usuario actual.
     * -Amarillo si el usuario ya compartió la localizacióon.
     * -Blanco si no cumple ninguno de los tres criterios anteriores.
     * Realizamos este método para evitar repetir código.
     * Cabecera: private void changeBackgroundColorItemView(View view, int position)
     *  -View view
     *  -int position
     * Postcondiciones: El método cambiar el color de un item según el estado de este.
     */
    private void changeBackgroundColorItemView(View view, int position){
        if(!viewModel.get_itemsLocalizationList().get(position).get_localizationPoint().getEmailCreator().equals(viewModel.get_actualEmailUser())){//Si la localización no es del usuario actual
            setBackgroundAnimation(view, R.drawable.gradient_no_owner_list);
        }else{
            if(viewModel.get_itemsLocalizationList().get(position).get_localizationPoint().isShared()){//Si la localización esta compartida
                setBackgroundAnimation(view, R.drawable.gradient_shared_list);
            }else{
                view.setBackgroundResource(R.color.WhiteItem);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: setBackgroundAnimation
     * Comentario: Este método nos permite insertar un background animado a una vista,
     * a través de un elemento xml animation list.
     * Cabecera: private void setBackgroundAnimation(View view, int animationList)
     * Entrada:
     *  -View view
     *  -int animtionList //Dirección del fichero xml
     * Precondiciones:
     *  -animtionList debe apuntar a un fichero xml que contenga un tag de animationList.
     * Postcondiciones: El método inserta una animación de fondo a la vista, a partir del
     * animatioList introducido por parámetros.
     */
    private void setBackgroundAnimation(View view, int animationList){
        view.setBackgroundResource(animationList);
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    /**
     * Interfaz
     * Nombre: loadLocalizationModelList
     * Comentario: Este método nos permite cargar la lista de localizaciones, en la que los
     * items son una clase modelo de una localización. Esta clase modelo contiene la localización
     * del usuario y otro atributo booleano que indicará si el usuario tiene marcada como favorita
     * dicha localización. Para crear esta lista, el método utiliza la lista de _localizationsIdActualUser
     * y _localizationsActualUser del VM.
     * Cabecera: private void loadLocalizationModelList()
     * Postcondiciones: El método carga un listado de la clase modelo ClsLocalizationPointWithFav en el atributo _itemsLocalizationList del VM.
     */
    private void loadLocalizationModelList(){
        boolean fav;
        viewModel.get_itemsLocalizationList().clear();//Limpiamos los items de la lista
        for(int i = 0; i < viewModel.get_localizationsActualUser().size(); i++){
            fav = viewModel.get_localizationsIdActualUser().contains(viewModel.get_localizationsActualUser().get(i).getLocalizationPointId());
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteLocalization != null && alertDialogDeleteLocalization.isShowing()) {//Si se encuentra abierto el dialogo de deleteGameMode
            alertDialogDeleteLocalization.dismiss();// close dialog to prevent leaked window
            viewModel.set_dialogDeleteLocalizationShowing(true);
        }else{
            if(alertDialogShareLocalization != null && alertDialogShareLocalization.isShowing()){
                alertDialogShareLocalization.dismiss();// close dialog to prevent leaked window
                viewModel.set_dialogShareLocalizationShowing(true);
            }else{
                if(alertDialogShortList != null && alertDialogShortList.isShowing()){
                    alertDialogShortList.dismiss();
                    viewModel.set_dialogShortLocalizationListShowing(true);
                }
            }
        }
    }

    /**
     * Interfaz
     * Nombre: orderList
     * Comentario: Este método nos permite ordenar la lista de localizaciones según un criterio
     * en específico. La lista se ordenará según los parámetros de entrada.
     * Field:
     *  -1 (LocalizationName)
     *  -2 (DateOfCreation)
     *  -3 (Shared and LocalizationName)
     *  -4 (Shared and DateOfCreation)
     *  -5 (NoOwner and LocalizationName)
     *  -6 (NoOwner and DateOfCreation)
     * Favourite:
     *  -true (Order by favourite localizations)
     *  -false (Does not take into account favorite localizations)
     * Cabecera: private void orderList(int field, boolean favourite)
     * Entrada:
     *  -int field
     *  -boolean favourite
     * Precondiciones:
     *  -fields debe ser un número entre 1 y 6.
     * Postcondiciones: El método ordena la lista de localizaciones según los criterios introducidos
     * por parámetros.
     */
    private void orderList(int field, boolean favourite){
        if(favourite){//Si la lista se encuentra ordenada por favoritos
            switch (field){
                case 1:
                    new OrderLists().orderLocalizationListAscByNameAndFavourite(viewModel.get_itemsLocalizationList());//Order list by name and fav
                    break;
                case 2:
                    new OrderLists().orderLocalizationListAscByDateAndFavourite(viewModel.get_itemsLocalizationList());//Order list by date of creation and fav
                    break;
                case 3:
                    new OrderLists().orderLocalizationListAscBySharedAndNameAndFav(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by shared and name and fav
                    break;
                case 4:
                    new OrderLists().orderLocalizationListAscBySharedAndDateAndFav(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by shared and date and fav
                    break;
                case 5:
                    new OrderLists().orderLocalizationListAscByNoOwnerAndNameAndFav(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by no owner and name and fav
                    break;
                case 6:
                    new OrderLists().orderLocalizationListAscByNoOwnerAndDateAndFav(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by no owner and date and fav
                    break;
            }
        }else{
            switch (field){
                case 1:
                    viewModel.set_itemsLocalizationList(new OrderLists().orderLocalizationListAscByName(viewModel.get_itemsLocalizationList()));//Order list by name
                    break;
                case 2:
                    viewModel.set_itemsLocalizationList(new OrderLists().orderLocalizationListAscByDate(viewModel.get_itemsLocalizationList()));//Order list by date of creation
                    break;
                case 3:
                    new OrderLists().orderLocalizationListAscBySharedAndName(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by shared and name
                    break;
                case 4:
                    new OrderLists().orderLocalizationListAscBySharedAndDate(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by shared and date
                    break;
                case 5:
                    new OrderLists().orderLocalizationListAscByNoOwnerAndName(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by no owner and name
                    break;
                case 6:
                    new OrderLists().orderLocalizationListAscByNoOwnerAndDate(viewModel.get_itemsLocalizationList(), viewModel.get_actualEmailUser());//Order list by no owner and date
                    break;
            }
        }
    }

    /**
     * Interfaz
     * Nombre: openShareDialog
     * Comentario: Este método muestra por pantalla un dialogo para compartir una localización seleccionada
     * con la plataforma. Si el usuario confirma la acción, se comparte el punto de localización y en caso
     * contrario no sucede nada.
     * Cabecera: private void openShareDialog()
     * Precondiciones:
     *  -Solo debe haber una ruta seleccionada almacenada en el VM.
     * Postcondiciones: Si el usuario confirma el dialogo, se compartirá la ruta seleccionada y en caso contrario
     * no sucede nada.
     */
    private void openShareDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_share);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_share_localization_point);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), R.string.localization_point_shared, Toast.LENGTH_SHORT).show();

                viewModel.shareLocalizationPoint();//Compartimos el punto de localización
                viewModel.set_dialogShareLocalizationShowing(false);//Indicamos que el dialogo ha finalizado
                viewModel.get_selectedLocalizations().clear();//Vaciamos la lista de selecionadas
                loadLocalizationsUserFromPlataform();//Recargamos la lista de rutas del usuario actual
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogShareLocalizationShowing(false);
            }
        });

        alertDialogShareLocalization = alertDialogBuilder.create();
        alertDialogShareLocalization.show();
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
     * Nombre: showOrderLocalizationListDialog
     * Comentario: Este método muestra por pantalla un dialogo con los diferentes tipos de ordenación, que se puede aplicar
     * sobre la lista de localizaciones, si el usuario confirma el dialogo, se ordenará la lista actual dependiendo
     * del tipo seleccionado.
     * Cabecera: private void showOrderLocalizationListDialog()
     * Postcondiciones: El método abre un dialogo de ordenación, si el usuario confirma el dialogo se
     * ordena la lista de localizaciones por el criterio seleccionado.
     */
    private void showOrderLocalizationListDialog() {
        final CharSequence [] orderTypes = {getResources().getString(R.string.name),
                getResources().getString(R.string.date_of_creation), getResources().getString(R.string.shared_by_name), getResources().getString(R.string.shared_by_date_of_creaction),
                getResources().getString(R.string.no_owner_by_name), getResources().getString(R.string.no_owner_by_date_of_creation)};

        viewModel.set_positionSelectedOrderTypesLocations(sharedpreferencesField.getInt(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FIELD, 1) -1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.order_types);
        builder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo
        builder.setSingleChoiceItems(orderTypes, viewModel.get_positionSelectedOrderTypesLocations(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_positionSelectedOrderTypesLocations(which);
            }
        });
        builder.setPositiveButton(R.string.apply_changes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor01 = sharedpreferencesField.edit();//Guardamos los filtros
                editor01.putInt(ApplicationConstants.SP_ORDER_LOCALIZATION_LIST_BY_FIELD, viewModel.get_positionSelectedOrderTypesLocations()+1);
                editor01.apply();
                loadList();//Recargamos la lista

                viewModel.set_dialogShortLocalizationListShowing(false);//Indicamos que se finalizó el dialogo
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogShortLocalizationListShowing(false);//Indicamos que se finalizó el dialogo
            }
        });
        alertDialogShortList = builder.create();
        alertDialogShortList.show();
    }
}
