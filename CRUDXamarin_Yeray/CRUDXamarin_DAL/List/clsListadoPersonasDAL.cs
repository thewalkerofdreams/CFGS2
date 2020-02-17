

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
        /*Aqui tendras que declarar un objeto sql connection 
         * e igualarlo al que tienes en MyConnection (getConnection)
           luego haces las cosillas que sean con el dataReader y tal
           y luego cierras
         */

        /// <summary>
        /// Metodo que devuelve la lista completa de personas
        /// que hay en la BBDD
        /// </summary>
        /// <returns>
        /// Listado de todas las personas
        /// </returns>
        public async Task<List<clsPersona>> listadoCompletoPersonas()
        {
            List<clsPersona> listaPersonas = new List<clsPersona>();
            //Cliente HTTP
            HttpClient httpClient = new HttpClient();
            string cadena = clsMyConnection.getUriBase() + "PersonaApi/";
            Uri uri = new Uri(cadena);
            //clsPersona persona = null;
            /*Envuelvelo en un try catch*/
            HttpResponseMessage response = await httpClient.GetAsync(uri);

            if (response.IsSuccessStatusCode)
            {
                //string result = await response.Content.ReadAsStringAsync();

                string result = await httpClient.GetStringAsync(uri);

                listaPersonas = JsonConvert.DeserializeObject<List<clsPersona>>(result);
            }

            
            return listaPersonas;
        }



        /// <summary>
        /// Metodo que devuelve el objeto persona correspondiente a la ID pasada como parámetro
        /// </summary>
        /// <param name="idPersona">
        /// ID de la persona a devolver
        /// </param>
        /// <returns>
        /// Objeto persona correspondiente a la ID dada o null 
        /// si no existe o no se encuentra en la BBDD
        /// </returns>
        public clsPersona obtenerObjetoPersonaPorID(int idPersona)
        {
            /*
            clsMyConnection miConexion = new clsMyConnection();

            SqlConnection connection = null;
            SqlCommand command = new SqlCommand();
            SqlDataReader dataReader = null;
            clsPersona oPersona = new clsPersona();

            

            try
            {
                command.Parameters.AddWithValue("@idPersona", idPersona);
                string sql = "select * from PD_Personas where IdPersona = @idPersona";

                connection = miConexion.getConnection();
                command.CommandText = sql;
                command.Connection = connection;
                dataReader = command.ExecuteReader();

                if (dataReader.HasRows)
                {
                    while (dataReader.Read())
                    {

                        if (!dataReader.IsDBNull(dataReader.GetOrdinal("IdPersona")))
                        {
                            oPersona.idPersona = (int)dataReader["IdPersona"];
                        }
                        if (!dataReader.IsDBNull(dataReader.GetOrdinal("NombrePersona")))
                        {
                            oPersona.nombrePersona = (string)dataReader["NombrePersona"];
                        }
                        if (!dataReader.IsDBNull(dataReader.GetOrdinal("ApellidosPersona")))
                        {
                            oPersona.apellidosPersona = (string)dataReader["ApellidosPersona"];
                        }
                        if (!dataReader.IsDBNull(dataReader.GetOrdinal("FechaNacimientoPersona")))
                        {
                            oPersona.fechaNacimientoPersona = (DateTime)dataReader["FechaNacimientoPersona"];
                        }
                        if (!dataReader.IsDBNull(dataReader.GetOrdinal("TelefonoPersona")))
                        {
                            oPersona.telefonoPersona = (string)dataReader["TelefonoPersona"];
                        }

                        if (!dataReader.IsDBNull(dataReader.GetOrdinal("IDDepartamento")))
                        {
                            oPersona.idDepartamento = (int)dataReader["IDDepartamento"];
                        }

                        if (!dataReader.IsDBNull(dataReader.GetOrdinal("FotoPersona")))
                        {
                            oPersona.fotoPersona = (byte[])dataReader["FotoPersona"];
                        }
                    }

                }
            }
            catch(Exception e)
            {
                throw e;
            }
            finally
            {
                if(dataReader != null)
                {
                    dataReader.Close();
                }
                if(connection != null)
                {
                    miConexion.closeConnection(ref connection);
                }
            }
            return oPersona;
            */
            return null;
        }
    }
}
