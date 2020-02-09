using _31_PracticaListasAnimadas_DAL.Connection;
using _31_PracticaListasAnimadas_ENTITIES;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_DAL.Handlers
{
    public class clsManejadoraDAL
    {
        /// <summary>
        /// Con este metodo llamamos a borrar persona de la api
        /// </summary>
        /// <param name="idPersona">El id de la persona a eliminar</param>
        public async Task borrarPersona(int idPersona)
        {
            List<clsPersona> listado = new List<clsPersona>();
            HttpClient miCliente = new HttpClient();

            Uri requestUri = new Uri(clsMyConnection.getUriBase() + "/Persona/" + idPersona);

            //Send the GET request asynchronously and retrieve the response as a string.
            HttpResponseMessage httpResponse = new HttpResponseMessage();
            string httpResponseBody = "";

            try
            {

                httpResponse = await miCliente.DeleteAsync(requestUri);

                /*
                if (httpResponse.IsSuccessStatusCode)
                {
                    httpResponseBody = await miCliente.DeleteAsync(requestUri);
                    listado = JsonConvert.DeserializeObject<List<clsPersona>>(httpResponseBody);
                }
                */

            }
            catch (Exception ex)
            {
                httpResponseBody = "Error: " + ex.HResult.ToString("X") + " Message: " + ex.Message;
            }
        }

        /// <summary>
        /// Con este metodo enviamos a la api la persona a introducir en la BBDD
        /// </summary>
        /// <param name="persona">La persona a introducir en la BBDD</param>
        /// <returns>Devuelve la tarea asincrona</returns>
        public async Task anhadirPersona(clsPersona persona)
        {
            HttpClient miCliente = new HttpClient();

            //Uri requestUri = new Uri(clsMyConnection.getUriBase() + "/Persona/");
            Uri requestUri = new Uri(clsMyConnection.getUriBase() + "/PersonaAPI/");

            //Send the GET request asynchronously and retrieve the response as a string.
            HttpResponseMessage httpResponse = new HttpResponseMessage();
            string httpResponseBody = "";


            try
            {
                string json = JsonConvert.SerializeObject(persona);
                //HttpStringContent content = new HttpStringContent(json);
                HttpContent content = new StringContent(json, Encoding.UTF8, "application/json");
                await miCliente.PostAsync(requestUri, content);

                
                if (httpResponse.IsSuccessStatusCode)
                {
                    //httpResponseBody = await miCliente.DeleteAsync(requestUri);
                    //listado = JsonConvert.DeserializeObject<List<clsPersona>>(httpResponseBody);
                }
                

            }
            catch (Exception ex)
            {
                httpResponseBody = "Error: " + ex.HResult.ToString("X") + " Message: " + ex.Message;
            }

        }
    }
}
