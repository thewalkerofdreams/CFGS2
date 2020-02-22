using CRUDXamarin_DAL.Connection;
using CRUDXamarin_Ent;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace CRUDXamarin_DAL.Handler
{
    public class clsGestionDepartamentosDal
    {
        /// <summary>
        /// Comentario: Este método nos permite obtener un departamento de la base de datos.
        /// </summary>
        /// <param name="id">
        /// Id del departamento.
        /// </param>
        /// <returns>El departamento de la base de datos</returns>
        public async Task<clsDepartamento> obtenerDepartamento(int id)
        {
            String ruta = clsMyConnection.getUriBase();

            clsDepartamento departamento = new clsDepartamento();

            HttpClient client = new HttpClient();

            HttpResponseMessage response = new HttpResponseMessage();

            try
            {
                response = await client.GetAsync($"{ruta}DepartamentoApi/{id}");
            }
            catch (Exception ex)
            {
                throw ex;
            }
            String prueba = "";
            if (response.IsSuccessStatusCode)
            {
                string dep = await response.Content.ReadAsStringAsync();
                prueba = JsonConvert.DeserializeObject<String>(dep);//Tengo que decirle a Ángela que la desearialización fallo porque solo obtiene el nombre del departamento en el Json
            }
            departamento = new clsDepartamento(id, prueba);

            return departamento;
        }
    }
}
