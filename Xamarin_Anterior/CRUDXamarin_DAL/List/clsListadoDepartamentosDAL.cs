using CRUDXamarin_DAL.Connection;
using CRUDXamarin_Ent;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace CRUDXamarin_DAL.List
{
    public class clsListadoDepartamentosDAL
    {
        /// <summary>
        /// Metodo que devuelve la lista completa de departamentos
        /// que hay en la BBDD
        /// </summary>
        /// <returns>
        /// Listado de todas los departamentos
        /// </returns>
        public async Task<List<clsDepartamento>> listadoCompletoDepartamentos()
        {
            List<clsDepartamento> listaDepartamentos = new List<clsDepartamento>();
            HttpClient httpClient = new HttpClient();
            string cadena = clsMyConnection.getUriBase() + "DepartamentoApi/";
            Uri uri = new Uri(cadena);
            try
            {
                HttpResponseMessage response = await httpClient.GetAsync(uri);
                if (response.IsSuccessStatusCode)
                {
                    string result = await httpClient.GetStringAsync(uri);
                    listaDepartamentos = JsonConvert.DeserializeObject<List<clsDepartamento>>(result);
                }
            }
            catch(Exception e)
            {
                throw e;
            }

            return listaDepartamentos;
        }
    }
}
