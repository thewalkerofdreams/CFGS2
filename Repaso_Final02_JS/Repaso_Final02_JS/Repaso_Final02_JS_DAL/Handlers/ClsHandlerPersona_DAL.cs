using Repaso_Final02_JS_DAL.Connections;
using Repaso_Final02_JS_Entities;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repaso_Final02_JS_DAL.Handlers
{
    public class ClsHandlerPersona_DAL
    {
        /// <summary>
        /// Comentario: Este método nos permite insertar una persona en la base de datos. 
        /// </summary>
        /// <param name="persona">Un objeto del tipo ClsPersona</param>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si se ha conseguido insertar la nueva persona o false en caso contrario.</returns>
        public bool insertarPersona(ClsPersona persona)
        {
            bool personaInsertada = true;
            ClsMyConnection clsMyConnection = new ClsMyConnection();
            SqlConnection connection = null;

            try
            {
                connection = clsMyConnection.getConnection();//Es posible que no se pueda llegar a realizar la conexión y salte una excepción.
                SqlCommand sqlCommand = new SqlCommand();
                sqlCommand.CommandType = CommandType.Text;

                sqlCommand.CommandText = "INSERT INTO PD_Personas(NombrePersona, ApellidosPersona, IDDepartamento, FechaNacimientoPersona, TelefonoPersona, FotoPersona) values (@NombrePersona, @ApellidosPersona, @IDDepartamento ,@FechaNacimientoPersona, @TelefonoPersona, @FotoPersona)";
                sqlCommand.Parameters.Add("@NombrePersona", SqlDbType.VarChar).Value = persona.nombre;
                sqlCommand.Parameters.Add("@ApellidosPersona", SqlDbType.VarChar).Value = persona.apellidos;
                sqlCommand.Parameters.Add("@IDDepartamento", SqlDbType.Int).Value = persona.idDepartamento;
                sqlCommand.Parameters.Add("@FechaNacimientoPersona", SqlDbType.Date).Value = persona.fechaNacimiento;
                sqlCommand.Parameters.Add("@TelefonoPersona", SqlDbType.VarChar).Value = persona.telefono;
                if (persona.fotoPersona != null)
                {
                    sqlCommand.Parameters.Add("@FotoPersona", SqlDbType.VarBinary).Value = persona.fotoPersona;
                }
                else
                {
                    sqlCommand.Parameters.Add("@FotoPersona", SqlDbType.VarBinary).Value = new Byte[0];
                }

                sqlCommand.Connection = connection;
                sqlCommand.ExecuteNonQuery();//Ejecutamos la inserción 
            }
            catch (Exception e)
            {
                personaInsertada = false;
                throw e;
            }
            finally
            {
                if (connection != null)
                {
                    clsMyConnection.closeConnection(ref connection);
                }
            }

            return personaInsertada;
        }

        /// <summary>
        /// Comentario: Este método nos permite eliminar una persona de la base de datos.
        /// </summary>
        /// <param name="id">Id de la persona</param>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si ha conseguido eliminar la persona o false en caso contrario.</returns>
        public bool eliminarPersona(int id)
        {
            bool personaEliminada = true;
            ClsMyConnection clsMyConnection = new ClsMyConnection();
            SqlConnection connection = null;

            try
            {
                connection = clsMyConnection.getConnection();
                SqlCommand sqlCommand = new SqlCommand();
                sqlCommand.CommandType = CommandType.Text;

                sqlCommand.CommandText = "DELETE FROM PD_Personas WHERE IdPersona = @Id";
                sqlCommand.Parameters.Add("@Id", System.Data.SqlDbType.Int).Value = id;

                sqlCommand.Connection = connection;
                if (sqlCommand.ExecuteNonQuery() == 0)//Si no indica que se haya modificado alguna fila
                {
                    personaEliminada = false;
                }
            }
            catch (Exception e)
            {
                personaEliminada = false;
                throw e;
            }
            finally
            {
                if (connection != null)
                {
                    clsMyConnection.closeConnection(ref connection);//Cerramos la conexión con la base de datos
                }
            }

            return personaEliminada;
        }

        /// <summary>
        /// Comentario: Este método nos permite modificar una persona de la base de datos.
        /// </summary>
        /// <param name="persona">El tipo ClsPersona</param>
        /// <returns></returns>
        public bool editarPersona(int id, ClsPersona persona)
        {
            bool personaEditada = true;
            ClsMyConnection clsMyConnection = new ClsMyConnection();
            SqlConnection connection = null;

            try
            {
                connection = clsMyConnection.getConnection();//Es posible que no se pueda llegar a realizar la conexión y salte una excepción.
                SqlCommand sqlCommand = new SqlCommand();
                sqlCommand.CommandType = CommandType.Text;

                sqlCommand.CommandText = "UPDATE [dbo].[PD_Personas] SET NombrePersona = @Nombre, ApellidosPersona = @Apellidos, IDDepartamento = @IdDepartamento, FechaNacimientoPersona = @FechaNacimiento, FotoPersona = @FotoPersona, TelefonoPersona = @Telefono WHERE IdPersona = @Id";
                sqlCommand.Parameters.Add("@Id", System.Data.SqlDbType.Int).Value = id;
                sqlCommand.Parameters.Add("@Nombre", System.Data.SqlDbType.VarChar).Value = persona.nombre;
                sqlCommand.Parameters.Add("@Apellidos", System.Data.SqlDbType.VarChar).Value = persona.apellidos;
                if (persona.fechaNacimiento == new DateTime(1, 1, 1))//Si la fecha es por defecto, le damos el valor mínimo permitido en la aplicación "01/01/1919".
                {
                    persona.fechaNacimiento = new DateTime(1919, 1, 1);
                }
                sqlCommand.Parameters.Add("@FechaNacimiento", System.Data.SqlDbType.DateTime).Value = persona.fechaNacimiento;
                sqlCommand.Parameters.Add("@Telefono", System.Data.SqlDbType.VarChar).Value = persona.telefono;
                sqlCommand.Parameters.Add("@IdDepartamento", System.Data.SqlDbType.Int).Value = persona.idDepartamento;
                if (persona.fotoPersona != null && persona.fotoPersona.Length != 0)//Si la foto no es nula o por defecto
                {
                    sqlCommand.Parameters.Add("@FotoPersona", SqlDbType.VarBinary).Value = persona.fotoPersona;
                }
                else
                {
                    sqlCommand.Parameters.Add("@FotoPersona", SqlDbType.VarBinary).Value = new Byte[0];
                }

                sqlCommand.Connection = connection;
                if (sqlCommand.ExecuteNonQuery() == 0)//Si no indica que se haya modificado alguna fila
                {
                    personaEditada = false;
                }

                clsMyConnection.closeConnection(ref connection);//Cerramos la conexión con la base de datos
            }
            catch (Exception e)
            {
                personaEditada = false; //Esto podría modificarlo
                throw e;
            }
            finally
            {
                if (connection != null)
                {
                    clsMyConnection.closeConnection(ref connection);
                }
            }


            return personaEditada;
        }

        /// <summary>
        /// Comentario: Este método nos permite obtener una persona de la base de datos.
        /// </summary>
        /// <param name="id">Id de la persona</param>
        /// <returns>El método devuelve un tipo ClsPersona asociado al nombre, que es la persona buscada o null si esa persona no existe en la base de datos.</returns>
        public ClsPersona obtenerPersona(int id)
        {
            ClsPersona persona = null;
            ClsMyConnection clsMyConnection = new ClsMyConnection();
            SqlConnection connection = null;

            try
            {
                connection = clsMyConnection.getConnection();
                SqlDataReader miLector;
                SqlCommand sqlCommand = new SqlCommand();
                sqlCommand.CommandType = CommandType.Text;

                sqlCommand.CommandText = "SELECT IDPersona, NombrePersona, ApellidosPersona, FechaNacimientoPersona, TelefonoPersona, FotoPersona, IDDepartamento FROM PD_Personas WHERE IdPersona = @Id";
                sqlCommand.Parameters.Add("@Id", System.Data.SqlDbType.Int).Value = id;

                sqlCommand.Connection = connection;
                miLector = sqlCommand.ExecuteReader();

                if (miLector.HasRows)
                {
                    miLector.Read();//Leemos la primera columna
                    persona = new ClsPersona();
                    persona.id = (int)miLector["IDPersona"];
                    persona.idDepartamento = (miLector["IDDepartamento"] is DBNull) ? 0 : (int)miLector["IDDepartamento"];
                    persona.fotoPersona = (miLector["FotoPersona"] is DBNull) ? new byte[0] : (byte[])miLector["FotoPersona"];
                    persona.nombre = (miLector["NombrePersona"] is DBNull) ? "DEFAULT" : (string)miLector["NombrePersona"];
                    persona.apellidos = (miLector["ApellidosPersona"] is DBNull) ? "DEFAULT" : (string)miLector["ApellidosPersona"];
                    persona.fechaNacimiento = (miLector["FechaNacimientoPersona"] is DBNull) ? new DateTime() : (DateTime)miLector["FechaNacimientoPersona"];
                    persona.telefono = (miLector["TelefonoPersona"] is DBNull) ? "000000000" : (string)miLector["TelefonoPersona"];
                }

            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                if (connection != null)
                {
                    clsMyConnection.closeConnection(ref connection);//Cerramos la conexión con la base de datos
                }
            }

            return persona;
        }
    }
}
