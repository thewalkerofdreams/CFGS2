using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repaso_Final02_JS_DAL.Connections
{
    public class ClsMyConnection
    {
        //Atributos
        public String server { get; set; }
        public String dataBase { get; set; }
        public String user { get; set; }
        public String pass { get; set; }

        //Constructores

        public ClsMyConnection()
        {
            this.server = "yeray.database.windows.net";
            this.dataBase = "PersonasDB";
            this.user = "yeray";
            this.pass = "Mi_tesoro";
            /*this.server = "107-12";
            this.dataBase = "PersonasDB";
            this.user = "pruebaApi";
            this.pass = "123";*/
        }

        //Con parámetros por si quisiera cambiar las conexiones
        public ClsMyConnection(String server, String database, String user, String pass)
        {
            this.server = server;
            this.dataBase = database;
            this.user = user;
            this.pass = pass;
        }


        //METODOS

        /// <summary>
        /// Método que abre una conexión con la base de datos
        /// </summary>
        /// <pre>Nada.</pre>
        /// <returns>Una conexión con la base de datso</returns>
        public SqlConnection getConnection()
        {
            SqlConnection connection = new SqlConnection();

            try
            {
                connection.ConnectionString = $"server={server};database={dataBase};uid={user};pwd={pass};";
                connection.Open();
            }
            catch (SqlException)
            {
                throw;
            }

            return connection;

        }




        /// <summary>
        /// Este metodo cierra una conexión con la Base de datos
        /// </summary>
        /// <post>The connection is closed.</post>
        /// <param name="connection">La entrada es la conexión a cerrar
        /// </param>
        public void closeConnection(ref SqlConnection connection)
        {
            try
            {
                connection.Close();
            }
            catch (SqlException)
            {
                throw;
            }
            catch (InvalidOperationException)
            {
                throw;
            }
            catch (Exception)
            {
                throw;
            }
        }
    }
}
