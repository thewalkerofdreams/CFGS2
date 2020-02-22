using CRUDXamarin_DAL.Handler;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace CRUDXamarin_BL.Handler
{
    public class clsGestionDepartamentosBL
    {
        /// <summary>
        /// Comentario: Este método nos permite obtener un departamento de la base de datos.
        /// </summary>
        /// <param name="id">
        /// Id del departamento.
        /// </param>
        /// <returns>El departamento de la base de datos</returns>
        public async Task<clsDepartamento> obtenerDepartamento(int id)
        {
            clsGestionDepartamentosDal gestionDepartamentosDAL = new clsGestionDepartamentosDal();
            return await gestionDepartamentosDAL.obtenerDepartamento(id);
        }
    }
}
