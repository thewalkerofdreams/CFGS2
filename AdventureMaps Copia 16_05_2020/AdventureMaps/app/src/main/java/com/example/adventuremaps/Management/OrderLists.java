package com.example.adventuremaps.Management;

import com.example.adventuremaps.Models.ClsLocalizationPointWithFav;
import com.example.adventuremaps.FireBaseEntities.ClsRoute;

import java.util.ArrayList;
import java.util.Date;

public class OrderLists {

    /**
     * Interfaz
     * Nombre: orderRouteListAscByName
     * Comentario: Este método nos permite ordenar una lista de rutas ascendentemente por su nombre.
     * Internamente este método realiza el método de ordenación por selección directa,
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderRouteListByName(ArrayList<ClsRoute> routeList)
     * Entrada/Salida:
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
     * Nombre: orderLocalizationListAscByName
     * Comentario: Este método nos permite obtener una lista de localizaciones ordenada por sus nombres ascendentemente,
     * dada otra lista de localizaciones.
     * Debido a que el número de elemento de la lista de localizaciones será posiblemente bastante superior al de la lista
     * de rutas, se ha implementado el método QuickSort para su ordenación.
     * Cabecera: public ArrayList<ClsLocalizationPointWithFav> orderLocalizationListAscByName(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> sortedList
     * Postcondiciones: El método devuelve una lista de localizaciones ordenadas por sus nombres ascendentemente, dada otra lista
     * por parámetros.
     */
    public ArrayList<ClsLocalizationPointWithFav> orderLocalizationListAscByName(ArrayList<ClsLocalizationPointWithFav> localizationList){
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
            smaller= orderLocalizationListAscByName(smaller);  //Ordenamos la lista de localizaciones a la izquierda del pivote
            greater= orderLocalizationListAscByName(greater);  //Ordenamos la lista de localizaciones a la derecha del pivote
            smaller.add(pivot);          //Añadimos el pivote al final de la lista de la izquierda(menores) ya ordenada
            smaller.addAll(greater);     //Añadimos el resto de elementos de la lista de la derecha (mayores) ya ordenada
            sortedList = smaller;            //Asignamos la lista completa a la lista de ordenadas
        }else{
            sortedList = localizationList;
        }

        return sortedList;
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByDate
     * Comentario: Este método nos permite ordenar una lista de rutas ascendentemente por su fecha de creación.
     * Internamente este método realiza el método de ordenación por selección directa,
     * debido a que el número de rutas dudosamente será muy elevado.
     * Cabecera: public void orderRouteListAscByDate(ArrayList<ClsRoute> routeList)
     * Entrada/Salida:
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
     * Comentario: Este método nos permite obtener una lista de localizaciones ordenada ascendentemente por sus fechas de creación,
     * dada otra lista de localizaciones.
     * Debido a que el número de elemento de la lista de localizaciones sea bastante superior al de la lista
     * de rutas, se ha implementado el método QuickSort para su ordenación.
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
            sortedList = smaller;            //Asignamos la lista completa a la lista de ordenadas
        }else{
            sortedList = localizationList;
        }

        return sortedList;
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByNameAndFavourite
     * Comentario: Este método nos permite ordenar una lista de rutas por su nombre y por la sección
     * de favoritos, primero se colocarán los elementos favoritos.
     * Cabecera: public void orderRouteListAscByNameAndFavourite(ArrayList<ClsRoute> routeList)
     * Entrada/Salida:
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
     * Entrada/Salida:
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

        listadoFavoritos = orderLocalizationListAscByName(listadoFavoritos);//Ordenamos la lista de favoritos
        listadoNoFavoritos = orderLocalizationListAscByName(listadoNoFavoritos);//Ordenamos la lista de no favoritos

        localizationList.clear();//Limpiamos la lista
        localizationList.addAll(listadoFavoritos);//Añadimos las localizaciones favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos el resto de localizaciones no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderRouteListAscByDateAndFavourite
     * Comentario: Este método nos permite ordenar una lista de rutas por su fecha de creación y priorizando
     * los elementos favoritos.
     * Cabecera: public void orderRouteListAscByDateAndFavourite(ArrayList<ClsRoute> routeList)
     * Entrada/Salida:
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
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación y priorizando
     * los elementos favoritos.
     * Cabecera: public void orderLocalizationListAscByDateAndFavourite(ArrayList<ClsLocalizationPointWithFav> localizationList)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su fecha de creación
     * y priorizando las localizaciones favoritas.
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
        localizationList.addAll(listadoFavoritos);//Añadimos las localizaciones favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos el resto de localizaciones no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscBySharedAndDate
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación, colocando primero
     * las que han sido compartidas con la aplicación por un usuario en específico.
     * Cabecera: public void orderLocalizationListAscBySharedAndDate(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     * -String actualUserEmail
     * Postcondiciones: El método ordena la lista de localizaciones por fecha, colocando primero las localizaciones
     * compartidas con la aplicación y que pertenezcan al email introducido por parámetros.
     */
    public void orderLocalizationListAscBySharedAndDate(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
        ArrayList<ClsLocalizationPointWithFav> listadoCompartidas= new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoCompartidas = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)//Separamos las localizaciones en compartidas y no compartidas
        {
            if(localizationList.get(i).get_localizationPoint().isShared() && localizationList.get(i).get_localizationPoint().getEmailCreator().equals(actualUserEmail)){
                listadoCompartidas.add(localizationList.get(i));//Si la localización se encuentra compartida y pertenece al usuario
            }else{
                listadoNoCompartidas.add(localizationList.get(i));
            }
        }

        this.orderLocalizationListAscByDate(listadoCompartidas);//Ordenamos la lista de compartidas
        this.orderLocalizationListAscByDate(listadoNoCompartidas);//Ordenamos la lista de no compartidas

        localizationList.clear();
        localizationList.addAll(listadoCompartidas);//Añadimos las localizaciones compartidas
        localizationList.addAll(listadoNoCompartidas);//Añadimos el resto de localizaciones no compartidas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscBySharedAndDateAndFav
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación, colocando primero
     * las favoritas que han sido compartidas con la aplicación. Criterio de prioridad en la ordenación:
     * -Localizaciones favoritas compartidas por el usuario
     * -Localizaciones favoritas no compartidas por el usuario
     * -Localizaciones no favoritas compartidas por el usuario
     * -Localizaciones no favoritas no compartidas por el usuario
     * Cabecera: public void orderLocalizationListAscBySharedAndDateAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     *  -String actualUserEmail
     * Postcondiciones: El método ordena la lista de localizaciones por fecha, priorizando primero las localizaciones
     * favoritas compartidas.
     */
    public void orderLocalizationListAscBySharedAndDateAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
        ArrayList<ClsLocalizationPointWithFav> listadoFavoritos= new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoFavoritos = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)//Separamos los localizaciones favoritas y no favoritas
        {
            if(localizationList.get(i).is_favourite()){
                listadoFavoritos.add(localizationList.get(i));
            }else{
                listadoNoFavoritos.add(localizationList.get(i));
            }
        }

        this.orderLocalizationListAscBySharedAndDate(listadoFavoritos, actualUserEmail);//Ordenamos la lista de favoritas
        this.orderLocalizationListAscBySharedAndDate(listadoNoFavoritos, actualUserEmail);//Ordenamos la lista de no favoritas

        localizationList.clear();
        localizationList.addAll(listadoFavoritos);//Añadimos las localizaciones favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos el resto de localizaciones no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscBySharedAndName
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su nombre, colocando primero las que han
     * sido compartidas con la aplicación por un usuario en específico.
     * Cabecera: public void orderLocalizationListAscBySharedAndName(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     * -String actualUserEmail
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su nombre y priorizando
     * las localizaciones compartidas por el email especificado en uno de los parámetros de entrada.
     */
    public void orderLocalizationListAscBySharedAndName(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
        ArrayList<ClsLocalizationPointWithFav> listadoCompartidos = new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoCompartidos = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)
        {
            if(localizationList.get(i).get_localizationPoint().isShared() && localizationList.get(i).get_localizationPoint().getEmailCreator().equals(actualUserEmail)){
                listadoCompartidos.add(localizationList.get(i));
            }else{
                listadoNoCompartidos.add(localizationList.get(i));
            }
        }

        listadoCompartidos = orderLocalizationListAscByName(listadoCompartidos);//Ordenamos la lista de compartidas
        listadoNoCompartidos = orderLocalizationListAscByName(listadoNoCompartidos);//Ordenamos la lista de no compartidas

        localizationList.clear();//Limpiamos la lista
        localizationList.addAll(listadoCompartidos);//Añadimos las localizaciones compartidas
        localizationList.addAll(listadoNoCompartidos);//Añadimos el resto de localizaciones no compartidas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscBySharedAndNameAndFav
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su nombre, colocando primero las
     * favoritas y que han sido compartidas con la aplicación por un usuario en específico.
     * Criterio de prioridad en la ordenación:
     *  -Favorita y compartida
     *  -Favorita y no compartida
     *  -No favorita y compartida
     *  -No favorita y no compartida
     * Cabecera: public void orderLocalizationListAscBySharedAndNameAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     * -String actualUserEmail
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su nombre y priorizando
     * las localizaciones favoritas compartidas.
     */
    public void orderLocalizationListAscBySharedAndNameAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
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

        this.orderLocalizationListAscBySharedAndName(listadoFavoritos, actualUserEmail);//Ordenamos la lista de favoritos
        this.orderLocalizationListAscBySharedAndName(listadoNoFavoritos, actualUserEmail);//Ordenamos la lista de no favoritos

        localizationList.clear();//Limpiamos la lista
        localizationList.addAll(listadoFavoritos);//Añadimos las localizaciones favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos el resto de localizaciones no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByNoOwnerAndDate
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación, colocando primero
     * las que no pertenecen a un usuario en específico. El parámetros de entrada actualUserEmail indica el email del usuario.
     * Cabecera: public void orderLocalizationListAscByNoOwnerAndDate(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     * -String actualUserEmail
     * Postcondiciones: El método ordena la lista de localizaciones por fecha, colocando primero las localizaciones
     * que no pertenecen al usuario.
     */
    public void orderLocalizationListAscByNoOwnerAndDate(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
        ArrayList<ClsLocalizationPointWithFav> listadoPropias= new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoPropias = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)//Separamos los localizaciones en propias y no propias
        {
            if(localizationList.get(i).get_localizationPoint().getEmailCreator().equals(actualUserEmail)){
                listadoPropias.add(localizationList.get(i));
            }else{
                listadoNoPropias.add(localizationList.get(i));
            }
        }

        this.orderLocalizationListAscByDate(listadoPropias);//Ordenamos la lista de localizaciones del usuario actual
        this.orderLocalizationListAscByDate(listadoNoPropias);//Ordenamos la lista de localizaciones que no creó ell usuario

        localizationList.clear();
        localizationList.addAll(listadoNoPropias);//Añadimos las localizaciones no propias
        localizationList.addAll(listadoPropias);//Añadimos las localizaciones creadas por el usuario
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByNoOwnerAndDateAndFav
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su fecha de creación, colocando primero
     * las que no pertenecen a un usuario específico. El parámetros de entrada actualUserEmail indica el email del usuario.
     * Criterio de prioridad en la ordenación:
     * -Localizaciones que no pertenecen al usuario //Todas son favoritas
     * -Localizaciones favoritas que pertenecen al usuario
     * -Localizaciones no favoritas que pertenecen al usuario
     * Cabecera: public void orderLocalizationListAscByNoOwnerAndDateAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     * -String actualUserEmail
     * Postcondiciones: El método ordena una lista de localizaciones por fecha, colocando primero las localizaciones
     * que no pertenecen al usuario.
     */
    public void orderLocalizationListAscByNoOwnerAndDateAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
        ArrayList<ClsLocalizationPointWithFav> listadoFavoritos= new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoFavoritos = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)//Separamos los localizaciones en favoritas y no favoritas
        {
            if(localizationList.get(i).is_favourite()){
                listadoFavoritos.add(localizationList.get(i));//Aquí se encontrarán todas las localizaciones favoritas y/o no propias
            }else{
                listadoNoFavoritos.add(localizationList.get(i));
            }
        }

        this.orderLocalizationListAscByNoOwnerAndDate(listadoFavoritos, actualUserEmail);//Ordenamos la lista de localizaciones favoritas
        this.orderLocalizationListAscByDate(listadoNoFavoritos);//Ordenamos la lista de localizaciones no favoritas

        localizationList.clear();
        localizationList.addAll(listadoFavoritos);//Añadimos las localizaciones favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos las localizaciones no favoritas
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByNoOwnerAndName
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su nombre, colocando primero las que
     * no pertenecen al usuario actual.
     * Cabecera: public void orderLocalizationListAscByNoOwnerAndName(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     * -String actualUserEmail
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su nombre y priorizando
     * las localizaciones no propias.
     */
    public void orderLocalizationListAscByNoOwnerAndName(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
        ArrayList<ClsLocalizationPointWithFav> listadoPropias = new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoPropias = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)
        {
            if(localizationList.get(i).get_localizationPoint().getEmailCreator().equals(actualUserEmail)){
                listadoPropias.add(localizationList.get(i));
            }else{
                listadoNoPropias.add(localizationList.get(i));
            }
        }

        listadoPropias = orderLocalizationListAscByName(listadoPropias);//Ordenamos la lista de propias
        listadoNoPropias = orderLocalizationListAscByName(listadoNoPropias);//Ordenamos la lista de no propias

        localizationList.clear();//Limpiamos la lista
        localizationList.addAll(listadoNoPropias);//Añadimos las localizaciones no propias
        localizationList.addAll(listadoPropias);//Añadimos las localizaciones propias
    }

    /**
     * Interfaz
     * Nombre: orderLocalizationListAscByNoOwnerAndNameAndFav
     * Comentario: Este método nos permite ordenar una lista de localizaciones por su nombre, colocando primero las
     * que no pertenecen a un usuario en específico. El parámetro de entrada actualUserEmail denota al usuario.
     * Criterio de prioridad en la ordenación:
     * -Localizaciones que no pertenecen al usuario
     * -Localizaciones favoritas que pertenecen al usuario
     * -Localizaciones no favoritas que pertenecen al usuario
     * Cabecera: public void orderLocalizationListAscByNoOwnerAndNameAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail)
     * Entrada/Salida:
     *  -ArrayList<ClsLocalizationPointWithFav> localizationList
     * Entrada:
     * -String actualUserEmail
     * Postcondiciones: El método ordená la lista de localizaciones ascendentemente según su nombre y priorizando
     * las localizaciones no propias.
     */
    public void orderLocalizationListAscByNoOwnerAndNameAndFav(ArrayList<ClsLocalizationPointWithFav> localizationList, String actualUserEmail){
        ArrayList<ClsLocalizationPointWithFav> listadoFavoritos = new ArrayList<>();
        ArrayList<ClsLocalizationPointWithFav> listadoNoFavoritos = new ArrayList<>();

        for (int i = 0; i < localizationList.size(); i++)
        {
            if(localizationList.get(i).is_favourite()){
                listadoFavoritos.add(localizationList.get(i));//Aquí se encuntran todas las localiazciones favoritas y/o no propias
            }else{
                listadoNoFavoritos.add(localizationList.get(i));
            }
        }

        this.orderLocalizationListAscByNoOwnerAndName(listadoFavoritos, actualUserEmail);//Ordenamos la lista de favoritas
        this.orderLocalizationListAscByNoOwnerAndName(listadoNoFavoritos, actualUserEmail);//Ordenamos la lista de no favoritas

        localizationList.clear();//Limpiamos la lista
        localizationList.addAll(listadoFavoritos);//Añadimos las localizaciones favoritas
        localizationList.addAll(listadoNoFavoritos);//Añadimos las localizaciones no favoritas
    }
}
