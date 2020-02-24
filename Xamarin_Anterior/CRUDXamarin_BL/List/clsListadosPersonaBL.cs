using CRUDXamarin_DAL.List;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CRUDXamarin_BL.List
{
    public class clsListadosPersonaBL
    {

        /// <summary>
        /// Metodo que devolvera el listado de personas ordinario
        /// 
        /// </summary>
        /// <returns>
        /// Listado de todas las personas
        /// </returns>
       public async Task<List<clsPersona>> listadoPersonasOrdinario()
        {
            /*Algun tipo de filtro de la empresa*/

            //Aqui no haria falta poner try catch, la excepcion viaja 
            //por aqui y esto lo manda pa fuera, con cogerla en el 
            //controller ya esta ok

            clsListadoPersonasDAL listadoPersonasDAL = new clsListadoPersonasDAL();

            List<clsPersona> listadoFiltradoPorBL = new List<clsPersona>();

            listadoFiltradoPorBL = 
                await listadoPersonasDAL.listadoCompletoPersonas();
            return listadoFiltradoPorBL;
            //return listadoPersonasDAL.listadoCompletoPersonas();
        }

        /// <summary>
        /// Metodo que devuelve la persona buscada
        /// cuyo ID coincide con el recibido como parametro
        /// Aplica reglas de negocio
        /// </summary>
        /// <param name="idPersona">
        /// entero id de la persona a buscar
        /// </param>
        /// <returns>
        /// devuelve un objeto persona que corresponde con el id
        /// enviado o bien un objeto persona construido por defecto.
        /// </returns>
        public clsPersona obtenerObjetoPersonaPorID(int idPersona)
        {
            clsListadoPersonasDAL listadoPersonasDAL = new clsListadoPersonasDAL();
            return listadoPersonasDAL.obtenerObjetoPersonaPorID(idPersona);
        }
    }
}
