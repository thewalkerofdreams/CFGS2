

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
        /// Método que llama al eliminar persona de la capa DAL o no, dependiendo de 
        /// la lógica de la empresa. Si se puede eliminar, se eliminará.
        /// </summary>
        /// <param name="oPersona">
        /// objeto persona que se quiere eliminar
        /// </param>
        /// <returns>
        /// devuelve el numero de filas afectadas o -1 si no se puede eliminar
        /// porque la empresa así lo quiere
        /// </returns>
        public async Task<int> eliminarPersona(clsPersona oPersona)
        {
            
            int resultado;
            clsGestionPersonasDAL gestionPersonasDAL = new clsGestionPersonasDAL();
            resultado = await gestionPersonasDAL.eliminarPersonaAsync(oPersona.idPersona);
            
            //de momento no hay logica de empresa
            return resultado;
            
            //return 0;
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
