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
        /// Comentario: Este método nos devuelve la lista completa de personas de la base de datos.
        /// </summary>
        /// <returns>
        /// Listado de todas las personas de la base de datos.
        /// </returns>
        public async Task<List<clsPersona>> listadoCompletoPersonas()
        {
            clsListadoPersonasDAL listadoPersonasDAL = new clsListadoPersonasDAL();

            List<clsPersona> listadoFiltradoPorBL = new List<clsPersona>();
            listadoFiltradoPorBL = await listadoPersonasDAL.listadoCompletoPersonas();
            return listadoFiltradoPorBL;
        }

    }
}
