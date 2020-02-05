using Repaso_Final02_JS_DAL.Lists;
using Repaso_Final02_JS_Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repaso_Final02_JS_BL.Lists
{
    public class ClsListadosDepartamentos_BL
    {
        /// <summary>
        /// Comentario: Este método nos permite obtener todos los departamentos de la base de datos.
        /// </summary>
        /// <returns>El método devuelve una lista del tipo clsDepartamento, que son todos los departamentos de la base de datos.</returns>
        public List<ClsDepartamento> obtenerListadoDeDepartamentos()
        {
            return new ClsListadosDepartamentos_DAL().obtenerListadoDeDepartamentos();
        }
    }
}
