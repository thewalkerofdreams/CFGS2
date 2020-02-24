using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.SqlClient;

// Esta clase contiene los métodos necesarios para trabajar con el acceso a una base de datos SQL Server
//PROPIEDADES
//   _server: cadena 
//   _database: cadena, básica. Consultable/modificable.
//   _user: cadena, básica. Consultable/modificable.
//   _pass: cadena, básica. Consultable/modificable.



namespace CRUDXamarin_DAL.Connection
{
    public class clsMyConnection
    {
        /// <summary>
        /// Devuelve la uri base de la api en forma de string
        /// </summary>
        /// <returns>
        /// devuelve la cadena base 
        /// </returns>
       public static string getUriBase()
        {
            string uriBase;
            uriBase = "https://personasapi-avazquez.azurewebsites.net/api/";
            return uriBase;
        }


    }

}
