using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using RepasoApi_Entities;
using RepasoApi_BL.Handlers;
using RepasoApi_BL.Lists;

namespace RepasoApi_UI.Controllers
{
    public class PersonasApiController : ApiController
    {
        // GET: api/PersonasApi
        public IEnumerable<ClsPersona> Get()
        {
            return new ClsListadosPersonas_BL().obtenerListadoDePersonas();
        }

        // GET: api/PersonasApi/5
        public ClsPersona Get(int id)
        {
            return new ClsHandlerPersona_BL().obtenerPersona(id);
        }

        // POST: api/PersonasApi
        public void Post([FromBody]ClsPersona persona)
        {
            new ClsHandlerPersona_BL().insertarPersona(persona);
        }

        // PUT: api/PersonasApi/5
        public HttpResponseMessage Put([FromBody]ClsPersona persona)
        {
            bool ret = new ClsHandlerPersona_BL().editarPersona(persona);
            HttpResponseMessage httpResponseMessage;

            if (ret)//Si hemos conseguido editar la persona
            {
                httpResponseMessage = new HttpResponseMessage(HttpStatusCode.OK);
            }
            else
            {
                httpResponseMessage = new HttpResponseMessage(HttpStatusCode.NotFound);
            }
            return httpResponseMessage;
        }

        // DELETE: api/PersonasApi/5
        public HttpResponseMessage Delete(int id)
        {
            bool ret = new ClsHandlerPersona_BL().eliminarPersona(id);
            HttpResponseMessage httpResponseMessage;

            if (ret)//Si hemos conseguido eliminar la persona
            {
                httpResponseMessage = new HttpResponseMessage(HttpStatusCode.OK);
            }
            else
            {
                httpResponseMessage = new HttpResponseMessage(HttpStatusCode.NotFound);
            }

            return httpResponseMessage;
        }
    }
}
