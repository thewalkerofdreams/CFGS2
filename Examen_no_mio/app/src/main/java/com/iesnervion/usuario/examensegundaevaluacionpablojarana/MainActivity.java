package com.iesnervion.usuario.examensegundaevaluacionpablojarana;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Database;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Futbolista;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.FutbolistaPosicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Posicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.Fragments.AddJugadorFragment;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.Fragments.ButtonsFragment;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.Fragments.ListFragment;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.Models.FutbolistaConPosiciones;

/*
README!!!!!!!!!!!!!!!!!!!!!!!!!

-He intentado colocar los checkBoxes dinamicamente pero no me acordaba qué poner en el layoutParams.

-La implementación en tablets es la primera vez que lo oigo, por lo que no está implementado

-La lista la muestra correctamente, para comprobarlo descomenta las lineas comentadas en el oncreate

-En cuanto al insert, me han quedado por controlar un par de cosas, ya que como pido los ID de las posiciones y el ID del futbolista
insertado en segundo plano, tengo que esperar a que me lleguen ambos.
 */
public class MainActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Futbolista futbolista=new Futbolista("Pablo","Jarana");
        Posicion posicion= new Posicion("Delantero");
        Posicion posicion1=new Posicion("Portero");
        Posicion posicion2=new Posicion("Defensa");
        Posicion posicion3=new Posicion("Centrocampista");
        //FutbolistaPosicion FP=new FutbolistaPosicion(1,1);
        //Database.getDatabase(this).getEquipoDao().insertFutbolista(futbolista);
        Database.getDatabase(this).getEquipoDao().insertPosicion(posicion);
        Database.getDatabase(this).getEquipoDao().insertPosicion(posicion1);
        Database.getDatabase(this).getEquipoDao().insertPosicion(posicion2);
        Database.getDatabase(this).getEquipoDao().insertPosicion(posicion3);
        //Database.getDatabase(this).getEquipoDao().insertFutbolistaPosicion(FP);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void instanceListFragment() {
        ListFragment listFragment=new ListFragment();
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, listFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void instanceAddJugadorFragment() {
        AddJugadorFragment fragment=new AddJugadorFragment();
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
