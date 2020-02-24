

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

namespace CRUDXamarin_DAL.Handler
{
    public class clsGestionPersonasDAL
    {
        /// <summary>
        /// Comentario: Este método nos permite insertar una persona en la base de datos.
        /// </summary>
        /// <param name="oPersona">
        /// La persona ha insertar
        /// </param>
        /// <returns>
        /// El método devuelve un número entero que son el número de filas afectadas.
        /// </returns>
        public async Task<int> insertarPersonaAsync(clsPersona oPersona)
        {
            HttpClient client = new HttpClient();
            String ruta = clsMyConnection.getUriBase();
            String datos;
            HttpContent contenido;
            Uri miUri = new Uri($"{ruta}PersonaApi");
            int filas = 0;

            HttpResponseMessage response = new HttpResponseMessage();

            try
            {
                datos = JsonConvert.SerializeObject(oPersona);
                contenido = new StringContent(datos, System.Text.Encoding.UTF8, "application/json");
                response = await client.PostAsync(miUri, contenido);
            }
            catch (Exception e)
            {
                throw e;
            }

            if (response.IsSuccessStatusCode)
            {
                filas = 1;
            }

            return filas;
        }

        /// <summary>
        /// Método que elimina una persona de la BBDD
        /// </summary>
        /// <param name="idPersona">
        /// ID de la persona a eliminar
        /// </param>
        /// <returns>
        /// entero que sera el numero de filas afectadas
        /// </returns>
        public async Task<int> eliminarPersonaAsync(int idPersona)
        {
            String ruta = clsMyConnection.getUriBase();
            int filasAfectadas = 0;
            HttpClient httpClient = new HttpClient();
            Uri uri = new Uri($"{ruta}PersonaApi/{idPersona}");

            try
            {
                HttpResponseMessage response = await httpClient.DeleteAsync(uri);
                if (response.IsSuccessStatusCode)
                {
                    filasAfectadas = 1;
                }
            }
            catch (Exception e)
            {
                throw e;
            }
           
            return filasAfectadas;
        }

        /// <summary>
        /// Comentario: Este método nos permite actualizar una persona en la base de datos.
        /// </summary>
        /// <param name="oPersona">
        /// Actualización de la persona.
        /// </param>
        /// <returns>
        /// El método devuelve un entero asociado al nombre que es el número de filas afectadas.
        /// </returns>
        public async Task<int> actualizarPersona(clsPersona oPersona)
        {
            HttpClient client = new HttpClient();
            String ruta = clsMyConnection.getUriBase();
            String datos;
            HttpContent contenido;
            Uri uri = new Uri($"{ruta}PersonaApi/{oPersona.idPersona}");
            int filas = 0;

            HttpResponseMessage response = new HttpResponseMessage();

            try
            {
                datos = JsonConvert.SerializeObject(oPersona);
                contenido = new StringContent(datos, System.Text.Encoding.UTF8, "application/json");
                response = await client.PutAsync(uri, contenido);
            }
            catch (Exception e)
            {
                throw e;
            }

            if (response.IsSuccessStatusCode)
            {
                filas = 1;
            }

            return filas;
        }

        /// <summary>
        /// Comentario: Este método nos permite obtener una persona de la base de datos.
        /// </summary>
        /// <param name="id">
        /// Id de la persona.
        /// </param>
        /// <returns>La persona de la base de datos</returns>
        public async Task<clsPersona> obtenerPersona(int id)
        {
            String ruta = clsMyConnection.getUriBase();
            clsPersona persona = new clsPersona();
            HttpClient client = new HttpClient();
            HttpResponseMessage response = new HttpResponseMessage();

            try
            {
                response = await client.GetAsync($"{ruta}PersonaApi/{id}");
            }
            catch (Exception e)
            {
                throw e;
            }

            if (response.IsSuccessStatusCode)
            {
                string pers = await response.Content.ReadAsStringAsync();
                persona = JsonConvert.DeserializeObject<clsPersona>(pers);
            }

            return persona;
        }
    }
}
