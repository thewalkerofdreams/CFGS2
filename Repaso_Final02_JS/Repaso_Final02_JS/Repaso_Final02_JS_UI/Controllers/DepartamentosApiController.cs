using Repaso_Final02_JS_BL.Handlers;
using Repaso_Final02_JS_BL.Lists;
using Repaso_Final02_JS_Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Repaso_Final02_JS_UI.Controllers
{
    public class DepartamentosApiController : ApiController
    {
        // GET api/<controller>
        public IEnumerable<ClsDepartamento> Get()
        {
            return new ClsListadosDepartamentos_BL().obtenerListadoDeDepartamentos();
        }

        // GET api/<controller>/5
        public ClsDepartamento Get(int id)
        {
            return new ClsHandlerDepartamento_BL().obtenerDepartamento(id);
        }

        // POST api/<controller>
        public void Post([FromBody]string value)
        {
        }

        // PUT api/<controller>/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/<controller>/5
        public void Delete(int id)
        {
        }
    }
}