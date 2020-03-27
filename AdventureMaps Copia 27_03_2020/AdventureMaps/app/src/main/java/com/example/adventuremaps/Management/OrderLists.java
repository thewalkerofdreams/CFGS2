package com.example.adventuremaps.Management;

import com.example.adventuremaps.Activities.Models.ClsLocalizationPointWithFav;
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
     * Comentario: Este método nos permite obtener una lista de localizaciones ordenada por nombre ascendentemente,
     * dada otra lista de localizaciones.
     * Debido a que el número de elemento de la lista de localizaciones sea bastante superior al de la lista
     * de rutas, se ha implementado el método QuickSort para su ordenación.
     * Cabecera: public ArrayList<ClsLocalizationPointWithFav> orderLocalizationListByName(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> sortedList
     * Postcondiciones: El método devuelve una lista de localizaciones ordenadas por nombre ascendentemente, dada otra lista
     * por parámetros.
     */
    public ArrayList<ClsLocalizationPointWithFav> orderLocalizationListByName(ArrayList<ClsLocalizationPointWithFav> localizationList){
        ArrayList<ClsLocalizationPointWithFav> sortedList;  //La lista ordenada que se devolverá
        ArrayList<ClsLocalizationPointWithFav> smaller = new ArrayList<>(); //Localizaciones menores que el pivote
        ArrayList<ClsLocalizationPointWithFav> greater = new ArrayList<>(); //Localizaciones mayores que el pivote
        ClsLocalizationPointWithFav pivot;

        if (!localizationList.isEmpty()){//Si la lista se encuentra vacía
            pivot = localizationList.get(0);  //La primera localización será utilizada como pivote

            int i;
            ClsLocalizationPointWithFav aux;
            for (i=1;i<localizationList.size();i++)
            {
                aux=localizationList.get(i);//Almacenamos la localización actual
                if (aux.get_localizationPoint().getName().compareTo(pivot.get_localizationPoint().getName()) < 0)   //Si la localización auxiliar es menor lexicograficamente
                    smaller.add(aux);
                else
                    greater.add(aux);
            }
            smaller=orderLocalizationListByName(smaller);  //Ordenamos la lista de localizaciones a la izquierda del pivote
            greater=orderLocalizationListByName(greater);  //Ordenamos la lista de localizaciones a la derecha del pivote
            smaller.add(pivot);          //Añadimos el pivote al final de la lista de la izquierda(menores) ya ordenada
            smaller.addAll(greater);     //Añadimos el resto de elementos de la lista de la derecha (mayores) ya ordenada
            sortedList = smaller;            //Asiganamos la lista completa a la lista de ordenadas
        }else{
            sortedList = localizationList;
        }

        return sortedList;
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
     * Comentario: Este método nos permite obtener una lista de localizaciones ordenadas por fecha de creación
     * ascendentemente, dada otra lista de localizaciones.
     * Debido a que el número de elemento de la lista de localizaciones sea bastante superior al de la lista
     * de rutas, se ha implementado el método QuickSort para su ordenación.
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public ArrayList<ClsLocalizationPointWithFav> orderLocalizationListAscByDate(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> sortedList
     * Postcondiciones: El método devuelve una lista de localizaciones ordenadas por fecha de creación ascendentemente, dada otra lista
     * por parámetros.
     */
    public ArrayList<ClsLocalizationPointWithFav> orderLocalizationListAscByDate(ArrayList<ClsLocalizationPointWithFav> localizationList){
        Date dateActualComparator;
        Date dateActualPivot;
        ArrayList<ClsLocalizationPointWithFav> sortedList;  //La lista ordenada que se devolverá
        ArrayList<ClsLocalizationPointWithFav> smaller = new ArrayList<>(); //Localizaciones menores que el pivote
        ArrayList<ClsLocalizationPointWithFav> greater = new ArrayList<>(); //Localizaciones mayores que el pivote
        ClsLocalizationPointWithFav pivot;

        if (!localizationList.isEmpty()){//Si la lista se encuentra vacía
            pivot = localizationList.get(0);  //La primera localización será utilizada como pivote

            int i;
            ClsLocalizationPointWithFav aux;
            dateActualPivot = new Date(pivot.get_localizationPoint().getDateOfCreation());//Fecha de creación del pivote actual
            for (i=1;i<localizationList.size();i++)
            {
                aux=localizationList.get(i);//Almacenamos la localización actual
                dateActualComparator = new Date(aux.get_localizationPoint().getDateOfCreation());//La fecha de la localización actual
                if (dateActualComparator.compareTo(dateActualPivot) < 0)   //Si la localización auxiliar tiene una fecha de creación menor a la del pivote
                    smaller.add(aux);
                else
                    greater.add(aux);
            }
            smaller=orderLocalizationListAscByDate(smaller);  //Ordenamos la lista de localizaciones a la izquierda del pivote
            greater=orderLocalizationListAscByDate(greater);  //Ordenamos la lista de localizaciones a la derecha del pivote
            smaller.add(pivot);          //Añadimos el pivote al final de la lista de la izquierda(menores) ya ordenada
            smaller.addAll(greater);     //Añadimos el resto de elementos de la lista de la derecha (mayores) ya ordenada
            sortedList = smaller;            //Asiganamos la lista completa a la lista de ordenadas
        }else{
            sortedList = localizationList;
        }

        return sortedList;
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByNameAndFavourite
     * Comentario: Este método nos permite ordenar una lista de rutas por su nombre y por la sección
     * de favoritos, primero se mostrarán los elementos favoritos.
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
        routeList.addAll(listadoFavoritos);//Añadimos las rutas favoritas
        routeList.addAll(listadoNoFavoritos);//Añadimos el resto de rutas no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByNameAndFavourite
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su nombre y por la sección
     * de favoritos, primero se mostrarán los elementos favoritos.
     * Cabecera: public void orderLocalizationListAscByNameAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su nombre y priorizando
     * las rutas favoritas.
     */
    public void orderLocalizationListAscByNameAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList){
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

        listadoFavoritos = orderLocalizationListByName(listadoFavoritos);//Ordenamos la lista de favoritos
        listadoNoFavoritos = orderLocalizationListByName(listadoNoFavoritos);//Ordenamos la lista de no favoritos

        localizationList.clear();//Limpiamos la lista
        localizationList.addAll(listadoFavoritos);//Añadimos las rutas favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos el resto de rutas no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByDateAndFavourite
     * Comentario: Este método nos permite ordenar una lista de rutas por su fecha de creación y por
     * la sección de favoritos, primero se mostrarán los elementos favoritos.
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
        routeList.addAll(listadoFavoritos);//Añadimos las rutas favoritas
        routeList.addAll(listadoNoFavoritos);//Añadimos el resto de rutas no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByDateAndFavourite
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación y por
     * la sección de favoritos, primero se mostrarán los elementos favoritos.
     * Cabecera: public void orderLocalizationListAscByDateAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su fecha de creación
     * y priorizando las rutas favoritas.
     */
    public void orderLocalizationListAscByDateAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList){
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
        localizationList.addAll(listadoFavoritos);//Añadimos las rutas favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos el resto de rutas no favoritas
    }
}
