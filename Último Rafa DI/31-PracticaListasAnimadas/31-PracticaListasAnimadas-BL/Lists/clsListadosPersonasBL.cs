using _31_PracticaListasAnimadas_DAL.Lists;
using _31_PracticaListasAnimadas_ENTITIES;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_BL.Lists
{
    public class clsListadosPersonasBL
    {
        /// <summary>
        /// Recoge de la capa DAL el listado y lo pasa a la CAPA UI
        /// </summary>
        /// <returns>El listado de personas List<clsPersona></returns>
        public async Task<List<clsPersona>> listadoPersonas()
        {
            clsListadosPersonasDAL listBBDD = new clsListadosPersonasDAL();
            Task<List<clsPersona>> l = listBBDD.listadoPersona();
            List<clsPersona> listado = await listBBDD.listadoPersona();
            return listado;
        }
    }
}
