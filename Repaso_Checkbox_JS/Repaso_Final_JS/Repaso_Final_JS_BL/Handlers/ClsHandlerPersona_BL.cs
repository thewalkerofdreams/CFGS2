using Repaso_Final_JS_DAL.Handlers;
using Repaso_Final_JS_Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repaso_Final_JS_BL.Handlers
{
    public class ClsHandlerPersona_BL
    {
        /// <summary>
        /// Comentario: Este método nos permite insertar una persona en la base de datos. 
        /// </summary>
        /// <param name="persona">Un objeto del tipo ClsPersona</param>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si se ha conseguido insertar la nueva persona o false en caso contrario.</returns>
        public bool insertarPersona(ClsPersona persona)
        {
            return new ClsHandlerPersona_DAL().insertarPersona(persona);
        }

        /// <summary>
        /// Comentario: Este método nos permite eliminar una persona de la base de datos.
        /// </summary>
        /// <param name="id">Id de la persona</param>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si ha conseguido eliminar la persona o false en caso contrario.</returns>
        public bool eliminarPersona(int id)
        {
            return new ClsHandlerPersona_DAL().eliminarPersona(id);
        }

        /// <summary>
        /// Comentario: Este método nos permite modificar una persona de la base de datos.
        /// </summary>
        /// <param name="persona">El tipo ClsPersona</param>
        /// <returns></returns>
        public bool editarPersona(int id, ClsPersona persona)
        {
            return new ClsHandlerPersona_DAL().editarPersona(id, persona);
        }

        /// <summary>
        /// Comentario: Este método nos permite obtener una persona de la base de datos.
        /// </summary>
        /// <param name="id">Id de la persona</param>
        /// <returns>El método devuelve un tipo ClsPersona asociado al nombre, que es la persona buscada o null si esa persona no existe en la base de datos.</returns>
        public ClsPersona obtenerPersona(int id)
        {
            return new ClsHandlerPersona_DAL().obtenerPersona(id);
        }
    }
}
