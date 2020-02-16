

using CRUDXamarin_DAL.Handler;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CRUDXamarin_BL.Handler
{
    public class clsGestionPersonasBL
    {
        /// <summary>
        /// Comentario: Este método nos permite insertar una persona en la base de datos.
        /// </summary>
        /// <param name="oPersona">
        /// La persona ha insertar
        /// </param>
        /// <returns>
        /// El método devuelve un número entero que son el número de filas afectadas.
        /// </returns>
        public async Task<int> insertarPersonaAsync(clsPersona oPersona)
        {
            clsGestionPersonasDAL gestionPersonasDAL = new clsGestionPersonasDAL();
            return await gestionPersonasDAL.insertarPersonaAsync(oPersona); 
        }

        /// <summary>
        /// Comentario: Este método nos permite eliminar a una persona de la base de datos.
        /// </summary>
        /// <param name="idPersona">
        /// ID de la persona a eliminar
        /// </param>
        /// <returns>
        /// El método devuelve un número entero asociado al nombre que es el número de filas afectadas.
        /// </returns>
        public async Task<int> eliminarPersonaAsync(int idPersona)
        {
            clsGestionPersonasDAL gestionPersonasDAL = new clsGestionPersonasDAL();
            return await gestionPersonasDAL.eliminarPersonaAsync(idPersona);
        }

        /// <summary>
        /// Comentario: Este método nos permite actualizar una persona en la base de datos.
        /// </summary>
        /// <param name="oPersona">
        /// Actualización de la persona.
        /// </param>
        /// <returns>
        /// El método devuelve un entero asociado al nombre que es el número de filas afectadas.
        /// </returns>
        public async Task<int> actualizarPersona(clsPersona oPersona)
        {
            clsGestionPersonasDAL gestionPersonasDAL = new clsGestionPersonasDAL();
            return await gestionPersonasDAL.actualizarPersona(oPersona);
        }

        /// <summary>
        /// Comentario: Este método nos permite obtener una persona de la base de datos.
        /// </summary>
        /// <param name="id">
        /// Id de la persona.
        /// </param>
        /// <returns>La persona de la base de datos</returns>
        public async Task<clsPersona> obtenerPersona(int id)
        {
            clsGestionPersonasDAL gestionPersonasDAL = new clsGestionPersonasDAL();
            return await gestionPersonasDAL.obtenerPersona(id);
        }
    }
}
