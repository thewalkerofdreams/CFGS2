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
        /// Comentario: Este método devuelve el listado de personas.
        /// </summary>
        /// <returns>
        /// Listado de todas las personas
        /// </returns>
       public async Task<List<clsPersona>> listadoPersonasCompleto()
        {
            clsListadoPersonasDAL listadoPersonasDAL = new clsListadoPersonasDAL();
            return await listadoPersonasDAL.listadoCompletoPersonas(); ;
        }
    }
}
