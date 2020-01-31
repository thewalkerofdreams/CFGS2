using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using RepasoApi_UI.Models;
using RepasoApi_BL.Handlers;
using RepasoApi_BL.Lists;
using RepasoApi_Entities;

namespace RepasoApi_UI.Controllers
{
    public class PersonasController : Controller
    {
        // GET: PruebaListaPersonas
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult ListaPersonas()
        {
            ClsListadoPersonasConNombreDepartamentos listadoPersonas = new ClsListadoPersonasConNombreDepartamentos();
            try
            {
                return View(listadoPersonas.obtenerListadoPersonasConDepartamento());
            }
            catch (Exception e)
            {
                return View("Error");//Lo mandaría a una página de error
            }

        }
        #region CreateView
        public ActionResult Create()
        {
            ClsPersonaConListaDepartamentos clsPersonaConListaDepartamentos = null;//En esta clase en el constructor por defecto obtenemos datos de la base de datos, intentaremos capturar las posibles excepciones.
            try
            {
                clsPersonaConListaDepartamentos = new ClsPersonaConListaDepartamentos();//Creamos un modelo ClsPersonaConListaDepartamentos
            }
            catch (Exception e)
            {
                return View("Error");
            }

            return View(clsPersonaConListaDepartamentos);
        }

        [HttpPost]
        public ActionResult Create(ClsPersonaConListaDepartamentos personaConDepartamento)
        {
            if (ModelState.IsValid)
            {
                try
                {
                    ClsHandlerPersona_BL clsPersonaHandler_BL = new ClsHandlerPersona_BL();
                    if (clsPersonaHandler_BL.insertarPersona(personaConDepartamento))
                    {
                        ViewBag.MensajePersonaGuardada = "La persona se ha almacenado correctamente";//Esta vista tendra que mostrar un mensaje de guardado correctamente. (ViewBag)
                    }

                    return View(personaConDepartamento);//Volvemos a devolver la persona con departamento para que salgan los mismos datos de antes.
                }
                catch (Exception e)
                {
                    return View("Error");//Es posible que no podamos realizar una conexión a la base de datos
                }
            }
            else
            {
                return View();  //Retornamos a la misma vista para ver los fallos
            }
        }

        #endregion

        #region DeleteView

        public ActionResult Delete(int id)
        {
            try
            {
                ClsHandlerPersona_BL clsPersonaHandler_BL = new ClsHandlerPersona_BL();
                ClsPersona clsPersona = clsPersonaHandler_BL.obtenerPersona(id);

                ClsPersonaConDepartamento personaConDepartamento = new ClsPersonaConDepartamento(clsPersona);
                return View(personaConDepartamento);
            }
            catch (Exception e)
            {
                return View("Error");//Lo mandaría a una página de error
            }
        }

        [HttpPost, ActionName("Delete")]
        public ActionResult PostDelete(int id)
        {
            try
            {
                ClsHandlerPersona_BL clsPersonaHandler_BL = new ClsHandlerPersona_BL();
                if (clsPersonaHandler_BL.eliminarPersona(id))
                {
                    ViewBag.MensajePersonaEliminada = "La persona se ha eliminado de la base de datos";
                }

                return View();
            }
            catch (Exception e)
            {
                return View("Error");
            }

        }

        #endregion

        #region EditView

        public ActionResult Edit(int id)//Realizamos una busqueda de la persona por su id
        {
            ClsHandlerPersona_BL clsPersonaHandler_BL = new ClsHandlerPersona_BL();
            ClsPersona clsPersona;
            ClsPersonaConListaDepartamentos personaConDepartamentos = null;
            try
            {
                clsPersona = clsPersonaHandler_BL.obtenerPersona(id);
                personaConDepartamentos = new ClsPersonaConListaDepartamentos(clsPersona);
            }
            catch (Exception e)
            {
                return View("Error");
            }

            return View(personaConDepartamentos);
        }

        [HttpPost]
        public ActionResult Edit(ClsPersonaConListaDepartamentos personaConDepartamentos)
        {
            if (ModelState.IsValid)
            {
                try
                {
                    ClsHandlerPersona_BL clsPersonaHandler_BL = new ClsHandlerPersona_BL();
                    if (clsPersonaHandler_BL.editarPersona(personaConDepartamentos))
                    {
                        ViewBag.MensajePersonaModificada = "La persona se ha modificado correctamente";
                    }

                    return View(personaConDepartamentos);//Volvemos a devolver la persona con departamento para que salgan los mismos datos de antes.
                }
                catch (Exception e)
                {
                    return View("Error");//Es posible que no podamos realizar una conexión a la base de datos
                }
            }
            else
            {
                return View();  //Retornamos a la misma vista para ver los fallos
            }
        }

        #endregion

        public ActionResult Details(int id)
        {
            ClsHandlerPersona_BL clsPersonaHandler_BL = new ClsHandlerPersona_BL();
            ClsPersona clsPersona;
            ClsPersonaConDepartamento personaConDepartamento = null;

            try
            {
                clsPersona = clsPersonaHandler_BL.obtenerPersona(id);
                personaConDepartamento = new ClsPersonaConDepartamento(clsPersona);
            }
            catch (Exception e)
            {
                return View("Error");
            }

            return View(personaConDepartamento);
        }
    }
}