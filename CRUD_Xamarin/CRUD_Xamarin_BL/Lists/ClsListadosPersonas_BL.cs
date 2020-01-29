using CRUD_Xamarin_DAL.Lists;
using CRUD_Xamarin_Entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace CRUD_Xamarin_BL.Lists
{
    public class ClsListadosPersonas_BL
    {
        /// <summary>
        /// Nombre: listadoPersonas
        /// Comentario: Este método nos permite obetner un listado de personas de la base de datos.
        /// Dentor se llama al método obtenerListadoDePersonas de la clase clsListadosPersona.
        /// </summary>
        /// <returns>Devuelve un listado de personas (List<ClsPersona>).</returns>
        public static List<ClsPersona> listadoPersonas()
        {
            ClsListadosPersonas_DAL clsListadosPersona = new ClsListadosPersonas_DAL();
            return clsListadosPersona.obtenerListadoDePersonas();
        }
    }
}
