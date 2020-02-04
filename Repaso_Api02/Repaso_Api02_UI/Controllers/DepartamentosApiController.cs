using Repaso_Api02_BL.Handlers;
using Repaso_Api02_BL.Lists;
using Repaso_Api02_Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Repaso_Api02_UI.Controllers
{
    public class DepartamentosApiController : ApiController
    {
        // GET: api/DepartamentosApi
        public IEnumerable<ClsDepartamento> Get()
        {
            return new ClsListadosDepartamentos_BL().obtenerListadoDeDepartamentos();
        }

        // GET: api/DepartamentosApi/5
        public ClsDepartamento Get(int id)
        {
            return new ClsHandlerDepartamento_BL().obtenerDepartamento(id);
        }

        // POST: api/DepartamentosApi
        public void Post([FromBody]string value)
        {
        }

        // PUT: api/DepartamentosApi/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE: api/DepartamentosApi/5
        public void Delete(int id)
        {
        }
    }
}
