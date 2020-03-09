package com.example.adventuremaps.Management;

import com.example.adventuremaps.FireBaseEntities.ClsRoute;

import java.util.ArrayList;

public class OrderLists {

    /**
     * Interfaz
     * Nombre: orderRouteListAscByName
     * Comentario: Este método nos permite ordenar una lista de rutas por su nombre.
     * Internamente este método realiza el método de ordenación por selección directa,
     * debido a que el número de rutas dudosamente sea muy elevado.
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
}
