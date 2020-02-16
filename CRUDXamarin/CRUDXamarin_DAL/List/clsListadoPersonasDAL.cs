

using CRUDXamarin_DAL.Connection;
using CRUDXamarin_Ent;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;


namespace CRUDXamarin_DAL.List
{
    public class clsListadoPersonasDAL
    {
        /// <summary>
        /// Comentario: Este método nos devuelve la lista completa de personas de la base de datos.
        /// </summary>
        /// <returns>
        /// Listado de todas las personas de la base de datos.
        /// </returns>
        public async Task<List<clsPersona>> listadoCompletoPersonas()
        {
            List<clsPersona> listaPersonas = new List<clsPersona>();
            HttpClient httpClient = new HttpClient();
            string cadena = clsMyConnection.getUriBase() + "PersonaApi/";
            Uri uri = new Uri(cadena);
            HttpResponseMessage response = new HttpResponseMessage();

            try
            {
                response = await httpClient.GetAsync(uri);
            }
            catch (Exception e)
            {
                throw e;
            }

            if (response.IsSuccessStatusCode)
            {
                string result = await httpClient.GetStringAsync(uri);
                listaPersonas = JsonConvert.DeserializeObject<List<clsPersona>>(result);
            }
  
            return listaPersonas;
        }

    }
}
