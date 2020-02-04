using Repaso_Api03_DAL.Connections;
using Repaso_Api03_Entities;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repaso_Api03_DAL.Lists
{
    public class ClsListadosPersonas_DAL
    {
        /// <summary>
        /// Nombre: obtenerListadoDePersonas
        /// Comentario: Este método nos permite obtener un listado de las personas almacenadas en la base de datos.
        /// Cabecera: public List<ClsPersona> obtenerListadoDePersonas()
        /// </summary>
        /// <returns>Devuelve un list del tipo ClsPersona</returns>
        public List<ClsPersona> obtenerListadoDePersonas()
        {
            List<ClsPersona> listadoPersonas = new List<ClsPersona>();
            SqlDataReader miLector = null;
            ClsMyConnection clsMyConnection = new ClsMyConnection("yeray.database.windows.net", "PersonasDB", "yeray", "Mi_tesoro");
            SqlConnection connection = null;
            try
            {
                connection = clsMyConnection.getConnection();//Es posible que no se pueda llegar a realizar la conexión y salte una excepción.
                SqlCommand sqlCommand = new SqlCommand();

                ClsPersona persona;


                sqlCommand.CommandText = "SELECT * FROM PD_Personas";
                sqlCommand.Connection = connection;

                miLector = sqlCommand.ExecuteReader();

                if (miLector.HasRows)
                {
                    while (miLector.Read())
                    {
                        persona = new ClsPersona();
                        persona.id = (int)miLector["IDPersona"];
                        persona.idDepartamento = (miLector["IDDepartamento"] is DBNull) ? 0 : (int)miLector["IDDepartamento"];
                        persona.fotoPersona = (miLector["FotoPersona"] is DBNull) ? new Byte[0] : (Byte[])miLector["FotoPersona"];
                        persona.nombre = (miLector["NombrePersona"] is DBNull) ? "DEFAULT" : (string)miLector["NombrePersona"];
                        persona.apellidos = (miLector["ApellidosPersona"] is DBNull) ? "DEFAULT" : (string)miLector["ApellidosPersona"];
                        persona.fechaNacimiento = (miLector["FechaNacimientoPersona"] is DBNull) ? new DateTime() : (DateTime)miLector["FechaNacimientoPersona"];
                        persona.telefono = (miLector["TelefonoPersona"] is DBNull) ? "000000000" : (string)miLector["TelefonoPersona"];
                        listadoPersonas.Add(persona);
                    }
                }

            }
            catch (Exception e)//Es posible que no podamos acceder a la base de datos
            {
                throw e;
            }
            finally
            {
                if (miLector != null)
                {
                    miLector.Close();
                }

                if (connection != null)
                {
                    clsMyConnection.closeConnection(ref connection);
                }
            }

            return listadoPersonas;
        }
    }
}
