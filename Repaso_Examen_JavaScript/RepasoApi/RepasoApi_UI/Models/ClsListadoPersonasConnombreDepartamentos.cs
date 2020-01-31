using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RepasoApi_Entities;
using RepasoApi_BL.Handlers;
using RepasoApi_BL.Lists;


namespace RepasoApi_UI.Models
{
    public class ClsListadoPersonasConNombreDepartamentos
    {
        /// <summary>
        /// Comentario: Este método nos permite obtener un listado con las personas de la base de datos con su respectivo
        /// departamento. En caso de error de conexión el método devuelve null.
        /// </summary>
        /// <returns>El método devuelve un listado de personas con su departamento.</returns>
        public List<ClsPersonaConDepartamento> obtenerListadoPersonasConDepartamento()
        {
            List<ClsPersonaConDepartamento> listadoPersonasConDepartamento = new List<ClsPersonaConDepartamento>();
            List<ClsPersona> listadoPersonas = new ClsListadosPersonas_BL().obtenerListadoDePersonas();
            ClsListadosDepartamentos_BL clsListadosDepartamentosBL = new ClsListadosDepartamentos_BL();
            ClsPersona personaAux;

            for (int i = 0; i < listadoPersonas.Count; i++)//Recorremos la lista de personas
            {
                personaAux = listadoPersonas.ElementAt(i);
                listadoPersonasConDepartamento.Add(new ClsPersonaConDepartamento(personaAux));
            }

            return listadoPersonasConDepartamento;
        }
    }
}