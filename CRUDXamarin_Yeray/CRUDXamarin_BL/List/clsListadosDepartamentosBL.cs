using CRUDXamarin_DAL.List;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace CRUDXamarin_BL.List
{
    public class clsListadosDepartamentosBL
    {
        /// <summary>
        /// Comentario: Este método devuelve la lista completa de departamentos
        /// que hay en la BBDD
        /// </summary>
        /// <returns>
        /// Listado de todas los departamentos
        /// </returns>
        public async Task<List<clsDepartamento>> listadoCompletoDepartamentos()
        {
            clsListadoDepartamentosDAL listadoDepartamentosDAL = new clsListadoDepartamentosDAL();
            return await listadoDepartamentosDAL.listadoCompletoDepartamentos(); ;
        }
    }
}
