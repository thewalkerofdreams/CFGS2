package com.example.adventuremaps.Management;

import com.example.adventuremaps.Activities.Models.ClsLocalizationPointWithFav;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;

import java.util.ArrayList;
import java.util.Date;

public class OrderLists {

    /**
     * Interfaz
     * Nombre: orderRouteListAscByName
     * Comentario: Este método nos permite ordenar una lista de rutas por su nombre.
     * Internamente este método realiza el método de ordenación por selección directa,
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderRouteListByName(ArrayList<ClsRoute> routeList)
     * Entrada:
     *  -ArrayList<ClsRoute> routeList
     * Postcondiciones: El método ordená la lista de rutas ascendentemente según su nombre.
     */
    public void orderRouteListByName(ArrayList<ClsRoute> routeList){
        for (int i = 0; i < routeList.size() - 1; i++)
        {
            int min = i;
            for (int j = i + 1; j < routeList.size(); j++)
            {
                if (routeList.get(j).getName().compareTo(routeList.get(min).getName()) < 0)
                {
                    min = j;
                }
            }
            if (i != min)
            {
                ClsRoute aux= routeList.get(i);
                routeList.set(i, routeList.get(min));
                routeList.set(min, aux);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListByName
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su nombre.
     * Internamente este método realiza el método de ordenación por selección directa,//TODO Change to quicksort
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderLocalizationListByName(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su nombre.
     */
    public void orderLocalizationListByName(ArrayList<ClsLocalizationPointWithFav> localizationList){//TODO Change to quicksort
        for (int i = 0; i < localizationList.size() - 1; i++)
        {
            int min = i;
            for (int j = i + 1; j < localizationList.size(); j++)
            {
                if (localizationList.get(j).get_localizationPoint().getName().compareTo(localizationList.get(min).get_localizationPoint().getName()) < 0)
                {
                    min = j;
                }
            }
            if (i != min)
            {
                ClsLocalizationPointWithFav aux= localizationList.get(i);
                localizationList.set(i, localizationList.get(min));
                localizationList.set(min, aux);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByDate
     * Comentario: Este método nos permite ordenar una lista de rutas por su fecha de creación.
     * Internamente este método realiza el método de ordenación por selección directa,
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderRouteListAscByDate(ArrayList<ClsRoute> routeList)
     * Entrada:
     *  -ArrayList<ClsRoute> routeList
     * Postcondiciones: El método ordená la lista de rutas ascendentemente según su fecha de creación.
     */
    public void orderRouteListAscByDate(ArrayList<ClsRoute> routeList){

        Date dateActualComparator;
        Date dateActualMin;

        for (int i = 0; i < routeList.size() - 1; i++)
        {
            int min = i;
            for (int j = i + 1; j < routeList.size(); j++)
            {
                dateActualComparator = new Date(routeList.get(j).getDateOfCreation());
                dateActualMin = new Date(routeList.get(min).getDateOfCreation());
                if (dateActualComparator.compareTo(dateActualMin) < 0)
                {
                    min = j;
                }
            }
            if (i != min)
            {
                ClsRoute aux= routeList.get(i);
                routeList.set(i, routeList.get(min));
                routeList.set(min, aux);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByDate
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación.
     * Internamente este método realiza el método de ordenación por selección directa,//TODO Change to quicksort
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderLocalizationListAscByDate(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su fecha de creación.
     */
    public void orderLocalizationListAscByDate(ArrayList<ClsLocalizationPointWithFav> localizationList){//TODO Change to quicksort

        Date dateActualComparator;
        Date dateActualMin;

        for (int i = 0; i < localizationList.size() - 1; i++)
        {
            int min = i;
            for (int j = i + 1; j < localizationList.size(); j++)
            {
                dateActualComparator = new Date(localizationList.get(j).get_localizationPoint().getDateOfCreation());
                dateActualMin = new Date(localizationList.get(min).get_localizationPoint().getDateOfCreation());
                if (dateActualComparator.compareTo(dateActualMin) < 0)
                {
                    min = j;
                }
            }
            if (i != min)
            {
                ClsLocalizationPointWithFav aux= localizationList.get(i);
                localizationList.set(i, localizationList.get(min));
                localizationList.set(min, aux);
            }
        }
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByNameAndFavourite
     * Comentario: Este método nos permite ordenar una lista de rutas por su nombre y por la sección
     * de favoritos, primero se mostrarán los elementos favoritos.
     * Internamente este método realiza el método de ordenación por selección directa,
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderRouteListAscByNameAndFavourite(ArrayList<ClsRoute> routeList)
     * Entrada:
     *  -ArrayList<ClsRoute> routeList
     * Postcondiciones: El método ordená la lista de rutas ascendentemente según su nombre y priorizando
     * las rutas favoritas.
     */
    public void orderRouteListAscByNameAndFavourite(ArrayList<ClsRoute> routeList){
        ArrayList<ClsRoute> listadoFavoritos = new ArrayList<>();
        ArrayList<ClsRoute> listadoNoFavoritos = new ArrayList<>();

        for (int i = 0; i < routeList.size(); i++)
        {
            if(routeList.get(i).getFavourite()){
                listadoFavoritos.add(routeList.get(i));
            }else{
                listadoNoFavoritos.add(routeList.get(i));
            }
        }

        this.orderRouteListByName(listadoFavoritos);//Ordenamos la lista de favoritos
        this.orderRouteListByName(listadoNoFavoritos);//Ordenamos la lista de no favoritos

        routeList.clear();
        for(int i = 0; i < listadoFavoritos.size(); i++){//Añadimos las rutas favoritas
            routeList.add(listadoFavoritos.get(i));
        }

        for(int i = 0; i < listadoNoFavoritos.size(); i++){//Añadimos el resto de rutas no favoritas
            routeList.add(listadoNoFavoritos.get(i));
        }
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByNameAndFavourite
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su nombre y por la sección
     * de favoritos, primero se mostrarán los elementos favoritos.
     * Internamente este método realiza el método de ordenación por selección directa,//TODO Change to quicksort
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderLocalizationListAscByNameAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su nombre y priorizando
     * las rutas favoritas.
     */
    public void orderLocalizationListAscByNameAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList){//TODO Change to quicksort
        ArrayList<ClsLocalizationPointWithFav> listadoFavoritos = new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoFavoritos = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)
        {
            if(localizationList.get(i).is_favourite()){
                listadoFavoritos.add(localizationList.get(i));
            }else{
                listadoNoFavoritos.add(localizationList.get(i));
            }
        }

        this.orderLocalizationListByName(listadoFavoritos);//Ordenamos la lista de favoritos
        this.orderLocalizationListByName(listadoNoFavoritos);//Ordenamos la lista de no favoritos

        localizationList.clear();
        for(int i = 0; i < listadoFavoritos.size(); i++){//Añadimos las rutas favoritas
            localizationList.add(listadoFavoritos.get(i));
        }

        for(int i = 0; i < listadoNoFavoritos.size(); i++){//Añadimos el resto de rutas no favoritas
            localizationList.add(listadoNoFavoritos.get(i));
        }
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByDateAndFavourite
     * Comentario: Este método nos permite ordenar una lista de rutas por su fecha de creación y por
     * la sección de favoritos, primero se mostrarán los elementos favoritos.
     * Internamente este método realiza el método de ordenación por selección directa,
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderRouteListAscByDateAndFavourite(ArrayList<ClsRoute> routeList)
     * Entrada:
     *  -ArrayList<ClsRoute> routeList
     * Postcondiciones: El método ordená la lista de rutas ascendentemente según su fecha de creación
     * y priorizando las rutas favoritas.
     */
    public void orderRouteListAscByDateAndFavourite(ArrayList<ClsRoute> routeList){
        ArrayList<ClsRoute> listadoFavoritos = new ArrayList<>();
        ArrayList<ClsRoute> listadoNoFavoritos = new ArrayList<>();

        for (int i = 0; i < routeList.size(); i++)
        {
            if(routeList.get(i).getFavourite()){
                listadoFavoritos.add(routeList.get(i));
            }else{
                listadoNoFavoritos.add(routeList.get(i));
            }
        }

        this.orderRouteListAscByDate(listadoFavoritos);//Ordenamos la lista de favoritos
        this.orderRouteListAscByDate(listadoNoFavoritos);//Ordenamos la lista de no favoritos

        routeList.clear();
        for(int i = 0; i < listadoFavoritos.size(); i++){//Añadimos las rutas favoritas
            routeList.add(listadoFavoritos.get(i));
        }

        for(int i = 0; i < listadoNoFavoritos.size(); i++){//Añadimos el resto de rutas no favoritas
            routeList.add(listadoNoFavoritos.get(i));
        }
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByDateAndFavourite
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación y por
     * la sección de favoritos, primero se mostrarán los elementos favoritos.
     * Internamente este método realiza el método de ordenación por selección directa, //TODO Change to quicksort
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderLocalizationListAscByDateAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su fecha de creación
     * y priorizando las rutas favoritas.
     */
    public void orderLocalizationListAscByDateAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList){//TODO Change to quicksort
        ArrayList<ClsLocalizationPointWithFav> listadoFavoritos = new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoFavoritos = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)
        {
            if(localizationList.get(i).is_favourite()){
                listadoFavoritos.add(localizationList.get(i));
            }else{
                listadoNoFavoritos.add(localizationList.get(i));
            }
        }

        this.orderLocalizationListAscByDate(listadoFavoritos);//Ordenamos la lista de favoritos
        this.orderLocalizationListAscByDate(listadoNoFavoritos);//Ordenamos la lista de no favoritos

        localizationList.clear();
        for(int i = 0; i < listadoFavoritos.size(); i++){//Añadimos las rutas favoritas
            localizationList.add(listadoFavoritos.get(i));
        }

        for(int i = 0; i < listadoNoFavoritos.size(); i++){//Añadimos el resto de rutas no favoritas
            localizationList.add(listadoNoFavoritos.get(i));
        }
    }
}
