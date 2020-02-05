using Repaso_Final_JS_DAL.Connections;
using Repaso_Final_JS_Entities;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repaso_Final_JS_DAL.Lists
{
    public class ClsListadosDepartamentos_DAL
    {
        /// <summary>
        /// Comentario: Este método nos permite obtener todos los departamentos de la base de datos.
        /// </summary>
        /// <returns>El método devuelve una lista del tipo clsDepartamento, que son todos los departamentos de la base de datos.</returns>
        public List<ClsDepartamento> obtenerListadoDeDepartamentos()
        {
            List<ClsDepartamento> listadoDepartamentos = new List<ClsDepartamento>();

            ClsMyConnection clsMyConnection = clsMyConnection = new ClsMyConnection();
            SqlConnection connection = null;
            SqlDataReader miLector = null;
            try
            {
                connection = clsMyConnection.getConnection();//Es posible que no se pueda llegar a realizar la conexión y salte una excepción.
                SqlCommand sqlCommand = new SqlCommand();

                ClsDepartamento departamento;
                sqlCommand.CommandText = "SELECT * FROM PD_Departamentos";
                sqlCommand.Connection = connection;

                miLector = sqlCommand.ExecuteReader();

                if (miLector.HasRows)
                {
                    while (miLector.Read())
                    {
                        departamento = new ClsDepartamento();
                        departamento.Id = (int)miLector["IdDepartamento"];
                        departamento.Nombre = (string)miLector["NombreDepartamento"];
                        listadoDepartamentos.Add(departamento);
                    }
                }
            }
            catch (Exception e)//Es posible que no podamos acceder a la base de datos
            {
                throw e;
            }
            finally
            {
                if (connection != null)
                {
                    clsMyConnection.closeConnection(ref connection);
                }

                if (miLector != null)
                {
                    miLector.Close();
                }
            }

            return listadoDepartamentos;
        }
    }
}
