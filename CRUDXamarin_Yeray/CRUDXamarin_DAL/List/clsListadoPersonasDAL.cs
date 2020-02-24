

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
        /// Comentario: Este metodo devuelve la lista completa de personas
        /// que hay en la BBDD
        /// </summary>
        /// <returns>
        /// Listado de todas las personas
        /// </returns>
        public async Task<List<clsPersona>> listadoCompletoPersonas()
        {
            List<clsPersona> listaPersonas = new List<clsPersona>();
            HttpClient httpClient = new HttpClient();
            string cadena = clsMyConnection.getUriBase() + "PersonaApi/";
            Uri uri = new Uri(cadena);
            try
            {
                HttpResponseMessage response = await httpClient.GetAsync(uri);

                if (response.IsSuccessStatusCode)
                {
                    string result = await httpClient.GetStringAsync(uri);
                    listaPersonas = JsonConvert.DeserializeObject<List<clsPersona>>(result);
                }
            }
            catch (Exception e)
            {
                throw e;
            }

            return listaPersonas;
        }
 
    }
}
