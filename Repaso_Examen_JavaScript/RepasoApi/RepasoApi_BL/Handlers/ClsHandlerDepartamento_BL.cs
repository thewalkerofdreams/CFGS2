using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RepasoApi_Entities;
using RepasoApi_DAL.Handlers;

namespace RepasoApi_BL.Handlers
{
    public class ClsHandlerDepartamento_BL
    {
        /// <summary>
        /// Comentario: Este método nos permite obtener un departamento de la base de datos.
        /// </summary>
        /// <param name="id">Id del departamento</param>
        /// <returns>El método devuelve un clsDepartamento asociado al nombre o null, si no se ha encontrado un departamento con esa id en la base de datos.</returns>
        public ClsDepartamento obtenerDepartamento(int id)
        {
            return new ClsHandlerDepartamento_DAL().obtenerDepartamento(id);
        }
    }
}
