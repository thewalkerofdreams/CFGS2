using _31_PracticaListasAnimadas_DAL.Lists;
using _31_PracticaListasAnimadas_ENTITIES;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_BL.Lists
{
    public class clsListadosDepartamentosBL
    {
        public async Task<List<clsDepartamento>> listadoDepartamentos()
        {
            clsListadosDepartamentosDAL listBBDD = new clsListadosDepartamentosDAL();
            Task<List<clsDepartamento>> l = listBBDD.listadoDepartamentos();
            List<clsDepartamento> listado = await listBBDD.listadoDepartamentos();
            return listado;
        }
    }
}
