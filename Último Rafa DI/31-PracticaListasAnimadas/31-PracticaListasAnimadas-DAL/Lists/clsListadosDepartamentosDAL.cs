using _31_PracticaListasAnimadas_DAL.Connection;
using _31_PracticaListasAnimadas_ENTITIES;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_DAL.Lists
{
    public class clsListadosDepartamentosDAL
    {
        /// <summary>
        /// Con este metodo recogemos de la api el listado de departamentos
        /// </summary>
        /// <returns>devolvemos una tarea con la lista de departamentos</returns>
        public async Task<List<clsDepartamento>> listadoDepartamentos()
        {
            List<clsDepartamento> listado = new List<clsDepartamento>();
            HttpClient miCliente = new HttpClient();

            //Uri requestUri = new Uri(clsMyConnection.getUriBase() + "/Departamento");
            Uri requestUri = new Uri(clsMyConnection.getUriBase() + "DepartamentosAPI");

            //Send the GET request asynchronously and retrieve the response as a string.
            HttpResponseMessage httpResponse = new HttpResponseMessage();
            string httpResponseBody = "";

            try
            {

                httpResponse = await miCliente.GetAsync(requestUri);

                if (httpResponse.IsSuccessStatusCode)
                {
                    httpResponseBody = await miCliente.GetStringAsync(requestUri);
                    listado = JsonConvert.DeserializeObject<List<clsDepartamento>>(httpResponseBody);
                }


            }
            catch (Exception ex)
            {
                httpResponseBody = "Error: " + ex.HResult.ToString("X") + " Message: " + ex.Message;
            }

            return listado;

        }
    }
}
